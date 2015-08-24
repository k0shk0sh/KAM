package com.fast.access.kam.global.task.restore;

import android.content.Context;
import android.os.AsyncTask;

import com.fast.access.kam.global.model.ProgressModel;
import com.fast.access.kam.global.task.impl.OnProgress;
import com.fast.access.kam.global.task.impl.OnTaskLoading;

/**
 * Created by Kosh on 8/24/2015. copyrights are reserved
 */
public class RestoreContactsTasker extends AsyncTask<Void, ProgressModel, ProgressModel> {

    private Context context;
    private OnTaskLoading onTaskLoading;
    private OnProgress onProgress;

    public RestoreContactsTasker(Context context, OnTaskLoading onTaskLoading, OnProgress onProgress) {
        this.context = context;
        this.onTaskLoading = onTaskLoading;
        this.onProgress = onProgress;
    }

    @Override
    protected ProgressModel doInBackground(Void... params) {
        return null;
    }
}
