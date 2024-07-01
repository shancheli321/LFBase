package com.lf.net.listener;


import com.lf.net.base.AppHttpBaseModel;

public interface AppHttpTokenListener {

    public void onHttpCallBack(Integer code, String token, String tokenExpiresTime, AppHttpBaseModel errorModel);
}
