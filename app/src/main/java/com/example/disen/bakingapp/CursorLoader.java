package com.example.disen.bakingapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by disen on 1/16/2018.
 */

public class CursorLoader extends AsyncTaskLoader {
    public String url;
    public CursorLoader(Context context) {
        super(context);
        this.url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    }

    @Override
    public Object loadInBackground() {
        ArrayList<RecipeSample> samples = new ArrayList<>();
        try {
            samples = Utils.fetchData(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return samples;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
