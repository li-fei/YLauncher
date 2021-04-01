package com.yuneec.ylauncher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class SharedPreUtil {
    private static final String SP_NAME = "YLauncher";
    public static final String SP_APPS_LIST = "apps_list";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SP_NAME, 0);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        return sharedPreferences.getInt(key, 0);
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        return sharedPreferences.getString(key, "");
    }

    public static void remove(Context context, List<String> removeList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        for (int i = 0; i < removeList.size(); i++) {
            sharedPreferences.edit().remove(removeList.get(i)).commit();
        }
    }

    public static void removeSingle(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
        sharedPreferences.edit().remove(key).commit();
    }

    public static void saveApps(Context context, ArrayList<ResolveInfo> apps) {
        try {
            FileOutputStream fis = new FileOutputStream(new File(context.getFilesDir().getPath() + SP_APPS_LIST));
            ObjectOutputStream ois = new ObjectOutputStream(fis);
            ois.writeObject(apps);
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ResolveInfo> getApps(Context context) {
        ArrayList<ResolveInfo> showApps = new ArrayList<ResolveInfo>();
        try {
            FileInputStream fis = new FileInputStream(new File(context.getFilesDir().getPath() + SP_APPS_LIST));
            ObjectInputStream ois = new ObjectInputStream(fis);
            showApps = (ArrayList<ResolveInfo>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showApps;
    }


}
