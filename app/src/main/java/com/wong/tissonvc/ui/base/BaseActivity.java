package com.wong.tissonvc.ui.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.huawei.opensdk.commonservice.util.LogUtil;
import com.wong.tissonvc.R;
import com.wong.tissonvc.common.UIConstants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //Add a log to load the layout
        ActivityStack.getIns().push(this);


        initializeData();
        initializeComposition();
        initBackView();
        LogUtil.i(UIConstants.DEMO_TAG, "Activity onCreate: " + getClass().getSimpleName());
    }

    public abstract void initializeComposition();

    public abstract void initializeData();

    protected void initBackView() {
        initBackView(R.id.back_iv);
    }

    protected void initBackView(int resource) {
        View backView = findViewById(resource);
        if (null != backView) {
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack();
                }
            });
        }
    }

    protected void onBack() {
        ActivityStack.getIns().popup(this);
        // onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ActivityStack.getIns().popup(this);
        super.onDestroy();
    }

    public void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }




}
