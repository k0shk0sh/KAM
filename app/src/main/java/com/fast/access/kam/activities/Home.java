package com.fast.access.kam.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fast.access.kam.R;
import com.fast.access.kam.global.adapter.AppsAdapter;
import com.fast.access.kam.global.helper.FileUtil;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.global.tasks.ApplicationFetcher;
import com.fast.access.kam.global.tasks.impl.IAppFetcher;
import com.fast.access.kam.widget.impl.OnItemClickListener;
import com.fast.access.kam.widget.impl.RecyclerScrollListener;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity implements IAppFetcher {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.progress)
    ProgressBar progress;
    private GridLayoutManager manager;
    private AppsAdapter adapter;

    @OnClick(R.id.fab)
    public void onRefresh() {
        new ApplicationFetcher(this, this).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        manager = new GridLayoutManager(this, 1);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(manager);
        recycler.addOnScrollListener(onScroll);
        adapter = new AppsAdapter(new ArrayList<AppsModel>());
        recycler.setAdapter(adapter);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        OnItemClickListener.addTo(recycler).setOnItemClickListener(onClick);
        new ApplicationFetcher(this, this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.change_span:
                if (manager != null) {
                    if (manager.getSpanCount() == 2) {
                        manager.setSpanCount(1);
                        item.setIcon(R.drawable.ic_list);
                    } else {
                        item.setIcon(R.drawable.ic_grid);
                        manager.setSpanCount(2);
                    }
                    manager.requestLayout(); // must be called otherwise, exception!!!
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private RecyclerScrollListener onScroll = new RecyclerScrollListener() {
        @Override
        public void onHide() {
            if (fab != null) fab.hide();
        }

        @Override
        public void onShow() {
            if (fab != null) fab.show();
        }
    };

    private OnItemClickListener.onClick onClick = new OnItemClickListener.onClick() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            AppsModel apps = adapter.getModelList().get(position);
            FileUtil fileUtil = new FileUtil();
            File file = fileUtil.generateFile(apps.getFileName().getName());
            if (file != null && !file.exists()) {
                try {
                    FileUtils.copyFile(apps.getFileName(), file);
                    Snackbar.make(mainContent, apps.getName() + " " + apps.getFileName(), Snackbar.LENGTH_INDEFINITE).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar.make(mainContent, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        }
    };

    @Override
    public void onStartFetching() {
        progress.setVisibility(View.VISIBLE);
        fab.setEnabled(false);
    }

    @Override
    public void onUpdate(AppsModel appsModel) {
        if (appsModel != null) {
            if (adapter != null) {
                adapter.insert(appsModel);
            }
        }
    }

    @Override
    public void onFinish(List<AppsModel> appsModels) {
        progress.setVisibility(View.GONE);
        fab.setEnabled(true);
    }
}
