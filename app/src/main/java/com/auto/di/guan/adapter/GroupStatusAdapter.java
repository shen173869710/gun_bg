package com.auto.di.guan.adapter;

import android.app.Activity;
import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.R;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.dialog.SetTimeDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.MessageEvent;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.daimajia.numberprogressbar.NumberProgressBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class GroupStatusAdapter extends BaseQuickAdapter<GroupInfo, BaseViewHolder> {
    private Context context;
    private List<GroupInfo> mItems = new ArrayList<>();
    private HashMap<Integer, Integer>hashMap = new HashMap<>();



    public GroupStatusAdapter(@Nullable List<GroupInfo> data) {
        super(R.layout.group_status_item, data);
        int size = mItems.size();
        for (int i = 0; i < size; i++) {
            hashMap.put(mItems.get(i).groupId, mItems.get(i).groupTime/60);
        }
    }


    @Override
    protected void convert(BaseViewHolder holder, final GroupInfo info) {


        TextView status_name = holder.getView(R.id.status_name);
        NumberProgressBar status_par = holder.getView(R.id.status_par);

        status_name.setText("第 "+info.getGroupId()+" 组");
        status_par.setMax(info.getGroupTime());
        status_par.setProgress(info.getGroupRunTime());


        TextView status_start = holder.getView(R.id.status_start);
        TextView status_stop = holder.getView(R.id.status_stop);
        TextView status_next = holder.getView(R.id.status_next);
        TextView status_cur_time = holder.getView(R.id.status_cur_time);
        TextView status_set_time = holder.getView(R.id.status_set_time);
        TextView status_status = holder.getView(R.id.status_status);


        if (info.getGroupStatus() == 0) {
            status_start.setVisibility(View.GONE);
            status_stop.setVisibility(View.GONE);
            status_next.setVisibility(View.GONE);
            status_cur_time.setVisibility(View.GONE);
            status_set_time.setVisibility(View.GONE);
        }else {
            status_start.setVisibility(View.VISIBLE);
            status_stop.setVisibility(View.VISIBLE);
            status_next.setVisibility(View.VISIBLE);
            status_cur_time.setVisibility(View.VISIBLE);
            status_set_time.setVisibility(View.VISIBLE);
        }

        if (info.getGroupRunTime() >0) {
            hashMap.put(info.groupId, info.groupTime/60);
        }
        if (info.getGroupRunTime() == info.getGroupTime()) {
            status_par.setMax(100);
            status_par.setProgress(100);
            status_status.setVisibility(View.VISIBLE);
            int time = 0;
            if (hashMap.containsKey(info.getGroupId())) {
                time = hashMap.get(info.getGroupId());
            }
            status_status.setText("轮灌完成   灌溉时长" + time+ "分钟");
            status_start.setVisibility(View.GONE);
            status_stop.setVisibility(View.GONE);
            status_next.setVisibility(View.GONE);
            status_cur_time.setVisibility(View.GONE);
            status_set_time.setVisibility(View.GONE);
        }else {
            status_status.setVisibility(View.GONE);
        }

        status_cur_time.setText("总时间 "+info.getGroupTime() + " 剩余时间 "+(info.getGroupTime() - info.getGroupRunTime())+"秒");
        status_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetTimeDialog.ShowDialog((Activity)context,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String tag = v.getTag().toString();
                                if (!TextUtils.isEmpty(tag)) {
                                    int i = Integer.valueOf(tag);
                                    if(i  < 20) {

                                        Toast.makeText(context, "设置的时间不能小于20分钟", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    info.setGroupTime(i*60+info.getGroupRunTime());
                                    DBManager.getInstance(context).updateGroup(info);
                                }
                            }
                        });
            }
        });
        status_start.setVisibility(View.GONE);
        status_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(Entiy.GROUP_START, info));
            }
        });
        status_stop.setVisibility(View.GONE);
        status_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(Entiy.GROUP_STOP,info));
            }
        });

        status_next.setOnClickListener(new View.OnClickListener() {
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

        int position = holder.getAdapterPosition();
        if (mItems.size() == position +1 || info.getGroupStatus() == 0) {
            status_next.setVisibility(View.GONE);
        }
    }

    public void setData(List<GroupInfo> data) {
        mItems.clear();
        mItems.addAll(data);
        notifyDataSetChanged();
    }

}
