package com.wong.tissonvc.util;

import android.util.Log;

import com.wong.tissonvc.common.UIConstants;

public class ExceptionHandler extends CustomExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(UIConstants.DEMO_TAG, throwable.getMessage());
        super.uncaughtException(thread, throwable);
    }
}
