package com.wong.tissonvc.logic.conference.mvp;

import android.content.Context;
import android.view.ViewGroup;

import com.wong.tissonvc.logic.BaseView;



public interface IDataConfContract
{
    interface DataConfView extends BaseView
    {
        void finishActivity();
    }

    interface IDataConfPresenter
    {
        void registerBroadcast();

        void unregisterBroadcast();

        void setConfID(String confID);

        String getSubject();

        boolean muteSelf();

        int switchLoudSpeaker();

        boolean isChairMan();

        void closeConf();

        void finishConf();

        void attachSurfaceView(ViewGroup container, Context context);
    }
}
