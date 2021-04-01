package com.yuneec.ylauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.UserHandle;

import androidx.annotation.RequiresApi;

import com.yuneec.ylauncher.AppsConfigs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LauncherAppState {
    private Context context;
    private LauncherApps mLauncherApps;
    private static LauncherAppState launcherAppState;
    private ArrayList<ResolveInfo> blackApps = new ArrayList<>();
    private ArrayList<ResolveInfo> whiteApps = new ArrayList<>();

    public static LauncherAppState getInstance() {
        if (launcherAppState == null) {
            launcherAppState = new LauncherAppState();
        }
        return launcherAppState;
    }

    public LauncherAppState init(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mLauncherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
            mLauncherApps.registerCallback(new WrappedCallback());
        }

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        blackApps.clear();
        whiteApps.clear();
        for (ResolveInfo resolveInfo : apps) {
            ActivityInfo info = resolveInfo.activityInfo;
            Drawable icon = info.loadIcon(packageManager);
            String name = (String) info.loadLabel(packageManager);
            String pkg = info.packageName;
            String cls = info.name;
//            Logg.loge("-->name:" + name + ";pkg:" + pkg + ";cls:" + cls);
            sortAppsList(resolveInfo);
        }
//        Logg.loge("--> blackApps:" + blackApps.size()  + " ; whiteApps :" + whiteApps.size());
        return launcherAppState;
    }

    private void sortAppsList(ResolveInfo resolveInfo) {
        ActivityInfo info = resolveInfo.activityInfo;
        String pkg = info.packageName;
        if (Arrays.asList(AppsConfigs.blackApps).contains(pkg)) {
            blackApps.add(resolveInfo);
        } else {
            whiteApps.add(resolveInfo);
        }
    }

    public boolean isSystemApp(String pkg) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(pkg, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public boolean isAppInstalled(String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public void reLoadApps(Context context) {
        init(context);
    }

    public ArrayList<ResolveInfo> getWhiteApps() {
        return whiteApps;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    class WrappedCallback extends LauncherApps.Callback {
        @Override
        public void onPackageRemoved(String packageName, UserHandle user) {

        }

        @Override
        public void onPackageAdded(String packageName, UserHandle user) {

        }

        @Override
        public void onPackageChanged(String packageName, UserHandle user) {

        }

        @Override
        public void onPackagesAvailable(String[] packageNames, UserHandle user, boolean replacing) {

        }

        @Override
        public void onPackagesUnavailable(String[] packageNames, UserHandle user, boolean replacing) {

        }
    }

}
