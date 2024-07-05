package com.lf.ui.util;

import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.BlackToastStyle;
import com.hjq.toast.style.CustomToastStyle;
import com.hjq.toast.style.WhiteToastStyle;
import com.lf.base.R;
import android.app.Application;



/**
 * 1.toast的显示时间只有两种可能。我们查看源码可以得知它只有2秒和3.5秒
 */
public class AppToastUtil {


    /**
     * 初始化 Toast，需要在 Application.create 中初始化
     *
     * @param application       应用的上下文
     */
    public static void init(Application application) {
        Toaster.init(application);
    }

    /**
     * 判断当前框架是否已经初始化
     */
    public static boolean isInit() {
        return Toaster.isInit();
    }


    /**
     * 延迟显示 Toast
     */

    public static void showDelayed(int id, long delayMillis) {
        Toaster.delayedShow(id, delayMillis);
    }

    public static void showDelayed(Object object, long delayMillis) {
        Toaster.delayedShow(object, delayMillis);
    }

    public static void showDelayed(CharSequence text, long delayMillis) {
        Toaster.delayedShow(text, delayMillis);
    }


    /**
     * 显示一个短 Toast
     */

    public static void showShort(int id) {
        Toaster.showShort(id);
    }

    public static void showShort(Object object) {
        Toaster.showShort(object);
    }

    public static void showShort(CharSequence text) {
        Toaster.showShort(text);
    }


    /**
     * 显示一个长 Toast
     */

    public static void showLong(int id) {
        Toaster.showLong(id);
    }

    public static void showLong(Object object) {
        Toaster.showLong(object);
    }

    public static void showLong(CharSequence text) {
        Toaster.showLong(text);
    }

    /**
     * 显示 Toast
     */

    public static void show(int id) {
        Toaster.show(id);
    }

    public static void show(Object object) {
        Toaster.show(object);
    }

    public static void show(CharSequence text) {
        Toaster.show(text);
    }

    public static void show(ToastParams params) {
        Toaster.show(params);
    }

    /**
     * 取消吐司的显示
     */
    public static void cancel() {
        Toaster.cancel();
    }

    /**
     * 设置吐司的位置
     *
     * @param gravity  重心
     */
    public static void setGravity(int gravity) {
        Toaster.setGravity(gravity);
    }

    public static void setGravity(int gravity, int xOffset, int yOffset) {
        Toaster.setGravity(gravity, xOffset, yOffset);
    }

    /**
     * 给当前 Toast 设置新的布局
     */
    public static void setView(int id) {
        Toaster.setView(id);
    }

    public static void showSuccess(String text) {
        ToastParams params = new ToastParams();
        params.text = text;
        params.style = new CustomToastStyle(R.layout.toast_success);
        Toaster.show(params);
    }

    public static void showError(String text) {
        ToastParams params = new ToastParams();
        params.text = text;
        params.style = new CustomToastStyle(R.layout.toast_error);
        Toaster.show(params);
    }

    public static void showWarning(String text) {
        ToastParams params = new ToastParams();
        params.text = text;
        params.style = new CustomToastStyle(R.layout.toast_warn);
        Toaster.show(params);
    }

    public static void showWhite(String text) {
        ToastParams params = new ToastParams();
        params.text = text;
        params.style = new WhiteToastStyle();
        Toaster.show(params);
    }

    public static void showBlack(String text) {
        ToastParams params = new ToastParams();
        params.text = text;
        params.style = new BlackToastStyle();
        Toaster.show(params);
    }

}
