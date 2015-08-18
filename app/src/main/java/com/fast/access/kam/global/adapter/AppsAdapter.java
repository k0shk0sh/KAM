package com.fast.access.kam.global.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.fast.access.kam.AppController;
import com.fast.access.kam.R;
import com.fast.access.kam.global.helper.AppHelper;
import com.fast.access.kam.global.model.AppsModel;
import com.fast.access.kam.widget.EmptyHolder;
import com.fast.access.kam.widget.impl.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class AppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<AppsModel> searchList;

    public List<AppsModel> getModelList() {
        return modelList;
    }

    private List<AppsModel> modelList;
    private OnItemClickListener onClick;
    private ImageLoader imageLoader;

    public AppsAdapter(OnItemClickListener onClick, List<AppsModel> modelList) {
        this.modelList = modelList;
        this.onClick = onClick;
        this.imageLoader = AppController.getController().getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == EmptyHolder.EMPTY_TYPE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            return new EmptyHolder(v);
        }
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_list_items, parent, false);
        return new AppsHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) != EmptyHolder.EMPTY_TYPE) {
            AppsHolder h = (AppsHolder) holder;
            AppsModel app = modelList.get(position);
            if (app != null) {
                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClick.onItemClickListener(v, position);
                    }
                });
                if (app.getImageLocation() != null)
                    imageLoader.displayImage(ImageDownloader.Scheme.FILE.wrap(app.getImageLocation()), h.appIcon);
                else
                    h.appIcon.setImageDrawable(AppHelper.getDrawable(h.appIcon.getContext(), app.getPackageName()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return modelList == null || modelList.size() == 0 ? 1 : modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (modelList == null || modelList.size() == 0) {
            return EmptyHolder.EMPTY_TYPE;
        }
        return super.getItemViewType(position);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            final FilterResults oReturn = new FilterResults();
            final List<AppsModel> results = new ArrayList<>();
            if (searchList == null) {
                searchList = modelList;
            }
            if (charSequence != null) {
                if (searchList != null && searchList.size() > 0) {
                    for (final AppsModel appInfo : searchList) {
                        if (appInfo.getName().toLowerCase().contains(charSequence.toString())) {
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

        AppsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
