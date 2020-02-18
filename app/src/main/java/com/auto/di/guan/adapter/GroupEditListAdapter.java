package com.auto.di.guan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.entity.Entiy;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class GroupEditListAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context context;
    private List<ControlInfo> datas;

    public GroupEditListAdapter(Context context, List<ControlInfo> datas) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.group_edit_list_item, null);
            holder.group_item_name = (TextView) convertView.findViewById(R.id.group_item_name);
            holder.group_item_status = (TextView) convertView.findViewById(R.id.group_item_status);
            holder.group_edit_del = (TextView) convertView.findViewById(R.id.group_edit_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ControlInfo controlInfo = datas.get(position);
        holder.group_item_name.setText(controlInfo.getValve_id() + "阀控器");
        if (controlInfo.getValve_status() == Entiy.CONTROL_STATUS＿RUN) {
            holder.group_item_status.setText("工作当中");
        } else if (controlInfo.getValve_status() == Entiy.CONTROL_STATUS＿ERROR) {
            holder.group_item_status.setText("工作异常");
        } else {
            holder.group_item_status.setText("可以编辑");
        }

        holder.group_edit_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlInfo.getValve_status() != Entiy.CONTROL_STATUS＿RUN ||
                        controlInfo.getValve_status() != Entiy.CONTROL_STATUS＿ERROR) {
                    MainShowDialog.ShowDialog((Activity) context, "退出分组", "是退出当前分组", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            controlInfo.setValve_group_id(0);
                            datas.remove(position);
                            notifyDataSetChanged();
                            ControlInfoSql.updateControl(controlInfo);
                        }
                    });
                } else {
                    Toast.makeText(context, "阀控器处于不可以编辑状态", Toast.LENGTH_LONG).show();
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView group_item_name;
        public TextView group_item_status;
        public TextView group_edit_del;
    }
}
