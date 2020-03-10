package com.auto.di.guan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.entity.ImageInfo;
import com.auto.di.guan.utils.NoFastClickUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */

public class ImageExpandableListViewaAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ImageInfo> groupArray;//组列表

    public ImageExpandableListViewaAdapter(Context a, List<ImageInfo> groupArray) {
        this.groupArray = groupArray;
        this.context = a;
    }

    /*-----------------Child */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return groupArray.get(groupPosition).childs;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expand_list_child_item, null);
            holder.expand_list_child_item_1 = (LinearLayout) convertView.findViewById(R.id.expand_list_child_item_1);
            holder.expand_list_child_item_1_tv = (TextView) convertView.findViewById(R.id.expand_list_child_item_1_tv);
            holder.expand_list_child_item_1_iv = (ImageView) convertView.findViewById(R.id.expand_list_child_item_1_iv);

            holder.expand_list_child_item_2 = (LinearLayout) convertView.findViewById(R.id.expand_list_child_item_2);
            holder.expand_list_child_item_2_tv = (TextView) convertView.findViewById(R.id.expand_list_child_item_2_tv);
            holder.expand_list_child_item_2_iv = (ImageView) convertView.findViewById(R.id.expand_list_child_item_2_iv);

            holder.expand_list_child_item_3 = (LinearLayout) convertView.findViewById(R.id.expand_list_child_item_3);
            holder.expand_list_child_item_3_tv = (TextView) convertView.findViewById(R.id.expand_list_child_item_3_tv);
            holder.expand_list_child_item_3_iv = (ImageView) convertView.findViewById(R.id.expand_list_child_item_3_iv);

            holder.expand_list_child_item_4 = (LinearLayout) convertView.findViewById(R.id.expand_list_child_item_4);
            holder.expand_list_child_item_4_tv = (TextView) convertView.findViewById(R.id.expand_list_child_item_4_tv);
            holder.expand_list_child_item_4_iv = (ImageView) convertView.findViewById(R.id.expand_list_child_item_4_iv);

            convertView.setTag(holder);
        }else {
            holder = (ChildHolder) convertView.getTag();
        }
        bindChildView(holder, groupPosition, childPosition);
        return convertView;
    }

    private void bindChildView(final ChildHolder holder, final int groupPosition, int childPosition) {
        final ImageInfo.Child child = groupArray.get(groupPosition).childs.get(childPosition);
        holder.expand_list_child_item_1.setVisibility(View.VISIBLE);
        holder.expand_list_child_item_2.setVisibility(View.VISIBLE);
        holder.expand_list_child_item_3.setVisibility(View.VISIBLE);
        holder.expand_list_child_item_4.setVisibility(View.VISIBLE);

        holder.expand_list_child_item_1_tv.setBackgroundResource(R.drawable.img_unselected);
        holder.expand_list_child_item_2_tv.setBackgroundResource(R.drawable.img_unselected);
        holder.expand_list_child_item_3_tv.setBackgroundResource(R.drawable.img_unselected);
        holder.expand_list_child_item_4_tv.setBackgroundResource(R.drawable.img_unselected);
        if (child.isSel == 0) {
            holder.expand_list_child_item_1_tv.setBackgroundResource(R.drawable.img_selected);
        }else if (child.isSel == 1) {
            holder.expand_list_child_item_2_tv.setBackgroundResource(R.drawable.img_selected);
        }else if (child.isSel == 2) {
            holder.expand_list_child_item_3_tv.setBackgroundResource(R.drawable.img_selected);
        }else if (child.isSel == 3) {
            holder.expand_list_child_item_4_tv.setBackgroundResource(R.drawable.img_selected);
        }

        holder.expand_list_child_item_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                upData(groupPosition, 0);
            }
        });
        holder.expand_list_child_item_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                upData(groupPosition, 1);
            }
        });

        holder.expand_list_child_item_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                upData(groupPosition, 2);
            }
        });
        holder.expand_list_child_item_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                upData(groupPosition, 3);
            }
        });

        if (child.array.length == 1) {
            holder.expand_list_child_item_2.setVisibility(View.INVISIBLE);
            holder.expand_list_child_item_3.setVisibility(View.INVISIBLE);
            holder.expand_list_child_item_4.setVisibility(View.INVISIBLE);
            holder.expand_list_child_item_2.setOnClickListener(null);
            holder.expand_list_child_item_3.setOnClickListener(null);
            holder.expand_list_child_item_4.setOnClickListener(null);

            holder.expand_list_child_item_1_iv.setImageResource(child.array[0]);
        }else if (child.array.length == 2) {
            holder.expand_list_child_item_3.setVisibility(View.INVISIBLE);
            holder.expand_list_child_item_4.setVisibility(View.INVISIBLE);
            holder.expand_list_child_item_3.setOnClickListener(null);
            holder.expand_list_child_item_4.setOnClickListener(null);

            holder.expand_list_child_item_1_iv.setImageResource(child.array[0]);
            holder.expand_list_child_item_2_iv.setImageResource(child.array[1]);
        }else {
            holder.expand_list_child_item_3.setVisibility(View.VISIBLE);
            holder.expand_list_child_item_4.setVisibility(View.VISIBLE);

            holder.expand_list_child_item_1_iv.setImageResource(child.array[0]);
            holder.expand_list_child_item_2_iv.setImageResource(child.array[1]);
            holder.expand_list_child_item_3_iv.setImageResource(child.array[2]);
            holder.expand_list_child_item_4_iv.setImageResource(child.array[3]);
        }

    }

    public void upData (int postion, int pos) {
        int size = groupArray.size();
        for (int i = 0; i < size; i++) {
            if (i == postion) {
                groupArray.get(i).childs.get(0).isSel = pos;
                groupArray.get(i).childs.get(0).imageDir = pos;
            }else {
                groupArray.get(i).childs.get(0).isSel = -1;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupArray.get(groupPosition).childs.size();
    }

    /* ----------------------------Group */
    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition);
    }

    @Override
    public int getGroupCount() {
        Log.e("----", "groupArray.size() == "+groupArray.size());
        return groupArray.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expand_list_group_item, null);
            holder.expand_list_group_num = (TextView) convertView.
                    findViewById(R.id.expand_list_group_num);
            holder.expand_list_group_time = (TextView) convertView.
                    findViewById(R.id.expand_list_group_time);
            holder.expand_list_group_state = (TextView) convertView.
                    findViewById(R.id.expand_list_group_state);
            holder.expand_list_group_icon = (ImageView) convertView.
                    findViewById(R.id.expand_list_group_icon);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        if (isExpanded) {
            holder.expand_list_group_icon.setBackgroundResource(R.mipmap.expand_group_p);
        } else {
            holder.expand_list_group_icon.setBackgroundResource(R.mipmap.expand_group_n);
        }
        holder.expand_list_group_num.setText(groupArray.get(groupPosition).groupName);

        bindGroupView(holder, groupPosition);
        return convertView;
    }

    private void bindGroupView(GroupHolder holder, int groupPosition) {
        holder.expand_list_group_num.setText(groupArray.get(groupPosition).groupName);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupHolder {
        TextView expand_list_group_num;
        TextView expand_list_group_time;
        TextView expand_list_group_state;
        ImageView expand_list_group_icon;
    }

    public class ChildHolder {
        LinearLayout expand_list_child_item_1;
        TextView expand_list_child_item_1_tv;
        ImageView expand_list_child_item_1_iv;

        LinearLayout expand_list_child_item_2;
        TextView expand_list_child_item_2_tv;
        ImageView expand_list_child_item_2_iv;

        LinearLayout expand_list_child_item_3;
        TextView expand_list_child_item_3_tv;
        ImageView expand_list_child_item_3_iv;

        LinearLayout expand_list_child_item_4;
        TextView expand_list_child_item_4_tv;
        ImageView expand_list_child_item_4_iv;

    }

}
