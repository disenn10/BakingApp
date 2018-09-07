package com.example.disen.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by disen on 12/9/2017.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.recipeViewHolder> {
    Context context;
    ArrayList<RecipeSample> recipe_name;
    listItemClickListener mOnclicklistener;


    public RecipeCardAdapter(Context context, ArrayList<RecipeSample> name, listItemClickListener listener) {
        this.context = context;
        this.recipe_name = name;
        mOnclicklistener = listener;
    }

    @Override
    public RecipeCardAdapter.recipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card,parent,false);
        return new recipeViewHolder(view);
    }

    public interface listItemClickListener{
        void onItemclicked(int position);
    }

    @Override
    public void onBindViewHolder(RecipeCardAdapter.recipeViewHolder holder, int position) {
        holder.name.setText(recipe_name.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipe_name.size();
    }

    public class recipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        public recipeViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnclicklistener.onItemclicked(position);
        }
    }
}
