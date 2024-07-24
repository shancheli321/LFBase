package com.lf.ui.util;

import android.content.Context;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

/**
 * @date: 2024/7/8
 */
public class AppDialogUtil {

    private static BasePopupView mPopupView;

    public static void show(Context context,
                            String title,
                            String content,
                            String leftText,
                            String rightText,
                            OnConfirmListener confirmListener,
                            OnCancelListener cancelListener,
                            boolean isHideCancle) {
       if (mPopupView == null) {
           mPopupView = new XPopup.Builder(context)
                   .isDestroyOnDismiss(true)
                   .dismissOnBackPressed(false)
                   .dismissOnTouchOutside(false)
//              .popupAnimation(PopupAnimation.NoAnimation)
                   .asConfirm(title, content , leftText, rightText, confirmListener, cancelListener, isHideCancle);
       }
        mPopupView.show();
    }


    /**
     * 显示 单个确定
     * @param context
     * @param title
     * @param content
     * @param onConfirmListener
     */
    public static void showOK(Context context,
                              String title,
                              String content,
                              OnConfirmListener onConfirmListener) {
        show(context, title, content, "", "确定", onConfirmListener, null, true);
    }

    /**
     * 显示 单个按钮
     * @param context
     * @param title
     * @param content
     * @param rightText
     * @param confirmListener
     */
    public static void showSingle(Context context,
                                  String title,
                                  String content,
                                  String rightText,
                                  OnConfirmListener confirmListener) {
        show(context, title, content, "", rightText, confirmListener, null, true);
    }


    /**
     * 显示 取消和确定
     * @param context
     * @param title
     * @param content
     * @param confirmListener
     */
    public static void showNormal(Context context,
                                  String title,
                                  String content,
                                  OnConfirmListener confirmListener) {
        show(context, title, content, "取消", "确定", confirmListener, new OnCancelListener() {
            @Override
            public void onCancel() {
                dismiss();
            }
        }, false);
    }


    public static void dismiss() {
        mPopupView.dismiss();
        mPopupView = null;
    }

}
