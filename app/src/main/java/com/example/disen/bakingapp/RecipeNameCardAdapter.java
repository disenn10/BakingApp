package com.example.disen.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by disen on 12/26/2017.
 */

public class RecipeNameCardAdapter extends RecyclerView.Adapter<RecipeNameCardAdapter.RecipeNameViewHolder> {
    Context context;
    ArrayList<RecipeSample> steps;
    ArrayList<RecipeSample> ingredients;
    OnstepClicklistener mOnclickListener;

    public RecipeNameCardAdapter(Context context, ArrayList<RecipeSample>ingredients,ArrayList<RecipeSample>steps, OnstepClicklistener onstepClicklistener){
        this.context = context;
        this.ingredients = ingredients;
        this.steps = steps;
        mOnclickListener = onstepClicklistener;
    }
    public interface OnstepClicklistener{
        void onStepClicked(int position);
    }
    @Override
    public RecipeNameCardAdapter.RecipeNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recipe_name_card,parent,false);
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card_step,parent,false);
        if(ingredients!= null){
            return new RecipeNameViewHolder(v);
        }
        else{
            return new RecipeNameViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecipeNameCardAdapter.RecipeNameViewHolder holder, int position) {
        if(ingredients != null ){
            holder.quantity.setText(ingredients.get(position).getQuantity());
            holder.measure.setText(ingredients.get(position).getMeasure());
            holder.ingredient.setText(ingredients.get(position).getIngredient());
        }
        else{
            holder.short_desc.setText(steps.get(position).getShort_desc());
        }


    }

    @Override
    public int getItemCount() {
        if(ingredients != null){
            return ingredients.size();
        }
        else {
            return steps.size();
        }

    }

    public class RecipeNameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView quantity;
        TextView measure;
        TextView ingredient;
        TextView short_desc;
        public RecipeNameViewHolder(View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.quantity);
            measure = itemView.findViewById(R.id.measure);
            ingredient = itemView.findViewById(R.id.ingredient);
            short_desc = itemView.findViewById(R.id.recipe_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(ingredients == null){
                int position = getAdapterPosition();
                mOnclickListener.onStepClicked(position);
            }
        }
    }
}
