package com.fast.access.kam.global.loader.impl;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public interface OnTaskLoading {

    void onPreExecute();

    void onPostExecute(boolean isBackup);

    void onErrorExecuting(String msg, boolean isBackup);
}
