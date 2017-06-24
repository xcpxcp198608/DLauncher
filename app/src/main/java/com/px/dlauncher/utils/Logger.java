package com.px.dlauncher.utils;

import android.util.Log;

public class Logger {

    private static String tag1;
    private static final int V = 1;
    private static final int D = 2;
    private static final int I = 3;
    private static final int W = 4;
    private static final int E = 5;
    private static final int NO = 6;
    private static final int LEVEL = D;

    /**
     * 初始化
     * @param tag 设置标签
     */
    public static void init (String tag){
        tag1 = tag;
    }


    public static void v (String message){
        if(LEVEL <= V){
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.v(tag1 ,  "----->"+message+ "          "+getInfo(stackTraceElement));
        }

    }

    public static void d (String message){
        if(LEVEL <= D){
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(tag1 ,  "----->"+message+ "          "+getInfo(stackTraceElement));
        }
    }

    public static void i (String message){
        if(LEVEL <= I){
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(tag1 ,  "----->"+message+ "          "+getInfo(stackTraceElement));
        }
    }

    public static void w (String message){
        if(LEVEL <= W){
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w(tag1 ,  "----->"+message+ "          "+getInfo(stackTraceElement));
        }
    }

    public static void e (String message){
        if(LEVEL <= E){
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(tag1 ,  "----->"+message+ "          "+getInfo(stackTraceElement));
        }
    }

    private static String getInfo(StackTraceElement stackTraceElement) {
        StringBuilder s = new StringBuilder();
        String threadName = Thread.currentThread().getName(); //当前线程名称
        long threadId = Thread.currentThread().getId(); //当前线程ID
        String javaName = stackTraceElement.getFileName(); //当前java文件名
//        String className = stackTraceElement.getClassName(); //当前类名
//        String methodName = stackTraceElement.getMethodName(); //当前方法名称
        int lineNumber = stackTraceElement.getLineNumber(); //当前语句行数
        s.append("<");
        s.append("ThreadName= "+threadName+",");
        s.append("ThreadId= "+threadId+",");
        s.append("JavaName= "+javaName+",");
        //s.append("ClassName= "+className+",");
        //s.append("MethodName= "+methodName+",");
        s.append("LineNumber= "+lineNumber+",");
        s.append(">");
        return s.toString();
    }
}
