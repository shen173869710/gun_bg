package com.auto.di.guan.adapter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.auto.di.guan.R;
import com.auto.di.guan.socket.SocketEntiy;
import com.auto.di.guan.socket.SocketResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class PumpLeftAdapter extends BaseQuickAdapter<SocketResult, BaseViewHolder> {
    public PumpLeftAdapter(@Nullable List<SocketResult> data) {
        super(R.layout.pump_left_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final SocketResult info) {

        holder.setText(R.id.item_name, info.getName());
        holder.setText(R.id.item_1, info.getVoltage());
        if (!TextUtils.isEmpty(info.getVoltageValue())) {
            holder.setText(R.id.item_1_value, info.getVoltageValue());
        }
        holder.setText(R.id.item_2, info.getElectricity());
        if (!TextUtils.isEmpty(info.getElectricity())) {
            holder.setText(R.id.item_2_value, info.getElectricityValue());
        }

        holder.setText(R.id.item_3, info.getStatus());
        if (!TextUtils.isEmpty(info.getStatusValue())) {
            holder.setText(R.id.item_3_value, SocketEntiy.getSocketStatus(info.getStatusValue()));
        }

        holder.setText(R.id.item_4, info.getErrorCode());
        if (!TextUtils.isEmpty(info.getErrorCode())) {
            holder.setText(R.id.item_4_value, info.getErrorCodeValue());
        }
    }

}
