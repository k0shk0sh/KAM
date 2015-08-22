package com.fast.access.kam.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.fast.access.kam.global.adapter.AppDetailsAdapter;
import com.fast.access.kam.global.model.AppDetailModel;

/**
 * Created by kosh20111 on 7/31/2015. CopyRights @ Innov8tif
 */
public class ParallaxRecyclerView extends RecyclerView {

    public ParallaxRecyclerView(Context context) {
        super(context);
    }

    public ParallaxRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParallaxRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getAdapter() instanceof AppDetailsAdapter) {
                    AppDetailsAdapter adapter = (AppDetailsAdapter) recyclerView.getAdapter();
                    int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (adapter.getItemViewType(position) == AppDetailModel.HEADER) {
                        View view = recyclerView.getChildAt(position);
                        if (view != null)
                            view.setTranslationY(-view.getTop() / 2);
                    }
                }
            }
        });
    }

}
