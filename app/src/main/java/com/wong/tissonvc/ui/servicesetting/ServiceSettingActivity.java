package com.wong.tissonvc.ui.servicesetting;

import android.view.View;

import com.wong.tissonvc.R;
import com.wong.tissonvc.ui.base.BaseActivity;

public class ServiceSettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void initializeComposition() {
        setContentView(R.layout.activity_service_setting);
    }

    @Override
    public void initializeData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        }
    }
}
