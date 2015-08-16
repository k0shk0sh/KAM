package com.fast.access.kam.global.helper;

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

    public File getFile(String path) {
        return new File(path);
    }

    private String getJpgImagePath(String path) {
        return path + ".jpg";
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

    public String generateFileName() {
        return getJpgImagePath("mobiz-" + String.valueOf(System.currentTimeMillis()));
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
        return new File(folderName(), path);
    }

    public boolean exists(String path) {
        return getFile(path) != null && getFile(path).exists();
    }
}
