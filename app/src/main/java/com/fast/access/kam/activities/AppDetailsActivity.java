package com.fast.access.kam.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bowyer.app.fabtoolbar.FabToolbar;
import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.model.AppsModel;

import net.grobas.view.PolygonImageView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.fabtoolbar)
    FabToolbar fabtoolbar;
    private AppsModel appsModel;

    @OnClick(R.id.fab)
    public void onExpand() {
        fabtoolbar.expandFab();
    }

    @OnClick(R.id.extract)
    public void onExtract() {
        FileUtil fileUtil = new FileUtil();
        File file = fileUtil.generateFile(appsModel.getName());
        try {
            FileUtils.copyFile(appsModel.getFile(), file);
            showMessage("File extracted to KAM/" + appsModel.getName() + ".apk");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage(e.getMessage() != null ? e.getMessage() : "Error Extracting App");
        }
        fabtoolbar.slideOutFab();
    }

    @OnClick(R.id.share)
    public void onShare() {
        fabtoolbar.slideOutFab();
    }

    @OnClick(R.id.delete)
    public void onDelete() {
        fabtoolbar.slideOutFab();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_details);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            finish();
            return;
        }
        setSupportActionBar(toolbar);
        fabtoolbar.setFab(fab);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        appsModel = getIntent().getExtras().getParcelable("AppsModel");
        if (appsModel != null) {
            Palette palette = Palette.from(AppHelper.getBitmap(this, appsModel.getPackageName())).generate();
            iconBackground.setBackgroundColor(palette.getLightMutedColor(getResources().getColor(R.color.primary)));
            collapsingToolbar.setTitle(appsModel.getName());
            appIcon.setImageDrawable(AppHelper.getDrawable(this, appsModel.getPackageName()));
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showMessage(String msg) {
        final Snackbar snackbar = Snackbar.make(mainContent, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED).show();
        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

}
