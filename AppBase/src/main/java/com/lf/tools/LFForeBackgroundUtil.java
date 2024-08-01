package com.lf.tools;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @date: 2024/8/1
 */
public class LFForeBackgroundUtil {
    private static volatile LFForeBackgroundUtil util;
    private boolean isInited;
    private boolean isBackground;
    List<CallBack> mCallBackList;
    private int mActiveActivityCount = 0;
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        public void onActivityStarted(@NonNull Activity activity) {
            if (LFForeBackgroundUtil.this.mActiveActivityCount == 0) {
                LFForeBackgroundUtil.this.isBackground = false;
                Iterator var2 = LFForeBackgroundUtil.this.mCallBackList.iterator();

                while(var2.hasNext()) {
                    CallBack callBack = (CallBack)var2.next();
                    callBack.onAppForeground(activity);
                }
            }

            LFForeBackgroundUtil.this.mActiveActivityCount++;
        }

        public void onActivityResumed(@NonNull Activity activity) {
        }

        public void onActivityPaused(@NonNull Activity activity) {
        }

        public void onActivityStopped(@NonNull Activity activity) {
            LFForeBackgroundUtil.this.mActiveActivityCount--;
            if (LFForeBackgroundUtil.this.mActiveActivityCount == 0) {
                LFForeBackgroundUtil.this.isBackground = true;
                Iterator var2 = LFForeBackgroundUtil.this.mCallBackList.iterator();

                while(var2.hasNext()) {
                    CallBack callBack = (CallBack)var2.next();
                    callBack.onAppBackground(activity);
                }
            }

        }

        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    };

    private LFForeBackgroundUtil() {

    }

    public static LFForeBackgroundUtil getInstance() {
        if (util == null) {
            Class var0 = LFForeBackgroundUtil.class;
            synchronized(LFForeBackgroundUtil.class) {
                if (util == null) {
                    util = new LFForeBackgroundUtil();
                }
            }
        }

        return util;
    }

    public void init(Application application) {
        if (!this.isInited) {
            this.mCallBackList = new CopyOnWriteArrayList();
            application.registerActivityLifecycleCallbacks(this.activityLifecycleCallbacks);
            this.isInited = true;
        }
    }

    public static boolean isAppForeground() {
        return getInstance().isBackground;
    }

    public static boolean isAppBackground() {
        return getInstance().isBackground;
    }

    public void register(CallBack callBack) {
        if (!this.mCallBackList.contains(callBack)) {
            this.mCallBackList.add(callBack);
        }
    }

    public interface CallBack {
        void onAppForeground(Activity var1);

        void onAppBackground(Activity var1);
    }
}