package com.leileitest.paperdemo.activity;

import android.view.View;
import android.widget.Button;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.adapter.SceneViewPagerAdapter;
import com.leileitest.paperdemo.entity.LastItem;
import com.leileitest.paperdemo.entity.SenceInfo;
import com.leileitest.paperdemo.widget.WrapHeightViewPagerWithSmart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * think
 * 2015/12/9
 * 19:20
 */
public class FixViewPagerActivity extends BaseActivity {

    private WrapHeightViewPagerWithSmart viewPager;
    private SceneViewPagerAdapter sceneAdapter;
    private Button clickMe;

    @Override
    public void initView() {
        viewPager = findView(R.id.main_scene_viewpager);
        clickMe = findView(R.id.clickMe);
        setClickListenersForViews(clickMe);
    }

    @Override
    public void onViewClicked(int vId, View v) {
        super.onViewClicked(vId, v);
        if (v == clickMe) {
            loadData();
        }
    }

    @Override
    public void configUi() {
        super.configUi();
        List<SenceInfo> scenInfoList = new ArrayList<>();
        sceneAdapter = new SceneViewPagerAdapter<>(this, scenInfoList, 3, 1);
        viewPager.setAdapter(sceneAdapter);
    }

    @Override
    public void obtainData() {
        super.obtainData();
        loadData();
    }

    private void loadData() {
        List<Object> senceInfoList = new ArrayList<>();
        senceInfoList.add(new LastItem(null, "最后一个"));
        Random random = new Random();
        for (int i = 0; i < 12 + random.nextInt(5); i++) {
            SenceInfo senceInfo = new SenceInfo();
            senceInfo.setSenceIconPath(R.drawable.ic_scene_home + "");
            senceInfo.setSenceName("sence" + (i + 1));
            senceInfoList.add(senceInfo);
        }
        senceInfoList.add(new LastItem(null, "最后一个"));

        //noinspection unchecked
        sceneAdapter.setList(senceInfoList);
        //viewPager.setAdapter(sceneAdapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fixviewpager;
    }
}
