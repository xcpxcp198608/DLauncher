package com.px.dlauncher.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.px.dlauncher.Application;
import com.px.dlauncher.F;
import com.px.dlauncher.R;
import com.px.dlauncher.adapter.AppsShortcutAdapter;
import com.px.dlauncher.animator.Rotation;
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

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnFocusChangeListener {

    private ImageView ivGooglePlay, ivYoutube, ivBrowser, ivFile, ivMedia;
    private LinearLayout llGooglePlay, llYoutube, llBrowser, llFile, llMedia, llApps, llSettings;
    private ImageButton ibtPower, ibtVolumeUp, ibtVolumeDown;
    private ImageView ivWifi, ivUsb, ivClean;
    private TextView tvGooglePlay, tvYoutube, tvBrowser, tvFile, tvMedia, tvTime, tvDate, tvClean;
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
        llGooglePlay = (LinearLayout) findViewById(R.id.ll_google_play);
        llYoutube = (LinearLayout) findViewById(R.id.ll_youtube);
        llBrowser = (LinearLayout) findViewById(R.id.ll_browser);
        llFile = (LinearLayout) findViewById(R.id.ll_file);
        llMedia = (LinearLayout) findViewById(R.id.ll_media);
        llApps = (LinearLayout) findViewById(R.id.ll_apps);
        llSettings = (LinearLayout) findViewById(R.id.ll_settings);
        ivGooglePlay = (ImageView) findViewById(R.id.ibt_google_play);
        ivYoutube = (ImageView) findViewById(R.id.ibt_youtube);
        ivBrowser = (ImageView) findViewById(R.id.ibt_browser);
        ivFile = (ImageView) findViewById(R.id.ibt_file);
        ivMedia = (ImageView) findViewById(R.id.ibt_media);
        ivClean = (ImageView) findViewById(R.id.ivClean);
//        ivApps = (ImageView) findViewById(R.id.ibt_apps);
//        ivSettings = (ImageView) findViewById(R.id.ibt_settings);
        tvGooglePlay = (TextView) findViewById(R.id.tv_google_play);
        tvYoutube = (TextView) findViewById(R.id.tv_youtube);
        tvBrowser = (TextView) findViewById(R.id.tv_browser);
        tvFile = (TextView) findViewById(R.id.tv_file);
        tvMedia = (TextView) findViewById(R.id.tv_media);

        ivGooglePlay.setImageDrawable(AppUtils.getIcon(MainActivity.this, F.packageName.youtube));
        ivYoutube.setImageDrawable(AppUtils.getIcon(MainActivity.this, F.packageName.chrome));
        ivBrowser.setImageDrawable(AppUtils.getIcon(MainActivity.this, F.packageName.file_aml));
        ivFile.setImageDrawable(AppUtils.getIcon(MainActivity.this, F.packageName.quick_support));
        ivMedia.setImageDrawable(AppUtils.getIcon(MainActivity.this, F.packageName.market));
        tvGooglePlay.setText(AppUtils.getLabelName(MainActivity.this, F.packageName.youtube));
        tvYoutube.setText(AppUtils.getLabelName(MainActivity.this, F.packageName.chrome));
        tvBrowser.setText(AppUtils.getLabelName(MainActivity.this, F.packageName.file_aml));
        tvFile.setText(AppUtils.getLabelName(MainActivity.this, F.packageName.quick_support));
        tvMedia.setText(AppUtils.getLabelName(MainActivity.this, F.packageName.market));

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
        llGooglePlay.setOnClickListener(this);
        llYoutube.setOnClickListener(this);
        llBrowser.setOnClickListener(this);
        llFile.setOnClickListener(this);
        llMedia.setOnClickListener(this);
        llApps.setOnClickListener(this);
        llSettings.setOnClickListener(this);
        ibtPower.setOnClickListener(this);
        ibtVolumeUp.setOnClickListener(this);
        ibtVolumeDown.setOnClickListener(this);
        flClean.setOnClickListener(this);
        llGooglePlay.setOnFocusChangeListener(this);
        llYoutube.setOnFocusChangeListener(this);
        llBrowser.setOnFocusChangeListener(this);
        llFile.setOnFocusChangeListener(this);
        llMedia.setOnFocusChangeListener(this);
        llApps.setOnFocusChangeListener(this);
        llSettings.setOnFocusChangeListener(this);
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
        gvShortcut.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this , ShortcutSelectActivity.class);
                intent.putExtra("shortcut" ,F.app_type.shortcut);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_google_play:
                AppUtils.launchApp(MainActivity.this, F.packageName.youtube);
                break;
            case R.id.ll_youtube:
                AppUtils.launchApp(MainActivity.this, F.packageName.chrome);
                break;
            case R.id.ll_browser:
                AppUtils.launchApp(MainActivity.this, F.packageName.file_aml);
                break;
            case R.id.ll_file:
                AppUtils.launchApp(MainActivity.this, F.packageName.quick_support);
                break;
            case R.id.ll_media:
                AppUtils.launchApp(MainActivity.this, F.packageName.market);
                break;
            case R.id.ll_apps:
                startActivity(new Intent(MainActivity.this , AppsActivity.class));
                break;
            case R.id.ll_settings:
                Intent intent = new Intent();
                intent.setClassName("com.android.tv.settings", "com.android.tv.settings.MainSettings");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.ibt_power:
                showShutDownDialog();
                break;
            case R.id.ibtVolumeUp:
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                break;
            case R.id.ibtVolumeDown:
                audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
                break;
            case R.id.fl_clean:
                Rotation.start(ivClean);
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
        builder.setMessage(getString(R.string.shutdown));
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shutdown();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void shutdown(){
        try {
            Intent intent =new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (SecurityException e){
            Logger.d(e.getMessage());
        }
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
                        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(MainActivity.this);
                        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(MainActivity.this);
//                        String time = new SimpleDateFormat("hh:mm A").format(d);
//                        String date = new SimpleDateFormat("EEEE\n dd MMMM").format(d);
                        String time = timeFormat.format(d);
                        String date = dateFormat.format(d);
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
