package com.yuneec.ylauncher.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static String PATH = Environment.getExternalStorageDirectory() + File.separator;

    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT >= 29) {
                sdDir = context.getFilesDir();
//                Logg.loge("... context.getFilesDir():" + context.getFilesDir()); ///data/user/0/com.yuneec.ylauncher/files
//                Logg.loge("... context.getDataDir():" + context.getDataDir());///data/user/0/com.yuneec.ylauncher
//                Logg.loge("... Environment.getExternalStorageDirectory():" + Environment.getExternalStorageDirectory());///storage/emulated/0
//                Logg.loge("... Environment.getRootDirectory():" + Environment.getRootDirectory());///system
            } else {
                sdDir = Environment.getExternalStorageDirectory();
            }
        } else {
            sdDir = Environment.getRootDirectory();
        }
        return sdDir.toString() + File.separator;
    }

    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists())
            Logg.loge("createDir ... path:" + path);
        dir.mkdir();
    }

    public static void createFile(String path, String filename) {
        createDir(path);
        File file = new File(path + File.separator + filename);
        if (!file.exists()) {
            try {
                Logg.loge("createFile ... path:" + path + " ,filename:" + filename);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
