package com.leileitest.paperdemo.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.adapter.IBaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.luckyandyzhang.cleverrecyclerview.CleverRecyclerView;

/**
 * Created by liulei
 * 21:52
 * 2015/11/30
 */
public class CleverRecyclerViewDemoActivity extends BaseActivity {

    private CleverRecyclerView mCleverRecyclerView;
    private CleverAdapter cleverAdapter;

    @Override
    public void initView() {
        mCleverRecyclerView = findView(R.id.mCleverRecyclerView);
        mCleverRecyclerView.setOrientation(RecyclerView.VERTICAL);
        cleverAdapter = new CleverAdapter(this, null);
        mCleverRecyclerView.setAdapter(cleverAdapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_clever_recycler;
    }

    @Override
    public void configUi() {
        super.configUi();
        /**
         * 设置一页可以显示的item的数量
         * <p>注意：此方法必须在{@link CleverRecyclerView#setAdapter(Adapter)}之后调用
         */
        mCleverRecyclerView.setVisibleChildCount(5);
    }

    @Override
    public void obtainData() {
        super.obtainData();
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            stringList.add((i + 1) + "");
        }
        cleverAdapter.addItemAll(stringList);
        cleverAdapter.notifyDataSetChanged();
    }

    public class CleverAdapter extends IBaseRecyclerAdapter<String, CleverAdapter.CleverHolder> {

        public CleverAdapter(Context context, List<String> stringList) {
            super(context, stringList);
        }

        @Override
        protected CleverHolder onCreateViewHolder(ViewGroup parent,
                                                  LayoutInflater layoutInflater,
                                                  int viewType) {
            return new CleverHolder(layoutInflater.inflate(R.layout.layout_item_forclever,
                    parent, false));
        }

        public class CleverHolder extends RecyclerView.ViewHolder {

            TextView itemText;

            public CleverHolder(View itemView) {
                super(itemView);
                itemText = findView(itemView, R.id.mText);
            }
        }
    }
}
