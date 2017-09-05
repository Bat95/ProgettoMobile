package com.myrecipebook.myrecipebook.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.lang.reflect.Type;


public class Preferences {

    private static final String PreferenceFileKey = "prefs_key";

    public static <T> T get(Context context, String key, Type type) {
        return get(context, key, type, null);
    }

    public static <T> T get(Context context, String key, Type type, T defaultValue) {
        String serializedObject = getPreferences(context).getString(key, null);
        if(serializedObject == null) {
            return defaultValue;
        }

        return new Gson().fromJson(serializedObject, type);
    }

    public static <T> void store(Context context, String key, T value) {
        String serializedObject = new Gson().toJson(value);

        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor =  preferences.edit();
        editor.putString(key, serializedObject);
        editor.apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PreferenceFileKey, Context.MODE_PRIVATE);
    }

}
