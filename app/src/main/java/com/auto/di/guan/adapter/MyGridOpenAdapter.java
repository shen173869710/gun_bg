package com.auto.di.guan.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.dialog.MainoptionDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.task.TaskFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyGridOpenAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context context;
    private List<DeviceInfo> datas = new ArrayList<DeviceInfo>();

    private int screenWidth;
    private int screenHight;
    private DisplayMetrics dm = new DisplayMetrics();
    private WindowManager manager;
    public MyGridOpenAdapter(Context context, List<DeviceInfo> datas) {
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
            convertView = mInflater.inflate(R.layout.grid_item, null);
            holder.grid_item_layout = (RelativeLayout) convertView.findViewById(R.id.grid_item_layout);
            holder.grid_item_device = (ImageView) convertView.findViewById(R.id.grid_item_device);
            holder.grid_item_device_id = (TextView) convertView.findViewById(R.id.grid_item_device_id);
            holder.grid_item_device_name = (TextView) convertView.findViewById(R.id.grid_item_device_name);
            holder.grid_item_device_value = (TextView) convertView.findViewById(R.id.grid_item_device_value);

            holder.grid_item_left_layout = (RelativeLayout) convertView.findViewById(R.id.grid_item_left_layout);
            holder.grid_item_left_image = (ImageView) convertView.findViewById(R.id.grid_item_left_image);
            holder.grid_item_left_group = (TextView) convertView.findViewById(R.id.grid_item_left_group);
            holder.grid_item_left_sel = (TextView) convertView.findViewById(R.id.grid_item_left_sel);
            holder.grid_item_left_id = (TextView) convertView.findViewById(R.id.grid_item_left_id);

            holder.grid_item_right_layout = (RelativeLayout) convertView.findViewById(R.id.grid_item_right_layout);
            holder.grid_item_right_image = (ImageView) convertView.findViewById(R.id.grid_item_right_image);
            holder.grid_item_right_group = (TextView) convertView.findViewById(R.id.grid_item_right_group);
            holder.grid_item_right_sel = (TextView) convertView.findViewById(R.id.grid_item_right_sel);
            holder.grid_item_right_id = (TextView) convertView.findViewById(R.id.grid_item_right_id);
            int itemWidth = screenWidth - (int)context.getResources().getDimension(R.dimen.main_table_list_width);
            int itemHeight = screenHight - (int)context.getResources().getDimension(R.dimen.main_grid_width)- MainActivity.windowTop;
            AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) convertView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new AbsListView.LayoutParams(itemWidth/ Entiy.GRID_COLUMNS, itemWidth/ Entiy.GRID_COLUMNS);
                holder.grid_item_layout.setLayoutParams(layoutParams);
            }else {
                layoutParams.width = itemWidth/ Entiy.GRID_COLUMNS;
                layoutParams.height = itemWidth/ Entiy.GRID_COLUMNS;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        int itemWidth = screenWidth - (int)context.getResources().getDimension(R.dimen.main_table_list_width);
//        int itemHeight = screenHight - (int)context.getResources().getDimension(R.dimen.main_grid_width)- MainActivity.windowTop;
//        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(itemWidth/ Entiy.GRID_COLUMNS, itemWidth/ Entiy.GRID_COLUMNS);
//        holder.grid_item_layout.setLayoutParams(layoutParams);
        final DeviceInfo deviceInfo = datas.get(position);
        holder.grid_item_device_id.setText(deviceInfo.getDeviceSort()+"");

        /******设备未绑定******/
        if (deviceInfo.getDeviceStatus() == Entiy.DEVEICE_UNBIND) {
            holder.grid_item_device_name.setVisibility(View.INVISIBLE);
            holder.grid_item_device.setVisibility(View.INVISIBLE);
            holder.grid_item_device_value.setVisibility(View.INVISIBLE);
            holder.grid_item_left_layout.setVisibility(View.INVISIBLE);
            holder.grid_item_right_layout.setVisibility(View.INVISIBLE);
        }else {
            if (!TextUtils.isEmpty(deviceInfo.getDeviceName())) {
                holder.grid_item_device_name.setText(deviceInfo.getDeviceName()+"");
                holder.grid_item_device_name.setVisibility(View.VISIBLE);
            }
            holder.grid_item_device_value.setVisibility(View.VISIBLE);
            holder.grid_item_device_value.setText(deviceInfo.getElectricQuantity()+"%");
            holder.grid_item_device.setVisibility(View.VISIBLE);
            holder.grid_item_left_layout.setVisibility(View.VISIBLE);
            holder.grid_item_left_sel.setVisibility(View.GONE);


            ControlInfo controlInfo_0 = deviceInfo.getValveDeviceSwitchList().get(0);
            if (controlInfo_0.getValve_group_id() == 0) {
                holder.grid_item_left_group.setVisibility(View.GONE);
            }else {
                holder.grid_item_left_group.setVisibility(View.VISIBLE);
                holder.grid_item_left_group.setText(controlInfo_0.getValve_group_id()+"");
            }

            if (controlInfo_0.getValve_imgage_id() == 0) {
                holder.grid_item_left_image.setVisibility(View.INVISIBLE);
                holder.grid_item_left_layout.setOnClickListener(null);
            }else {
                holder.grid_item_left_image.setVisibility(View.VISIBLE);
                holder.grid_item_left_image.setImageResource(controlInfo_0.getValve_imgage_id());
                holder.grid_item_left_id.setText(""+controlInfo_0.getValve_alias());

                holder.grid_item_left_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDevice(controlInfo_0);
                    }
                });
            }
            ControlInfo controlInfo_1 = deviceInfo.getValveDeviceSwitchList().get(1);
            holder.grid_item_right_layout.setVisibility(View.VISIBLE);
            holder.grid_item_right_sel.setVisibility(View.GONE);

            if (controlInfo_1.getValve_group_id() == 0) {
                holder.grid_item_right_group.setVisibility(View.GONE);
            }else {
                holder.grid_item_right_group.setVisibility(View.VISIBLE);
                holder.grid_item_right_group.setText(controlInfo_1.getValve_group_id()+"");
            }
            if (controlInfo_1.getValve_imgage_id() == 0) {
                holder.grid_item_right_image.setVisibility(View.INVISIBLE);
                holder.grid_item_right_layout.setOnClickListener(null);
            }else {
                holder.grid_item_right_image.setVisibility(View.VISIBLE);
                holder.grid_item_right_image.setImageResource(controlInfo_1.getValve_imgage_id());
                holder.grid_item_right_id.setText(""+controlInfo_1.getValve_alias());


                holder.grid_item_right_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       openDevice(controlInfo_1);
                    }
                });
            }
        }
        return convertView;
    }

    public void setData(List<DeviceInfo> controlInfos) {
        datas.clear();
        datas.addAll(controlInfos);
        notifyDataSetChanged();
    }
    class ViewHolder {
        public RelativeLayout grid_item_layout;
        public ImageView grid_item_device;
        public TextView grid_item_device_id;
        public TextView grid_item_device_name;
        public TextView grid_item_device_value;

        public RelativeLayout grid_item_left_layout;
        public ImageView grid_item_left_image;
        public TextView grid_item_left_group;
        public TextView grid_item_left_sel;
        public TextView grid_item_left_id;

        public RelativeLayout grid_item_right_layout;
        public ImageView grid_item_right_image;
        public TextView grid_item_right_group;
        public TextView grid_item_right_sel;
        public TextView grid_item_right_id;
    }

    private void openDevice(final ControlInfo controlInfo) {
        String status = "关闭";
        if (controlInfo.getValve_status() == Entiy.CONTROL_STATUS＿RUN) {
            status = "开启";
        }
        MainoptionDialog.ShowDialog((Activity) context,controlInfo , "手动操作",status,new MainoptionDialog.ItemClick() {
            @Override
            public void onItemClick(int index) {
                /**
                 *    index = 0  读
                 *    index = 1  开阀
                 *    index = 2  关阀
                 */
                if (index == 0) {
                    TaskFactory.createReadTask(controlInfo, TaskEntiy.TASK_OPTION_READ ,Entiy.ACTION_TYPE_4);
                    TaskFactory.createReadEndTask();
                    TaskManager.getInstance().startTask();
                }else if (index == 1) {
                    TaskFactory.createOpenTask(controlInfo);
                    TaskFactory.createReadTask(controlInfo, TaskEntiy.TASK_OPTION_OPEN_READ ,Entiy.ACTION_TYPE_4);
                    TaskFactory.createReadEndTask();
                    TaskManager.getInstance().startTask();
                }else if (index == 2) {
                    TaskFactory.createCloseTask(controlInfo);
                    TaskFactory.createReadTask(controlInfo, TaskEntiy.TASK_OPTION_CLOSE_READ ,Entiy.ACTION_TYPE_4);
                    TaskFactory.createReadEndTask();
                    TaskManager.getInstance().startTask();
                }
            }
        });
    }
}
