package com.fast.access.kam.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fast.access.kam.R;
import com.fast.access.kam.activities.base.BaseActivity;
import com.fast.access.kam.fragments.MemberFragment;
import com.fast.access.kam.global.helper.AppHelper;

import butterknife.Bind;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by Kosh on 8/20/2015. copyrights are reserved
 */
public class TeamActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pager)
    VerticalViewPager pager;

    @Override
    protected int layout() {
        return R.layout.team_activity;
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
        if (AppHelper.isDarkTheme(this)) {
            setTheme(R.style.AboutActivityDark);
        }
        super.onCreate(savedInstanceState);
        pager.setAdapter(new TeamPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TeamPagerAdapter extends FragmentStatePagerAdapter {

        public TeamPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MemberFragment fragment = new MemberFragment();
            switch (position) {
                case 0:
                    fragment.setIsDeveloper(true);
                    return fragment;
                case 1:
                    fragment.setIsDeveloper(false);
                    return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
