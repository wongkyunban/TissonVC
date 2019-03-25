package com.wong.tissonvc.ui.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.huawei.opensdk.callmgr.CallMgr;
import com.huawei.opensdk.commonservice.common.LocContext;
import com.huawei.opensdk.commonservice.localbroadcast.CustomBroadcastConstants;
import com.huawei.opensdk.commonservice.localbroadcast.LocBroadcast;
import com.huawei.opensdk.commonservice.localbroadcast.LocBroadcastReceiver;
import com.huawei.opensdk.contactmgr.ContactMgr;
import com.huawei.opensdk.demoservice.MeetingMgr;
import com.huawei.opensdk.loginmgr.LoginMgr;
import com.huawei.opensdk.servicemgr.ServiceMgr;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.wong.tissonvc.R;
import com.wong.tissonvc.common.UIConstants;
import com.wong.tissonvc.logic.call.CallFunc;
import com.wong.tissonvc.logic.conference.ConfFunc;
import com.wong.tissonvc.logic.contact.ContactFunc;
import com.wong.tissonvc.logic.login.LoginFunc;
import com.wong.tissonvc.ui.IntentConstant;
import com.wong.tissonvc.ui.base.MVPBaseActivity;
import com.wong.tissonvc.util.ActivityUtil;
import com.wong.tissonvc.logic.login.ILoginContract;
import com.wong.tissonvc.logic.login.LoginPresenter;
import com.huawei.opensdk.commonservice.util.LogUtil;
import com.wong.tissonvc.util.FileUtil;
import com.wong.tissonvc.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Observer;

/**
 * This class is about login ui logic
 */
public class LoginActivity extends MVPBaseActivity<ILoginContract.LoginBaseView, LoginPresenter>
        implements ILoginContract.LoginBaseView, LocBroadcastReceiver {

    /**
     * user name
     */
    private String mUserName;

    /**
     * password
     */
    private String mPassword;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    private ImageView mLoginSettingBtn;

    private ProgressDialog mDialog;
    private String[] mActions = new String[]{CustomBroadcastConstants.LOGIN_SUCCESS, CustomBroadcastConstants.LOGIN_FAILED,
            CustomBroadcastConstants.LOGOUT};

    private static final int EXPECTED_FILE_LENGTH = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        init();

        super.onCreate(savedInstanceState);
    }

    private void init() {
        String appPath = getApplicationInfo().dataDir + "/lib";
        ServiceMgr.getServiceMgr().startService(this, appPath);
        Log.i("SDKDemo", "onCreate: MAIN Process.");

        LoginMgr.getInstance().regLoginEventNotification(LoginFunc.getInstance());
        CallMgr.getInstance().regCallServiceNotification(CallFunc.getInstance());
        MeetingMgr.getInstance().regConfServiceNotification(ConfFunc.getInstance());
        ContactMgr.getInstance().setContactNotification(ContactFunc.getInstance());

        initResourceFile();
    }


//    ~~~~~~~~~~

    private void initResourceFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initDataConfRes();
            }
        }).start();
    }


    private void initDataConfRes() {
        String path = LocContext.
                getContext().getFilesDir() + "/AnnoRes";
        File file = new File(path);
        if (file.exists()) {
            LogUtil.i(UIConstants.DEMO_TAG, file.getAbsolutePath());
            File[] files = file.listFiles();
            if (null != files && EXPECTED_FILE_LENGTH == files.length) {
                return;
            } else {
                FileUtil.deleteFile(file);
            }
        }

        try {
            InputStream inputStream = getAssets().open("AnnoRes.zip");
            ZipUtil.unZipFile(inputStream, path);
        } catch (IOException e) {
            LogUtil.i(UIConstants.DEMO_TAG, "close...Exception->e" + e.toString());
        }
    }

    //    ~~~~~~~~~~~~~~
    @Override
    public void initializeComposition() {
        setContentView(R.layout.activity_login);

        mUsernameEditText = (EditText) findViewById(R.id.et_account);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginSettingBtn = (ImageView) findViewById(R.id.iv_login_setting);

        mPresenter.onLoginParams();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog(getString(R.string.logining_msg));
                mUserName = mUsernameEditText.getText().toString().trim();
                mPassword = mPasswordEditText.getText().toString();
                new Thread(runnable).start();
            }
        });

        mLoginSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissLoginDialog();
                ActivityUtil.startActivity(LoginActivity.this, IntentConstant.LOGIN_SETTING_ACTIVITY_ACTION,
                        new String[]{IntentConstant.DEFAULT_CATEGORY});
            }
        });
    }

    /*最新的API要求需要在子线程里面判断网络信息，和一些耗时操作*/
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.doLogin(mUserName, mPassword);
        }
    };

    @Override
    public void initializeData() {
        mPresenter.initServerData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocBroadcast.getInstance().registerBroadcast(this, mActions);
    }

    @Override
    public void dismissLoginDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoginDialog();
        LocBroadcast.getInstance().unRegisterBroadcast(this, mActions);
    }

    public void showLoginDialog(String msg) {
        if (null == mDialog) {
            mDialog = new ProgressDialog(this);
        }

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setTitle(msg);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();
    }

    @Override
    public void setEditText(String account, String password) {
        mUsernameEditText.setText(account);
        mPasswordEditText.setText(password);
    }

    @Override
    public void showToast(int resId) {
        super.showToast(resId);
    }

    @Override
    protected ILoginContract.LoginBaseView createView() {
        return this;
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onReceive(String broadcastName, Object obj) {
        switch (broadcastName) {
            case CustomBroadcastConstants.LOGIN_SUCCESS:
                LogUtil.i(UIConstants.DEMO_TAG, "login success");
                dismissLoginDialog();
                break;
            case CustomBroadcastConstants.LOGIN_FAILED:
                LogUtil.i(UIConstants.DEMO_TAG, "login fail");
                dismissLoginDialog();
                break;
            case CustomBroadcastConstants.LOGOUT:
                break;
            default:
                break;
        }
    }

}
