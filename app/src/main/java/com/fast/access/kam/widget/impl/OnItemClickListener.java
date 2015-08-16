package com.fast.access.kam.widget.impl;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fast.access.kam.R;

/**
 * Created by Kosh on 8/16/2015. copyrights are reserved
 */
public class OnItemClickListener {

    private final RecyclerView mRecyclerView;
    private onClick mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };
    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                return mOnItemLongClickListener.onItemLongClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
            return false;
        }
    };
    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    private OnItemClickListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(R.id.item_click_support, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static com.fast.access.kam.widget.impl.OnItemClickListener addTo(RecyclerView view) {
        com.fast.access.kam.widget.impl.OnItemClickListener support = (com.fast.access.kam.widget.impl.OnItemClickListener) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new com.fast.access.kam.widget.impl.OnItemClickListener(view);
        }
        return support;
    }

    public static com.fast.access.kam.widget.impl.OnItemClickListener removeFrom(RecyclerView view) {
        com.fast.access.kam.widget.impl.OnItemClickListener support = (com.fast.access.kam.widget.impl.OnItemClickListener) view.getTag(R.id.item_click_support);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    public com.fast.access.kam.widget.impl.OnItemClickListener setOnItemClickListener(onClick listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public com.fast.access.kam.widget.impl.OnItemClickListener setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(R.id.item_click_support, null);
    }

    public interface onClick {

        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

    public interface OnItemLongClickListener {

        boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
    }
}