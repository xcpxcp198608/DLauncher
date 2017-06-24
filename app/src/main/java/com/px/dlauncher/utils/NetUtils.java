package com.px.dlauncher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


public class NetUtils {

    public static boolean isConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isAvailable()){
            return true;
        }else {
            return false;
        }
    }

    public static int networkConnectType (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        //NetworkInfo.State mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State ethernet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState();
        if(wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING){
            return 1;//wifi网络连接
        }
        //else if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING){
        //    return 2;//移动网络连接
        //}
        else if (ethernet == NetworkInfo.State.CONNECTED || ethernet == NetworkInfo.State.CONNECTING){
            return 3;//有线网络连接
        }else {
            return 0;//没有网络连接
        }
    }

    public static int getWifiLevel(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = wifiInfo.getRssi();
        if(level <= 0 && level >= -50){
            return 4;//信号最好
        }else if (level < -50 && level >= -70) {
            return 3;//信号好
        }else if (level < -70 && level >= -80) {
            return 2;//信号差
        }else if (level < -80 && level >= -100) {
            return 1;//信号很差
        }else {
            return 0;//没信号
        }
    }

}
