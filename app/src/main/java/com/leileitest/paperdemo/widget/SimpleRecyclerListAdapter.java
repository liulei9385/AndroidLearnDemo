package com.leileitest.paperdemo.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.adapter.IBaseRecyclerAdapter;

import java.util.List;

/**
 * Created by liulei
 * 22:00
 * 2015/11/29
 */
public class SimpleRecyclerListAdapter extends IBaseRecyclerAdapter<String, SimpleRecyclerListAdapter.SimpleHolder> {

    private int layoutResId;
    private AdaperCallback callback;

    public void setCallback(AdaperCallback callback) {
        this.callback = callback;
    }

    public interface AdaperCallback {
        void onBindView(SimpleHolder holder, int position);
    }

    public SimpleRecyclerListAdapter(Context context, @LayoutRes int itemLayout, List<String> tList) {
        super(context, tList);
        this.layoutResId = itemLayout;
    }

    @Override
    protected SimpleHolder onCreateViewHolder(ViewGroup parent,
                                              LayoutInflater layoutInflater,
                                              int viewType) {
        View itemView = layoutInflater.inflate(layoutResId, parent, false);
        return new SimpleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = getItemAtPosition(position);
        holder.mTextView.setText(text);
        if (callback != null)
            callback.onBindView(holder, position);
    }

    public static class SimpleHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public SimpleHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }
    }

}