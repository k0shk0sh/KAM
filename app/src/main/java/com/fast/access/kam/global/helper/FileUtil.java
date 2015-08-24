package com.fast.access.kam.global.helper;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class FileUtil {

    private String TAG = this.getClass().getSimpleName();
    private final String folderName = Environment.getExternalStorageDirectory() + "/KAM/";

    public File folderName() {
        File file = new File(folderName);
        if (!file.exists())
            file.mkdir();
        return file;
    }

    public String getBaseFolderName() {
        return folderName;
    }

    public static File getFile(String path) {
        return new File(path);
    }

    private static String getPng(String path) {
        return path + ".png";
    }

    public boolean deleteFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                return file.delete();
            } else {
                file = new File(folderName(), path);
                if (file.exists()) {
                    return file.delete();
                }
            }
        }
        return false;
    }

    public void deleteFile(List<String> paths) {
        for (String path : paths) {
            if (path != null) {
                File file = new File(folderName(), path);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }


    public static String generateFileName(String packageName) {
        return getPng(packageName);
    }

    public File generateFile(String path) {
        File file = new File(folderName, ".nomedia");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                file.mkdir();
            }
        }
        return new File(folderName(), path + ".apk");
    }

    public File generateZipFile(String name) {
        File file = new File(folderName, ".nomedia");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                file.mkdir();
            }
        }
        return new File(folderName(), name + ".zip");
    }

    public static String getCacheFile(Context context, String packageName) {
        return context.getCacheDir().getPath() + "/" + generateFileName(packageName);
    }

    public static boolean exists(String path) {
        return getFile(path).exists();
    }


}
