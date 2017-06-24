package com.px.dlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.px.dlauncher.Application;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.sql.AppsDao;
import com.px.dlauncher.utils.AppUtils;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class AppChangeReceiver extends BroadcastReceiver {
    private AppsDao appsDao;
    private Context mContext;

    public AppChangeReceiver() {
        mContext = Application.getContext();
        appsDao = AppsDao.getInstance(mContext);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){
            String packageName = intent.getData().getSchemeSpecificPart();
            Observable.just(packageName)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<String, Object>() {
                        @Override
                        public Object call(String s) {
                            AppInfo appInfo = new AppInfo();
                            appInfo.setPackageName(s);
                            appInfo.setLabel(AppUtils.getLabelName(mContext , s));
                            appsDao.insertOrUpdateData(appInfo);
                            return null;
                        }
                    })
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {

                        }
                    });
        }else if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
            String packageName = intent.getData().getSchemeSpecificPart();
            Observable.just(packageName)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<String, Object>() {
                        @Override
                        public Object call(String s) {
                            appsDao.deleteByPackageName(s);
                            return null;
                        }
                    })
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {

                        }
                    });

        }else if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){

        }
    }
}
