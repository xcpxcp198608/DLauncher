package com.px.dlauncher.beans;

public class AppInfo {

    private int id;
    private String label;
    private String packageName;
    private String type;
    private String shortcut;

    @Override
    public String toString() {
        return "AppInfo{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", packageName='" + packageName + '\'' +
                ", type='" + type + '\'' +
                ", shortcut='" + shortcut + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
}
