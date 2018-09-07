package com.example.disen.bakingapp;


import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.disen.bakingapp.MainActivity;
import com.example.disen.bakingapp.R;
import com.example.disen.bakingapp.RecipeCardAdapter;
import com.example.disen.bakingapp.RecipeSample;
import com.example.disen.bakingapp.databinding.FragmentRecipeNameBinding;

import java.util.ArrayList;

public class Select_recipe extends Fragment implements RecipeCardAdapter.listItemClickListener {
    ArrayList<String> sample;
    String name;
    FragmentRecipeNameBinding fragmentRecipeNameBinding;

    public Select_recipe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRecipeNameBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_select_recipe, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        fragmentRecipeNameBinding.recipeNameCardRecycler.setLayoutManager(linearLayoutManager);

        sample = RecipeSample.getAllnames(getContext());
       // RecipeCardAdapter recipeCardAdapter = new RecipeCardAdapter(getContext(), sample,this);
        //fragmentRecipeNameBinding.recipeNameCardRecycler.setAdapter(recipeCardAdapter);
        fragmentRecipeNameBinding.recipeNameCardRecycler.setHasFixedSize(true);
        return fragmentRecipeNameBinding.getRoot();
    }



    @Override
    public void onItemclicked(int position) {
        String recipe_name = sample.get(position);
        Recipe_name recipeName = new Recipe_name();
        Bundle bundle = new Bundle();
        bundle.putString("name", recipe_name);
        name = recipe_name;
        recipeName.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, recipeName,"recipe_name").addToBackStack(null).commit();

    }

}
