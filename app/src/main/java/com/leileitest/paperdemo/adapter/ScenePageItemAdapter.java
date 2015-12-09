package com.leileitest.paperdemo.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.entity.CollectItem;
import com.leileitest.paperdemo.entity.LastItem;
import com.leileitest.paperdemo.entity.SenceInfo;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * think
 * 2015/12/9
 * 19:25
 */
public class ScenePageItemAdapter extends IBaseRecyclerAdapter<Object,
        ScenePageItemAdapter.ItemViewHolder> {

    private int row = 1;

    private WeakReference<RecyclerView> mRecyclerViewRef;
    private WeakReference<ViewPager> mViewPagerRef;

    public ScenePageItemAdapter(Context context, List<Object> pagerItemlistScene, int row) {
        super(context, pagerItemlistScene);
        this.row = row;
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPagerRef = new WeakReference<>(mViewPager);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerViewRef = new WeakReference<>(recyclerView);
    }

    @Override
    protected ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                LayoutInflater layoutInflater,
                                                int viewType) {
        View view = layoutInflater.inflate(R.layout.main_scene_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Object itemInfo = getItemAtPosition(position);
        if (itemInfo instanceof SenceInfo) {
            SenceInfo sceneInfo = (SenceInfo) itemInfo;
            holder.textView.setText(sceneInfo.getSenceName());
            holder.icon.setImageResource(Integer.valueOf(sceneInfo.getSenceIconPath()));
            holder.icon.setVisibility(View.VISIBLE);

        } else if (itemInfo instanceof CollectItem) {

            CollectItem collectItem = (CollectItem) itemInfo;
            holder.textView.setText(collectItem.getName());

            if (collectItem.getIconPath() == 0) {
                String zoneType = collectItem.getZoneType();
                String accountType = collectItem.getAccountType();
                if (!TextUtils.isEmpty(zoneType) && !zoneType.equals("0"))
                    accountType = zoneType;
                try {
                    int type = Integer.parseInt(accountType);
                } catch (NumberFormatException ignored) {
                }
            } else {
                holder.icon.setImageResource(collectItem.getIconPath());
            }
            holder.icon.setVisibility(View.VISIBLE);

        } else if (itemInfo instanceof LastItem) {

            LastItem lastItem = (LastItem) itemInfo;
            holder.textView.setText(lastItem.getNotice());
            holder.icon.setVisibility(View.GONE);

            RecyclerView recyclerView = mRecyclerViewRef != null ? mRecyclerViewRef.get() : null;
            if (recyclerView != null) {
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                ViewPager viewPager = mViewPagerRef.get();
                //在row为1的时候出问题
                if (mViewPagerRef != null && viewPager != null) {
                    params.height = viewPager.getMeasuredHeight() / (row > 1 ? row : 1);
                    holder.itemView.setLayoutParams(params);
                }
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView icon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.name_tv);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
