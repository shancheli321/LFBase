package com.lf.ui.util;

import android.content.Context;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

public class AppLoadingUtil {

    private static LoadingPopupView mLoadingPopup;


    /**
     * 基础方法
     * @param context
     * @param text
     */
    public static void showLoading(Context context, String text) {
        getPopup(context);
        mLoadingPopup.setTitle(text);
        mLoadingPopup.show();
    }


    public static void showLoading(Context context) {
        showLoading(context, "加载中...");
    }


    public static void showWithDelayed(Context context, int delay) {
        showLoading(context);
        mLoadingPopup.delayDismiss(delay);
    }


    public static void dismiss() {
        if (mLoadingPopup != null) {
            mLoadingPopup.dismiss();
            mLoadingPopup = null;
        }
    }


    /**
     * 获取popup
     * @param context
     * @return
     */
    private static LoadingPopupView getPopup(Context context) {
        if (mLoadingPopup == null) {
            mLoadingPopup = (LoadingPopupView) new XPopup.Builder(context)
                    .dismissOnBackPressed(true)
                    .isLightNavigationBar(true)
                    .dismissOnTouchOutside(false)
                    .asLoading()
                    .setStyle(LoadingPopupView.Style.Spinner);
        }

        return mLoadingPopup;
    }

}
