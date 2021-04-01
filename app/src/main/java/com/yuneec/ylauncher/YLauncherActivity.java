package com.yuneec.ylauncher;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yuneec.ylauncher.utils.Brightness;
import com.yuneec.ylauncher.utils.HomeListen;
import com.yuneec.ylauncher.utils.LauncherAppState;
import com.yuneec.ylauncher.utils.Logg;
import com.yuneec.ylauncher.utils.SharedPreUtil;
import com.yuneec.ylauncher.utils.ToastUtil;
import com.yuneec.ylauncher.views.ScreenView;

import java.util.ArrayList;
import java.util.List;

public class YLauncherActivity extends BaseActivity implements View.OnClickListener {

    private LauncherViewModel launcherViewModel;
    private Button brightness_add, brightness_less;
    private Button settings, apps, btn_wifi;
    private Button btn_dataPilot;
    private SeekBar seekBar;
    private ScreenView screenView;
    private int currentBrightness = 0;
    private int maxBrightness;
    private int step = 20;
    private int stepNum;
    private int currentBrightnessStep;
    private View launcher_fisrt,launcher_apps;
    private Button apps_back;
    private IntentFilter mIntentFilter;
    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;
    private ArrayList<ResolveInfo> showApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Logg.loge("onCreate()");
        setContentView(R.layout.activity_launcher);
        showApps = LauncherAppState.getInstance().init(this).getWhiteApps();
        init();
        initReceiverYuneec();
        initHomeListen();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Logg.loge("onResume()");
        mHomeListen.start();
//        if (SharedPreUtil.getApps(this).size() > 0){
//            showApps = SharedPreUtil.getApps(this);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Logg.loge("onPause()");
        mHomeListen.stop();
//        SharedPreUtil.saveApps(this,showApps);
    }

    private HomeListen mHomeListen = null;
    private void initHomeListen(){
        mHomeListen = new HomeListen( this );
        mHomeListen.setOnHomeBtnPressListener( new HomeListen.OnHomeBtnPressLitener( ) {
            @Override
            public void onHomeBtnPress() {
                exitAppsView();
            }
            @Override
            public void onHomeBtnLongPress() {
            }
        });
    }

    private void initReceiverYuneec() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        mIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        mIntentFilter.addDataScheme("package");
        registerReceiver(receiverYuneec, mIntentFilter);
    }

    private void deInitReceiverYuneec() {
        unregisterReceiver(receiverYuneec);
    }

    private void init() {
        screenView = findViewById(R.id.screenview);
        screenView.setFirstRun(true);
        screenView.startScreen();
        launcher_fisrt = findViewById(R.id.launcher_first);
        launcher_apps = findViewById(R.id.launcher_apps);
        apps_back = findViewById(R.id.apps_back);
        apps_back.setOnClickListener(this);
        recyclerView = findViewById(R.id.rv_apps);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 6));
        if (showApps.size() > 0){
            gridAdapter = new GridAdapter(this,showApps);
            recyclerView.setAdapter(gridAdapter);
        }
//        NewItemTouchHelper helper = new NewItemTouchHelper(this, gridAdapter, showApps);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(helper);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
        handBrightness();
        launcherViewModel = new ViewModelProvider(this).get(LauncherViewModel.class);
        launcherViewModel.number++;
        btn_dataPilot = findViewById(R.id.btn_dataPilot);
        apps = findViewById(R.id.apps);
        settings = findViewById(R.id.settings);
        btn_wifi = findViewById(R.id.btn_wifi);
        btn_dataPilot.setOnClickListener(this);
        apps.setOnClickListener(this);
        settings.setOnClickListener(this);
        btn_wifi.setOnClickListener(this);
    }

    private void handBrightness() {
        brightness_add = findViewById(R.id.brightness_add);
        brightness_less = findViewById(R.id.brightness_less);
        brightness_add.setOnClickListener(this);
        brightness_less.setOnClickListener(this);
        seekBar = findViewById(R.id.id_seekbar);
        currentBrightness = Brightness.getScreenBrightness(this);
        maxBrightness = Brightness.getBrightnessMax();
        stepNum = maxBrightness / step;
        currentBrightnessStep = currentBrightness / stepNum;
        seekBar.setProgress(maxBrightness / currentBrightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentBrightness = (maxBrightness / 100) * progress;
                Brightness.setAppScreenBrightness(getApplicationContext(), getWindow(), currentBrightness);
//                Logg.loge("progress:" + progress + "  currentBrightness:" + currentBrightness);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                exitAppsView();
            } else {
                //exit;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
//        Logg.loge("onDestroy()");
        super.onDestroy();
        screenView.stopScreen();
        deInitReceiverYuneec();
//        SharedPreUtil.saveApps(this,showApps);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dataPilot:
                String DataPilotPkg = AppsConfigs.DataPilot_PackageName;
                String DataPilotCls = "com.yuneec.datapilot.QGCActivity";
                if (LauncherAppState.getInstance().isAppInstalled(DataPilotPkg)){
                    ComponentName componentName = new ComponentName(DataPilotPkg, DataPilotCls);
                    startActivity(new Intent().setComponent(componentName));
                }else {
                    ToastUtil.getInstance().toastShow(this, "No install DP ...");
                }
                break;
            case R.id.apps:
                enterAppsView();
                break;
            case R.id.apps_back:
                exitAppsView();
                break;
            case R.id.settings:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_wifi:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.brightness_add:
                setBrightness(true);
                break;
            case R.id.brightness_less:
                setBrightness(false);
                break;
        }

    }

    private void setBrightness(boolean flag) {
        if (flag) {
            if (currentBrightness >= maxBrightness - stepNum) {
                ToastUtil.getInstance().toastShow(this, "Now is Max Brightness!");
            } else {
                currentBrightness = currentBrightness + stepNum;
                currentBrightnessStep++;
                float birght = Brightness.setAppScreenBrightness(this, this.getWindow(), currentBrightness);
                ToastUtil.getInstance().toastShow(this, "Brightness: " + currentBrightnessStep);
            }
        } else {
            if (currentBrightness <= stepNum) {
                ToastUtil.getInstance().toastShow(this, "Now is Min Brightness!");
            } else {
                currentBrightness = currentBrightness - stepNum;
                currentBrightnessStep--;
                float birght = Brightness.setAppScreenBrightness(this, this.getWindow(), currentBrightness);
                ToastUtil.getInstance().toastShow(this, "Brightness: " + currentBrightnessStep);
            }
        }
    }

    public void enterAppsView(){
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(launcher_fisrt, "alpha", 1f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(launcher_apps, "alpha", 0f, 1f);
        animatorSet.play(animator1).with(animator2);
        animatorSet.setDuration(800);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                launcher_apps.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                launcher_fisrt.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public void exitAppsView(){
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(launcher_apps, "alpha", 1f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(launcher_fisrt, "alpha", 0f, 1f);
        animatorSet.play(animator1).with(animator2);
        animatorSet.setDuration(800);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                launcher_fisrt.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                launcher_apps.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void showView(View view1,View view2){
        AlphaAnimation showAnimation = new AlphaAnimation(0f,1f);
        showAnimation.setDuration(1000);
        AlphaAnimation hideAnimation = new AlphaAnimation(1f,0f);
        hideAnimation.setDuration(1000);
        view1.startAnimation(showAnimation);
        view1.setVisibility(View.VISIBLE);
        view2.startAnimation(hideAnimation);
        view2.setVisibility(View.GONE);
    }

    private BroadcastReceiver receiverYuneec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
                String packageName = intent.getDataString();
                Logg.loge("add :" + packageName);
                mHandler.sendEmptyMessage(0);
            }
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                String packageName = intent.getDataString();
                Logg.loge("remove:" + packageName);
                mHandler.sendEmptyMessage(0);
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                LauncherAppState.getInstance().reLoadApps(getApplicationContext());
                gridAdapter.notifyDataSetChanged();
            }
        }
    };
}