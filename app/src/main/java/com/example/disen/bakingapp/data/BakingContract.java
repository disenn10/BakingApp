package com.example.disen.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by disen on 1/10/2018.
 */

public class BakingContract {

    public static String bakingAuthority = "com.example.disen.bakingapp";
    public static Uri BaseContent = Uri.parse("content://"+bakingAuthority);
    public static String path = "baking";
    public static Uri Content_Uri = Uri.withAppendedPath(BaseContent, path);

    public static class BakingEntry implements BaseColumns {
        public static String db_name = "Recipes";
        public static String ColumnID = BaseColumns._ID;
        public static String ColumnName = "name";
        public static String ColumnIngredients = "ingredients";
    }
}
