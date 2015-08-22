package com.fast.access.kam.activities.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;

import butterknife.ButterKnife;

/**
 * Created by Kosh on 8/22/2015. copyrights are reserved
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int layout();

    protected abstract boolean canBack();

    protected abstract boolean hasMenu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppHelper.isDarkTheme(this)) {
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(layout());
        ButterKnife.bind(this);
        if (AppHelper.isLollipop()) {
            getWindow().setNavigationBarColor(AppHelper.getAccentColor(this));
            if (canBack()) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(AppHelper.getPrimaryDarkColor(AppHelper.getPrimaryColor(this)));
            }
        }
        if (findViewById(R.id.toolbar) != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(AppHelper.getPrimaryColor(this));
            setSupportActionBar(toolbar);
            final ActionBar ab = getSupportActionBar();
            if (ab != null) {
                if (canBack()) {
                    ab.setHomeAsUpIndicator(R.drawable.ic_back);
                    ab.setDisplayHomeAsUpEnabled(true);
                } else {
                    ab.setHomeAsUpIndicator(R.drawable.ic_menu);
                    ab.setDisplayHomeAsUpEnabled(true);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (hasMenu()) {
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (canBack()) {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
