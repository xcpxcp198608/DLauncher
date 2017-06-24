package com.px.dlauncher.fragment;

import android.content.Intent;
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
import com.px.dlauncher.activity.AppSelectActivity;
import com.px.dlauncher.activity.AppsActivity;
import com.px.dlauncher.adapter.AppsCustomAdapter;
import com.px.dlauncher.animator.Zoom;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.sql.AppsDao;
import com.px.dlauncher.utils.AppUtils;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;



public class FragmentMusic extends Fragment {

    private AppsCustomAdapter appsCustomAdapter;
    private AppsDao appsDao;
    private AppsActivity activity;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        activity = (AppsActivity) getContext();
        appsDao = AppsDao.getInstance(activity);
        gridView = (GridView) view.findViewById(R.id.gv_music);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.just(F.app_type.music)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return appsDao.queryDataByType(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AppInfo>>() {
                    @Override
                    public void call(final List<AppInfo> appInfos) {
                        appsCustomAdapter = new AppsCustomAdapter(activity, appInfos);
                        gridView.setAdapter(appsCustomAdapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(position <appInfos.size()){
                                    String packageName = appInfos.get(position).getPackageName();
                                    AppUtils.launchApp(getContext() , packageName);
                                }else {
                                    Intent intent = new Intent(getActivity() , AppSelectActivity.class);
                                    intent.putExtra("app_type" ,F.app_type.music);
                                    startActivity(intent);
                                }
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

}
