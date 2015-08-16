package com.fast.access.kam.global.helper;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by kosh on 12/16/2014. CopyRights @ styleme
 */
public class FileUtil {

    /**
     * TAG.
     */
    private String TAG = this.getClass().getSimpleName();
    /**
     * Folder name.
     */
    private final String folderName = Environment.getExternalStorageDirectory() + "/KAM/";

    /**
     * Folder name.
     *
     * @return the file
     */
    public File folderName() {
        File file = new File(folderName);
        if (!file.exists())
            file.mkdir();
        return file;
    }

    public String getBaseFolderName() {
        return folderName;
    }

    /**
     * Gets file from path.
     *
     * @param path path
     * @return file from path
     */
    public File getFile(String path) {
        return new File(path);
    }


    /**
     * Gets full image path.
     *
     * @param path path
     * @return full image path
     */
    private String getJpgImagePath(String path) {
        return path + ".jpg";
    }

    /**
     * Delete file.
     *
     * @param path path
     */
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

    /**
     * Delete files.
     *
     * @param paths paths
     */
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

    /**
     * Generate file name.
     *
     * @return the string
     */
    public String generateFileName() {
        return getJpgImagePath("mobiz-" + String.valueOf(System.currentTimeMillis()));
    }

    /**
     * Gets final name.
     *
     * @return final name
     */
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

    /**
     * Check file.
     *
     * @param path path
     * @return the boolean
     */
    public boolean exists(String path) {
        return getFile(path) != null && getFile(path).exists();
    }
}
