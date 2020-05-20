package com.auto.di.guan.adapter;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.auto.di.guan.R;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class RecyclerListAdapter extends BaseQuickAdapter<GroupInfo, BaseViewHolder> {
    public RecyclerListAdapter(@Nullable List<GroupInfo> data) {
        super(R.layout.group_option_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final GroupInfo info) {
        TextView option_switch = holder.getView(R.id.option_switch);
        TextView option_name = holder.getView(R.id.option_name);
        EditText option_time = holder.getView(R.id.option_time);
        EditText option_level = holder.getView(R.id.option_level);
        TextView option_time_value = holder.getView(R.id.option_time_value);
        TextView option_level_value = holder.getView(R.id.option_level_value);
        option_time.clearFocus();
        option_level.clearFocus();
        option_name.setText("第 " + info.getGroupId() + " 组");
        option_time.setText("");
        option_level.setText("");
        option_time_value.setText("");

        if (info.getGroupIsJoin()) {
            option_time.setVisibility(View.VISIBLE);
            option_level.setVisibility(View.VISIBLE);
            holder.setBackgroundResource(R.id.option_switch, R.mipmap.select_bg_yellow);
        } else {
            option_time.setVisibility(View.INVISIBLE);
            option_level.setVisibility(View.INVISIBLE);
            holder.setBackgroundResource(R.id.option_switch, R.mipmap.select_bg_gray);
        }
        if (info.getGroupTime() > 0) {
            String time = new StringBuilder()
                    .append("当前设置时间  ")
                    .append("<font color=\"#FF5757\">")
                    .append(info.getGroupTime() / Entiy.RUN_TIME)
                    .append("</font>")
                    .append(" 分钟").toString();

            option_time_value.setText(Html.fromHtml(time));
        }

        option_level_value.setText("");
        if (info.getGroupLevel() > 0) {
            String lv = new StringBuilder()
                    .append("当前设置优先级  ")
                    .append("<font color=\"#FF5757\">")
                    .append(info.getGroupLevel())
                    .append(" </font>")
                    .toString();
            option_level_value.setText(Html.fromHtml(lv));
        }


        if (option_time.getTag() != null && option_time.getTag() instanceof TextWatcher) {
            option_time.removeTextChangedListener((TextWatcher) option_time.getTag());
        }

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int time = 0;
                String editTime = s.toString().trim();
                if (!TextUtils.isEmpty(editTime)) {
                    time = Integer.valueOf(option_time.getText().toString().trim()) * Entiy.RUN_TIME;
                    info.setGroupTime(time);
                    if (info.getGroupTime() > 0) {
                        String desc = new StringBuilder()
                                .append("当前设置时间  ")
                                .append("<font color=\"#FF5757\">")
                                .append(info.getGroupTime() / Entiy.RUN_TIME)
                                .append("</font>")
                                .append(" 分钟").toString();
                        info.setGroupRunTime(0);
                        option_time_value.setText(Html.fromHtml(desc));
                    }
                }
            }
        };

        option_time.addTextChangedListener(textWatcher);
        option_time.setTag(textWatcher);

        if (option_level.getTag() != null && option_level.getTag() instanceof TextWatcher) {
            option_level.removeTextChangedListener((TextWatcher) option_level.getTag());
        }

        final TextWatcher level = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String editTime = s.toString().trim();
                if (!TextUtils.isEmpty(editTime)) {
                    info.setGroupLevel(Integer.valueOf(editTime));
                    if (info.getGroupLevel() > 0) {
                        String lv = new StringBuilder()
                                .append("当前设置优先级  ")
                                .append("<font color=\"#FF5757\">")
                                .append(info.getGroupLevel())
                                .append(" </font>")
                                .toString();
                        option_level_value.setText(Html.fromHtml(lv));
                    }
                }
            }
        };
        option_level.addTextChangedListener(level);
        option_level.setTag(level);

        option_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                if (info.getGroupRunTime() > 0) {
                    ToastUtils.showLongToast("设备处于运行状态,无法关闭");
                    return;
                }
                info.setGroupIsJoin(!info.getGroupIsJoin());
                if (!info.getGroupIsJoin()) {
                    info.setGroupTime(0);
                    info.setGroupRunTime(0);
                }
                notifyDataSetChanged();
            }
        });
    }

}
