package com.px.dlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.px.dlauncher.beans.UsbEvent;
import com.px.dlauncher.utils.RxBus;

public class USBStatusReceiver extends BroadcastReceiver {

    public USBStatusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())){
            String em1 = "/storage/emulated/0";
            String path = intent.getData().getPath();
            if(em1.equals(path)){

            }else {
                RxBus.getDefault().post(new UsbEvent(UsbEvent.USB_MOUNTED, path));
            }
        }else if(Intent.ACTION_MEDIA_EJECT.equals(intent.getAction())){
            String path = intent.getData().getPath();
            RxBus.getDefault().post(new UsbEvent(UsbEvent.USB_UNMOUNTED , path));
        }
    }
}
