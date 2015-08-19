package com.fast.access.kam.global.loader.cache;

import android.content.Context;

import com.fast.access.kam.global.model.AppsModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 8/19/2015. copyrights are reserved
 */
public class DiskCacheAppList {
    private final Context mContext;

    public DiskCacheAppList(Context context) {
        mContext = context;
    }

    public List<AppsModel> getAppList(AppIconCache iconCache) {
        List<AppsModel> appList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(mContext.openFileInput("app_list")));
            String lineBuffer;
            while ((lineBuffer = br.readLine()) != null) {
                String[] infos = lineBuffer.split(",");
                AppsModel model = new AppsModel();
                model.setDrawable(new AppIcon(iconCache, infos[0]));
                model.setPackageName(infos[0]);
                model.setName(infos[1]);
                appList.add(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return appList;
    }

    public void save(List<AppsModel> list) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(mContext.openFileOutput("app_list", Context.MODE_PRIVATE)));
            for (AppsModel app : list) {
                bw.write(app.getPackageName());
                bw.write(",");
                bw.write(app.getName().replace(",", ""));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
