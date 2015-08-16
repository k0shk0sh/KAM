package com.fast.access.kam.global.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;

import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.global.tasks.impl.IAppFetcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class ApplicationFetcher extends AsyncTask<AppsModel, AppsModel, List<AppsModel>> {

    private Context context;
    private IAppFetcher iAppFetcher;

    public ApplicationFetcher(Context context, IAppFetcher iAppFetcher) {
        this.context = context;
        this.iAppFetcher = iAppFetcher;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (iAppFetcher != null) iAppFetcher.onStartFetching();
    }

    @Override
    protected void onPostExecute(List<AppsModel> appsModel) {
        super.onPostExecute(appsModel);
        if (iAppFetcher != null) iAppFetcher.onFinish(appsModel);
    }

    @Override
    protected void onProgressUpdate(AppsModel... values) {
        super.onProgressUpdate(values);
        if (iAppFetcher != null) iAppFetcher.onUpdate(values[0]);
    }

    @Override
    protected List<AppsModel> doInBackground(AppsModel... params) {
        List<AppsModel> appsModels = new ArrayList<>();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(list, new ResolveInfo.DisplayNameComparator(pm));
        for (ResolveInfo resolveInfo : list) {
            AppsModel model = new AppsModel();
            File file = new File(resolveInfo.activityInfo.applicationInfo.publicSourceDir);
            model.setFileName(file);
            model.setName(resolveInfo.loadLabel(pm).toString());
            model.setDrawable(resolveInfo.loadIcon(pm));
            publishProgress(model);
            appsModels.add(model);
        }
        return appsModels;
    }
}
