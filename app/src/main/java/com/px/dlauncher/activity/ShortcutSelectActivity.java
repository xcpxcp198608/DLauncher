package com.px.dlauncher.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;

import com.px.dlauncher.F;
import com.px.dlauncher.R;
import com.px.dlauncher.adapter.ShortcutAdapter;
import com.px.dlauncher.animator.Zoom;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.sql.AppsDao;

import java.util.List;


public class ShortcutSelectActivity extends AppCompatActivity {

    private String shortcut;
    private ShortcutAdapter shortcutAdapter;
    private List<AppInfo> list;
    private GridView gvShortcut;
    private AppsDao appsDao;
    private List<AppInfo> shortcutApps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        gvShortcut = (GridView) findViewById(R.id.gv_shortcut);
        shortcut = getIntent().getStringExtra("shortcut");
        appsDao = AppsDao.getInstance(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        list = appsDao.queryData();
        shortcutAdapter = new ShortcutAdapter(this , list, shortcut);
        gvShortcut.setAdapter(shortcutAdapter);
        gvShortcut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shortcutApps = appsDao.queryDataByShortcut(F.app_type.shortcut);
                AppInfo appInfo = list.get(position);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                if(shortcutApps.size() >= 10){
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        appInfo.setShortcut("1");
                        appsDao.updateShortcut(appInfo);
                    } else {
                        Toast.makeText(ShortcutSelectActivity.this, "Only 10 shortcut keys can be set", Toast.LENGTH_LONG).show();
                    }
                }else {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        appInfo.setShortcut("1");
                        appsDao.updateShortcut(appInfo);
                    } else {
                        checkBox.setChecked(true);
                        appInfo.setShortcut(shortcut);
                        appsDao.updateShortcut(appInfo);
                        finish();
                    }
                }
            }
        });
        gvShortcut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zoom.zoomIn09_10(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
