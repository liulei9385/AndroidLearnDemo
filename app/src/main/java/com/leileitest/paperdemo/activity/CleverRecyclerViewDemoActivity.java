package com.leileitest.paperdemo.activity;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
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
    private List<String> pictureList;

    @Override
    public void initView() {
        mCleverRecyclerView = findView(R.id.mCleverRecyclerView);
        //mCleverRecyclerView.setOrientation(RecyclerView.VERTICAL);
        mCleverRecyclerView.setOrientation(RecyclerView.HORIZONTAL);
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
        mCleverRecyclerView.setVisibleChildCount(1);
        mCleverRecyclerView.setFlingFriction(0.65f);
    }

    @Override
    public void obtainData() {
        super.obtainData();
        List<String> stringList = new ArrayList<>();
        pictureList = new ArrayList<>();
        pictureList.add("http://imgsrc.baidu.com/forum/pic/item/9f510fb30f2442a72c033b31d143ad4bd1130211.jpg");
        pictureList.add("http://imgsrc.baidu.com/forum/pic/item/8dd7307adab44aed13fe4919b31c8701a38bfbee.jpg");
        pictureList.add("http://pica.nipic.com/2008-01-05/200815121123445_2.jpg");
        pictureList.add("http://e.hiphotos.baidu.com/zhidao/pic/item/38dbb6fd5266d01695ab94bf952bd40734fa35f2.jpg");
        pictureList.add("http://pic2.nipic.com/20090506/1055421_080356081_2.jpg");
        for (int i = 0; i < 5; i++) {
            stringList.add("Item" + (i + 1));
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

        @Override
        public void onBindViewHolder(CleverHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.itemText.setText(getItemAtPosition(position));
            holder.draweeView.setImageURI(Uri.parse(pictureList.get(position)));
        }

        public class CleverHolder extends RecyclerView.ViewHolder {

            TextView itemText;
            SimpleDraweeView draweeView;

            public CleverHolder(View itemView) {
                super(itemView);
                itemText = findView(itemView, R.id.mText);
                draweeView = findView(itemView, R.id.draweeView);
            }
        }
    }
}
