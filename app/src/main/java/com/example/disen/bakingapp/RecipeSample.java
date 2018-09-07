package com.example.disen.bakingapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by disen on 12/5/2017.
 */

public class RecipeSample {
    private static int servings;
    private static String image;
    String name;
    int recipe_id;
    ArrayList<RecipeSample> ingredients;
    ArrayList<RecipeSample> steps;
    String quantity;
    String measure;
    String ingredient;
    String video_id;
    String short_desc;
    String video_desc;
    String videoUrl;
    String video_thumbnail;

    //Default constructor
    public RecipeSample(String name, int recipe_id, ArrayList<RecipeSample> ingredients, ArrayList<RecipeSample> steps, int servings, String image){
        this.name = name;
        this.recipe_id = recipe_id;
        this.ingredients = ingredients;
        this.servings = servings;
        this.image = image;
        this.steps = steps;
    }
    //Constructor to save ingredients
    public RecipeSample(String quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
    //Constructor to save steps
    public RecipeSample(String id, String short_desc, String video_desc,String videoUrl, String video_thumbnail){
        this.video_id = id;
        this.short_desc = short_desc;
        this.video_desc = video_desc;
        this.videoUrl = videoUrl;
        this.video_thumbnail = video_thumbnail;
    }


//read the json file
    public static JsonReader readJsonStream(Context context) throws IOException {
        InputStream is = context.getAssets().open("recipe.asset.json");
        JsonReader reader = null;
        reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        return reader;
    }



    //get the recipe by its name
    static RecipeSample getSampleByName(Context context, String name) {
        JsonReader reader;
        try {
            reader = readJsonStream(context);
            reader.beginArray();
            while (reader.hasNext()) {
                RecipeSample currentSample = readRecipeObject(reader);
                if (currentSample.getName().equals(name)) {
                    reader.close();
                    return currentSample;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    //get the recipe by its name
    static RecipeSample getSampleByvideo_id(Context context, int position, String video_id) {
        JsonReader reader;
        try {
            reader = readJsonStream(context);
            reader.beginArray();
            while (reader.hasNext()) {
                RecipeSample currentSample = readRecipeObject(reader);
                //if (currentSample.getSteps().get(position).getVideo_id().equals(video_id)) {
                if (currentSample.getSteps().get(position).getVideo_id().equals(video_id)) {
                    reader.close();
                    return currentSample;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    //read recipe entries
    public static RecipeSample readRecipeObject(JsonReader reader) throws IOException {
        String recipe_name = null;
        int recipe_id = 0;
        ArrayList<RecipeSample> ingredients = new ArrayList<>();
        ArrayList<RecipeSample> steps = new ArrayList<>();
        int recipe_servings = 0;
        String image = null;
        reader.beginObject();
        while (reader.hasNext()){
            String name = null;
            name = reader.nextName();
            if(name.equals("id")){
                recipe_id = reader.nextInt();
            }
            else if(name.equals("name")){
                recipe_name = reader.nextString();
            }
            else if(name.equals("ingredients")){
                ingredients = getIngredientsArray(reader);
            }
            else if(name.equals("steps")){
                steps = getAlltheSteps(reader);
            }
            else if(name.equals("servings")){
                recipe_servings = reader.nextInt();
            }
            else if(name.equals("image")){
                String img = reader.nextString();
                if(img.equals("")){
                    image = "";
                }
            }
            else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return new RecipeSample(recipe_name, recipe_id,ingredients,steps,recipe_servings,image);
    }

    //This one does nothing for now.
    public static ArrayList<RecipeSample> readRecipeArray(JsonReader reader) throws IOException {
        ArrayList<RecipeSample> readRecipe = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            readRecipe.add(readRecipeObject(reader));
        }
        reader.endArray();
        return readRecipe;
    }

    //get all sample
    public static ArrayList<String> getAllnames(Context context){
        JsonReader reader;
        ArrayList<String> sampleIDs = new ArrayList<>();
        try {
            reader = readJsonStream(context);
            reader.beginArray();
            while (reader.hasNext()) {
                sampleIDs.add(readRecipeObject(reader).getName());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleIDs;
    }

    //Traverse ingredient entries and get all ingredients info
    public static ArrayList<RecipeSample> getAlltheSteps(JsonReader reader) throws IOException {
        reader.beginArray();
        ArrayList<RecipeSample> stepEntries = new ArrayList<>();
        int id = 0;
        String short_desc = null;
        String video_desc = null;
        String video_url = null;
        String video_thumbnail = null;
        while (reader.hasNext()){
            reader.beginObject();
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name){
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "shortDescription":
                        short_desc = reader.nextString();
                        break;
                    case "description":
                        video_desc = reader.nextString();
                        break;
                    case "videoURL":
                        video_url = reader.nextString();
                        break;
                    case "thumbnailURL":
                        video_thumbnail = reader.nextString();
                        break;
                    default:
                        Log.e(RecipeSample.class.getSimpleName(), "Error on getting steps entries " );
                }
            }
            stepEntries.add(new RecipeSample(String.valueOf(id),short_desc,video_desc,video_url,video_thumbnail));
            reader.endObject();
        }
        reader.endArray();
        return stepEntries;
    }

    //Traverse ingredient entries and get all ingredients info
    public static ArrayList<RecipeSample> getIngredientsArray(JsonReader reader) throws IOException {
        reader.beginArray();
        ArrayList<RecipeSample> ingredientEntries = new ArrayList<>();
        double quantity = 0;
        String measure = null;
        String ingredient = null;
        while (reader.hasNext()){
            reader.beginObject();

            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name){
                    case "quantity":
                        quantity = reader.nextDouble();
                        break;
                    case "measure":
                        measure = reader.nextString();
                        break;
                    case "ingredient":
                        ingredient = reader.nextString();
                        break;
                    default:
                        Log.e(RecipeSample.class.getSimpleName(), "Error on getting ingredients entries " );
                }
            }
            ingredientEntries.add(new RecipeSample(String.valueOf(quantity), measure, ingredient));
            reader.endObject();
        }
        reader.endArray();
        return ingredientEntries;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public ArrayList<RecipeSample> getSteps() {
        return steps;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public ArrayList<RecipeSample> getIngredients() {
        return ingredients;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }


    public String getName() {
        return name;
    }
}
