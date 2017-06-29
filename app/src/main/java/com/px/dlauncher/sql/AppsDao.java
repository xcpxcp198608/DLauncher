package com.px.dlauncher.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.px.dlauncher.beans.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppsDao {

    private SQLiteDatabase sqLiteDatabase;

    private AppsDao (Context context){
        sqLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    private static volatile AppsDao instance;
    public static AppsDao getInstance(Context context){
        if(instance ==null){
            synchronized (AppsDao.class){
                if(instance ==null){
                    instance = new AppsDao(context);
                }
            }
        }
        return instance;
    }

    public boolean isExists(AppInfo appInfo){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "packageName=?",
                new String []{appInfo.getPackageName()} ,null ,null ,null);
        boolean isExists = cursor.moveToNext();
        cursor.close();
        return isExists;
    }


    public void insertData(AppInfo appInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("type",appInfo.getType());
        contentValues.put("label",appInfo.getLabel());
        contentValues.put("packageName" ,appInfo.getPackageName());
        contentValues.put("shortcut" ,appInfo.getShortcut());
        sqLiteDatabase.insert(SQLiteHelper.TABLE_NAME ,null , contentValues);
    }

    public void updateData(AppInfo appInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("type",appInfo.getType());
        sqLiteDatabase.update(SQLiteHelper.TABLE_NAME , contentValues,"packageName=?" ,new String []{appInfo.getPackageName()});
    }

    public void insertOrUpdateData(AppInfo appInfo){
        if(isExists(appInfo)){
            updateData(appInfo);
        }else{
            insertData(appInfo);
        }
    }

    public List<AppInfo> queryDataByType(String type){
        List<AppInfo> list =new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "type=?",
                new String []{type} ,null ,null ,"label");
        while(cursor.moveToNext()){
            AppInfo appInfo = new AppInfo();
            appInfo.setType(type);
            appInfo.setShortcut(cursor.getString(cursor.getColumnIndex("shortcut")));
            appInfo.setLabel(cursor.getString(cursor.getColumnIndex("label")));
            appInfo.setPackageName(cursor.getString(cursor.getColumnIndex("packageName")));
            list.add(appInfo);
        }
        cursor.close();
        return list;
    }

    public List<AppInfo> queryDataByShortcut(String shortcut){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "shortcut=?",
                new String []{shortcut} ,null ,null ,"label");
        List<AppInfo> list =new ArrayList<>();
        while(cursor.moveToNext()){
            AppInfo appInfo = new AppInfo();
            appInfo.setShortcut(shortcut);
            appInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            appInfo.setLabel(cursor.getString(cursor.getColumnIndex("label")));
            appInfo.setPackageName(cursor.getString(cursor.getColumnIndex("packageName")));
            list.add(appInfo);
        }
        cursor.close();
        return list;
    }

    public List<AppInfo> showShortcutData(){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "shortcut=?",
                new String []{"1"} ,null ,null ,"label");
        List<AppInfo> list =new ArrayList<>();
        while(cursor.moveToNext()){
            AppInfo appInfo = new AppInfo();
            appInfo.setShortcut("1");
            appInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            appInfo.setLabel(cursor.getString(cursor.getColumnIndex("label")));
            appInfo.setPackageName(cursor.getString(cursor.getColumnIndex("packageName")));
            list.add(appInfo);
        }
        cursor.close();
        return list;
    }

    public List<AppInfo> showShortcutData(String shortcut){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "shortcut=?",
                new String []{shortcut} ,null ,null ,"label");
        List<AppInfo> list =new ArrayList<>();
        while(cursor.moveToNext()){
            AppInfo appInfo = new AppInfo();
            appInfo.setShortcut(shortcut);
            appInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            appInfo.setLabel(cursor.getString(cursor.getColumnIndex("label")));
            appInfo.setPackageName(cursor.getString(cursor.getColumnIndex("packageName")));
            list.add(appInfo);
        }
        cursor.close();
        return list;
    }

    public List<AppInfo> queryData(){
        List<AppInfo> list =new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "_id>?",
                new String []{"0"} ,null ,null ,"label");
        while(cursor.moveToNext()){
            AppInfo appInfo = new AppInfo();
            appInfo.setShortcut(cursor.getString(cursor.getColumnIndex("shortcut")));
            appInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            appInfo.setLabel(cursor.getString(cursor.getColumnIndex("label")));
            appInfo.setPackageName(cursor.getString(cursor.getColumnIndex("packageName")));
            list.add(appInfo);
        }
        cursor.close();
        return list;
    }

    public void deleteByPackageName(String packageName){
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME ,"packageName=?" ,new String []{packageName});
    }

    public void deleteAll (){
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME ,"_id>?" ,new String []{"0"});
    }

    public void updateShortcut(AppInfo appInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("shortcut",appInfo.getShortcut());
        sqLiteDatabase.update(SQLiteHelper.TABLE_NAME , contentValues,"packageName=?" ,new String []{appInfo.getPackageName()});
    }
}
