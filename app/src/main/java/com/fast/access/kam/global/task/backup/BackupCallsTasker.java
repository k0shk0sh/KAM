package com.fast.access.kam.global.task.backup;

import android.content.Context;
import android.os.AsyncTask;

import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.model.ProgressModel;
import com.fast.access.kam.global.task.impl.OnProgress;
import com.fast.access.kam.global.task.impl.OnTaskLoading;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;

import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;

/**
 * Created by Kosh on 8/24/2015. copyrights are reserved
 */
public class BackupCallsTasker extends AsyncTask<Void, ProgressModel, ProgressModel> {

    private final String TAG = "BackupCallsTasker";
    private Context context;
    private OnTaskLoading onTaskLoading;
    private OnProgress onProgress;
    private CallsProvider callsProvider;
    ZipFile zipFile;

    public BackupCallsTasker(Context context, OnTaskLoading onTaskLoading, OnProgress onProgress) {
        this.context = context;
        this.onTaskLoading = onTaskLoading;
        this.onProgress = onProgress;
    }

    @Override
    protected ProgressModel doInBackground(Void... params) {
        InputStream stream = null;
        String callsToSave = null;
        try {
            FileUtil fileUtil = new FileUtil();
            Gson gson = new GsonBuilder().serializeNulls()
                    .excludeFieldsWithModifiers(Modifier.FINAL | Modifier.TRANSIENT)
                    .setPrettyPrinting()
                    .create();
            zipFile = new ZipFile(fileUtil.generateZipFile("calls"));
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
            parameters.setFileNameInZip("calls.json");
            parameters.setSourceExternalStream(true);
            CallsProvider callsProvider = new CallsProvider(context);
            if (callsProvider.getCalls() != null) {
                for (Call calls : callsProvider.getCalls().getList()) {
                    if (calls != null) {
                        callsToSave += gson.toJson(calls);
                    }
                }
            }
            if (callsToSave != null) {
                stream = IOUtils.toInputStream(callsToSave, Charsets.UTF_8);
                zipFile.addStream(stream, parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
