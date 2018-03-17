package com.px.dlauncher;


import android.os.Environment;

public final class F {

    public static final class app_type {
        public static final String apps = "apps";
        public static final String videos = "videos";
        public static final String online = "online";
        public static final String music = "music";
        public static final String shortcut = "shortcut";
    }

    public static final class packageName {
        public static final String gallery = "com.android.gallery3d";
        public static final String browser = "com.android.browser";
        public static final String chrome = "com.android.chrome";
        public static final String setting = "com.android.tv.settings";
        public static final String setting1 = "com.android.settings";
        public static final String rk_media_center = "com.rockchips.mediacenter";
        public static final String app = "com.px.dlauncher";
        public static final String google_play = "com.android.vending";
        public static final String file = "com.android.rockchip";
        public static final String youtube = "com.google.android.youtube.tv";
        public static final String kodi = "org.xbmc.kodi";
        public static final String player = "com.rockchips.mediacenter";
    }
    public static final class path {
        public static final String logcat = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Android/data/com.px.dlauncher/files/logcat/";
        }

}
