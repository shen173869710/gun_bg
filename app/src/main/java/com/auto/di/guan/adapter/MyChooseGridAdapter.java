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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.ImageStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyChooseGridAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private Context context;
    private List<ControlInfo> datas = new ArrayList<ControlInfo>();

    private int screenWidth;
    private int screenHight;
    private DisplayMetrics dm = new DisplayMetrics();
    private WindowManager manager;
    public MyChooseGridAdapter(Context context, List<ControlInfo> datas) {
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
//            holder.grid_item_text = (TextView) convertView.findViewById(R.id.grid_item_text);
//            holder.grid_item_image = (ImageView) convertView.findViewById(R.id.grid_item_image);
//            holder.grid_item_sel = (TextView) convertView.findViewById(R.id.grid_item_sel);
//            holder.grid_item_id = (TextView) convertView.findViewById(R.id.grid_item_id);
            holder.grid_item_layout =  convertView.findViewById(R.id.grid_item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int itemWidth = screenWidth - (int)context.getResources().getDimension(R.dimen.main_table_list_width);
        int itemHeight = screenHight - (int)context.getResources().getDimension(R.dimen.main_table_height)*2- MainActivity.windowTop;
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(itemWidth/ Entiy.GRID_COLUMNS, itemHeight/Entiy.GRID_ROW);

        holder.grid_item_layout.setLayoutParams(layoutParams);
        holder.grid_item_id.setText(datas.get(position).getProtocalId()+"");
        ControlInfo info = datas.get(position);

//        if (datas.get(position).getGroupId() > 0) {
//            holder.grid_item_text.setVisibility(View.VISIBLE);
//            holder.grid_item_text.setText(datas.get(position).getControId()+"");
//        }else {
//            holder.grid_item_text.setVisibility(View.INVISIBLE);
//        }
//
//        holder.grid_item_image.setBackgroundResource(ImageStatus.getImageId(info));
//        holder.grid_item_sel.setVisibility(View.GONE);
//        if (info.isSelect) {
//            holder.grid_item_sel.setBackgroundResource(R.drawable.img_selected);
//        }else {
//            holder.grid_item_sel.setBackgroundResource(R.drawable.img_unselected);
//        }
        return convertView;
    }

    class ViewHolder {
        public TextView grid_item_text;
        public ImageView grid_item_image;
        public TextView grid_item_sel;
        public TextView grid_item_id;
        public View grid_item_layout;
    }
}
