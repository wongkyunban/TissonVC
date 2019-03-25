package com.wong.tissonvc.util;

import java.lang.Thread.UncaughtExceptionHandler;

public class CustomExceptionHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler defaultUEH = null;

    public CustomExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable throwable) {
        if (defaultUEH != null) {
            defaultUEH.uncaughtException(t, throwable);
        }
    }
}
