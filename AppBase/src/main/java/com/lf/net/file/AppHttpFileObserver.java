package com.lf.net.file;

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
 * File descripition:
 *
 * @author 宋宁
 * @date 2020/8/20
 */
public abstract class AppHttpFileObserver<T> extends DisposableObserver<T> {
    protected AppHttpBaseNetListener ZYBHttpBaseNetListener;

    /**
     * 网络连接失败  无网
     */
    public static final int NETWORK_ERROR = 100000;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1008;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1007;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1006;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1005;

    /**
     * 其他所有情况
     */
    public static final int NOT_TRUE_OVER = 1004;

    public AppHttpFileObserver(AppHttpBaseNetListener ZYBHttpBaseNetListener) {

        this.ZYBHttpBaseNetListener = ZYBHttpBaseNetListener;
    }

    @Override
    protected void onStart() {

        if (ZYBHttpBaseNetListener != null) {

            ZYBHttpBaseNetListener.showProgress();
        }
    }

    @Override
    public void onNext(T t) {

        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {

        if (ZYBHttpBaseNetListener != null) {

            ZYBHttpBaseNetListener.hideProgress();
        }

        if (ZYBHttpBaseNetListener != null) {

            ZYBHttpBaseNetListener.hideLoading();
        }

        if (e instanceof HttpException) {

            //   HTTP错误
            onException(BAD_NETWORK, "");
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {

            //   连接错误
            onException(CONNECT_ERROR, "");
        } else if (e instanceof InterruptedIOException) {

            //  连接超时
            onException(CONNECT_TIMEOUT, "");
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {

            //  解析错误
            onException(PARSE_ERROR, "");
            e.printStackTrace();
        } else {

            if (e != null) {

                onError(e.toString());
            } else {

                onError("未知错误");
            }
        }
    }

    private void onException(int unknownError, String message) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError("连接错误");
                break;
            case CONNECT_TIMEOUT:
                onError("连接超时");
                break;
            case BAD_NETWORK:
                onError("网络超时");
                break;
            case PARSE_ERROR:
                onError("数据解析失败");
                break;
            //非true的所有情况
            case NOT_TRUE_OVER:
                onError(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void onComplete() {

        if (ZYBHttpBaseNetListener != null) {

            ZYBHttpBaseNetListener.hideProgress();
        }
    }

    public abstract void onSuccess(T o);

    public abstract void onError(String msg);

}
