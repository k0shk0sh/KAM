package com.fast.access.kam.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.format.Formatter;

import com.fast.access.kam.BuildConfig;
import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.widget.colorpicker.dashclockpicker.ColorPreference;
import com.fast.access.kam.widget.colorpicker.dashclockpicker.ColorSelector;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.io.File;

/**
 * Created by Kosh on 8/22/2015. copyrights are reserved
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, ColorSelector {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_settings);
        findPreference("version").setSummary(BuildConfig.VERSION_NAME);
        FileUtil fileUtil = new FileUtil();
        File file = new File(fileUtil.getBaseFolderName());
        long folderSize = AppHelper.getFolderSize(file);
        findPreference("size").setSummary("File Location: " + file.getAbsolutePath() + "\nFile Size: " + Formatter.formatFileSize(getActivity(),
                folderSize));
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
                getActivity().recreate();
                return true;
        }
        return false;
    }

    @Override
    public void onColorSelected(int color) {
        getActivity().recreate();
    }
}
