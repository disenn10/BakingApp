package com.example.disen.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.disen.bakingapp.data.BakingContract;

/**
 * Created by disen on 1/11/2018.
 */

public class BakingAppService extends IntentService {
    public static final String UPDATE_WIDGET =  "updatewidgets";
    public BakingAppService() {
        super("Bakingappservice");
    }

    public static void startupdateWidgetAction(Context context){
        Intent intent = new Intent(context, BakingAppService.class);
        intent.setAction(UPDATE_WIDGET);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!= null){
            final String action = intent.getAction();
            if(action.equals(UPDATE_WIDGET)){
                updateWidgetAction();
            }
        }
    }

    private void updateWidgetAction() {
        String name =null;
        String ingredients =null;
        Cursor cursor = getContentResolver().query(BakingContract.Content_Uri,
                null,
                null,
                null,
                null);
        if(cursor!= null && cursor.getCount()>0){
            cursor.moveToNext();
            name = cursor.getString(cursor.getColumnIndex(BakingContract.BakingEntry.ColumnName));
            ingredients = cursor.getString(cursor.getColumnIndex(BakingContract.BakingEntry.ColumnIngredients));
            cursor.close();
        }
        AppWidgetManager widgetmanager = AppWidgetManager.getInstance(this);
        int [] appWigetIds = widgetmanager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        widgetmanager.notifyAppWidgetViewDataChanged(appWigetIds,R.layout.grid_widget_view);
        BakingAppWidget.updateWidgets(this,widgetmanager,appWigetIds);
    }
}
