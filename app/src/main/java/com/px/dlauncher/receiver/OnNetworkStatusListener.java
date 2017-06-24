package com.px.dlauncher.receiver;


public interface OnNetworkStatusListener {
    void onConnected(boolean isConnected);
    void onDisconnect(boolean disConnected);
}
