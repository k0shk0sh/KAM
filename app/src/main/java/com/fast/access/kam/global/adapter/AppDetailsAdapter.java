package com.fast.access.kam.global.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.model.AppDetailModel;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.widget.FontTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kosh on 8/22/2015. copyrights are reserved
 */
public class AppDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AppDetailModel> appDetails;
    private int backgroundColor;

    public AppDetailsAdapter(List<AppDetailModel> appDetails, int backgroundColor) {
        this.appDetails = appDetails;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == AppDetailModel.HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_app_details, parent, false);
            return new HeaderHolder(v);
        }
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_items, parent, false);
        return new PermissionsHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == AppDetailModel.HEADER) {
            HeaderHolder holder = (HeaderHolder) viewHolder;
            AppsModel model = appDetails.get(position).getAppsModel();
            if (model != null) {
                holder.appName.setText(model.getAppName());
                holder.appIcon.setImageDrawable(AppHelper.getDrawable(holder.itemView.getContext(), model.getPackageName()));
                holder.versionCode.setText("Version Code\n" + model.getVersionCode());
                holder.versionName.setText("Version Name\n" + model.getVersionName());
                holder.firstInstall.setText("Installtion Time\n" + AppHelper.prettifyDate(model.getFirstInstallTime()));
                holder.lastUpdate.setText("Update Time\n" + AppHelper.prettifyDate(model.getLastUpdateTime()));
            }
        } else {
            PermissionsHolder holder = (PermissionsHolder) viewHolder;
            String info = appDetails.get(position).getAppPermission();
            holder.permissionName.setText(info != null ? info : "N/A");
            holder.parallaxBackground.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public int getItemCount() {
        return appDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        return appDetails != null && appDetails.size() != 0 ? appDetails.get(position).getType() : super.getItemViewType(position);
    }

    public void insert(List<AppDetailModel> apps) {
        appDetails.clear();
        appDetails.addAll(apps);
        notifyDataSetChanged();
    }

    public List<AppDetailModel> getAppDetails() {
        return appDetails;
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.appIcon)
        ImageView appIcon;
        @Bind(R.id.appName)
        FontTextView appName;
        @Bind(R.id.versionName)
        FontTextView versionName;
        @Bind(R.id.versionCode)
        FontTextView versionCode;
        @Bind(R.id.firstInstall)
        FontTextView firstInstall;
        @Bind(R.id.lastUpdate)
        FontTextView lastUpdate;

        HeaderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'list_layout_items.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class PermissionsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.permissionName)
        FontTextView permissionName;
        @Bind(R.id.parallaxBackground)
        View parallaxBackground;

        PermissionsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
