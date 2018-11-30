package com.auto.di.guan.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.MessageEvent;
import com.daimajia.numberprogressbar.NumberProgressBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class GroupStatusAdapter extends RecyclerView.Adapter <GroupStatusAdapter.MyViewHolder>{
    private Context context;
    private List<GroupInfo> mItems = new ArrayList<>();



    public GroupStatusAdapter(Context context, List<GroupInfo> mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                this.context).inflate(R.layout.group_status_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GroupInfo info = mItems.get(position);
        holder.status_name.setText("第 "+mItems.get(position).getGroupId()+" 组");
        holder.status_par.setMax(info.getGroupTime());
        holder.status_par.setProgress(info.getGroupRunTime());

        if (info.getGroupStatus() == 0) {
            holder.status_start.setVisibility(View.GONE);
            holder.status_stop.setVisibility(View.GONE);
            holder.status_next.setVisibility(View.GONE);
        }else {
            holder.status_start.setVisibility(View.VISIBLE);
            holder.status_stop.setVisibility(View.VISIBLE);
            holder.status_next.setVisibility(View.VISIBLE);
        }
        if (info.getGroupRunTime() == info.getGroupTime()) {
            holder.status_par.setMax(100);
            holder.status_par.setProgress(100);
            holder.status_status.setVisibility(View.VISIBLE);
            holder.status_start.setVisibility(View.GONE);
            holder.status_stop.setVisibility(View.GONE);
            holder.status_next.setVisibility(View.GONE);

        }else {
            holder.status_status.setVisibility(View.GONE);
        }
        holder.status_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(Entiy.GROUP_START, info));
            }
        });

        holder.status_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(Entiy.GROUP_STOP,info));
            }
        });

        holder.status_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setGroupTime(0);
                info.setGroupRunTime(0);
                info.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
                DBManager.getInstance(context).updateGroup(info);
                notifyDataSetChanged();
                EventBus.getDefault().post(new MessageEvent(Entiy.GROUP_NEXT,info));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class MyViewHolder extends ViewHolder
    {
        TextView status_name;
        NumberProgressBar status_par;
        TextView status_start;
        TextView status_stop;
        TextView status_next;
        TextView status_status;
        public MyViewHolder(View view)
        {
            super(view);
            status_name = (TextView) view.findViewById(R.id.status_name);
            status_par = (NumberProgressBar) view.findViewById(R.id.status_par);
            status_start = (TextView) view.findViewById(R.id.status_start);
            status_stop = (TextView) view.findViewById(R.id.status_stop);
            status_next = (TextView) view.findViewById(R.id.status_next);
            status_status= (TextView) view.findViewById(R.id.status_status);
        }
    }

    public void setData(List<GroupInfo>groupInfos) {
        this.mItems = groupInfos;
        notifyDataSetChanged();
    }

}
