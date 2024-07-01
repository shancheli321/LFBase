package com.lf.base.single;

import android.content.Context;

public class AppSingle {

    private Context mContext;

    private volatile static AppSingle appSingle;
    
    private AppSingle() {
        
    }

    // 当第一次加载Singleton类时并不会初始化INSTANCE，只有在第一次调用getInstance方法时才会导致INSTANCE被初始化。
    // 这种方式不仅能够保证线程安全，也能保证单例对象的唯一性，同时也延长了单例的实例化
    public static AppSingle getInstance() {
        if (appSingle == null) {
            synchronized (AppSingle.class) {
                if (appSingle == null) {
                    appSingle = new AppSingle();
                }
            }
        }
        return appSingle;
    }


    public void setContext(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }
}
