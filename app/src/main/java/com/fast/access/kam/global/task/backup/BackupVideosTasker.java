package com.fast.access.kam.global.task.backup;

import android.content.Context;
import android.os.AsyncTask;

import com.fast.access.kam.global.model.ProgressModel;

import com.fast.access.kam.global.task.impl.OnTaskLoading;

/**
 * Created by Kosh on 8/24/2015. copyrights are reserved
 */
public class BackupVideosTasker extends AsyncTask<Void, ProgressModel, ProgressModel> {

    private Context context;
    private OnTaskLoading onTaskLoading;


    public BackupVideosTasker(Context context, OnTaskLoading onTaskLoading) {
        this.context = context;
        this.onTaskLoading = onTaskLoading;

    }

    @Override
    protected ProgressModel doInBackground(Void... params) {
        return null;
    }
}
