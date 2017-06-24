package com.px.dlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.ImageView;

import com.px.dlauncher.R;
import com.px.dlauncher.utils.NetUtils;


public class WifiStatusReceiver extends BroadcastReceiver {
    private OnWifiStatusListener onWifiStatusListener;
    private ImageView imageView;

    public WifiStatusReceiver(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setOnWifiStatusListener (OnWifiStatusListener onWifiStatusListener){
        this.onWifiStatusListener = onWifiStatusListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.RSSI_CHANGED_ACTION.equals(intent.getAction())){
            showWifiStatus(context);
            if(onWifiStatusListener != null) {
                onWifiStatusListener.onWifiLevelChange(NetUtils.getWifiLevel(context));
            }
        }
    }

    private void showWifiStatus(Context context){
        if(imageView ==null){
            return;
        }
        int i = NetUtils.getWifiLevel(context);
        switch (i) {
            case 4:
                imageView.setImageResource(R.drawable.wifi4);
                break;
            case 3:
                imageView.setImageResource(R.drawable.wifi3);
                break;
            case 2:
                imageView.setImageResource(R.drawable.wifi2);
                break;
            case 1:
                imageView.setImageResource(R.drawable.wifi1);
                break;
            case 0:
                imageView.setImageResource(R.drawable.wifi0);
                break;
        }
    }
}
