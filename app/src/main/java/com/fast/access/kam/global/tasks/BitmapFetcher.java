package com.fast.access.kam.global.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.helper.BitmapCache;

/**
 * Created by Kosh on 8/17/2015. copyrights are reserved
 */
public class BitmapFetcher extends AsyncTask<String, String, Bitmap> {
    private ImageView appIcon;
    private BitmapCache bitmapCache;
    private Context context;


    public BitmapFetcher(Context context, ImageView appIcon, BitmapCache bitmapCache) {
        this.appIcon = appIcon;
        this.bitmapCache = bitmapCache;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            appIcon.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = AppHelper.getBitmap(context, params[0]);
        bitmapCache.putBitmap(params[0], bitmap);
        return bitmap;
    }
}
