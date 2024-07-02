package com.lf.net;

import android.content.Context;

import com.lf.net.listener.AppHttpTokenListener;


public class AppHttpUtils {

    private static AppHttpUtils httpUtils;

    private Context mContext;

    // host
    private String baseUrl;

    // 登录令牌 标识
    private String token;

    // 成功标识码
    private int successCode;

    // token 过期时间, 毫秒级时间戳
    private String tokenExpiresTime;

    // 密钥
    private String secret;

    // 密钥过期时间 毫秒时间戳
    private String secretExpirsTime;

    private String userId;


    private AppHttpTokenListener tokenListener;

    // app 版本
    private String appVersion;

    // 系统版本
    private String osVersion;

    // 客户端标识（Android  pad区分）
    private String clientId;

    // 是否强制退出登录
    private boolean isLogout = false;

    // 是否输出日志
    private boolean isInitLog = false;

    public AppHttpUtils setInitLog(boolean initLog) {
        isInitLog = initLog;
        return this;
    }

    public static AppHttpUtils getInstance() {

        if (httpUtils == null) {
            httpUtils = new AppHttpUtils();

        }
        return httpUtils;
    }

    public AppHttpUtils setmContext(Context mContext) {

        this.mContext = mContext;
        return this;
    }

    public Context getmContext() {
        return mContext;
    }

    public AppHttpUtils setBaseUrl(String baseUrl) {

        this.baseUrl = baseUrl;
        return this;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public AppHttpUtils setToken(String token) {

        this.token = token;
        return this;
    }

    public String getToken() {
        return this.token;
    }

    public AppHttpUtils setSuccessCode(int successCode) {
        this.successCode = successCode;
        return this;
    }

    public int getSuccessCode() {
        return successCode;
    }

    public AppHttpUtils setTokenExpiresTime(String tokenExpiresTime) {
        this.tokenExpiresTime = tokenExpiresTime;
        return this;
    }

    public AppHttpUtils setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public AppHttpUtils setSecretExpirsTime(String secretExpirsTime) {
        this.secretExpirsTime = secretExpirsTime;
        return this;
    }

    public AppHttpUtils setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public AppHttpUtils setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public AppHttpUtils setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setLogout(boolean logout) {
        isLogout = logout;
    }

    public boolean isLogout() {
        return isLogout;
    }

    public AppHttpUtils setTokenListener(AppHttpTokenListener tokenListener) {
        this.tokenListener = tokenListener;
        return this;
    }

    public AppHttpTokenListener getTokenListener() {
        return tokenListener;
    }

    public String getClientId() {
        return clientId;
    }

    public AppHttpUtils setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }
}
