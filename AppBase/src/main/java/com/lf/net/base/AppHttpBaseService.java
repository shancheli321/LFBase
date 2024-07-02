package com.lf.net.base;

import android.os.Handler;

import com.lf.net.AppHttpConstants;
import com.lf.net.AppHttpUtils;
import com.lf.net.utils.AppNetUtils;

import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class AppHttpBaseService {

    private static boolean isLoading = false;

    private static Scheduler mScheduler;

    /**
     * 不需要token的请求
     *
     * @param observable
     * @param observer
     */
    public void dispatchRequst(final Observable observable, final AppHttpBaseObserver observer) {

        if (!checkNetWork(observer)) {
            return;
        }

        observable
                .subscribeOn(getScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }


    private static Scheduler getScheduler() {
        if (mScheduler == null) {
            synchronized (AppHttpBaseService.class) {
                if (mScheduler == null) {
                    mScheduler = Schedulers.from(Executors.newFixedThreadPool(15));
                }
            }
        }
        return mScheduler;
    }


    /**
     * 检查网络状态
     *
     * @param observer
     */
    public boolean checkNetWork(AppHttpBaseObserver observer) {

        // 无网路的状态
        if (!AppNetUtils.isConnected()) {

            if (isLoading) {
                if (observer != null) {
                    observer.onError(new AppHttpBaseModel());
                }
                return false;
            }

            isLoading = true;

            // 延迟检测
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!AppNetUtils.isConnected()) {
                        AppHttpBaseModel errorModel = new AppHttpBaseModel();
                        errorModel.setMessage("网络不可用，请检查网络设置");
                        errorModel.setCode(AppHttpConstants.ZYBHttpCode_Nonetwork);

                        if (AppHttpUtils.getInstance().getTokenListener() != null) {
                            AppHttpUtils.getInstance().getTokenListener().onHttpCallBack(AppHttpConstants.ZYBHttpCode_Nonetwork, null, null, errorModel);
                        }
                        isLoading = false;
                    }
                }
            }, 3000);


            if (observer != null) {
                observer.onError(new AppHttpBaseModel());
            }
            return false;
        }

        return true;
    }

}
