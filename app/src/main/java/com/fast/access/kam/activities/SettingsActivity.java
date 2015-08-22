package com.fast.access.kam.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.fast.access.kam.R;
import com.fast.access.kam.activities.base.BaseActivity;
import com.fast.access.kam.fragments.SettingsFragment;
import com.fast.access.kam.global.helper.AppHelper;

/**
 * Created by Kosh on 8/22/2015. copyrights are reserved
 */
public class SettingsActivity extends BaseActivity {
    @Override
    protected int layout() {
        return R.layout.settings_layout;
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppHelper.isDarkTheme(this)) {
            setTheme(R.style.AboutActivityDark);
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment())
                    .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                    .commit();
        }
    }
}
