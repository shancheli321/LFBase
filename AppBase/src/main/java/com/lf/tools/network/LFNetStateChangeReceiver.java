package com.lf.tools.network;

import static com.lf.tools.network.LFNetworkType.NETWORK_2G;
import static com.lf.tools.network.LFNetworkType.NETWORK_3G;
import static com.lf.tools.network.LFNetworkType.NETWORK_4G;
import static com.lf.tools.network.LFNetworkType.NETWORK_NO;
import static com.lf.tools.network.LFNetworkType.NETWORK_UNKNOW;
import static com.lf.tools.network.LFNetworkType.NETWORK_WIFI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


import java.util.ArrayList;
import java.util.List;


public class LFNetStateChangeReceiver extends BroadcastReceiver {

    private int mType = 0;

    private List<LFNetStateChangeListener> mObservers = new ArrayList<>();

    private static class InstanceHolder{
        private static final LFNetStateChangeReceiver INSTANCE = new LFNetStateChangeReceiver();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            int networkType = getNetworkType(context);
            notifyObservers(networkType);
        }
    }


    /**
     * 注册监听
     * @param context
     * @param observer
     */
    public static void registerReceiver(Context context, LFNetStateChangeListener observer){

        if (observer == null) {
            return;
        }

        if (!InstanceHolder.INSTANCE.mObservers.contains(observer)){
            InstanceHolder.INSTANCE.mObservers.add(observer);
        }

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver( InstanceHolder.INSTANCE,intentFilter);
    }


    /**
     * 移除监听
     * @param context
     * @param observer
     */
    public static void unRegisterReceiver(Context context, LFNetStateChangeListener observer){

        if (observer == null) {
            return;
        }

        if (InstanceHolder.INSTANCE.mObservers == null) {
            return;
        }
        InstanceHolder.INSTANCE.mObservers.remove(observer);

        context.unregisterReceiver( InstanceHolder.INSTANCE);
    }


    private void notifyObservers(int networkType){
        if (mType == networkType) {
            return;
        }

        mType = networkType;
        if (networkType == NETWORK_NO || networkType == NETWORK_UNKNOW){
            for (LFNetStateChangeListener observer : mObservers){
                observer.onNetDisconnected();
            }
        }else {
            for (LFNetStateChangeListener observer : mObservers){
                observer.onNetConnected(networkType);
            }
        }
    }


    /**
     * 判断当前网络类型-1为未知网络0为没有网络连接1网络断开或关闭2为以太网3为WiFi4为2G5为3G6为4G
     */
    private int getNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            /** 没有任何网络 */
            return NETWORK_NO;
        }
        if (!networkInfo.isConnected()) {
            /** 网络断开或关闭 */
            return NETWORK_NO;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            /** 以太网网络 */
            return NETWORK_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            /** wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接 */
            return NETWORK_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            /** 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭 */
            switch (networkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    /** 2G网络 */
                    return NETWORK_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    /** 3G网络 */
                    return NETWORK_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    /** 4G网络 */
                    return NETWORK_4G;
            }
        }
        /** 未知网络 */
        return NETWORK_UNKNOW;
    }
}
