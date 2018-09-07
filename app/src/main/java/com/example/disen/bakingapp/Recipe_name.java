package com.example.disen.bakingapp;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.disen.bakingapp.data.BakingContract;

import java.util.ArrayList;


public class Recipe_name extends Fragment implements RecipeNameCardAdapter.OnstepClicklistener {
    private static String recipe_name;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecipeSample sample;
    int size;
    int ingredients_size;
    ArrayList<RecipeSample> Arr_steps;
    String name;
    communicator communicator;
    int screenSize;
    String orientation;


    public Recipe_name() {
        // Required empty public constructor
    }

    public interface communicator{
        void sendVideosInfo(String steps, String recipe_name, String video, int position, int size, String video_id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            communicator = (communicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement communicator interface");
        }

    }

    @Override
    public void onDetach() {
        communicator = null;
        super.onDetach();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(checkDuplicates(recipe_name)){
            menu.findItem(R.id.favorite).setVisible(false);
            menu.findItem(R.id.remove).setVisible(true);
        }
        else{
            menu.findItem(R.id.remove).setVisible(false);
            menu.findItem(R.id.favorite).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ContentValues contentValues = new ContentValues();
        StringBuilder builder = new StringBuilder();
        int itemId = item.getItemId();
        if(sample == null){
            sample = RecipeSample.getSampleByName(getContext(),name);
        }
        //create function to check if already in
        for (int i = 0; i<= ingredients_size-1;i++){
            builder.append(sample.getIngredients().get(i).getQuantity()).append(" ")
                    .append(sample.getIngredients().get(i).getMeasure()).append(" ")
                    .append(sample.getIngredients().get(i).getIngredient()).append("\n");
        }
        if(itemId == R.id.favorite) {
            //put recipe name and builder in Db---change db to only hold 2 columns
            if (recipe_name != null) {
                if (!checkDuplicates(recipe_name)) {
                    contentValues.put(BakingContract.BakingEntry.ColumnName, recipe_name);
                    contentValues.put(BakingContract.BakingEntry.ColumnIngredients, builder.toString());
                    getContext().getContentResolver().insert(BakingContract.Content_Uri, contentValues);
                    BakingAppService.startupdateWidgetAction(getContext());
                    Toast.makeText(getContext(), getString(R.string.favorite), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), recipe_name + getString(R.string.duplicate), Toast.LENGTH_LONG).show();
                }
            }
        }
        else if(item.getItemId() == R.id.remove){
            unfavoriteConfirmation();
            BakingAppService.startupdateWidgetAction(getContext());
        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean checkDuplicates(String name){
        String[] projection = new String[]{BakingContract.BakingEntry.ColumnName};
        String selection = BakingContract.BakingEntry.ColumnName + "=? ";
        String[] args = new String[]{name};
        Cursor cursor = getActivity().getContentResolver().query(BakingContract.Content_Uri,projection,selection,args,null);

        if(cursor != null && cursor.getCount()>0){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_name, container, false);
        setHasOptionsMenu(true);
        screenSize = getResources().getConfiguration().smallestScreenWidthDp;
        getActivity().invalidateOptionsMenu();
        recyclerView = (RecyclerView) v.findViewById(R.id.recipe_name_card_recycler);
        recyclerView2 = (RecyclerView) v.findViewById(R.id.reciperecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);


        name = getArguments().getString("name");
        sample = RecipeSample.getSampleByName(getContext(),name);
        if(getArguments().getInt("ingredients_size") == -1 && sample != null){
            ingredients_size = getArguments().getInt("ingredients_size");
        }
        else {
            ingredients_size = sample.getIngredients().size();
        }
        recipe_name = name;
        getActivity().setTitle(recipe_name);
        Arr_steps = sample.getSteps();
        ArrayList<RecipeSample> ingredients = sample.getIngredients();


        RecipeNameCardAdapter recipeCardAdapter = new RecipeNameCardAdapter(getContext(),ingredients,null,this);
        RecipeNameCardAdapter recipeCardAdapter2 = new RecipeNameCardAdapter(getContext(),null,Arr_steps,this);
        size = Arr_steps.size();
        recyclerView.setAdapter(recipeCardAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setAdapter(recipeCardAdapter2);
        recyclerView2.setHasFixedSize(true);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nameof_recipe",recipe_name);
        outState.putInt("size_ingredients",sample.getIngredients().size());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            Recipe_name recipeName = new Recipe_name();
            Bundle bundle = new Bundle();
            String recipe_Name = savedInstanceState.getString("nameof_recipe");
            bundle.putString("name", recipe_Name);
            int size = savedInstanceState.getInt("size_ingredients");
            bundle.putInt("size_ingredients", size);
            recipeName.setArguments(bundle);
            BakingAppService.startupdateWidgetAction(getContext());
            //getFragmentManager().beginTransaction().replace(R.id.container, recipeName,"recipe_name").commit();
        }
    }

    @Override
    public void onStepClicked(int position) {

        FragmentManager fragmentManager = getFragmentManager();
        String steps = Arr_steps.get(position).getVideo_desc();
        String video = Arr_steps.get(position).getVideoUrl();
        String video_id = Arr_steps.get(position).getVideo_id();
        if(screenSize >=600){
            communicator.sendVideosInfo(steps,recipe_name,video,position,size,video_id);
        }
        else{
        Bundle bundle = new Bundle();
        bundle.putString("step",steps);
        bundle.putString("name",recipe_name);
        bundle.putString("video",video);
        bundle.putInt("position",position);
        bundle.putInt("size",size);
        bundle.putString("video_id",video_id);
        VideoSteps videoSteps = new VideoSteps();
        videoSteps.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container,videoSteps,"video_steps").commit();}
    }
        private void unfavoriteConfirmation(){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.would) +recipe_name+ getString(R.string.from));
            builder.setTitle(getString(R.string.confirmation));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String selection = BakingContract.BakingEntry.ColumnName+"=?";
                    String[] args = {recipe_name};
                    getActivity().getContentResolver().delete(BakingContract.Content_Uri,selection,args);
                    Toast.makeText(getContext(),getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(dialogInterface != null){
                        dialogInterface.dismiss();
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

}
