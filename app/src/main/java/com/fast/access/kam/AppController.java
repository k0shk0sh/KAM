package com.fast.access.kam;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.StrictMode;

import com.fast.access.kam.activities.Home;
import com.fast.access.kam.global.helper.BitmapCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppController extends Application {

    private static AppController controller;
    private BitmapCache bitmapCache;

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        CustomActivityOnCrash.setRestartActivityClass(Home.class);
        CustomActivityOnCrash.install(this);
    }

    public static AppController getController() {
        return controller;
    }

    public ImageLoader getImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .writeDebugLogs()
                    .defaultDisplayImageOptions(getOptions())
                    .threadPriority(Thread.MAX_PRIORITY)
                    .denyCacheImageMultipleSizesInMemory()
                    .build();
            ImageLoader.getInstance().init(config);
        }
        return imageLoader;
    }

    /**
     * Gets options.
     *
     * @return the options
     */
    public DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .showImageForEmptyUri(R.drawable.ic_not_found)
                .showImageOnFail(R.drawable.ic_not_found)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

}
