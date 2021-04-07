package com.yuneec.ylauncher.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtil {

    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        return language;
    }

    public static void initAppLanguage(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = Locale.getDefault();
        resources.updateConfiguration(config, dm);
    }
}
