package com.lf.net.listener;


/**
 * File descripition:   基本回调 可自定义添加所需回调
 *
 * @author 宋宁
 * @date 2020/8/20
 */

public interface AppHttpBaseNetListener {
    /**
     * 显示dialog
     */
    void showLoading();

    /**
     * 隐藏 dialog
     */

    void hideLoading();


    /**
     * 进度条显示
     */
    void showProgress();

    /**
     * 进度条隐藏
     */
    void hideProgress();

    /**
     * 文件下载进度监听
     *
     * @param progress
     */
    void onProgress(int progress);
}
