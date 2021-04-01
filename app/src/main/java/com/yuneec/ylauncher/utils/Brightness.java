package com.yuneec.ylauncher.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Brightness {

    public static int getScreenBrightness(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        int defVal = 127;
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, defVal);
    }

    public static void setScreenBrightness(Context context, int birghtessValue) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == 0) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, birghtessValue);
            } else {
//                Logg.loge("自动亮度!");
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getBrightnessMax() {
        try {
            Resources system = Resources.getSystem();
            int resId = system.getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android");
            if (resId != 0) {
                return system.getInteger(resId);
            }
        } catch (Exception ignore) {
        }
        return 255;
    }

    public static float setAppScreenBrightness(Context context, Window window, int birghtessValue) {
        if (birghtessValue <= 0) {
            birghtessValue = 0;
        }
        float max = getBrightnessMax();
        if (birghtessValue >= max) {
            birghtessValue = (int) max;
        }
        setScreenBrightness(context, birghtessValue);
        float systemBright = getScreenBrightness(context);
        WindowManager.LayoutParams lp = window.getAttributes();
//        float ratio = systemBright / max;
//        float setbright = birghtessValue / 255.0f;
        float birght = birghtessValue / max;
//        Logg.loge("systemBright: "+systemBright +" ;birghtessValue: "+ birghtessValue + " ;max ：" + max  + " ;birght :" + birght);
        lp.screenBrightness = birght;

        window.setAttributes(lp);
        return birght;
    }

}
