package com.leileitest.paperdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * USER: liulei
 * DATE: 2015/3/20.
 * TIME: 16:33
 */
public abstract class IBaseRecyclerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected Context context;
    protected List<?> dataList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener itemClickListener;

    @SuppressWarnings("unchecked")
    public <T> IBaseRecyclerAdapter(Context context, List<T> tList) {
        this.dataList = new ArrayList<Object>();
        if (tList != null) {
            ((List<T>) this.dataList).addAll(tList);
        }
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressWarnings("unchecked")
    public <T> void addItemAll(List<T> tList) {
        if (this.dataList == null)
            this.dataList = new ArrayList<T>();
        if (tList == null) return;
        ((List<T>) this.dataList).addAll(tList);
    }

    @SuppressWarnings("unchecked")
    public <T> void addItem(T t) {
        if (this.dataList == null)
            this.dataList = new ArrayList<T>();
        if (t == null) return;
        ((List<T>) this.dataList).add(t);
    }

    @SuppressWarnings("unchecked")
    public <T> void addItem(int index, T t) {
        if (this.dataList == null)
            this.dataList = new ArrayList<T>();
        if (t == null) return;
        ((List<T>) this.dataList).add(index, t);
    }

    @SuppressWarnings("unchecked")
    public <T> void removeItem(int index) {
        if (this.dataList == null) {
            this.dataList = new ArrayList<T>();
            return;
        }
        ((List<T>) this.dataList).remove(index);
    }

    @SuppressWarnings("unchecked")
    public <T> T getItemAtPosition(int postion) {
        return (T) this.dataList.get(postion);
    }

    public void clear() {
        if (this.dataList != null && this.dataList.size() > 0)
            this.dataList.clear();
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent, layoutInflater, viewType);
    }

    protected abstract VH onCreateViewHolder(ViewGroup parent, LayoutInflater layoutInflater, int viewType);

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performItemClick(v, position);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void performItemClick(View convertView, int position) {
        if (itemClickListener != null)
            itemClickListener.onItemClick(convertView, position);
    }

    protected void setOnClickListener(View.OnClickListener clickListener, View... views) {
        for (View view : views)
            view.setOnClickListener(clickListener);
    }

    protected void setOnLongClickListener(View.OnLongClickListener longClickListener, View... views) {
        for (View view : views)
            view.setOnLongClickListener(longClickListener);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getDataList() {
        return (List<T>) dataList;
    }

    public interface OnItemClickListener {
        void onItemClick(View convertView, int position);
    }
}