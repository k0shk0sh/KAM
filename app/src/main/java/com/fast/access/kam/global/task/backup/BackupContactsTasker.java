package com.fast.access.kam.global.task.backup;

import android.content.Context;
import android.os.AsyncTask;

import com.fast.access.kam.global.model.ProgressModel;
import com.fast.access.kam.global.task.impl.OnTaskLoading;

import net.lingala.zip4j.core.ZipFile;


/**
 * Created by Kosh on 8/24/2015. copyrights are reserved
 */
public class BackupContactsTasker extends AsyncTask<Void, ProgressModel, ProgressModel> {

    private final String TAG = "BackupCallsTasker";
    private Context context;
    private OnTaskLoading onTaskLoading;
    private ZipFile zipFile;

    public BackupContactsTasker(Context context, OnTaskLoading onTaskLoading) {
        this.context = context;
        this.onTaskLoading = onTaskLoading;
    }

    @Override
    protected ProgressModel doInBackground(Void... params) {
//        InputStream stream = null;
//        try {
//            FileUtil fileUtil = new FileUtil();
//            Gson gson = new GsonBuilder().serializeNulls()
//                    .excludeFieldsWithModifiers(Modifier.FINAL | Modifier.TRANSIENT)
//                    .setPrettyPrinting()
//                    .create();
//            zipFile = new ZipFile(fileUtil.generateZipFile("contacts"));
//            if (!zipFile.isValidZipFile()) {
//                if (zipFile.getFile() != null && zipFile.getFile().exists())
//                    zipFile.getFile().delete();
//            }
//            ZipParameters parameters = new ZipParameters();
//            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
//            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
//            parameters.setFileNameInZip("contacts.json");
//            parameters.setSourceExternalStream(true);
//            ContactsProvider callsProvider = new ContactsProvider(context);
//            if (callsProvider.getContacts() != null) {
//                if (callsProvider.getContacts().getList() != null) {
//                    stream = IOUtils.toInputStream(gson.toJson(callsProvider.getContacts().getList()), Charsets.UTF_8);
//                    zipFile.addStream(stream, parameters);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return null;
    }
}
