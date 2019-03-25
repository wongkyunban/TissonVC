package com.wong.tissonvc.ui.conference;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.opensdk.callmgr.CallConstant;
import com.huawei.opensdk.commonservice.util.LogUtil;
import com.wong.tissonvc.R;
import com.wong.tissonvc.common.UIConstants;
import com.wong.tissonvc.logic.conference.mvp.DataConfPresenter;
import com.wong.tissonvc.logic.conference.mvp.IDataConfContract;
import com.wong.tissonvc.ui.base.ActivityStack;
import com.wong.tissonvc.ui.base.MVPBaseActivity;
import com.wong.tissonvc.widget.ConfirmDialog;
import com.wong.tissonvc.widget.TripleDialog;

/**
 * This class is about data conf Activity.
 */
public class DataConfActivity extends MVPBaseActivity<IDataConfContract.DataConfView, DataConfPresenter>
        implements IDataConfContract.DataConfView, View.OnClickListener {

    private FrameLayout mConfShareLayout;
    private ImageView mBackIV;
    private TextView mTitleTV;
    private ImageView mRightIV;
    private FrameLayout mConfHangup;
    private FrameLayout mConfMute;
    private FrameLayout mConfSpeaker;
    private String mSubject;
    private String confID;
    private DataConfPresenter mPresenter;

    @Override
    protected IDataConfContract.DataConfView createView() {
        return this;
    }

    @Override
    protected DataConfPresenter createPresenter() {
        mPresenter = new DataConfPresenter();
        return mPresenter;
    }

    @Override
    public void initializeComposition() {
        setContentView(R.layout.data_conf_activity);
        //video layout
        mConfShareLayout = (FrameLayout) findViewById(R.id.conf_share_layout);

        //title
        mBackIV = (ImageView) findViewById(R.id.back_iv);
        mRightIV = (ImageView) findViewById(R.id.right_iv);
        mTitleTV = (TextView) findViewById(R.id.conf_title);

        mTitleTV.setText(mSubject);
        mRightIV.setVisibility(View.GONE);

        //main tab
        mConfHangup = (FrameLayout) findViewById(R.id.conf_hangup);
        mConfMute = (FrameLayout) findViewById(R.id.conf_mute);
        mConfSpeaker = (FrameLayout) findViewById(R.id.conf_loud_speaker);

        mConfHangup.setOnClickListener(this);
        mConfMute.setOnClickListener(this);
        mConfSpeaker.setOnClickListener(this);

        mPresenter.attachSurfaceView(mConfShareLayout, this);
    }

    @Override
    public void initializeData() {
        Intent intent = getIntent();
        confID = intent.getStringExtra(UIConstants.CONF_ID);
        if (confID == null) {
            showToast(R.string.empty_conf_id);
            return;
        }

        mPresenter.setConfID(confID);
        mSubject = mPresenter.getSubject();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.registerBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterBroadcast();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showCustomToast(final int resID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(resID);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.conf_hangup:
                LogUtil.i(UIConstants.DEMO_TAG, "conference hangup!");
                if (!mPresenter.isChairMan()) {
                    showLeaveConfDialog();
                } else {
                    showEndConfDialog();
                }
                break;

            case R.id.conf_mute:
                LogUtil.i(UIConstants.DEMO_TAG, "conference mute!");
                mPresenter.muteSelf();
                break;

            case R.id.conf_loud_speaker:
                LogUtil.i(UIConstants.DEMO_TAG, "conference speaker!");
                updateLoudSpeakerButton(mPresenter.switchLoudSpeaker());
                break;
            default:
                break;
        }
    }


    private void showLeaveConfDialog() {
        ConfirmDialog dialog = new ConfirmDialog(this, R.string.leave_conf);
        dialog.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.closeConf();
                ActivityStack.getIns().popup(ConfManagerActivity.class);
                finish();
            }
        });
        dialog.show();
    }

    private void showEndConfDialog() {
        TripleDialog dialog = new TripleDialog(this);
        dialog.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.closeConf();
                ActivityStack.getIns().popup(ConfManagerActivity.class);
                finish();
            }
        });
        dialog.setLeftButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.finishConf();
                ActivityStack.getIns().popup(ConfManagerActivity.class);
                finish();
            }
        });
        dialog.show();
    }

    private void updateLoudSpeakerButton(int type) {
        if (type == CallConstant.TYPE_LOUD_SPEAKER) {
            mConfSpeaker.setActivated(true);
        } else {
            mConfSpeaker.setActivated(false);
        }
    }
}
