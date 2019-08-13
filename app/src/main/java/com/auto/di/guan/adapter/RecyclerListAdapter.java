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
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

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
        holder.option_name.setText("第 "+mItems.get(position).getGroupId()+" 组");
//        holder.option_time_second.setText(""+mItems.get(position).groupLevel);
        // Start a drag whenever the handle view it touched
//        holder.option_layout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                    mDragStartListener.onStartDrag(holder);
//                }
//                return false;
//            }
//        });


        if (holder.option_level_value.getTag() != null && holder.option_level_value.getTag() instanceof TextWatcher) {
            holder.option_level_value.removeTextChangedListener((TextWatcher) holder.option_level_value.getTag());
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


                int time =0;
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    time = Integer.valueOf(s.toString().trim());
                    if (time == 0) {
                        Toast.makeText(mContext, "优先级必须大于0",Toast.LENGTH_LONG).show();
                        return;
                    }
                    int size = mItems.size();
//                        for (int i = 0; i < size; i++) {
//                            if (mItems.get(i).getGroupLevel() == time) {
//                                LogUtils.e("----level---", "level =="+mItems.get(i).getGroupLevel() +"     time = "+time);
//                                Toast.makeText(mContext, "优先级不能相同",Toast.LENGTH_LONG).show();
//                                holder.option_level_value.setText("");
//                                return;
//                            }
//                        }
                    mItems.get(position).setGroupLevel(time);
                }
            }
        };
        holder.option_level_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    holder.option_level_value.addTextChangedListener(level);
                }else{
                    holder.option_level_value.removeTextChangedListener(level);
                }
            }
        });

        holder.option_level_value.setTag(level);

        if (holder.option_time_second.getTag() != null && holder.option_time_second.getTag() instanceof TextWatcher) {
            holder.option_time_second.removeTextChangedListener((TextWatcher) holder.option_time_second.getTag());
        }

        holder.option_time_second.setText(mItems.get(position).groupTime+"");
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
                if (!TextUtils.isEmpty(s.toString().trim())) {
//                    if (!TextUtils.isEmpty(holder.option_time.getText().toString().trim())) {
//                        time = Integer.valueOf(holder.option_time.getText().toString().trim())*60+Integer.valueOf(holder.option_time_second.getText().toString().trim());
//                    }else {
                    time = (int)(Float.valueOf(holder.option_time_second.getText().toString().trim())*60);
//                    }
                    mItems.get(position).setGroupTime(time);
                }
            }
        };

        holder.option_time_second.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    holder.option_time_second.addTextChangedListener(textWatcher);
                }else{
                    holder.option_time_second.removeTextChangedListener(textWatcher);
                }
            }
        });

        holder.option_time_second.addTextChangedListener(textWatcher);
//        holder.option_time_second.setTag(textWatcher);
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

        public LinearLayout option_layout;
        public TextView option_name;
        public EditText option_level_value;
        public EditText option_time_second;
        public TextView option_level;

        public ItemViewHolder(View itemView) {
            super(itemView);
            option_layout = (LinearLayout) itemView.findViewById(R.id.option_layout);
            option_name = (TextView) itemView.findViewById(R.id.option_name);
            option_level_value = (EditText) itemView.findViewById(R.id.option_level_value);
            option_time_second= (EditText) itemView.findViewById(R.id.option_time_second);
            option_level = (TextView) itemView.findViewById(R.id.option_level);
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
