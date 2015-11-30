package com.leileitest.paperdemo.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.listener.UiClickListener;
import com.leileitest.paperdemo.listener.UiLoadListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liulei
 * 17:16
 * 2015/11/29
 */
public abstract class BaseActivity extends AppCompatActivity
        implements UiLoadListener {

    protected Toolbar mToolbar;
    private String actionBarTitle;
    private boolean hasDrawer = false;  //是否有抽屉
    private SystemBarTintManager tintManager;
    private Toast toast;

    public void setHasDrawer(boolean hasDrawer) {
        this.hasDrawer = hasDrawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSomeWork();
    }

    private void initSomeWork() {
        setContentView(getLayoutResId());
        initSystemBar();

        mToolbar = findView(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE);
        }
        setActionTitle();
        initView();
    }

    /**
     * 设置actionBar标题
     */
    private void setActionTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (!TextUtils.isEmpty(actionBarTitle))
                actionBar.setTitle(actionBarTitle);
        }
    }

    private void initSystemBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        if (tintManager == null)
            tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        //tintManager.setStatusBarTintColor(0x80303F9F);

        View rootView = findView(R.id.rootLayout);
        if (rootView == null) return;
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        int top = rootView.getPaddingTop();
        int bottom = rootView.getPaddingBottom();
        int left = rootView.getPaddingLeft();
        int right = rootView.getPaddingRight();
        //config.getPixelInsetTop(), config.getPixelInsetRight(), config.getPixelInsetBottom()
        rootView.setPadding(left, top + config.getPixelInsetTop(false), right + config.getPixelInsetRight(),
                bottom + config.getPixelInsetBottom());
    }

    /**
     * 修复toolbar不正常的问题
     *
     * @param rootView 当前布局的顶层view
     */
    public void fixSystemBar(View rootView) {
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        int top = rootView.getPaddingTop();
        int bottom = rootView.getPaddingBottom();
        int left = rootView.getPaddingLeft();
        int right = rootView.getPaddingRight();
        //config.getPixelInsetTop(), config.getPixelInsetRight(), config.getPixelInsetBottom()
        rootView.setPadding(left, top + config.getPixelInsetTop(false), right + config.getPixelInsetRight(),
                bottom + config.getPixelInsetBottom());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (!hasDrawer) {
            super.setContentView(R.layout.activity_base);
            addToolbarView(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    private void addToolbarView(int layoutResID) {
        LayoutInflater inflater = getLayoutInflater();
        if (layoutResID > 0) {
            FrameLayout frameLayout = findView(R.id.main_content);
            if (frameLayout != null)
                inflater.inflate(layoutResID, frameLayout, true);
        }
    }

    @Override
    public abstract void initView();

    @Override
    public final void initFramgentView(View itemView) {
        //此方法最好在 fragment 中使用。
        throw new RuntimeException("cann't call this method in activity.");
    }

    @Override
    public void prepareData() {
    }

    private boolean isLoaded = false;

    @Override
    public void obtainData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLoaded) {
            configUi();
            obtainData();
            isLoaded = true;
        }
    }

    @Override
    public void configUi() {
    }

    private View.OnClickListener clickListener;

    /**
     * @param vId
     * @param v
     */
    public void onViewClicked(int vId, View v) {
    }

    public void setClickListenersForViews(View... views) {
        if (clickListener == null) {
            clickListener = new UiClickListener() {
                @Override
                public void onViewClicked(int vId, View v) {
                    BaseActivity.this.onViewClicked(vId, v);
                }
            };
        }
        for (View view : views) {
            view.setOnClickListener(clickListener);
        }
    }

    public <T extends View> T findView(@IdRes int id) {
        T t = null;
        if (id > 0) t = (T) findViewById(id);
        return t;
    }

    public <T extends View> T findView(View view, @IdRes int id) {
        T t = null;
        if (id > 0) t = (T) view.findViewById(id);
        return t;
    }

    /**
     * 不会一直重复重复重复重复的提醒了
     */
    protected void showToast(String msg, int length) {
        if (toast == null) {
            toast = Toast.makeText(this.getApplicationContext(), msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 不会一直重复重复重复重复的提醒了
     */
    protected void showToastAsync(final String msg, final int length) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(BaseActivity.this.getApplicationContext(), msg, length);
                } else {
                    toast.setText(msg);
                }
                toast.show();
            }
        });
    }

    public void setActionBarTitle(String actionBarTitle) {
        this.actionBarTitle = actionBarTitle;
        if (mToolbar != null) {
            setActionTitle();
        }
    }

    protected abstract int getLayoutResId();
}