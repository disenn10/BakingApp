package com.example.disen.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.disen.bakingapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecipeCardAdapter.listItemClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<RecipeSample>>{

    RecyclerView recyclerView;
    ArrayList<String> sample;
    ActivityMainBinding activityMainBinding;
    ArrayList<RecipeSample> recipe_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityMainBinding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityMainBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        activityMainBinding.navView.setNavigationItemSelectedListener(this);

        int screenSize = getResources().getConfiguration().smallestScreenWidthDp;
        //if on tablet
        if(screenSize >= 600){
            recyclerView = (RecyclerView)findViewById(R.id.recipe_card_recycler);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        else {
            recyclerView = (RecyclerView)findViewById(R.id.recipe_card_recycler);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        //sample = RecipeSample.getAllnames(this);
        //RecipeCardAdapter recipeCardAdapter = new RecipeCardAdapter(getApplicationContext(), sample, this);
        //recyclerView.setAdapter(recipeCardAdapter);
        //recyclerView.setHasFixedSize(true);
        LoaderManager loader = getSupportLoaderManager();
        loader.initLoader(0,null,this);

    }
    void updateUI(ArrayList<RecipeSample> data){
        RecipeCardAdapter recipeCardAdapter = new RecipeCardAdapter(getApplicationContext(), data, this);
        recyclerView.setAdapter(recipeCardAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String recipe_name =null;
        if (id == R.id.nav_nutella) {
            recipe_name = getString(R.string.nutella);;
            gotoMasterDetailActivity(recipe_name);
        } else if (id == R.id.nav_brownies) {
            recipe_name = getString(R.string.brownies);;
            gotoMasterDetailActivity(recipe_name);
        } else if (id == R.id.nav_cheesecake) {
            recipe_name = getString(R.string.cheesecake);;
            gotoMasterDetailActivity(recipe_name);
        } else if (id == R.id.nav_yellowCaked) {
            recipe_name = getString(R.string.yellowcake);;
            gotoMasterDetailActivity(recipe_name);
        }
        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemclicked(int position) {
        String recipe_name = sample.get(position);
        Intent intent = new Intent(this, MasterDetailFragment.class);
        intent.putExtra("name", recipe_name);
        startActivity(intent);
    }
    public void gotoMasterDetailActivity(String name){
        Intent intent = new Intent(this, MasterDetailFragment.class);
        String recipe_name = name;
        intent.putExtra("name", recipe_name);
        startActivity(intent);
    }

    @Override
    public Loader<ArrayList<RecipeSample>> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<RecipeSample>> loader, ArrayList<RecipeSample> data) {
        if(data!= null){
            Log.e(MainActivity.class.getSimpleName(), "onLoadFinished: "+data.size() );
            updateUI(data);
        }
        else{
            Log.e(MainActivity.class.getSimpleName(), "Null null null null " );
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<RecipeSample>> loader) {

    }
}
