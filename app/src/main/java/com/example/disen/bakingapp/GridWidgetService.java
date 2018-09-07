package com.example.disen.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.disen.bakingapp.data.BakingContract;

/**
 * Created by disen on 1/10/2018.
 */

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mcontext;
        Cursor cursor;
        public GridRemoteViewsFactory(Context context){
            mcontext = context;
        }
    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if(cursor != null) cursor.close();
        cursor = mcontext.getContentResolver().query(
                BakingContract.Content_Uri,
                null,
                null,null,null);
    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        if (cursor == null){
            return 0;
        }
        else {
            return cursor.getCount();
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if(cursor.getCount() == 0 || cursor == null) return null;
        cursor.moveToPosition(i);

        String recipe_name = cursor.getString(cursor.getColumnIndex(BakingContract.BakingEntry.ColumnName));
        String ingredients = cursor.getString(cursor.getColumnIndex(BakingContract.BakingEntry.ColumnIngredients));
        RemoteViews remoteViews = new RemoteViews(mcontext.getPackageName(), R.layout.baking_app_widget);
        remoteViews.setTextViewText(R.id.recipe_name, recipe_name);
        remoteViews.setTextViewText(R.id.ingredient,ingredients);
        Bundle bundle = new Bundle();
        bundle.putString("name",recipe_name);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.recipe_widget, fillIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
