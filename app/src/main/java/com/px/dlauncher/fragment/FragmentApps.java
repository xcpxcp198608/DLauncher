package com.px.dlauncher.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.px.dlauncher.F;
import com.px.dlauncher.R;
import com.px.dlauncher.activity.AppsActivity;
import com.px.dlauncher.adapter.AppsAdapter;
import com.px.dlauncher.animator.Zoom;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.sql.AppsDao;
import com.px.dlauncher.utils.AppUtils;
import com.px.dlauncher.utils.Logger;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FragmentApps extends Fragment {

    private AppsAdapter appsAdapter;
    private AppsDao appsDao;
    private AppsActivity activity;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apps,container ,false);
        activity = (AppsActivity) getContext();
        appsDao = AppsDao.getInstance(activity);
        gridView = (GridView) view.findViewById(R.id.gv_apps);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable.just(F.app_type.apps)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return appsDao.queryData();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AppInfo>>() {
                    @Override
                    public void call(final List<AppInfo> appInfos) {
                        appsAdapter = new AppsAdapter(activity , appInfos);
                        gridView.setAdapter(appsAdapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String packageName = appInfos.get(position).getPackageName();
                                Logger.d(packageName);
                                AppUtils.launchApp(getContext() , packageName);
                            }
                        });
                        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Zoom.zoomIn09_10(view);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(appsAdapter != null){
            appsAdapter.notifyDataSetChanged();
        }
    }
}
