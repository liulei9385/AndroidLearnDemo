package com.leileitest.paperdemo.activity;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.alertdialogpro.AlertDialogPro;
import com.leileitest.paperdemo.R;

/**
 * Created by liulei
 * 22:45
 * 2015/12/1
 */
public class DialogActivity extends BaseActivity {

    private Button mButton;
    private Button mButtonShowCus;
    private Button mButtonShowCusV7;

    @Override
    public void initView() {
        mButton = findView(R.id.btn);
        mButtonShowCus = findView(R.id.btnShowCus);
        mButtonShowCusV7 = findView(R.id.btnShowCusV7);
    }

    @Override
    public void configUi() {
        super.configUi();
        setClickListenersForViews(mButton, mButtonShowCus, mButtonShowCusV7);
    }

    @Override
    public void onViewClicked(int vId, View v) {
        if (vId == R.id.btn) {
            showDialog();
        } else if (vId == R.id.btnShowCus) {
            showCustomDialog();
        } else if (vId == R.id.btnShowCusV7) {
            showCustomDialogV7();
        }
    }

    private void showDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this, 0);
        mBuilder.setTitle("标题")
                .setMessage("这是dialog.")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })//
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void showCustomDialog() {
        AlertDialogPro.Builder mBuilder = new AlertDialogPro.Builder(this, R.style.Ui_Dialog_Custom);
        AlertDialogPro alertDialogPro = mBuilder.setTitle("标题")
                .setMessage("这是custom dialog.")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })//
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialogPro.show();
    }

    private void showCustomDialogV7() {
        android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7
                .app.AlertDialog.Builder(this);
        mBuilder.setTitle("标题")
                .setMessage("这是custom dialog.")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })//
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_dialog_demo;
    }
}
