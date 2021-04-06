package com.yuneec.ylauncher.utils;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

public class AppsPopMenuManager {

    private Context context;
    private static AppsPopMenuManager appsPopMenuManager;

    public static AppsPopMenuManager getInstance() {
        if (appsPopMenuManager == null) {
            appsPopMenuManager = new AppsPopMenuManager();
        }
        return appsPopMenuManager;
    }

    public void setAppsMenu(Context context, String packageName, String menu) {
        this.context = context;
        EventBus.getDefault().post(new MessageEvent(packageName + ":" + menu));
    }
}
