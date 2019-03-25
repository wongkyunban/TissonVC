package com.wong.tissonvc.logic.conference.mvp;

import android.content.Intent;

import com.wong.tissonvc.logic.BaseView;


public interface ConfCtrlContract
{
    interface ConfView extends BaseView
    {
        void doStartActivity(Intent intent);
    }

    interface ConfPresenter
    {
        void gotoConfList();

        void gotoOneKeyJoin();

    }
}
