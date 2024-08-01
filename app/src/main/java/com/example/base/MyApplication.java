package com.example.base;

import android.app.Activity;
import android.app.Application;

import com.lf.base.manager.AppActivityManager;
import com.lf.tools.LFForeBackgroundUtil;
import com.lf.tools.network.LFNetStateChangeListener;
import com.lf.tools.network.LFNetStateChangeReceiver;
import com.lf.ui.util.AppToastUtil;
import com.lf.util.log.AppLog;

/**
 * @date: 2024/7/2
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppToastUtil.init(this);

        AppActivityManager.getActivityManager().setAppLog(new AppActivityManager.AppActivityLogInterface() {
            @Override
            public void d(String tag, String log) {
                AppLog.d(tag, log);
            }
        });

        LFNetStateChangeReceiver.registerReceiver(getApplicationContext(), new LFNetStateChangeListener() {
            @Override
            public void onNetDisconnected() {

            }

            @Override
            public void onNetConnected(int networkType) {

            }
        });

        LFForeBackgroundUtil.getInstance().register(new LFForeBackgroundUtil.CallBack() {
            @Override
            public void onAppForeground(Activity var1) {

            }

            @Override
            public void onAppBackground(Activity var1) {

            }
        });
    }
}
