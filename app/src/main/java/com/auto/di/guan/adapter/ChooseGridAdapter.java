package com.auto.di.guan.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
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

public class ChooseGridAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context context;
    private List<DeviceInfo> datas = new ArrayList<>();

    private int screenWidth;
    private int screenHight;
    private DisplayMetrics dm = new DisplayMetrics();
    private WindowManager manager;
    public ChooseGridAdapter(Context context, List<DeviceInfo> datas) {
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

            holder.grid_item_left_layout = (RelativeLayout) convertView.findViewById(R.id.grid_item_left_layout);
            holder.grid_item_left_image = (ImageView) convertView.findViewById(R.id.grid_item_left_image);
            holder.grid_item_left_group = (TextView) convertView.findViewById(R.id.grid_item_left_group);
            holder.grid_item_left_sel = (TextView) convertView.findViewById(R.id.grid_item_left_sel);

            holder.grid_item_right_layout = (RelativeLayout) convertView.findViewById(R.id.grid_item_right_layout);
            holder.grid_item_right_image = (ImageView) convertView.findViewById(R.id.grid_item_right_image);
            holder.grid_item_right_group = (TextView) convertView.findViewById(R.id.grid_item_right_group);
            holder.grid_item_right_sel = (TextView) convertView.findViewById(R.id.grid_item_right_sel);

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


        holder.grid_item_device_id.setText(datas.get(position).deviceId+"");
        bindView(holder, position);
        return convertView;
    }

    private void bindView(ViewHolder holder, int position) {
        final DeviceInfo deviceInfo = datas.get(position);
        if (deviceInfo.status == Entiy.DEVEICE_UNBIND) {
            holder.grid_item_device.setVisibility(View.INVISIBLE);
            holder.grid_item_left_layout.setVisibility(View.INVISIBLE);
            holder.grid_item_right_layout.setVisibility(View.INVISIBLE);
        }else {
            holder.grid_item_device.setVisibility(View.VISIBLE);

            ControlInfo info1 = deviceInfo.controlInfos.get(0);
            if (info1.imageId == 0) {
                holder.grid_item_left_layout.setVisibility(View.INVISIBLE);
                holder.grid_item_left_layout.setOnClickListener(null);
            }else {
                holder.grid_item_left_layout.setVisibility(View.VISIBLE);
                holder.grid_item_left_image.setVisibility(View.VISIBLE);
                holder.grid_item_left_image.setImageResource(info1.imageId);
                holder.grid_item_left_sel.setVisibility(View.VISIBLE);
                if (info1.isSelect) {
                    holder.grid_item_left_sel.setBackgroundResource(R.drawable.img_selected);
                }else {
                    holder.grid_item_left_sel.setBackgroundResource(R.drawable.img_unselected);
                }

                if (info1.groupId == 0) {
                    holder.grid_item_left_group.setVisibility(View.INVISIBLE);
                }else {
                    holder.grid_item_left_group.setVisibility(View.VISIBLE);
                    holder.grid_item_left_group.setText(info1.groupId+"");
                }

                if (info1.groupId > 0) {
                    holder.grid_item_left_sel.setVisibility(View.GONE);
                    deviceInfo.getControlInfos().get(0).isSelect = false;
                }else {
                    holder.grid_item_left_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deviceInfo.getControlInfos().get(0).isSelect = !deviceInfo.getControlInfos().get(0).isSelect;
                            notifyDataSetChanged();
                        }
                    });
                }



            }

            ControlInfo info2 = deviceInfo.controlInfos.get(1);
            if (info2.imageId == 0) {
                holder.grid_item_right_layout.setVisibility(View.INVISIBLE);
            }else {
                holder.grid_item_right_layout.setVisibility(View.VISIBLE);
                holder.grid_item_right_image.setVisibility(View.VISIBLE);
                holder.grid_item_right_image.setImageResource(info1.imageId);
                holder.grid_item_right_sel.setVisibility(View.VISIBLE);
                if (info2.isSelect) {
                    holder.grid_item_right_sel.setBackgroundResource(R.drawable.img_selected);
                }else {
                    holder.grid_item_right_sel.setBackgroundResource(R.drawable.img_unselected);
                }

                if (info2.groupId == 0) {
                    holder.grid_item_right_group.setVisibility(View.INVISIBLE);
                }else {
                    holder.grid_item_right_group.setVisibility(View.VISIBLE);
                    holder.grid_item_right_group.setText(info2.groupId+"");
                }

                if (info2.groupId > 0) {
                    holder.grid_item_right_sel.setVisibility(View.GONE);
                    deviceInfo.getControlInfos().get(1).isSelect = false;
                }else {
                    holder.grid_item_right_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deviceInfo.getControlInfos().get(1).isSelect = !deviceInfo.getControlInfos().get(1).isSelect;
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        }
    }

    class ViewHolder {
        public LinearLayout grid_item_layout;
        public ImageView grid_item_device;
        public TextView grid_item_device_id;
        public RelativeLayout grid_item_left_layout;
        public ImageView grid_item_left_image;
        public TextView grid_item_left_group;
        public TextView grid_item_left_sel;

        public RelativeLayout grid_item_right_layout;
        public ImageView grid_item_right_image;
        public TextView grid_item_right_group;
        public TextView grid_item_right_sel;
    }
}
