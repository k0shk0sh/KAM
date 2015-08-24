package com.fast.access.kam.global.task.impl;

import com.fast.access.kam.global.model.ProgressModel;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public interface OnProgress {

    void onProgressUpdate(ProgressModel progressModel, boolean isBackup);

}
