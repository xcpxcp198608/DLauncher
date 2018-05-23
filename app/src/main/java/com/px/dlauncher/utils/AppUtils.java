package com.px.dlauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class AppUtils {

    public static boolean isInstalled (Context context , String packageName){
        if(TextUtils.isEmpty(packageName)){
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(packageName , PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static Drawable getIcon (Context context , String packageName){
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName ,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(applicationInfo != null){
            return applicationInfo.loadIcon(packageManager);
        }else{
            return null;
        }
    }

    public static String getLabelName (Context context , String packageName) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName ,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(applicationInfo != null){
            return applicationInfo.loadLabel(packageManager).toString();
        }else{
            return null;
        }
    }

    public static String getVersionName (Context context , String packageName) {
        if(TextUtils.isEmpty(packageName)){
            return "apkPackageName error";
        }
        String apkVersionName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName , PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                apkVersionName = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return apkVersionName;
    }

    public static int getVersionCode (Context context , String packageName) {
        if(TextUtils.isEmpty(packageName)){
            return 0;
        }
        int apkVersionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName , PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                apkVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return apkVersionCode;
    }

    public static boolean isLastVersion (Context context ,String packageName ,int versionCode) {
        if(AppUtils.isInstalled(context , packageName)) {
            int localVersionCode = AppUtils.getVersionCode(context,packageName);
            if(versionCode > localVersionCode){
                return true;
            }else {
                return false;
            }
        }else {
            return true;
        }
    }

    public static String getApkPackageName (Context context , String filePath ,String fileName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        String apkPackageName = null;
        packageInfo = packageManager.getPackageArchiveInfo(filePath+fileName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            applicationInfo = packageInfo.applicationInfo;
            apkPackageName = applicationInfo.packageName;
        }else{
            return null;
        }
        return apkPackageName;
    }

    public static String getApkVersionName (Context context , String filePath ,String fileName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        String apkVersionName = null;
        packageInfo = packageManager.getPackageArchiveInfo(filePath+fileName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            apkVersionName = packageInfo.versionName;
        }else{
            return null;
        }
        return apkVersionName;
    }

    public static boolean isApkCanInstall (Context context , String filePath ,String fileName){
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(filePath+"/"+fileName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            return true;
        }else{
            return false;
        }
    }

    public static int getApkVersionCode (Context context , String filePath ,String fileName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        int apkVersionCode = 0;
        packageInfo = packageManager.getPackageArchiveInfo(filePath+fileName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            apkVersionCode = packageInfo.versionCode;
        }else{
            return 0;
        }
        return apkVersionCode;
    }

    public static void installApk (Context context , String filePath , String fileName){
        File file = new File(filePath, fileName);
        if(!file.exists()) {
            Toast.makeText(context , "Apk file is not exists" ,Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isApkCanInstall(context , filePath ,fileName)){
            Toast.makeText(context , "Apk file can not install" ,Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void launchApp (Context context ,String packageName){
        if(isInstalled(context , packageName)){
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                context.startActivity(intent);
            }else{
                intent = context.getPackageManager().getLeanbackLaunchIntentForPackage(packageName);
                if(intent != null){
                    context.startActivity(intent);
                }
            }
        }
    }
}
