package com.auto.di.guan.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.auto.di.guan.R;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.dialog.DialogUtil;
import com.auto.di.guan.dialog.OnDialogClick;
import com.auto.di.guan.dialog.SetTimeDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.event.AutoTaskEvent;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.daimajia.numberprogressbar.NumberProgressBar;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.annotation.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GroupStatusAdapter extends BaseQuickAdapter<GroupInfo, BaseViewHolder> {
    private List<GroupInfo> mItems = new ArrayList<>();
    private HashMap<Integer, Integer> hashMap = new HashMap<>();

    public GroupStatusAdapter(@Nullable List<GroupInfo> data) {
        super(R.layout.group_status_item, data);
        int size = mItems.size();
        for (int i = 0; i < size; i++) {
            hashMap.put(mItems.get(i).getGroupId(), mItems.get(i).getGroupTime() / Entiy.RUN_TIME);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, final GroupInfo info) {
        TextView status_name = holder.getView(R.id.status_name);
        NumberProgressBar status_par = holder.getView(R.id.status_par);
        status_name.setText("第 " + info.getGroupId() + " 组");
        status_par.setMax(info.getGroupTime());
        status_par.setProgress(info.getGroupRunTime());
        TextView status_stop = holder.getView(R.id.status_stop);
        TextView status_next = holder.getView(R.id.status_next);
        TextView status_cur_time = holder.getView(R.id.status_cur_time);
        TextView status_set_time = holder.getView(R.id.status_set_time);
        TextView status_status = holder.getView(R.id.status_status);

        if (info.getGroupStatus() == 0) {
            status_stop.setVisibility(View.GONE);
            status_next.setVisibility(View.GONE);
            status_cur_time.setVisibility(View.GONE);
            status_set_time.setVisibility(View.GONE);
        } else {
            status_stop.setVisibility(View.VISIBLE);
            status_next.setVisibility(View.VISIBLE);
            status_cur_time.setVisibility(View.VISIBLE);
            status_set_time.setVisibility(View.VISIBLE);
        }

        if (info.getGroupRunTime() > 0) {
            hashMap.put(info.getGroupId(), info.getGroupTime() / Entiy.RUN_TIME);
        }
        if (info.getGroupRunTime() == info.getGroupTime()) {
            status_par.setMax(100);
            status_par.setProgress(100);
            status_status.setVisibility(View.VISIBLE);
            int time = 0;
            if (hashMap.containsKey(info.getGroupId())) {
                time = hashMap.get(info.getGroupId());
            }
            status_status.setText("轮灌完成   灌溉时长" + time + "分钟");
            status_stop.setVisibility(View.GONE);
            status_next.setVisibility(View.GONE);
            status_cur_time.setVisibility(View.GONE);
            status_set_time.setVisibility(View.GONE);
        } else {
            status_status.setVisibility(View.GONE);
        }

        status_cur_time.setText("总时间 " + info.getGroupTime() + " 剩余时间 " + (info.getGroupTime() - info.getGroupRunTime()) + "秒");
        status_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoFastClickUtils.isFastClick()) {
                    return;
                }
                SetTimeDialog.ShowDialog((Activity) getContext(),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String tag = v.getTag().toString();
                                if (!TextUtils.isEmpty(tag)) {
                                    int i = Integer.valueOf(tag);
                                    if (i <= 0) {

                                        Toast.makeText(getContext(), "设置的时间不能小于20分钟", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    info.setGroupTime(i * Entiy.RUN_TIME + info.getGroupTime());
                                    GroupInfoSql.updateGroup(info);
                                }
                            }
                        });
            }
        });

        if (info.getGroupStop()) {
            status_stop.setText("开启计时");
        } else {
            status_stop.setText("暂停计时");
        }

        status_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoFastClickUtils.isFastClick()) {
                    return;
                }

                if (info.getGroupStop()) {
                    DialogUtil.showStartCount(getContext(), new OnDialogClick() {
                        @Override
                        public void onDialogOkClick(String value) {
                            info.setGroupStop(false);
                            GroupInfoSql.updateGroup(info);
                            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_START, info));
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onDialogCloseClick(String value) {

                        }
                    });

                } else {
                    DialogUtil.showStopCount(getContext(), new OnDialogClick() {
                        @Override
                        public void onDialogOkClick(String value) {
                            info.setGroupStop(true);
                            GroupInfoSql.updateGroup(info);
                            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_STOP));
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onDialogCloseClick(String value) {

                        }
                    });
                }
            }
        });

        status_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoFastClickUtils.isFastClick()) {
                    return;
                }
                info.setGroupRunTime(info.getGroupTime());
                GroupInfoSql.updateGroup(info);
            }
        });

        int position = holder.getAdapterPosition();
        if (info.getGroupStatus() == 0) {
            status_next.setVisibility(View.GONE);
        }

        if (getData().size() == position + 1) {
            status_next.setVisibility(View.GONE);
        }

    }

    public void setData(List<GroupInfo> data) {
        mItems.clear();
        mItems.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * This method will only be executed when there is payload info
     * <p>
     * 当有 payload info 时，只会执行此方法
     *
     * @param helper   A fully initialized helper.
     * @param item     The item that needs to be displayed.
     * @param payloads payload info.
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull GroupInfo item, @NotNull List<?> payloads) {
        for (Object p : payloads) {
            int payload = (int) p;
            convert(helper, item);
//            if (payload == ITEM_0_PAYLOAD) {
//                helper.setText(R.id.tweetName, item.getTitle())
//                        .setText(R.id.tweetText, item.getContent());
//            }
        }
    }

}
