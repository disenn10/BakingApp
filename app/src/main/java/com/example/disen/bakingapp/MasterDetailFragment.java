package com.example.disen.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MasterDetailFragment extends AppCompatActivity implements Recipe_name.communicator{
    FragmentManager fragmentManager;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BakingAppService.startupdateWidgetAction(this);
        if(getResources().getConfiguration().smallestScreenWidthDp>=600){
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orientation = sharedPreferences.getString("orientation", "portrait");
        fragmentManager = getSupportFragmentManager();
        Log.e(MainActivity.class.getSimpleName(), "onCreate: "+orientation );
        if(orientation.equals(getString(R.string.landscape))){
            setTheme(R.style.FullscreenCustom);

        }
        else {
            setTheme(R.style.ActionBarCustom);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_master_detail_fragment);
        int screenSize = getResources().getConfiguration().smallestScreenWidthDp;
        name = getIntent().getStringExtra("name");
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        Recipe_name recipe_name = new Recipe_name();
        recipe_name.setArguments(bundle);
        if(screenSize >= 600){
            fragmentManager.beginTransaction().replace(R.id.container, recipe_name, "recipe_name").addToBackStack(null).commit();
        }
        else{
            fragmentManager.beginTransaction().replace(R.id.container, recipe_name, "recipe_name").commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(getResources().getConfiguration().smallestScreenWidthDp<600) {
            VideoSteps videoSteps = (VideoSteps) fragmentManager.findFragmentByTag("video_steps");

            Bundle bundle = new Bundle();
            bundle.putString("name",name);
            Recipe_name recipe_name = new Recipe_name();
            recipe_name.setArguments(bundle);
            if (videoSteps != null) {
                fragmentManager.beginTransaction().remove(videoSteps).commit();
                fragmentManager.beginTransaction().replace(R.id.container, recipe_name, "recipe_name").commit();
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String orientation = "portrait";
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
           // editor.putString("orientation", orientation);
            //editor.apply();
        }

    }

    @Override
    public void sendVideosInfo(String steps, String recipe_name, String video, int position, int size, String video_id) {
        VideoSteps videoSteps = (VideoSteps) getSupportFragmentManager().findFragmentById(R.id.video);
        if(videoSteps!= null){
            videoSteps.setUpvideo(steps,video,position,size,recipe_name);
        }
        else{
            videoSteps.setUpvideo(steps,video,position,size,recipe_name);
        }

    }
}
