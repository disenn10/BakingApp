package com.example.disen.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by disen on 1/10/2018.
 */

public class BakingProvider extends ContentProvider {
    UriMatcher uriMatch = new UriMatcher(UriMatcher.NO_MATCH);
    final int recipe = 100;
    final int recipeID = 101;
    SQLiteDatabase sqLiteDatabase;
    BakingHelper mydbHelper;
    @Override
    public boolean onCreate() {
        uriMatch.addURI(BakingContract.bakingAuthority,BakingContract.path,recipe);
        uriMatch.addURI(BakingContract.bakingAuthority,BakingContract.path+"/#",recipeID);
        mydbHelper = new BakingHelper(getContext(),null);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int path = uriMatch.match(uri);
        Cursor cursor;
        sqLiteDatabase = mydbHelper.getReadableDatabase();
        switch (path){
            case recipe:
                cursor = sqLiteDatabase.query(BakingContract.BakingEntry.db_name, strings,s,strings1,null,null,null);
                break;
            case recipeID:
                s = BakingContract.BakingEntry.ColumnID + "=?";
                strings1 = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(BakingContract.BakingEntry.db_name, strings,s,strings1,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("Can't peform uri request");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        sqLiteDatabase = mydbHelper.getWritableDatabase();
        long ID;
        int path = uriMatch.match(uri);
        switch (path){
            case recipe:
                ID = sqLiteDatabase.insert(BakingContract.BakingEntry.db_name,null,contentValues);
                break;
            default:
                throw new IllegalArgumentException("Cannot insert values");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,ID);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int id;
        int path = uriMatch.match(uri);
        sqLiteDatabase = mydbHelper.getWritableDatabase();
        switch (path){
            case recipe:
                id = sqLiteDatabase.delete(BakingContract.BakingEntry.db_name,s,strings);
                break;
            case recipeID:
                s = BakingContract.BakingEntry.ColumnID = "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                id = sqLiteDatabase.delete(BakingContract.BakingEntry.ColumnID, s,strings);
                break;
            default:
                throw new IllegalArgumentException("could not delete record");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
