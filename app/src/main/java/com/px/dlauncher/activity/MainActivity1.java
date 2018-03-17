package com.px.dlauncher.activity;

import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;
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

    private LinearLayout llSC1, llSC2, llSC3, llSC4, llSC5, llYoutube, llPlayStore, llApps;
    private ImageButton ibtSettings;
    private ImageView  ibtApps,ibtGooglePlay, ibtYoutube , ibtSC1, ibtSC2,ibtSC3, ibtSC4, ibtSC5;
    private TextView tvTime, tvSC1, tvSC2, tvSC3, tvSC4, tvSC5, tvYoutube, tvPlayStroe, tvApps;
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
        llSC1.requestFocus();
    }

    private void initView() {
        llSC1 = (LinearLayout) findViewById(R.id.llSc1);
        llSC2 = (LinearLayout) findViewById(R.id.llSc2);
        llSC3 = (LinearLayout) findViewById(R.id.llSc3);
        llSC4 = (LinearLayout) findViewById(R.id.llSc4);
        llSC5 = (LinearLayout) findViewById(R.id.llSc5);
        llYoutube = (LinearLayout) findViewById(R.id.llYoutube);
        llPlayStore = (LinearLayout) findViewById(R.id.llPlayStore);
        llApps = (LinearLayout) findViewById(R.id.llApps);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvSC1 = (TextView) findViewById(R.id.tvSc1);
        tvSC2 = (TextView) findViewById(R.id.tvSc2);
        tvSC3 = (TextView) findViewById(R.id.tvSc3);
        tvSC4 = (TextView) findViewById(R.id.tvSc4);
        tvSC5 = (TextView) findViewById(R.id.tvSc5);
        tvYoutube = (TextView) findViewById(R.id.tvYoutube);
        tvPlayStroe = (TextView) findViewById(R.id.tvPlayStore);
        tvApps = (TextView) findViewById(R.id.tvApps);
        ibtApps = (ImageView) findViewById(R.id.ibtApps);
        ibtGooglePlay = (ImageView) findViewById(R.id.ibtGooglePlay);
        ibtYoutube = (ImageView) findViewById(R.id.ibtYoutube);
        ibtSettings = (ImageButton) findViewById(R.id.ibtSettings);
        ibtSC1 = (ImageView) findViewById(R.id.ibtSc1);
        ibtSC2 = (ImageView) findViewById(R.id.ibtSc2);
        ibtSC3 = (ImageView) findViewById(R.id.ibtSc3);
        ibtSC4 = (ImageView) findViewById(R.id.ibtSc4);
        ibtSC5 = (ImageView) findViewById(R.id.ibtSc5);
        ivWifi = (ImageView) findViewById(R.id.iv_wifi);
        ivUsb = (ImageView) findViewById(R.id.iv_usb);
        llSC1.setOnClickListener(this);
        llSC2.setOnClickListener(this);
        llSC3.setOnClickListener(this);
        llSC4.setOnClickListener(this);
        llSC5.setOnClickListener(this);
        llYoutube.setOnClickListener(this);
        llPlayStore.setOnClickListener(this);
        llApps.setOnClickListener(this);
        ibtSettings.setOnClickListener(this);
        llSC1.setOnFocusChangeListener(this);
        llSC2.setOnFocusChangeListener(this);
        llSC3.setOnFocusChangeListener(this);
        llSC4.setOnFocusChangeListener(this);
        llSC5.setOnFocusChangeListener(this);
        llYoutube.setOnFocusChangeListener(this);
        llPlayStore.setOnFocusChangeListener(this);
        llApps.setOnFocusChangeListener(this);
        ibtSettings.setOnFocusChangeListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        tvYoutube.setText(AppUtils.getLabelName(this, F.packageName.youtube));
        tvPlayStroe.setText(AppUtils.getLabelName(this, F.packageName.google_play));
        tvApps.setText(getString(R.string.apps));
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
        loadShortcut(llSC1, ibtSC1, tvSC1, F.app_type.shortcut1);
        loadShortcut(llSC2, ibtSC2, tvSC2, F.app_type.shortcut2);
        loadShortcut(llSC3, ibtSC3, tvSC3, F.app_type.shortcut3);
        loadShortcut(llSC4, ibtSC4, tvSC4, F.app_type.shortcut4);
        loadShortcut(llSC5, ibtSC5, tvSC5, F.app_type.shortcut5);
    }

    private void loadShortcut(final LinearLayout linearLayout, final ImageView imageView, final TextView textView, final String shortcut){
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
                            imageView.setImageDrawable(AppUtils.getIcon(MainActivity1.this , appInfo.getPackageName()));
                            textView.setText(AppUtils.getLabelName(MainActivity1.this, appInfo.getPackageName()));
                            linearLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppUtils.launchApp(MainActivity1.this , appInfo.getPackageName());
                                }
                            });
                            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
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
                            imageView.setImageResource(0);
                            textView.setText("");
                            linearLayout.setOnClickListener(new View.OnClickListener() {
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
            case R.id.llApps:
                startActivity(new Intent(MainActivity1.this , AppsActivity.class));
                break;
            case R.id.llPlayStore:
                AppUtils.launchApp(this, F.packageName.google_play);
                break;
            case R.id.llYoutube:
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
