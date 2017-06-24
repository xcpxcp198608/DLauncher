package com.px.dlauncher.beans;

public class UsbEvent {

    public static final int USB_MOUNTED = 1;
    public static final int USB_UNMOUNTED = 2;
    public static final int SD_MOUNTED = 3;
    public static final int SD_UNMOUNTED = 4;

    public static final String USB_MOUNTED_PATH ="/storage/38C5-0F8C";
    public static final String USB_UNMOUNTED_PATH = "";
    public static final String SD_MOUNTED_PATH = "/storage/FE6B-2123";
    public static final String SD_UNMOUNTED_PATH = "";

    private int status ;
    private String path;

    public UsbEvent(int status , String path) {
        this.status = status;
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
