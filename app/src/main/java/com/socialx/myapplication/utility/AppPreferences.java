package com.socialx.myapplication.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final  String USER_NAME="name";
    private static final String USER_EMAIL="email";
        private static final String USER_PASS="password";
    private static final String USER_PHONE="phone";
    private static String USER_LOGIN_STATUS="status";

    public static void PutBoolean(Context context, String key, boolean val){
        SharedPreferences preferences=context.getSharedPreferences("SharedPrefarence",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,val);
        editor.commit();
    }

    public static void setUserLoginStatus(Context context,boolean status){
        PutBoolean(context,USER_LOGIN_STATUS,status);
    }

    public static boolean GetBoolean(Context context,String key){
        SharedPreferences preferences=context.getSharedPreferences("SharedPrefarence",Context.MODE_PRIVATE);
        return preferences.getBoolean(key,false);
    }

    public static boolean getUserLoginStatus(Context context){
        return GetBoolean(context,USER_LOGIN_STATUS);
    }

    public static void PutString(Context context,String key,String val){
        SharedPreferences preferences=context.getSharedPreferences("SharedPrefarence",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,val);
        editor.commit();
    }
    public static String GetString(Context context,String key){
        SharedPreferences preferences=context.getSharedPreferences("SharedPrefarence",Context.MODE_PRIVATE);
        return preferences.getString(key,null);
    }

    public static void setUserName(Context context,String name){
        PutString(context,USER_NAME,name);
    }

    public static String getUserName(Context context){
        return GetString(context,USER_NAME);
    }

    public static void setUserPhone(Context context,String name){
        PutString(context,USER_PHONE,name);
    }

    public static String getUserPhone(Context context) {
        return GetString(context, USER_PHONE);
    }

    public static void setUserEmail(Context context,String name){
        PutString(context,USER_EMAIL,name);
    }

    public static String getUserEmail(Context context) {
        return GetString(context, USER_EMAIL);
    }

    public static void setUserPass(Context context,String password){
        PutString(context,USER_PASS,password);
    }

    public static String getUserPass(Context context){
        return GetString(context,USER_PASS);
    }

}
