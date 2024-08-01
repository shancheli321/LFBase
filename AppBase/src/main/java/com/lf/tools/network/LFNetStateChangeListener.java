package com.lf.tools.network;


public interface LFNetStateChangeListener {

    void onNetDisconnected();
    void onNetConnected(int networkType);
}
