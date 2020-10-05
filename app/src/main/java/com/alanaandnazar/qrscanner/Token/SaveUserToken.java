package com.alanaandnazar.qrscanner.Token;

import android.content.Context;
import android.content.SharedPreferences;


public class SaveUserToken {

    public static final String APP_PREFERENCES = "mysettings";


    SharedPreferences mSettings;

    public void saveToken(String Token, String userType, Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("Token", Token);
        editor.putString("Type", userType);
        editor.apply();
    }

    public String getToken(Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return mSettings.getString("Token", "empty");
    }

    public String getType(Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return mSettings.getString("Type", "empty");
    }

    public void clearToken(Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit().clear();
        editor.apply();
    }


}
