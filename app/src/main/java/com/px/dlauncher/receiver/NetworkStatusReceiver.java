package com.px.dlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.px.dlauncher.R;
import com.px.dlauncher.utils.NetUtils;


public class NetworkStatusReceiver extends BroadcastReceiver  {
    private ImageView imageView;
    private OnNetworkStatusListener onNetworkStatusListener;

    public NetworkStatusReceiver(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setOnNetworkStatusListener (OnNetworkStatusListener onNetworkStatusListener){
        this.onNetworkStatusListener = onNetworkStatusListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showNetworkStatus(context);
        if(NetUtils.isConnected(context)){
            if(onNetworkStatusListener !=null){
                onNetworkStatusListener.onConnected(true);
            }
        }else {
            if(onNetworkStatusListener !=null) {
            onNetworkStatusListener.onDisconnect(true);
        }
        }
    }

    private void showNetworkStatus(Context context){
        if(imageView ==null){
            return;
        }
        int i = NetUtils.networkConnectType(context);
        switch (i) {
            case 0:
                imageView.setImageResource(R.drawable.wifi0);
                break;
            case 1:
                imageView.setImageResource(R.drawable.wifi4);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ethernet);
                break;
        }
    }
}
