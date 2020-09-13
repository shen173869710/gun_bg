package com.auto.di.guan.adapter;

import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;



public class StatusAdapter extends BaseQuickAdapter<ControlInfo, BaseViewHolder> {

    public StatusAdapter( List<ControlInfo> data) {
        super(R.layout.group_status_list_item, data);
    }

    public void setData(List<ControlInfo> controlInfos) {
        getData().clear();
        getData().addAll(controlInfos);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder holder, ControlInfo info) {
        holder.setText(R.id.group_status_name, info.getValve_alias());
        holder.setImageResource(R.id.group_status_image,info.getValve_imgage_id());
    }


}
