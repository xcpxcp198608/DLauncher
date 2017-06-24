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

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnFocusChangeListener {

    private ImageButton ibtOnline, ibtVideo, ibtImages, ibtMusic, ibtBrowser, ibtApps,
            ibtSettings, ibtPower, ibtVolumeUp, ibtVolumeDown;
    private ImageView ivWifi, ivUsb;
    private TextView tvTime, tvDate, tvClean;
    private FrameLayout flClean;
    private GridView gvShortcut;
    private NetworkStatusReceiver networkStatusReceiver;
    private WifiStatusReceiver wifiStatusReceiver;
    private Subscription subscription;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        showTimeAndData();
        registerReceiver();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private void initView() {
        ibtOnline = (ImageButton) findViewById(R.id.ibt_online);
        ibtVideo = (ImageButton) findViewById(R.id.ibt_video);
        ibtImages = (ImageButton) findViewById(R.id.ibt_images);
        ibtMusic = (ImageButton) findViewById(R.id.ibt_music);
        ibtBrowser = (ImageButton) findViewById(R.id.ibt_browser);
        ibtApps = (ImageButton) findViewById(R.id.ibt_apps);
        ibtSettings = (ImageButton) findViewById(R.id.ibt_settings);
        ibtPower = (ImageButton) findViewById(R.id.ibt_power);
        ibtVolumeUp = (ImageButton) findViewById(R.id.ibtVolumeUp);
        ibtVolumeDown = (ImageButton) findViewById(R.id.ibtVolumeDown);
        ivWifi = (ImageView) findViewById(R.id.iv_wifi);
        ivUsb = (ImageView) findViewById(R.id.iv_usb);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvClean = (TextView) findViewById(R.id.tv_clean);
        flClean = (FrameLayout) findViewById(R.id.fl_clean);
        gvShortcut = (GridView) findViewById(R.id.gv_shortcut);
        ibtOnline.setOnClickListener(this);
        ibtVideo.setOnClickListener(this);
        ibtImages.setOnClickListener(this);
        ibtMusic.setOnClickListener(this);
        ibtBrowser.setOnClickListener(this);
        ibtApps.setOnClickListener(this);
        ibtSettings.setOnClickListener(this);
        ibtPower.setOnClickListener(this);
        ibtVolumeUp.setOnClickListener(this);
        ibtVolumeDown.setOnClickListener(this);
        flClean.setOnFocusChangeListener(this);
        ibtOnline.setOnFocusChangeListener(this);
        ibtVideo.setOnFocusChangeListener(this);
        ibtImages.setOnFocusChangeListener(this);
        ibtMusic.setOnFocusChangeListener(this);
        ibtBrowser.setOnFocusChangeListener(this);
        ibtApps.setOnFocusChangeListener(this);
        ibtSettings.setOnFocusChangeListener(this);
        ibtPower.setOnFocusChangeListener(this);
        ibtVolumeUp.setOnFocusChangeListener(this);
        ibtVolumeDown.setOnFocusChangeListener(this);
        flClean.setOnFocusChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShortcut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
    }

    private void loadShortcut(){
        AppsDao appsDao = AppsDao.getInstance(Application.getContext());
        final List<AppInfo> appInfoList = appsDao.queryDataByShortcut(F.app_type.shortcut);
        AppsShortcutAdapter adapter = new AppsShortcutAdapter(this, appInfoList);
        gvShortcut.setAdapter(adapter);
        gvShortcut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < appInfoList.size()){
                    String packageName = appInfoList.get(position).getPackageName();
                    AppUtils.launchApp(Application.getContext() , packageName);
                }else {
                    Intent intent = new Intent(MainActivity.this , ShortcutSelectActivity.class);
                    intent.putExtra("shortcut" ,F.app_type.shortcut);
                    startActivity(intent);
                }
            }
        });
        gvShortcut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zoom.zoomIn10_11_10(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            case R.id.ibt_images:
                AppUtils.launchApp(this, F.packageName.gallery);
                break;
            case R.id.ibt_music:
                Intent intent = new Intent(MainActivity.this , AppsActivity.class);
                intent.putExtra("currentItem",3);
                startActivity(intent);
                break;
            case R.id.ibt_browser:
                AppUtils.launchApp(this, F.packageName.browser);
                break;
            case R.id.ibt_apps:
                startActivity(new Intent(MainActivity.this , AppsActivity.class));
                break;
            case R.id.ibt_settings:
                AppUtils.launchApp(this, F.packageName.setting);
                break;
            case R.id.ibt_power:
                showShutDownDialog();
                break;
            case R.id.ibtVolumeUp:
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                break;
            case R.id.ibtVolumeDown:
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                break;
            case R.id.fl_clean:
                SysUtils.cleanMemory(MainActivity.this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            Zoom.zoomIn10_11_10(v);
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

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tvTime.setText((String)msg.obj);
                    break;
                case 2:
                    tvDate.setText((String)msg.obj);
                    break;
                case 3:
                    tvClean.setText(msg.obj + "%" +"\n" + getString(R.string.clean));
                    break;
                default:
                    break;
            }
        }
    };

    private void showTimeAndData() {
        Application.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        Date d = new Date(System.currentTimeMillis());
                        String time = new SimpleDateFormat("hh:mm a").format(d);
                        String date = new SimpleDateFormat("EEEE\n dd MMMM").format(d);
                        String memoryRate = SysUtils.getAvailMemory(MainActivity.this)+"";
                        handler.obtainMessage(1, time).sendToTarget();
                        handler.obtainMessage(2, date).sendToTarget();
                        handler.obtainMessage(3, memoryRate).sendToTarget();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
