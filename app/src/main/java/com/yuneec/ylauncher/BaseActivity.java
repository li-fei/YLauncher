package com.yuneec.ylauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yuneec.ylauncher.utils.Logg;
import com.yuneec.ylauncher.utils.YuneecPermissionManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        YuneecPermissionManager.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case YuneecPermissionManager.PERMISSON_REQUESTCODE:
                YuneecPermissionManager.getInstance(this).mPermissionList.clear();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (showRequestPermission) {
                            Logg.loge("first disable :" + permissions[i]);
                            YuneecPermissionManager.getInstance(this).mPermissionList.add(permissions[i]);
                        } else {
                            Logg.loge("second disable :" + permissions[i]);
//                            jumpToSelfSetting(this);
                        }
                    } else {
                        Logg.loge("agree :" + permissions[i]);
                    }
                }
//                YuneecPermissionManager.getInstance(this).checkPermissions(
//                        YuneecPermissionManager.getInstance(this).mPermissionList);
                break;
            default:
                break;
        }
    }

    private void jumpToSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        goActivity(mIntent,true);
    }

    public void goActivity(Intent intent, boolean anim) {
        startActivity(intent);
        if (anim) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
