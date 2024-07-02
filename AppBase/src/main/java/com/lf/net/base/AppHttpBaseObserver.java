package com.lf.net.base;

import com.lf.net.AppHttpConstants;
import com.lf.net.AppHttpUtils;
import com.lf.net.listener.AppHttpBaseNetListener;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.rxjava3.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * File descripition:   数据处理基类
 *
 * @author 宋宁
 * @date 2020/8/20
 */

public abstract class AppHttpBaseObserver<T> extends DisposableObserver<AppHttpBaseModel<T>> {
    protected AppHttpBaseNetListener ZYBHttpBaseNetListener;

    //传入监听
    public AppHttpBaseObserver(AppHttpBaseNetListener ZYBHttpBaseNetListener) {

        this.ZYBHttpBaseNetListener = ZYBHttpBaseNetListener;
    }

    //无监听
    public AppHttpBaseObserver() {

    }

    @Override
    protected void onStart() {
        if (ZYBHttpBaseNetListener != null) {
            ZYBHttpBaseNetListener.showLoading();
        }
    }

    @Override
    public void onNext(AppHttpBaseModel<T> o) {

        try {
            if (ZYBHttpBaseNetListener != null) {
                ZYBHttpBaseNetListener.hideLoading();
            }

            if (o.getCode() == AppHttpUtils.getInstance().getSuccessCode()) {
                onSuccess(o);

            } else {
                //非  true的所有情况
                onError(o);
            }

        } catch (Exception e) {

            e.printStackTrace();
            onError(new AppHttpBaseModel(10001,e.toString()));
        }
    }

    //消失写到这 有一定的延迟  对dialog显示有影响
    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {

            //   HTTP错误
            onError(new AppHttpBaseModel(AppHttpConstants.ZYBHttpCode_HttpError, "服务器故障，请稍后再试"));

        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {

            //   连接错误
            onError(new AppHttpBaseModel(AppHttpConstants.ZYBHttpCode_ConnectError, "连接错误"));
        } else if (e instanceof InterruptedIOException) {

            //  连接超时
            onError(new AppHttpBaseModel(AppHttpConstants.ZYBHttpCode_TimeOut, "连接超时"));
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {

            //  解析错误
            onError(new AppHttpBaseModel(AppHttpConstants.ZYBHttpCode_ParseError, "解析错误"));
        }else {
            //其他情况不给用户交互

        }
    }

    public abstract void onSuccess(AppHttpBaseModel<T> successModel);

    public abstract void onError(AppHttpBaseModel errorModel);
}
