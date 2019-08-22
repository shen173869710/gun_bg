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

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.helper.ItemTouchHelperAdapter;
import com.auto.di.guan.adapter.helper.ItemTouchHelperViewHolder;
import com.auto.di.guan.adapter.helper.OnStartDragListener;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private  List<GroupInfo> mItems = new ArrayList<>();

    private final OnStartDragListener mDragStartListener;
    private Context mContext;
    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener, List<GroupInfo>groupInfos) {
        mContext = context;
        mDragStartListener = dragStartListener;
        mItems = groupInfos;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_option_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        final GroupInfo info = mItems.get(position);
        holder.option_name.setText("第 "+info.getGroupId()+" 组");
        holder.option_time.setText("");
        holder.option_level.setText("");
        holder.option_time_value.setText("");
        if (info.getGroupTime() > 0) {
            String time = new StringBuilder()
                    .append("当前设置时间  ")
                    .append("<font color=\"#FF5757\">")
                    .append(info.getGroupTime()/60 )
                    .append("</font>")
                    .append(" 分钟").toString();

            holder.option_time_value.setText(Html.fromHtml(time));
        }

        holder.option_level_value.setText("");
        if (info.getGroupLevel() > 0) {
            String lv = new StringBuilder()
                    .append("当前设置优先级  ")
                    .append("<font color=\"#FF5757\">")
                    .append(info.getGroupLevel())
                    .append(" </font>")
                    .toString();
            holder.option_level_value.setText(Html.fromHtml(lv));
        }


        if (holder.option_time.getTag() != null && holder.option_time.getTag() instanceof TextWatcher) {
            holder.option_time.removeTextChangedListener((TextWatcher) holder.option_time.getTag());
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
                    Toast.makeText(mContext, "时间必须大于0",Toast.LENGTH_LONG).show();
                    holder.option_time.setText("");
                    return;
                }
                time = Integer.valueOf(holder.option_time.getText().toString().trim())*60;
                info.setGroupTime(time);
                if (info.getGroupTime() > 0) {
                    String desc = new StringBuilder()
                            .append("当前设置时间  ")
                            .append("<font color=\"#FF5757\">")
                            .append(info.getGroupTime()/60 )
                            .append("</font>")
                            .append(" 分钟").toString();

                    holder.option_time_value.setText(Html.fromHtml(desc));
                }else {
                    holder.option_time_value.setText("");
                }


            }
        };

        holder.option_time.addTextChangedListener(textWatcher);
        holder.option_time.setTag(textWatcher);

        if (holder.option_level.getTag() != null && holder.option_level.getTag() instanceof TextWatcher) {
            holder.option_level.removeTextChangedListener((TextWatcher) holder.option_level.getTag());
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
                    Toast.makeText(mContext, "优先级必须大于0",Toast.LENGTH_LONG).show();
                    holder.option_level.setText("");
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
                    holder.option_level_value.setText(Html.fromHtml(lv));
                }else {
                    holder.option_level_value.setText("");
                }
            }
        };
        holder.option_level.addTextChangedListener(level);
        holder.option_level.setTag(level);


    }


    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        int level = mItems.get(fromPosition).groupLevel;
//        Log.e("---","fromPosition = "+fromPosition+"toPosition ="+toPosition);
//        mItems.get(fromPosition).groupLevel =  mItems.get(toPosition).groupLevel;
//        mItems.get(toPosition).groupLevel = level;
//        Collections.swap(mItems, fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
//        notifyDataSetChanged();
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public TextView option_name;
        public EditText option_time;
        public EditText option_level;
        public TextView option_time_value;
        public TextView option_level_value;

        public ItemViewHolder(View itemView) {
            super(itemView);
            option_name = (TextView) itemView.findViewById(R.id.option_name);
            option_time = (EditText) itemView.findViewById(R.id.option_time);
            option_level= (EditText) itemView.findViewById(R.id.option_level);
            option_time_value = (TextView) itemView.findViewById(R.id.option_time_value);
            option_level_value = (TextView) itemView.findViewById(R.id.option_level_value);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }


    public void setDate( List<GroupInfo>groupInfos) {
        this.mItems = groupInfos;
        notifyDataSetChanged();
    }


}
