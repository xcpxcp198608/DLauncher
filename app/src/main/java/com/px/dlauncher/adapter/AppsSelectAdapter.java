package com.px.dlauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.px.dlauncher.R;
import com.px.dlauncher.beans.AppInfo;
import com.px.dlauncher.utils.AppUtils;

import java.util.List;


public class AppsSelectAdapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> list;
    private LayoutInflater layoutInflater;
    private String appType;

    public AppsSelectAdapter(Context context, List<AppInfo> list ,String appType) {
        this.context = context;
        this.list = list;
        this.appType = appType;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_app_select , null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvLabel = (TextView) convertView.findViewById(R.id.tv_label);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AppInfo appInfo = list.get(position);
        viewHolder.ivIcon.setImageDrawable(AppUtils.getIcon(context,appInfo.getPackageName()));
        viewHolder.tvLabel.setText(AppUtils.getLabelName(context, appInfo.getPackageName()));
        if(appType.equals(appInfo.getType())){
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);
        }
        return convertView;
    }

    static class ViewHolder {
         ImageView ivIcon;
         TextView tvLabel;
         CheckBox checkBox;
    }
}
