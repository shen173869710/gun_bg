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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.MyApplication;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.dialog.MainoptionDialog;
import com.auto.di.guan.entity.ControlOptionEvent;
import com.auto.di.guan.entity.Entiy;

import org.greenrobot.eventbus.EventBus;

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
            holder.grid_item_layout = (LinearLayout) convertView.findViewById(R.id.grid_item_layout);
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
        holder.grid_item_device_id.setText(datas.get(position).deviceId+"");
        final DeviceInfo deviceInfo = datas.get(position);

        /******设备未绑定******/
        if (deviceInfo.status == Entiy.DEVEICE_UNBIND) {
            holder.grid_item_device_name.setVisibility(View.INVISIBLE);
            holder.grid_item_device.setVisibility(View.INVISIBLE);
            holder.grid_item_device_value.setVisibility(View.INVISIBLE);
            holder.grid_item_left_layout.setVisibility(View.INVISIBLE);
            holder.grid_item_right_layout.setVisibility(View.INVISIBLE);
        }else {
            if (!TextUtils.isEmpty(datas.get(position).getDeviceName())) {
                holder.grid_item_device_name.setText(datas.get(position).getDeviceName()+"");
                holder.grid_item_device_name.setVisibility(View.VISIBLE);
            }
            holder.grid_item_device_value.setVisibility(View.VISIBLE);
            holder.grid_item_device_value.setText(deviceInfo.elect+"%");
            holder.grid_item_device.setVisibility(View.VISIBLE);
            holder.grid_item_left_layout.setVisibility(View.VISIBLE);
            holder.grid_item_left_sel.setVisibility(View.GONE);
            if (deviceInfo.controlInfos.get(0).groupId == 0) {
                holder.grid_item_left_group.setVisibility(View.GONE);
            }else {
                holder.grid_item_left_group.setVisibility(View.VISIBLE);
                holder.grid_item_left_group.setText(deviceInfo.controlInfos.get(0).groupId+"");
            }

            if (deviceInfo.controlInfos.get(0).imageId == 0) {
                holder.grid_item_left_image.setVisibility(View.INVISIBLE);
                holder.grid_item_left_layout.setOnClickListener(null);
            }else {
                holder.grid_item_left_image.setVisibility(View.VISIBLE);
                holder.grid_item_left_image.setImageResource(deviceInfo.controlInfos.get(0).imageId);
                if (deviceInfo.controlInfos.get(0).controId != 0) {
                    holder.grid_item_left_id.setText(""+deviceInfo.getControl_1());
                }
                holder.grid_item_left_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDevice(deviceInfo.controlInfos.get(0));
                    }
                });
            }

            holder.grid_item_right_layout.setVisibility(View.VISIBLE);
            holder.grid_item_right_sel.setVisibility(View.GONE);
            if (deviceInfo.controlInfos.get(1).groupId == 0) {
                holder.grid_item_right_group.setVisibility(View.GONE);
            }else {
                holder.grid_item_right_group.setVisibility(View.VISIBLE);
                holder.grid_item_right_group.setText(deviceInfo.controlInfos.get(1).groupId+"");
            }
            if (deviceInfo.controlInfos.get(1).imageId == 0) {
                holder.grid_item_right_image.setVisibility(View.INVISIBLE);
                holder.grid_item_right_layout.setOnClickListener(null);
            }else {
                holder.grid_item_right_image.setVisibility(View.VISIBLE);
                holder.grid_item_right_image.setImageResource(deviceInfo.controlInfos.get(1).imageId);
                if (deviceInfo.controlInfos.get(1).controId != 0) {
                    holder.grid_item_right_id.setText(""+deviceInfo.getControl_2());
                }

                holder.grid_item_right_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       openDevice(deviceInfo.controlInfos.get(1));
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
        public LinearLayout grid_item_layout;
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
        boolean isStart = false;

        MainoptionDialog.ShowDialog((Activity) context, "手动操作", new MainoptionDialog.ItemClick() {
            @Override
            public void onItemClick(int index) {
                EventBus.getDefault().post(new ControlOptionEvent(index,controlInfo, true));
            }
        });
//        if (controlInfo.status == Entiy.CONTROL_STATUS＿RUN) {
//                isStart = true;
//        }
//
//        if (controlInfo.status == Entiy.CONTROL_STATUS＿ERROR) {
//            MainShowDialog.ShowDialog((Activity) context, "查询状态", "读取阀门异常状态是的修复", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EventBus.getDefault().post(new ControlOptionEvent(controlInfo, true));
//                }
//            });
//            return;
//        }
//
//        if (!isStart) {
//            MainShowDialog.ShowDialog((Activity) context, "手动开启", "是否开启当前阀门", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    if (MyApplication.getInstance().isGroupStart()) {
////                        return;
////                    }
//                    EventBus.getDefault().post(new ControlOptionEvent(controlInfo, true));
////                    controlInfo.imageId = R.mipmap.lighe_2;
////                    controlInfo.status = Entiy.CONTROL_STATUS＿2;
////                    DBManager.getInstance(context).updateDeviceList(datas);
////                    notifyDataSetChanged();
//                }
//            });
//        }else {
//            MainShowDialog.ShowDialog((Activity) context, "手动关闭", "是否关闭当前阀门", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    if (MyApplication.getInstance().isGroupStart()) {
////                        return;
////                    }
//                    EventBus.getDefault().post(new ControlOptionEvent(controlInfo, false));
////                    controlInfo.imageId = R.mipmap.lighe_1;
////                    controlInfo.status = Entiy.CONTROL_STATUS＿1;
////                    DBManager.getInstance(context).updateDeviceList(datas);
////                    notifyDataSetChanged();
//                }
//            });
//        }

    }


}
