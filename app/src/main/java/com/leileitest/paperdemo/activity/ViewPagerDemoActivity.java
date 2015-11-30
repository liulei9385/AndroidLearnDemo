package com.leileitest.paperdemo.activity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.layout.WrapHeightViewPager;
import com.leileitest.paperdemo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liulei
 * 21:43
 * 2015/11/29
 */
public class ViewPagerDemoActivity extends BaseActivity {

    private WrapHeightViewPager<String> mViewPager;
    private LinearLayout showIndicatorLin;

    @Override
    public void initView() {
        mViewPager = findView(R.id.viewPager);
        showIndicatorLin = findView(R.id.showIndicator);
    }

    @Override
    public void obtainData() {
        super.obtainData();
        initData();
    }

    @Override
    public void configUi() {
        super.configUi();
        mViewPager.setPageSize(3, 5);
        mViewPager.setOnItemClickListener(new WrapHeightViewPager.onItemClickListener() {
            @Override
            public void onItemClick(View convertView, int itemPos) {
                String message = (itemPos + 1) + "";
                if (mViewPager.isSelected(itemPos))
                    message += "已经选择";
                showToast(message, Toast.LENGTH_SHORT);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setIndexSelected(position);
            }
        });
    }

    private TextView createDefaultText(int page, int maxPages) {
        TextView mTextView = new TextView(this);
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
        //14.0f+padd(10.0f)
        int height = (int) ViewUtils.dpToPx(this, 24.0f);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(wrapContent, height);
        if (page + 1 != maxPages) {
            mLayoutParams.rightMargin = height;
        }
        mTextView.setLayoutParams(mLayoutParams);
        mTextView.setGravity(Gravity.CENTER_VERTICAL);
        ColorStateList color = ContextCompat.getColorStateList(this, R.color.page_indicator_color);
        mTextView.setTextColor(color);
        return mTextView;
    }


    @SuppressLint("SetTextI18n")
    private void initData() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 44; i++) {
            dataList.add((i + 1) + "");
        }
        mViewPager.addItemAll(dataList);
        mViewPager.setCurrentItem(0);

        int pageCount = mViewPager.getPageCount();
        int pageSize = mViewPager.getPageSize();
        for (int i = 0; i < pageCount; i++) {
            TextView textView = createDefaultText(i, pageCount);
            textView.setId(R.id.indicator_text + i);
            if (i + 1 == pageCount)
                textView.setText((pageSize * i + 1) + "-" + mViewPager.getItemCount());
            else
                textView.setText((pageSize * i + 1) + "-" + (pageSize * (i + 1) - 1));

            if (i == 0)
                textView.setSelected(true);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = v.getId() - R.id.indicator_text;
                    setIndexSelected(index);
                    onShowIndicatorItemClicked(index);
                }
            });

            showIndicatorLin.addView(textView);
        }
    }

    public void onShowIndicatorItemClicked(int index) {
        mViewPager.setCurrentItem(index, false);
    }

    private void setIndexSelected(int index) {
        for (int i = 0; i < showIndicatorLin.getChildCount(); i++) {
            showIndicatorLin.getChildAt(i).setSelected(index == i);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_viewpager_demo;
    }

}
