package com.auto.di.guan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.entity.ManualItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/8.
 */

public class ControlGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ManualItem> alist;
    public ControlGridAdapter(Context context, ArrayList<ManualItem> list){
        this.mContext = context;
        this.alist = list;
    }
    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int position) {
        return alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.controlgridviewitem, null);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radiobtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.radioButton.setText(alist.get(position).getName());
        holder.radioButton.setChecked(alist.get(position).isSelect());
        return  convertView;
    }
    class ViewHolder {
        public RadioButton radioButton;
    }
}
