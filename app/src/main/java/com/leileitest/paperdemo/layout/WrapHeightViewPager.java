package com.leileitest.paperdemo.layout;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.adapter.IBaseRecyclerAdapter;
import com.leileitest.paperdemo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liulei
 * 23:44
 * 2015/11/29
 */
public class WrapHeightViewPager<T> extends ViewPager {

    private ViewPagerAdapter viewPagerAdapter;
    private SparseBooleanArray booleanArray;
    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(WrapHeightViewPager.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean isSelected(int itemPos) {
        return booleanArray.indexOfKey(itemPos) >= 0 &&
                booleanArray.get(itemPos, false);
    }

    public interface onItemClickListener {
        void onItemClick(View convertView, int itemPos);
    }

    public void setPageSize(int rows, int colums) {
        if (viewPagerAdapter != null) {
            viewPagerAdapter.setRows(rows);
            viewPagerAdapter.setColums(colums);
        }
    }

    public WrapHeightViewPager(Context context) {
        this(context, null);
    }

    public WrapHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void addItemAll(List<T> tList) {
        viewPagerAdapter.addItemAll(tList);
        viewPagerAdapter.notifyDataSetChanged();
    }

    public int getPageCount() {
        return viewPagerAdapter.getCount();
    }

    public int getPageSize() {
        return viewPagerAdapter.getRows() * viewPagerAdapter.getColums();
    }

    public int getItemCount() {
        return viewPagerAdapter.dataList.size();
    }

    private void init() {
        viewPagerAdapter = new ViewPagerAdapter(null);
        this.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                RecyclerView mRecyclerView = (RecyclerView) findViewWithTag(position);
                if (mRecyclerView != null) {
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
        this.setAdapter(viewPagerAdapter);
        booleanArray = new SparseBooleanArray();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureHeight = getDefaultSize(0, heightMeasureSpec);
        if (getChildCount() > 0) {
            View view = getChildAt(0);
            //view.measure(0, 0);
            measureHeight = Math.min(measureHeight, view.getMeasuredHeight());
        }
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), measureHeight);
    }

    private void addItemDecoration(RecyclerView recyclerView,
                                   final int horizontalSpace,
                                   final int verticalSpace,
                                   final int spanCount) {

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent,
                                       RecyclerView.State state) {
                int position = parent.getChildLayoutPosition(view);
                int column = position % spanCount;      //(0--1--2)
                outRect.left = column * horizontalSpace / spanCount;
                outRect.right = horizontalSpace - (column + 1) * horizontalSpace / spanCount;
                if (position >= spanCount) {
                    outRect.top = verticalSpace;
                }
            }
        });
    }

    private RecyclerView createSimpleListRecyclerView(int spanRows, int spanColums) {
        RecyclerView mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        int padding = (int) ViewUtils.dpToPx(getContext(), 5.0f);
        mRecyclerView.setPadding(padding, padding, padding, padding);
        final int horizontalSpace = (int) ViewUtils.dpToPx(getContext(), 5.0f);
        final int verticalSpace = (int) ViewUtils.dpToPx(getContext(), 5.0f);
        addItemDecoration(mRecyclerView, horizontalSpace, verticalSpace, spanColums);
        WrapHeightGridLayoutManager linearLayoutManager = new WrapHeightGridLayoutManager(getContext(),
                spanColums, GridLayoutManager.VERTICAL, false);
        linearLayoutManager.setVerticalSpace(verticalSpace);
        linearLayoutManager.setSpanRows(spanRows);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        SimpleRecyclerListAdapter simpleAdapter = new SimpleRecyclerListAdapter(getContext(),
                R.layout.pager_adapter_item, null);
        mRecyclerView.setAdapter(simpleAdapter);
        return mRecyclerView;
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private List<T> dataList;
        private int rows = 1;
        private int colums = 1;

        public int getRows() {
            return rows;
        }

        public int getColums() {
            return colums;
        }

        public void setRows(int rows) {
            if (this.rows != rows)
                this.rows = rows;
        }

        public void setColums(int colums) {
            if (this.colums != colums)
                this.colums = colums;
        }

        public ViewPagerAdapter(List<T> tList) {
            this.dataList = new ArrayList<>();
            if (tList != null && !tList.isEmpty())
                this.dataList.addAll(tList);
        }

        public void addItemAll(List<T> tList) {
            if (tList != null && !tList.isEmpty()
                    && !this.dataList.containsAll(tList))
                this.dataList.addAll(tList);
        }

        @Override
        public int getCount() {
            float count = dataList.size() / (float) (rows * colums);
            return dataList != null ? (int) Math.ceil(count) : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object instantiateItem(final ViewGroup container, final int page) {
            RecyclerView mRecyclerView = createSimpleListRecyclerView(rows, colums);
            container.addView(mRecyclerView);

            final SimpleRecyclerListAdapter adapter = (SimpleRecyclerListAdapter)
                    mRecyclerView.getAdapter();
            adapter.setCallback(new SimpleRecyclerListAdapter.AdaperCallback() {
                @Override
                public void onBindView(SimpleRecyclerListAdapter.SimpleHolder holder, int position) {
                    int itemPos = getPageSize() * page + position;
                    if (booleanArray == null)
                        return;
                    if (booleanArray.indexOfKey(itemPos) >= 0) {
                        holder.itemView.setSelected(booleanArray.get(itemPos));
                    } else
                        holder.itemView.setSelected(false);
                }
            });
            adapter.setOnItemClickListener(new IBaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View convertView, int position) {
                    int itemPos = getPageSize() * page + position;

                    if (WrapHeightViewPager.this.onItemClickListener != null) {
                        WrapHeightViewPager.this.onItemClickListener.onItemClick(convertView, itemPos);
                    }

                    for (int i = 0; i < booleanArray.size(); i++) {
                        boolean isSeleced = booleanArray.valueAt(i);
                        int key = booleanArray.keyAt(i);
                        if (isSeleced && key == itemPos) {
                            continue;
                        }
                        if (!isSeleced)
                            continue;
                        booleanArray.put(key, false);
                        if (getCurrentItem() == page) {
                            RecyclerView mRecyclerView = (RecyclerView) findViewWithTag(page);
                            SimpleRecyclerListAdapter.SimpleHolder simpleHolder =
                                    (SimpleRecyclerListAdapter.SimpleHolder) mRecyclerView
                                            .findViewHolderForAdapterPosition(position);
                            simpleHolder.mTextView.setSelected(false);
                            adapter.notifyItemChanged(key % (getPageSize()));
                        }
                    }
                    convertView.setSelected(true);
                    booleanArray.put(itemPos, true);


                }
            });
            int pageSize = rows * colums;
            int end = pageSize * (page + 1);
            int start = pageSize * page;
            if (end > dataList.size())
                end = dataList.size();
            if (start <= end) {
                adapter.addItemAll((List<String>) dataList.subList(start, end));
                adapter.notifyDataSetChanged();
            }

            mRecyclerView.setTag(page);
            return mRecyclerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
