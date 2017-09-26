package com.px.dlauncher;

import android.content.Context;
import android.os.Environment;

import com.px.dlauncher.data.InstalledAppData;
import com.px.dlauncher.utils.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Application extends android.app.Application {
    private static Context context;
    private static ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("----px----");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        context = getApplicationContext();
        executorService = Executors.newCachedThreadPool();
        executorService.execute(new InstalledAppData());
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext(), path);
    }

    public static Context getContext (){
        return context;
    }

    public static ExecutorService getThreadPool (){
        return executorService;
    }
}
