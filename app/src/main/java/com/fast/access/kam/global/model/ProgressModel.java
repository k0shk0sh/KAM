package com.fast.access.kam.global.model;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class ProgressModel {
    private String fileName;
    private int progress;
    private int max;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
