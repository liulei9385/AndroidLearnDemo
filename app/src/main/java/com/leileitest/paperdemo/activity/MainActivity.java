package com.leileitest.paperdemo.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.adapter.IBaseRecyclerAdapter;
import com.leileitest.paperdemo.adapter.ListDividerItemDecoration;
import com.leileitest.paperdemo.entity.ActEntity;
import com.leileitest.paperdemo.utils.IntentUtils;
import com.leileitest.paperdemo.widget.SimpePrintViewMeasureView;

import java.util.List;

/**
 * Created by liulei
 * 18:00
 * 2015/11/29
 */
public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MainRecyclerAdapter recyclerAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setHasDrawer(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.main_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView = findView(R.id.navigation_view);
        recyclerView = findView(R.id.recyclerView);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void openDrawer() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void obtainData() {
        super.obtainData();
        loadData();
    }

    private void loadData() {
        ActEntity actEntity = new ActEntity(PagerDemoActivity.class);
        recyclerAdapter.addItem(actEntity);
        actEntity = new ActEntity(ViewPagerDemoActivity.class);
        recyclerAdapter.addItem(actEntity);
        actEntity = new ActEntity(CleverRecyclerViewDemoActivity.class);
        recyclerAdapter.addItem(actEntity);
        actEntity = new ActEntity(DialogActivity.class);
        recyclerAdapter.addItem(actEntity);
        actEntity = new ActEntity(FixViewPagerActivity.class);
        recyclerAdapter.addItem(actEntity);
        actEntity = new ActEntity(PrintViewMearsureActivity.class);
        recyclerAdapter.addItem(actEntity);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void configUi() {
        super.configUi();
        this.setTitle("列表");

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return false;
            }
        });

        LinearLayoutManager linearLm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLm);

        ColorDrawable dividerDrawable = new ColorDrawable(0x80cdcdcd);
        recyclerView.addItemDecoration(new ListDividerItemDecoration(dividerDrawable));
        recyclerAdapter = new MainRecyclerAdapter(this, null);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new IBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                ActEntity actEntity = recyclerAdapter.getItemAtPosition(position);
                if (actEntity != null && actEntity.getActClazz() != null) {
                    startActivity(IntentUtils.setUpIntent(convertView.getContext(),
                            actEntity.getActClazz()));
                }
            }
        });
    }

    public class MainRecyclerAdapter extends IBaseRecyclerAdapter<ActEntity,
            MainRecyclerAdapter.MainViewHolder> {

        public MainRecyclerAdapter(Context context, List<ActEntity> actEntityList) {
            super(context, actEntityList);
        }

        @Override
        public ActEntity getItemAtPosition(int position) {
            return super.getItemAtPosition(position);
        }

        @Override
        protected MainViewHolder onCreateViewHolder(ViewGroup parent,
                                                    LayoutInflater layoutInflater,
                                                    int viewType) {
            View itemView = layoutInflater.inflate(R.layout.main_adapter_item, parent, false);
            return new MainViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            ActEntity actEntity = getItemAtPosition(position);
            holder.mTextView.setText(actEntity.getShowName());
        }

        public class MainViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView;

            public MainViewHolder(View itemView) {
                super(itemView);
                mTextView = findView(itemView, R.id.text);
            }
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

}