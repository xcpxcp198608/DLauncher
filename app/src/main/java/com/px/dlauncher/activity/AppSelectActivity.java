package com.px.dlauncher.activity;

import android.app.Instrumentation;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;

import com.px.dlauncher.F;
import com.px.dlauncher.R;
import com.px.dlauncher.adapter.AppsSelectAdapter;
import com.px.dlauncher.animator.Zoom;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.sql.AppsDao;

import java.util.List;


public class AppSelectActivity extends AppCompatActivity {

    private String appType;
    private AppsSelectAdapter appsSelectAdapter;
    private List<AppInfo> list;
    private GridView gvSelect;
    private AppsDao appsDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        gvSelect = (GridView) findViewById(R.id.gv_select);
        appType = getIntent().getStringExtra("app_type");
        appsDao = AppsDao.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        list = appsDao.queryData();
        appsSelectAdapter = new AppsSelectAdapter(this , list, appType);
        gvSelect.setAdapter(appsSelectAdapter);
        gvSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = list.get(position);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                //Toast.makeText(AppSelectActivity.this, appInfo.getPackageName(), Toast.LENGTH_SHORT).show();
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                    appInfo.setType(F.app_type.apps);
                    appsDao.updateData(appInfo);
                }else {
                    checkBox.setChecked(true);
                    appInfo.setType(appType);
                    appsDao.updateData(appInfo);
                }
            }
        });
        gvSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
