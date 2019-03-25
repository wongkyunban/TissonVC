package com.wong.tissonvc.logic.eaddrbook;

import android.content.Intent;

import com.wong.tissonvc.logic.BaseView;

public interface EnterpriseAddressBookContract {

    interface EAddrBookView extends BaseView
    {
        void doStartActivity(Intent intent);
    }

    interface EnterprisePresenter
    {
        void gotoEAddrBookEntry();
    }
}
