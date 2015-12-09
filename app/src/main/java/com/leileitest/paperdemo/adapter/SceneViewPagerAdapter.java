package com.leileitest.paperdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.widget.WrapHeightGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * sunjie
 * 2015/11/26
 * 21:25
 */
public class SceneViewPagerAdapter<T> extends PagerAdapter {

    private int row = 1;
    private Context context;
    private List<T> list;
    private LayoutInflater mInflater;
    private final int pageItemCount;
    private List<View> viewList;


    public SceneViewPagerAdapter(Context context, List<T> list, int pageItemCount) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(list);
        this.pageItemCount = pageItemCount;
        viewList = new ArrayList<>();
    }

    public SceneViewPagerAdapter(Context context, List<T> list, int pageItemCount, int row) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.pageItemCount = pageItemCount;
        this.row = row;
        viewList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        int size = 0;
        if (list != null) {
            size = list.size();
            size = (int) Math.ceil(size / (double) pageItemCount);
        }
        return size;
    }

    @Override
    public int getItemPosition(Object object) {
        // object recyclerView
        View view = (View) object;
        if (view != null) {
            boolean shouldUpdate = (boolean) view.getTag(R.id.saveShouldUpdateViewPager);
            if (shouldUpdate) {
                return POSITION_NONE;
            }
        }
        return POSITION_UNCHANGED;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        if (view != null) {
            viewList.remove(view);
            container.removeView(view);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        @SuppressLint("InflateParams")
        View convertView = mInflater.inflate(R.layout.scene_viewpager, container, false);
        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WrapHeightGridLayoutManager manager = new WrapHeightGridLayoutManager(context,
                pageItemCount / row, GridLayoutManager.VERTICAL, false);
        manager.setSpanRows(row);
        recyclerView.setLayoutManager(manager);
        int endPoint = position * pageItemCount + pageItemCount;
        endPoint = endPoint > list.size() ? list.size() : endPoint;
        @SuppressWarnings("unchecked")
        ScenePageItemAdapter adapter = new ScenePageItemAdapter(context,
                (List<Object>) list.subList(position * pageItemCount, endPoint), row);
        adapter.setViewPager((ViewPager) container);
        recyclerView.setAdapter(adapter);
        container.addView(convertView);
        //with tag
        convertView.setTag(R.id.savePos, position);
        convertView.setTag(R.id.saveShouldUpdateViewPager, false);
        viewList.add(convertView);
        return convertView;
    }

    @SuppressWarnings("unchecked")
    public void setList(List<T> sceneList) {
        this.list = sceneList;
        //此方法无效
        //此处要强制刷新
        //找到拿出需要刷新
        for (View view : viewList) {
            view.setTag(R.id.saveShouldUpdateViewPager, true);
        }
        notifyDataSetChanged();
    }
}