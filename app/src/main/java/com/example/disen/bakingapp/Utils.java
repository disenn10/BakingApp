package com.example.disen.bakingapp;

import android.support.v4.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by disen on 1/16/2018.
 */

public class Utils {


    public static ArrayList<RecipeSample> fetchData(String web_url) throws IOException {
        URL url = createURL(web_url);
        String getJson = makeHttpRequest(url);
        ArrayList<RecipeSample> ingredients = new ArrayList<>();
        try {
            ingredients = extractData(getJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public static URL createURL(String string){
        URL url = null;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        InputStream in = null;
        String output = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(1000);
        urlConnection.setReadTimeout(1000);
        if(urlConnection.getResponseCode() == 200){
            in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if(scanner.hasNext()){
                output =  scanner.next();
            }
            else {
                return null;
            }
            urlConnection.disconnect();
        }

        return output;
    }

    public static ArrayList<RecipeSample> extractData(String data) throws JSONException {
        int id = 0;
        String name = null;
        int servings = 0;
        String image = null;
        ArrayList<RecipeSample> steps = new ArrayList<>();
        ArrayList<RecipeSample> recipeSamples = new ArrayList<>();
        ArrayList<RecipeSample> ingredients = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(data);
        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.has("id")){
                id = jsonObject.getInt("id");
            }
            if(jsonObject.has("name")){
                name = jsonObject.getString("name");
            }
            if(jsonObject.has("ingredients")){
                ingredients = getIngredientsArray(data);
            }
            if(jsonObject.has("steps")){
                steps = getStepsArray(data);
            }
            if(jsonObject.has("servings")){
                servings = jsonObject.getInt("servings");
            }
            if(jsonObject.has("image")){
                image = jsonObject.getString("image");
            }
            recipeSamples.add(new RecipeSample(name,id,ingredients,steps,servings,image));
        }
        return recipeSamples;
    }
    public static ArrayList<RecipeSample> getStepsArray(String json) throws JSONException {
        ArrayList<RecipeSample> stepEntries = new ArrayList<>();
        int id = 0;
        String short_desc = null;
        String video_desc = null;
        String video_url = null;
        String video_thumbnail = null;
        JSONArray jsonArray = new JSONArray(json);
        for(int i = 0; i<jsonArray.length();i++) {
            JSONObject Object = jsonArray.getJSONObject(i);
            if (Object.has("steps")) {
                JSONArray array = Object.getJSONArray("steps");
                for (int x = 0; x < array.length(); x++) {
                    JSONObject stepsArray = array.getJSONObject(x);
                    if (stepsArray.has("id")) {
                        id = stepsArray.getInt("id");
                    }
                    if (stepsArray.has("shortDescription")) {
                        short_desc = stepsArray.getString("shortDescription");
                    }
                    if (stepsArray.has("description")) {
                        video_desc = stepsArray.getString("description");
                    }
                    if (stepsArray.has("videoURL")) {
                        video_url = stepsArray.getString("videoURL");
                    }
                    if (stepsArray.has("thumbnailURL")) {
                        video_thumbnail = stepsArray.getString("thumbnailURL");
                    }
                    stepEntries.add(new RecipeSample(String.valueOf(id), short_desc, video_desc, video_url, video_thumbnail));
                }
            }
        }
        return stepEntries;
    }
    public static ArrayList<RecipeSample> getIngredientsArray(String json) throws JSONException {
        String quantity = null;
        String measure = null;
        String ingredient = null;
        ArrayList<RecipeSample> samples = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for(int i = 0; i < jsonArray.length();i++){
            JSONObject Object = jsonArray.getJSONObject(i);
            if(Object.has("ingredients")){
                JSONArray array = Object.getJSONArray("ingredients");
                for (int x = 0; x< array.length();x++){
                    JSONObject ingredientObject = array.getJSONObject(x);
                    if(ingredientObject.has("quantity")){
                        quantity = ingredientObject.getString("quantity");
                    }
                    if(ingredientObject.has("measure")){
                        measure = ingredientObject.getString("measure");
                    }
                    if(ingredientObject.has("ingredient")){
                        ingredient = ingredientObject.getString("ingredient");
                    }
                    samples.add(new RecipeSample(quantity,measure,ingredient));
                }
            }
        }
        return samples;
    }

}
