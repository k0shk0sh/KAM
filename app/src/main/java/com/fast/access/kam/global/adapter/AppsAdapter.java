package com.fast.access.kam.global.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.widget.impl.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<AppsModel> searchableList;
    private List<AppsModel> modelList;
    private OnItemClickListener onClick;
    private SparseBooleanArray selectedPositions = new SparseBooleanArray();

    public void setItemChecked(int position, boolean isActivated) {
        selectedPositions.put(position, isActivated);
        notifyDataSetChanged();
    }

    public boolean isItemChecked(int position) {
        return selectedPositions.get(position);
    }

    public void clearSelection() {
        selectedPositions.clear();
        notifyDataSetChanged();
    }


    public AppsAdapter(OnItemClickListener onClick, List<AppsModel> modelList) {
        this.modelList = modelList;
        this.onClick = onClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_list_items, parent, false);
        return new AppsHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AppsHolder h = (AppsHolder) holder;
        AppsModel app = modelList.get(position);
        if (app != null) {
            h.iconHolder.setBackground(AppHelper.selector(AppHelper.getAccentColor(h.itemView.getContext())));
            if (isItemChecked(position)) {
                h.iconHolder.setActivated(true);
            } else {
                h.iconHolder.setActivated(false);
            }
            h.appIcon.setImageDrawable(new BitmapDrawable(h.iconHolder.getResources(), app.getBitmap()));
            h.appIcon.setContentDescription(app.getAppName());
            h.iconHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPositions.size() == 0)
                        onClick.onItemClickListener(v, position);
                    else
                        h.iconHolder.performLongClick();
                }
            });
            h.iconHolder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClick.onItemLongClickListener(v, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void insert(List<AppsModel> apps) {
        modelList.clear();
        modelList.addAll(apps);
        notifyDataSetChanged();
    }

    public void insert(AppsModel model) {
        modelList.add(model);
        if (getItemCount() < 2) { // we always going to have the first view as an empty after each restart of app.
            notifyItemInserted(modelList.size());
        } else {
            notifyItemInserted(modelList.size() - 1);
        }
    }

    public void clearAll() {
        modelList.clear();
        notifyDataSetChanged();
    }

    public void remove(AppsModel m) {
        modelList.remove(m);
        notifyDataSetChanged();
    }

    public List<AppsModel> getModelList() {
        return modelList;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            final FilterResults oReturn = new FilterResults();
            final List<AppsModel> results = new ArrayList<>();
            if (searchableList == null) {
                searchableList = modelList;
            }
            if (charSequence != null) {
                if (searchableList != null && searchableList.size() > 0) {
                    for (final AppsModel appInfo : searchableList) {
                        if (appInfo.getAppName().toLowerCase().contains(charSequence.toString())) {
                            results.add(appInfo);
                        }
                    }
                }
                oReturn.values = results;
                oReturn.count = results.size();
            }
            return oReturn;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            modelList = (List<AppsModel>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    static class AppsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.appIcon)
        ImageView appIcon;
        @Bind(R.id.iconHolder)
        FrameLayout iconHolder;


        AppsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
