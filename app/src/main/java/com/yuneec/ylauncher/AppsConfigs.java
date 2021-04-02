package com.yuneec.ylauncher;

import java.util.ArrayList;
import java.util.Arrays;

public class AppsConfigs {

    public static String YLauncher_PackageName = "com.yuneec.ylauncher";
    public static String DataPilot_PackageName = "com.yuneec.datapilot";
    public static String DataPilot_Cls = "com.yuneec.datapilot.QGCActivity";
    public static String H520C_PackageName = "com.yuneec.android.h520c";

    public static ArrayList<String[]> AppsList = new ArrayList<String[]>(Arrays.asList(
            new String[]{DataPilot_PackageName, "起飞 ..."},
            new String[]{H520C_PackageName, "一键连接 ...", "一键起飞 ..."}
    ));

    public static String[] blackApps = new String[]{
            YLauncher_PackageName,
    };

    /*
        name:Lightning;pkg:acr.browser.lightning;cls:acr.browser.lightning.MainActivity
        name:视频播放器;pkg:android.rk.RockVideoPlayer;cls:android.rk.RockVideoPlayer.RockVideoPlayer
        name:日历;pkg:com.android.calendar;cls:com.android.calendar.AllInOneActivity
        name:相机;pkg:com.android.camera2;cls:com.android.camera.CameraLauncher
        name:通讯录;pkg:com.android.contacts;cls:com.android.contacts.activities.PeopleActivity
        name:时钟;pkg:com.android.deskclock;cls:com.android.deskclock.DeskClock
        name:图库;pkg:com.android.gallery3d;cls:com.android.gallery3d.app.GalleryActivity
        name:音乐;pkg:com.android.music;cls:com.android.music.MusicBrowserActivity
        name:设置;pkg:com.android.settings;cls:com.android.settings.Settings
        name:录音机;pkg:com.android.soundrecorder;cls:com.android.soundrecorder.SoundRecorder
        name:DataPilot;pkg:com.yuneec.datapilot;cls:com.yuneec.datapilot.QGCActivity
        name:Y桌面;pkg:com.yuneec.ylauncher;cls:com.yuneec.ylauncher.YLauncherActivity
        name:计算器;pkg:com.android.calculator2;cls:com.android.calculator2.Calculator
        name:文件;pkg:com.android.documentsui;cls:com.android.documentsui.LauncherActivity
        name:Search;pkg:com.android.quicksearchbox;cls:com.android.quicksearchbox.SearchActivity
        name:资源管理器;pkg:com.android.rk;cls:com.android.rk.RockExplorer
        name:WebView Browser Tester;pkg:org.chromium.webview_shell;cls:org.chromium.webview_shell.WebViewBrowserActivity
    */
}
