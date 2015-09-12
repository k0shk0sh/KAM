package com.fast.access.kam.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;

import com.fast.access.kam.AppController;
import com.fast.access.kam.BuildConfig;
import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.model.EventsModel;
import com.fast.access.kam.widget.colorpicker.dashclockpicker.ColorPreference;
import com.fast.access.kam.widget.colorpicker.dashclockpicker.ColorSelector;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Kosh on 8/22/2015. copyrights are reserved
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, ColorSelector {
    private File file;
    private Preference fileSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_settings);
        findPreference("version").setSummary(BuildConfig.VERSION_NAME);
        FileUtil fileUtil = new FileUtil();
        file = new File(fileUtil.getBaseFolderName());
        fileSize = findPreference("size");
        fileSize.setOnPreferenceClickListener(this);
        findPreference("libraries").setOnPreferenceClickListener(this);
        getPreferenceManager().findPreference("dark_theme").setOnPreferenceClickListener(this);
        ColorPreference primary = (ColorPreference) getPreferenceManager().findPreference("primary_color");
        ColorPreference accent = (ColorPreference) getPreferenceManager().findPreference("accent_color");
        primary.onColorSelect(this);
        accent.onColorSelect(this);
        calculateFolderSize(false);
    }

    private void calculateFolderSize(final boolean deleteDir) {
        new AsyncTask<Long, Long, Long>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                fileSize.setSummary("Calculating...");
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                fileSize.setSummary("File Location: " + file.getAbsolutePath() + "\nFile Size: " + Formatter.formatFileSize(getActivity(), aLong));
            }

            @Override
            protected Long doInBackground(Long... params) {
                if (deleteDir) {
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return AppHelper.getFolderSize(file);
            }
        }.execute();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "libraries":
                LibsBuilder libsBuilder = new LibsBuilder()
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .withActivityTitle(getString(R.string.open_source_libraries))
                        .withAboutIconShown(true)
                        .withAboutVersionShown(true)
                        .withAnimations(true);
                if (!AppHelper.isDarkTheme(getActivity())) {
                    libsBuilder.withActivityTheme(R.style.AboutActivity);
                } else {
                    libsBuilder.withActivityTheme(R.style.AboutActivityDark);
                }
                libsBuilder.start(getActivity());
                return true;
            case "dark_theme":
                post();
                getActivity().recreate();
                return true;
            case "size":
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Folder?")
                        .setMessage("After deleting this file you won't be able to restore any app")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                calculateFolderSize(true);
                            }
                        }).setNegativeButton("Nah!", null)
                        .show();
                return true;
        }
        return false;
    }

    @Override
    public void onColorSelected(int color) {
        post();
        getActivity().recreate();
    }

    private void post() {
        EventsModel eventsModel = new EventsModel();
        eventsModel.setEventType(EventsModel.EventType.THEME);
        AppController.getController().getBus().post(eventsModel);
    }
}
