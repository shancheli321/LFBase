package com.example.base;

import android.app.Application;

import com.lf.base.manager.AppActivityManager;
import com.lf.util.log.AppLog;

/**
 * @date: 2024/7/2
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppActivityManager.getActivityManager().setAppLog(new AppActivityManager.AppActivityLogInterface() {
            @Override
            public void d(String tag, String log) {
                AppLog.d(tag, log);
            }
        });
    }
}
