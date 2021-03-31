package com.yuneec.ylauncher.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class YuneecPermissionManager {

    private final static String TAG = "YuneecPermissionManager";
    private static com.yuneec.ylauncher.utils.YuneecPermissionManager instance;
    private Context context;
    public static final int PERMISSON_REQUESTCODE = 100;
    public static String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    public List<String> mPermissionList = new ArrayList<String>(0);

    private YuneecPermissionManager(Context context) {
        this.context = context;
        checkPermission();
    }

    public static YuneecPermissionManager getInstance(Context context) {
        if (instance == null) {
            synchronized (YuneecPermissionManager.class) {
                if (instance == null) {
                    instance = new YuneecPermissionManager(context);
                }
            }
        }
        return instance;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(permissions);
        }
    }

    public void checkPermissions(List<String> permissionList) {
        if (permissionList.size() != 0) {
            requestPermission(permissionList.toArray(new String[permissionList.size()]));
        }
    }

    private void checkPermissions(String[] permissions) {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (!mPermissionList.isEmpty()) {
            requestPermission(mPermissionList.toArray(new String[mPermissionList.size()]));
        }
    }

    private void requestPermission(String[] permission) {
        ActivityCompat.requestPermissions((Activity) context, permission, PERMISSON_REQUESTCODE);
    }


}
