package com.fast.access.kam.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fast.access.kam.R;
import com.fast.access.kam.fragments.MemberFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by Kosh on 8/20/2015. copyrights are reserved
 */
public class TeamActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pager)
    VerticalViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        pager.setAdapter(new TeamPagerAdapter(getSupportFragmentManager()));
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
