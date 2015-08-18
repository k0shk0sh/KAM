package com.fast.access.kam.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.model.AppsModel;

import net.grobas.view.PolygonImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class AppDetailsActivity extends AppCompatActivity {

    @Bind(R.id.appIcon)
    PolygonImageView appIcon;
    @Bind(R.id.iconBackground)
    FrameLayout iconBackground;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_details);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            finish();
            return;
        }
        AppsModel appsModel = getIntent().getExtras().getParcelable("AppsModel");
        if (appsModel != null) {
            Palette palette = Palette.from(AppHelper.getBitmap(this, appsModel.getPackageName())).generate();
            iconBackground.setBackgroundColor(palette.getLightMutedColor(R.color.primary));
            collapsingToolbar.setTitle(appsModel.getName());
            appIcon.setImageDrawable(AppHelper.getDrawable(this, appsModel.getPackageName()));
        } else {
            finish();
        }
    }

}
