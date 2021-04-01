package com.yuneec.ylauncher.utils;

import android.content.Context;

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
        ToastUtil.getInstance().toastShow(context, packageName + ":" + menu);
        this.context = context;
    }
}
