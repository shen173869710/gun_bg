/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.auto.di.guan.adapter;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.auto.di.guan.R;
import com.auto.di.guan.db.GroupInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;



public class RecyclerListAdapter extends BaseQuickAdapter<GroupInfo, BaseViewHolder> {


    public RecyclerListAdapter(@Nullable List<GroupInfo> data) {
        super(R.layout.group_option_item, data);

    }



    @Override
    protected void convert(BaseViewHolder holder, final GroupInfo info) {

        final TextView option_name = holder.getView(R.id.option_name);
        final EditText option_time = holder.getView(R.id.option_time);
        final EditText option_level = holder.getView(R.id.option_level);
        final TextView option_time_value = holder.getView(R.id.option_time_value);
        final TextView option_level_value = holder.getView(R.id.option_level_value);


        option_name.setText("第 "+info.getGroupId()+" 组");
        option_time.setText("");
        option_level.setText("");
        option_time_value.setText("");
        if (info.getGroupTime() > 0) {
            String time = new StringBuilder()
                    .append("当前设置时间  ")
                    .append("<font color=\"#FF5757\">")
                    .append(info.getGroupTime()/60 )
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
                int time =0;
                String editTime = s.toString().trim();
                if (TextUtils.isEmpty(editTime) || editTime.equals("0")) {
                    Toast.makeText(getContext(), "时间必须大于0",Toast.LENGTH_LONG).show();
                    option_time.setText("");
                    return;
                }
                time = Integer.valueOf(option_time.getText().toString().trim())*60;
                info.setGroupTime(time);
                if (info.getGroupTime() > 0) {
                    String desc = new StringBuilder()
                            .append("当前设置时间  ")
                            .append("<font color=\"#FF5757\">")
                            .append(info.getGroupTime()/60 )
                            .append("</font>")
                            .append(" 分钟").toString();

                    option_time_value.setText(Html.fromHtml(desc));
                }else {
                    option_time_value.setText("");
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
                if (TextUtils.isEmpty(editTime) || editTime.equals("0")) {
                    Toast.makeText(getContext(), "优先级必须大于0",Toast.LENGTH_LONG).show();
                    option_level.setText("");
                    return;
                }

                info.setGroupLevel(Integer.valueOf(editTime));
                if (info.getGroupLevel() > 0) {
                    String lv = new StringBuilder()
                            .append("当前设置优先级  ")
                            .append("<font color=\"#FF5757\">")
                            .append(info.getGroupLevel())
                            .append(" </font>")
                            .toString();
                    option_level_value.setText(Html.fromHtml(lv));
                }else {
                    option_level_value.setText("");
                }
            }
        };
        option_level.addTextChangedListener(level);
        option_level.setTag(level);
    }

}
