package com.auto.di.guan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.entity.CmdStatus;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/8.
 */

public class DialogListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CmdStatus> list;
    public DialogListViewAdapter(Context context, ArrayList<CmdStatus> list){
        this.mContext = context;
        this.list = list;
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
       ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialoglistviewitem, null);
            holder.cmd_name = (TextView) convertView.findViewById(R.id.cmd_name);
            holder.cmd_start = (TextView) convertView.findViewById(R.id.cmd_start);
            holder.cmd_end = (TextView) convertView.findViewById(R.id.cmd_end);
            holder.cmd_read_start = (TextView) convertView.findViewById(R.id.cmd_read_start);
            holder.cmd_read_middle = (TextView) convertView.findViewById(R.id.cmd_read_middle);
            holder.cmd_read_end = (TextView) convertView.findViewById(R.id.cmd_read_end);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cmd_name.setText("控制阀"+ list.get(position).controlName+"号");

        if (TextUtils.isEmpty(list.get(position).cmd_start)) {
            holder.cmd_start.setText("");
        }else {
            holder.cmd_start.setText(list.get(position).cmd_start);
        }

        if (TextUtils.isEmpty(list.get(position).cmd_end)) {
            holder.cmd_end.setText("");
        }else {
            holder.cmd_end.setText(list.get(position).cmd_end);
        }

        if (TextUtils.isEmpty(list.get(position).cmd_read_start)) {
            holder.cmd_read_start.setText("");
        }else {
            holder.cmd_read_start.setText(list.get(position).cmd_read_start);
        }

        if (TextUtils.isEmpty(list.get(position).cmd_read_middle)) {
            holder.cmd_read_middle.setText("");
        }else {
            holder.cmd_read_middle.setText(list.get(position).cmd_read_middle);
        }

        if (TextUtils.isEmpty(list.get(position).cmd_read_end)) {
            holder.cmd_read_end.setText("");
        }else {
            holder.cmd_read_end.setText(list.get(position).cmd_read_end);
        }

        return  convertView;
    }
    class ViewHolder {
        public TextView cmd_name;
        public TextView cmd_start;
        public TextView cmd_end;
        public TextView cmd_read_start;
        public TextView cmd_read_middle;
        public TextView cmd_read_end;
    }


}
