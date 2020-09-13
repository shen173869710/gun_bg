package com.auto.di.guan.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.auto.di.guan.utils.CalculateUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */

public class ExpandableListViewaAdapter extends BaseExpandableListAdapter {
    Activity activity;
    private List<String> groupArray;//组列表
    private List<List<String>> childArray;//子列表
    public  ExpandableListViewaAdapter(Activity a,List<String> groupArray,List<List<String>> childArray) {
        this.groupArray = groupArray;
        this.childArray = childArray;
        this.activity = a;
    }
    /*-----------------Child */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String string =childArray.get(groupPosition).get(childPosition);

        return getGenericView(string);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return childArray.get(groupPosition).size();
    }
    /* ----------------------------Group */
    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return getGroup(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupArray.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String   string=groupArray.get(groupPosition);
        return getGenericView(string);
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return true;
    }

    private TextView getGenericView(String string )
    {
        AbsListView.LayoutParams  layoutParams =new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                CalculateUtil.dip2px(activity,25));

        TextView  textView =new TextView(activity);
        textView.setLayoutParams(layoutParams);

        textView.setGravity(Gravity.CENTER_VERTICAL |Gravity.LEFT);

        textView.setPadding( CalculateUtil.dip2px(activity,40), 0, 0, 0);
        textView.setText(string);
        return textView;
    }
}
