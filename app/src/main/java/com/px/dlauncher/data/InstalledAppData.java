package com.px.dlauncher.data;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;


import com.px.dlauncher.Application;
import com.px.dlauncher.F;
import com.px.dlauncher.activity.IMainActivity;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.sql.AppsDao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class InstalledAppData implements Runnable {
    @Override
    public void run() {
        getApps();
    }

    private List<AppInfo> getApps (){
        AppsDao appsDao = AppsDao.getInstance(Application.getContext());
        List<AppInfo> list = new ArrayList<>();
        PackageManager packageManager = Application.getContext().getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> localList = packageManager.queryIntentActivities(intent ,0);
        Iterator<ResolveInfo> iterator = null;
        if(localList != null) {
            iterator = localList.iterator();
        }
        while (true) {
            if (!iterator.hasNext()) {
                break;
            }
            ResolveInfo resolveInfo = iterator.next();
            String packageName = resolveInfo.activityInfo.packageName;
            if(!F.packageName.setting.equals(packageName) && !F.packageName.app.equals(packageName)){
                AppInfo appInfo = new AppInfo();
                appInfo.setLabel(resolveInfo.loadLabel(packageManager).toString());
                appInfo.setPackageName(packageName);
                if(F.packageName.kodi.equals(packageName) || F.packageName.youtube.equals(packageName)){
                    appInfo.setType(F.app_type.online);
                }else if(F.packageName.player.equals(packageName)){
                    appInfo.setType(F.app_type.videos);
                }else {
                    appInfo.setType(F.app_type.apps);
                }
                appInfo.setShortcut("1");
                list.add(appInfo);
                if(F.packageName.google_play.equals(packageName) || F.packageName.file.equals(packageName)){
                    appInfo.setShortcut(F.app_type.shortcut);
                }
                if(!appsDao.isExists(appInfo)) {
                    appsDao.insertData(appInfo);
                }
            }

        }
        return list;
    }
}
