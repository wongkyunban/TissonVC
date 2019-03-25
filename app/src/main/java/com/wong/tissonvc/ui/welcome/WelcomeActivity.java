package com.wong.tissonvc.ui.welcome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.wong.tissonvc.R;
import com.wong.tissonvc.ui.login.LoginActivity;

import rx.Observable;
import rx.Observer;

public class WelcomeActivity extends AppCompatActivity {


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        checkMyPermission();

    }

    private void checkMyPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            //检查版本是否大于M
            //动态申请内存存储权限
            try {
                RxPermissions rxPermissions = new RxPermissions(this);
                if (
                        (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
                    Observable<Boolean> observable = rxPermissions
                            .request(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_CONTACTS,
                                    Manifest.permission.READ_CONTACTS);

                    observable.subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            handler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {

                        }
                    });

                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            handler.sendEmptyMessage(0);
        }
    }
}
