package com.px.dlauncher.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.px.dlauncher.Application;
import com.px.dlauncher.F;
import com.px.dlauncher.R;
import com.px.dlauncher.adapter.AppsShortcutAdapter;
import com.px.dlauncher.adapter.ShortcutAdapter;
import com.px.dlauncher.animator.Zoom;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.beans.UsbEvent;
import com.px.dlauncher.receiver.NetworkStatusReceiver;
import com.px.dlauncher.receiver.WifiStatusReceiver;
import com.px.dlauncher.sql.AppsDao;
import com.px.dlauncher.utils.AppUtils;
import com.px.dlauncher.utils.Logger;
import com.px.dlauncher.utils.RxBus;
import com.px.dlauncher.utils.SysUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnFocusChangeListener {

    private ImageButton ibtOnline, ibtVideo, ibtMusic, ibtApps,ibtGooglePlay, ibtSC1, ibtSC2, ibtSC3, ibtSC4
            , ibtSC5, ibtSC6, ibtSC7;
    private ImageView ivWifi, ivUsb;
    private NetworkStatusReceiver networkStatusReceiver;
    private WifiStatusReceiver wifiStatusReceiver;
    private Subscription subscription;
    private AudioManager audioManager;
    private AppsDao appsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appsDao = AppsDao.getInstance(Application.getContext());
        initView();
        registerReceiver();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private void initView() {
        ibtOnline = (ImageButton) findViewById(R.id.ibt_online);
        ibtVideo = (ImageButton) findViewById(R.id.ibt_video);
        ibtMusic = (ImageButton) findViewById(R.id.ibt_music);
        ibtApps = (ImageButton) findViewById(R.id.ibt_apps);
        ibtGooglePlay = (ImageButton) findViewById(R.id.ibt_google_play);
        ibtSC1 = (ImageButton) findViewById(R.id.ibt_sc1);
        ibtSC2 = (ImageButton) findViewById(R.id.ibt_sc2);
        ibtSC3 = (ImageButton) findViewById(R.id.ibt_sc3);
        ibtSC4 = (ImageButton) findViewById(R.id.ibt_sc4);
        ibtSC5 = (ImageButton) findViewById(R.id.ibt_sc5);
        ibtSC6 = (ImageButton) findViewById(R.id.ibt_sc6);
        ibtSC7 = (ImageButton) findViewById(R.id.ibt_sc7);
        ivWifi = (ImageView) findViewById(R.id.iv_wifi);
        ivUsb = (ImageView) findViewById(R.id.iv_usb);
        ibtOnline.setOnClickListener(this);
        ibtVideo.setOnClickListener(this);
        ibtMusic.setOnClickListener(this);
        ibtGooglePlay.setOnClickListener(this);
        ibtApps.setOnClickListener(this);
        ibtOnline.setOnFocusChangeListener(this);
        ibtVideo.setOnFocusChangeListener(this);
        ibtGooglePlay.setOnFocusChangeListener(this);
        ibtMusic.setOnFocusChangeListener(this);
        ibtApps.setOnFocusChangeListener(this);
        ibtSC1.setOnFocusChangeListener(this);
        ibtSC2.setOnFocusChangeListener(this);
        ibtSC3.setOnFocusChangeListener(this);
        ibtSC4.setOnFocusChangeListener(this);
        ibtSC5.setOnFocusChangeListener(this);
        ibtSC6.setOnFocusChangeListener(this);
        ibtSC7.setOnFocusChangeListener(this);
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
        loadShortcut(ibtSC6, F.app_type.shortcut6);
        loadShortcut(ibtSC7, F.app_type.shortcut7);
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
                            imageButton.setImageDrawable(AppUtils.getIcon(MainActivity.this , appInfo.getPackageName()));
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppUtils.launchApp(MainActivity.this , appInfo.getPackageName());
                                }
                            });
                            imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    appInfo.setShortcut("1");
                                    appsDao.updateShortcut(appInfo);
                                    Intent intent = new Intent(MainActivity.this , ShortcutSelectActivity.class);
                                    intent.putExtra("shortcut" ,shortcut);
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        }else {
                            imageButton.setImageResource(R.drawable.add_72);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this , ShortcutSelectActivity.class);
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
            case R.id.ibt_online:
                Intent intent1 = new Intent(MainActivity.this , AppsActivity.class);
                intent1.putExtra("currentItem",1);
                startActivity(intent1);
                break;
            case R.id.ibt_video:
                Intent intent3 = new Intent(MainActivity.this , AppsActivity.class);
                intent3.putExtra("currentItem",2);
                startActivity(intent3);
                break;
            case R.id.ibt_music:
                Intent intent = new Intent(MainActivity.this , AppsActivity.class);
                intent.putExtra("currentItem",3);
                startActivity(intent);
                break;
            case R.id.ibt_apps:
                startActivity(new Intent(MainActivity.this , AppsActivity.class));
                break;
            case R.id.ibt_google_play:
                AppUtils.launchApp(this, F.packageName.google_play);
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

    private void showShutDownDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Shutdown now?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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

}
