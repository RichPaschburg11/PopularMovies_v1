package com.paschburg.rich.popularmovies_v1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by richardpaschburg on 10/10/15.
 * Credits: Mobile Zone at https://dzone.com/articles/storing-objects-android
 */
public class UserPrefs {

    private static final String packageName = "com.paschburg.rich.popularmovies_v1.";
    private static final String PREFS_NAME = packageName + "UserPrefs";
    private static final String PREFS_INDEX = packageName + "PrefsIndex";
    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    public UserPrefs(Context ctx){
        if (settings == null) {
            settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        editor = settings.edit();
    }

    public void setString(String index, String string) {
        editor.putString(PREFS_INDEX + index, string);
        editor.commit();
    }

     public String getString(String index){
         return settings.getString(PREFS_INDEX + index, "");

    }
    public void setSortOrder(String string) {
        editor.putString(PREFS_INDEX + "SortOrder", string);
        editor.commit();
    }

    public String getSortOrder(){
        String settingvalue = settings.getString(PREFS_INDEX + "SortOrder", "");
        return settingvalue;
    }
}
