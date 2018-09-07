package com.example.disen.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by disen on 1/10/2018.
 */


public class BakingHelper extends SQLiteOpenHelper {
    static String dbName = BakingContract.BakingEntry.db_name;
    static int db_version = 3;
    public BakingHelper(Context context,SQLiteDatabase.CursorFactory factory) {
        super(context, dbName, factory, db_version);
    }

    public static String CreateEntries = "CREATE TABLE " + BakingContract.BakingEntry.db_name+"("
            +BakingContract.BakingEntry.ColumnID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +BakingContract.BakingEntry.ColumnName+ " TEXT, "
            +BakingContract.BakingEntry.ColumnIngredients+ " TEXT) ";
    public static String deleteTable = "DROP TABLE IF EXISTS "+ dbName;
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CreateEntries);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(deleteTable);
        onCreate(sqLiteDatabase);
    }
}
