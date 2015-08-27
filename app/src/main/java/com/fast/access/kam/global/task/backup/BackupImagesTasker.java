package com.fast.access.kam.global.task.backup;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.model.ProgressModel;

import com.fast.access.kam.global.task.impl.OnTaskLoading;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * Created by Kosh on 8/24/2015. copyrights are reserved
 */
public class BackupImagesTasker extends AsyncTask<Void, ProgressModel, ProgressModel> {

    private Context context;
    private OnTaskLoading onTaskLoading;

    private ZipFile zipFile;

    public BackupImagesTasker(Context context, OnTaskLoading onTaskLoading) {
        this.context = context;
        this.onTaskLoading = onTaskLoading;

    }

    @Override
    protected ProgressModel doInBackground(Void... params) {
        try {
            FileUtil fileUtil = new FileUtil();
            zipFile = new ZipFile(fileUtil.generateZipFile("images"));
            if (!zipFile.isValidZipFile()) {
                if (zipFile.getFile() != null && zipFile.getFile().exists())
                    zipFile.getFile().delete();
            }
            String sdCard = Environment.getExternalStorageDirectory().getPath();
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
