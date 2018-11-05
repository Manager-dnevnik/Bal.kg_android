package com.alanaandnazar.qrscanner.Token;

import android.content.Context;
import android.content.SharedPreferences;


public class SaveUserToken {

    public static final String APP_PREFERENCES = "mysettings";


    SharedPreferences mSettings;

    public void saveToken(String Token, Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);

       SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("Token", Token);
        editor.apply();
    }

    public String getToken(Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
        return mSettings.getString("Token", "empty");
    }

    public void ClearToken(Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit().clear();
        editor.apply();
    }


}
