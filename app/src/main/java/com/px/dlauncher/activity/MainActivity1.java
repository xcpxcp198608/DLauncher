package com.px.dlauncher.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.px.dlauncher.Application;
import com.px.dlauncher.F;
import com.px.dlauncher.R;
import com.px.dlauncher.animator.Zoom;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.beans.UsbEvent;
import com.px.dlauncher.receiver.NetworkStatusReceiver;
import com.px.dlauncher.receiver.WifiStatusReceiver;
import com.px.dlauncher.sql.AppsDao;
import com.px.dlauncher.utils.AppUtils;
import com.px.dlauncher.utils.RxBus;
import com.px.dlauncher.utils.SysUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener ,
        View.OnFocusChangeListener {

    private ImageButton ibtApps,ibtGooglePlay, ibtYoutube, ibtSettings, ibtSC1, ibtSC2,
            ibtSC3, ibtSC4, ibtSC5;
    private TextView tvTime;
    private ImageView ivWifi, ivUsb;
    private NetworkStatusReceiver networkStatusReceiver;
    private WifiStatusReceiver wifiStatusReceiver;
    private Subscription subscription;
    private AppsDao appsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        appsDao = AppsDao.getInstance(Application.getContext());
        initView();
        registerReceiver();
        showTimeAndData();
    }

    private void initView() {
        tvTime = (TextView) findViewById(R.id.tvTime);
        ibtApps = (ImageButton) findViewById(R.id.ibtApps);
        ibtGooglePlay = (ImageButton) findViewById(R.id.ibtGooglePlay);
        ibtYoutube = (ImageButton) findViewById(R.id.ibtYoutube);
        ibtSettings = (ImageButton) findViewById(R.id.ibtSettings);
        ibtSC1 = (ImageButton) findViewById(R.id.ibtSc1);
        ibtSC2 = (ImageButton) findViewById(R.id.ibtSc2);
        ibtSC3 = (ImageButton) findViewById(R.id.ibtSc3);
        ibtSC4 = (ImageButton) findViewById(R.id.ibtSc4);
        ibtSC5 = (ImageButton) findViewById(R.id.ibtSc5);
        ivWifi = (ImageView) findViewById(R.id.iv_wifi);
        ivUsb = (ImageView) findViewById(R.id.iv_usb);
        ibtGooglePlay.setOnClickListener(this);
        ibtApps.setOnClickListener(this);
        ibtYoutube.setOnClickListener(this);
        ibtSettings.setOnClickListener(this);
        ibtGooglePlay.setOnFocusChangeListener(this);
        ibtApps.setOnFocusChangeListener(this);
        ibtYoutube.setOnFocusChangeListener(this);
        ibtSettings.setOnFocusChangeListener(this);
        ibtSC1.setOnFocusChangeListener(this);
        ibtSC2.setOnFocusChangeListener(this);
        ibtSC3.setOnFocusChangeListener(this);
        ibtSC4.setOnFocusChangeListener(this);
        ibtSC5.setOnFocusChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showShortcut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
    }

    private void showShortcut(){
        loadShortcut(ibtSC1, F.app_type.shortcut1);
        loadShortcut(ibtSC2, F.app_type.shortcut2);
        loadShortcut(ibtSC3, F.app_type.shortcut3);
        loadShortcut(ibtSC4, F.app_type.shortcut4);
        loadShortcut(ibtSC5, F.app_type.shortcut5);
    }

    private void loadShortcut(final ImageButton imageButton, final String shortcut){
        Observable.just(shortcut)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, AppInfo>() {
                    @Override
                    public AppInfo call(String s) {
                        List<AppInfo> list = appsDao.showShortcutData(s);
                        if(list.size() == 1){
                            return list.get(0);
                        }else {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AppInfo>() {
                    @Override
                    public void call(final AppInfo appInfo) {
                        if (appInfo != null) {
                            imageButton.setImageDrawable(AppUtils.getIcon(MainActivity1.this , appInfo.getPackageName()));
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppUtils.launchApp(MainActivity1.this , appInfo.getPackageName());
                                }
                            });
                            imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    appInfo.setShortcut("1");
                                    appsDao.updateShortcut(appInfo);
                                    Intent intent = new Intent(MainActivity1.this , ShortcutSelectActivity.class);
                                    intent.putExtra("shortcut" ,shortcut);
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        }else {
                            imageButton.setImageResource(0);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity1.this , ShortcutSelectActivity.class);
                                    intent.putExtra("shortcut" ,shortcut);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtApps:
                startActivity(new Intent(MainActivity1.this , AppsActivity.class));
                break;
            case R.id.ibtGooglePlay:
                AppUtils.launchApp(this, F.packageName.google_play);
                break;
            case R.id.ibtYoutube:
                AppUtils.launchApp(this, F.packageName.youtube);
                break;
            case R.id.ibtSettings:
                AppUtils.launchApp(this, F.packageName.setting);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            Zoom.zoomIn09_10(v);
        }
    }

    private void registerReceiver (){
        networkStatusReceiver = new NetworkStatusReceiver(ivWifi);
        registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        wifiStatusReceiver = new WifiStatusReceiver(ivWifi);
        registerReceiver(wifiStatusReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        subscription = RxBus.getDefault().toObservable(UsbEvent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        UsbEvent usbEvent = (UsbEvent) o;
                        if(usbEvent.getStatus() == UsbEvent.USB_MOUNTED){
                            ivUsb.setVisibility(View.VISIBLE);
                        }
                        if(usbEvent.getStatus() == UsbEvent.USB_UNMOUNTED){
                            ivUsb.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void unregister (){
        unregisterReceiver(networkStatusReceiver);
        unregisterReceiver(wifiStatusReceiver);
        if(subscription != null) subscription.unsubscribe();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showTimeAndData() {
        Application.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        Date d = new Date(System.currentTimeMillis());
                        String time = new SimpleDateFormat("HH:mm").format(d);
                        handler.obtainMessage(1, time).sendToTarget();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tvTime.setText((String)msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

}
