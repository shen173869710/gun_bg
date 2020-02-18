package com.auto.di.guan.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.entity.Entiy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class StatusAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context context;
    private List<ControlInfo> datas = new ArrayList<ControlInfo>();

    private int screenWidth;
    private int screenHight;
    private DisplayMetrics dm = new DisplayMetrics();
    private WindowManager manager;
    public StatusAdapter(Context context, List<ControlInfo> datas) {
        this.context = context;
        this.datas = datas;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHight = dm.heightPixels;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.group_status_list_item, null);
            holder.group_status_layout = (LinearLayout) convertView.findViewById(R.id.group_status_layout);
            holder.group_status_image = (ImageView) convertView.findViewById(R.id.group_status_image);
            holder.group_status_name = (TextView) convertView.findViewById(R.id.group_status_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int itemWidth = screenWidth - (int)context.getResources().getDimension(R.dimen.main_table_list_width);
        int itemHeight = screenHight - (int)context.getResources().getDimension(R.dimen.main_grid_width)- MainActivity.windowTop;
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(itemWidth/ Entiy.GRID_COLUMNS, itemWidth/ Entiy.GRID_COLUMNS);
        holder.group_status_layout.setLayoutParams(layoutParams);
        holder.group_status_name.setText(datas.get(position).getValve_name()+"");
        holder.group_status_image.setImageResource(datas.get(position).getValve_imgage_id());


        return convertView;
    }

    public void setData(List<ControlInfo> controlInfos) {
        datas.clear();
        datas.addAll(controlInfos);
//        datas = controlInfos;
        notifyDataSetChanged();
    }
    class ViewHolder {
        public LinearLayout group_status_layout;
        public ImageView group_status_image;
        public TextView group_status_name;

    }
}
