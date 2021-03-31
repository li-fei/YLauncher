package com.yuneec.ylauncher.utils;

import android.util.Log;

public class Logg {
    private static boolean showLog = true;

    private static String TAG = "Yuneec.Launcher:";

    public static void loge(String log) {
        if (showLog) {
            Log.e(TAG, log);
//            System.out.println(log);
        }
    }

    public static void loge(String tag,String log) {
        if (showLog) {
            Log.e(TAG + "_" + tag, log);
        }
    }
}
