package com.fast.access.kam.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.widget.Toast;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_settings);
        findPreference("version").setSummary(BuildConfig.VERSION_NAME);
        FileUtil fileUtil = new FileUtil();
        file = new File(fileUtil.getBaseFolderName());
        long folderSize = AppHelper.getFolderSize(file);
        findPreference("size").setSummary("File Location: " + file.getAbsolutePath() + "\nFile Size: " + Formatter.formatFileSize(getActivity(),
                folderSize));
        findPreference("size").setOnPreferenceClickListener(this);
        findPreference("libraries").setOnPreferenceClickListener(this);
        getPreferenceManager().findPreference("dark_theme").setOnPreferenceClickListener(this);
        ColorPreference primary = (ColorPreference) getPreferenceManager().findPreference("primary_color");
        ColorPreference accent = (ColorPreference) getPreferenceManager().findPreference("accent_color");
        primary.onColorSelect(this);
        accent.onColorSelect(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "libraries":
                new LibsBuilder()
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .withActivityTitle(getString(R.string.open_source_libraries))
                        .withAboutIconShown(true)
                        .withAboutVersionShown(true)
                        .withAnimations(true)
                        .withActivityTheme(R.style.AboutActivity)
                        .start(getActivity());
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
                                try {
                                    FileUtils.deleteDirectory(file);
                                    Toast.makeText(getActivity(), "Folder Deleted Successfully", Toast.LENGTH_LONG).show();
                                    long folderSize = AppHelper.getFolderSize(file);
                                    findPreference("size").setSummary("File Location: " + file.getAbsolutePath() + "\nFile Size: " + Formatter.formatFileSize(getActivity(),
                                            folderSize));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "Failed Deleting " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
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

    /**
     * tell home to recreate to reflect new theme.
     */
    private void post() {
        EventsModel eventsModel = new EventsModel();
        eventsModel.setEventType(EventsModel.EventType.THEME);
        AppController.getController().getBus().post(eventsModel);
    }
}
