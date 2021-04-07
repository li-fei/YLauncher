package com.yuneec.ylauncher;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yuneec.ylauncher.utils.Brightness;
import com.yuneec.ylauncher.utils.HomeListen;
import com.yuneec.ylauncher.utils.LanguageUtil;
import com.yuneec.ylauncher.utils.LauncherAppState;
import com.yuneec.ylauncher.utils.Logg;
import com.yuneec.ylauncher.utils.MessageEvent;
import com.yuneec.ylauncher.utils.SharedPreUtil;
import com.yuneec.ylauncher.utils.ShowViewAnima;
import com.yuneec.ylauncher.utils.ToastUtil;
import com.yuneec.ylauncher.views.ScreenView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
    private View launcher_fisrt, launcher_apps;
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
        EventBus.getDefault().register(this);
        showApps = LauncherAppState.getInstance().init(this).getWhiteApps();
        SharedPreUtil.saveString(this,"language", LanguageUtil.getLanguage());
        init();
        initReceiverYuneec();
        initHomeListen();
        mHomeListen.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Logg.loge("onResume()");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        if (SharedPreUtil.getApps(this).size() > 0){
//            showApps = SharedPreUtil.getApps(this);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Logg.loge("onPause()");
//        SharedPreUtil.saveApps(this,showApps);
    }

    private HomeListen mHomeListen = null;

    private void initHomeListen() {
        mHomeListen = new HomeListen(this);
        mHomeListen.setOnHomeBtnPressListener(new HomeListen.OnHomeBtnPressLitener() {
            @Override
            public void onHomeBtnPress() {
                ShowViewAnima.enterExitAppsView(false, launcher_fisrt, launcher_apps);
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
        if (showApps.size() > 0) {
            gridAdapter = new GridAdapter(this, showApps);
            gridAdapter.setAppListener(new GridAdapter.AppListener() {
                @Override
                public void appClick(String packageName, String cls) {
                    goActivity(new Intent().setComponent(new ComponentName(packageName, cls)), true);
                }
            });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dataPilot:
                if (LauncherAppState.getInstance().isAppInstalled(AppsConfigs.DataPilot_PackageName)) {
                    ComponentName componentName = new ComponentName(AppsConfigs.DataPilot_PackageName, AppsConfigs.DataPilot_Cls);
                    goActivity(new Intent().setComponent(componentName), true);
                } else {
                    ToastUtil.getInstance().toastShow(this, "None DataPilot App...");
                }
                break;
            case R.id.apps:
                ShowViewAnima.enterExitAppsView(true, launcher_fisrt, launcher_apps);
                break;
            case R.id.apps_back:
                ShowViewAnima.enterExitAppsView(false, launcher_fisrt, launcher_apps);
                break;
            case R.id.settings:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                goActivity(intent, true);
                break;
            case R.id.btn_wifi:
                goActivity(new Intent(Settings.ACTION_WIFI_SETTINGS), true);
                break;
            case R.id.brightness_add:
                setBrightness(true);
                break;
            case R.id.brightness_less:
                setBrightness(false);
                break;
        }
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

    private MyHandler mHandler = new MyHandler(Looper.myLooper(), this);

    private class MyHandler extends Handler {
        WeakReference<YLauncherActivity> mActivity;
        public MyHandler(@NonNull Looper looper, YLauncherActivity activity) {
            super(looper);
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                LauncherAppState.getInstance().reLoadApps(getApplicationContext());
                gridAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage();
        int flag = messageEvent.getFLAG();
        if (flag == MessageEvent.AppDetail){
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + msg));
            goActivity(intent, true);
        }else {
            ToastUtil.getInstance().toastShow(this, msg);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String lastLang = SharedPreUtil.getString(this,"language");
        String lang = LanguageUtil.getLanguage();
        SharedPreUtil.saveString(this,"language", LanguageUtil.getLanguage());
        if (!lang.equals(lastLang)) {
            LanguageUtil.initAppLanguage(this);
            mHandler.sendEmptyMessage(0);
        }
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                ShowViewAnima.enterExitAppsView(false, launcher_fisrt, launcher_apps);
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
        EventBus.getDefault().unregister(this);
        screenView.stopScreen();
        deInitReceiverYuneec();
//        SharedPreUtil.saveApps(this,showApps);
    }
}