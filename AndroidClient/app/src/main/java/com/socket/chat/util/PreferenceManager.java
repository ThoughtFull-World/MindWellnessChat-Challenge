package com.socket.chat.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static Context mContext;
    private static SharedPreferences mSharedPreferences;

    public PreferenceManager(Context context) {
        this.mContext = context;
        this.mSharedPreferences = this.mContext.getSharedPreferences("my_preference", Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPref(Context context) {
        mContext = context;
        return mSharedPreferences;
    }


    public static String getToken() {
        return mSharedPreferences.getString("token",null);
    }

    public static void setToken(String token) {
        mSharedPreferences.edit().putString("token", token).apply();
    }


    public static String getUser() {
        return mSharedPreferences.getString("user",null);
    }

    public static void setUser(String token) {
        mSharedPreferences.edit().putString("user", token).apply();
    }


    public static String getPassword() {
        return mSharedPreferences.getString("password",null);
    }

    public static void setPassword(String password) {
        mSharedPreferences.edit().putString("password", password).apply();
    }

}
