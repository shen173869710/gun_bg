package com.auto.di.guan.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.MyGridAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.entity.AdapterEvent;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FragmentTab0 extends BaseFragment {
    private GridView mGridView;
    private View view;
    private MyGridAdapter adapter;
    private List<DeviceInfo> deviceInfos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_0, null);
        mGridView = (GridView) view.findViewById(R.id.fragment_0_gridview);
        deviceInfos = DBManager.getInstance(getActivity()).queryDeviceList();
        if (deviceInfos.size() == 0) {
            for (int i = 0; i < Entiy.GEID_ALL_ITEM; i++) {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setDeviceId(i+1);
                deviceInfo.controlInfos = new ArrayList<>();
                deviceInfo.controlInfos.add(new ControlInfo(0,"0"));
                deviceInfo.controlInfos.add(new ControlInfo(0,"1"));
                deviceInfos.add(deviceInfo);
            }
            DBManager.getInstance(getActivity()).insertDeviceInfoList(deviceInfos);
        }

        adapter = new MyGridAdapter(getActivity(), deviceInfos);
        mGridView.setAdapter(adapter);
        mGridView.setNumColumns(Entiy.GRID_COLUMNS);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final DeviceInfo info = deviceInfos.get(position);
                if (info.getStatus() == Entiy.DEVEICE_UNBIND) {
                    MainShowDialog.ShowDialog(getActivity(), "添加阀控器", "添加阀控器到当前区域", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            info.setStatus(Entiy.DEVEICE_BIND);
                            info.controlInfos = new ArrayList<>();
                            info.controlInfos.add(new ControlInfo(0,"0"));
                            info.controlInfos.add(new ControlInfo(0,"1"));
                            info.setGroupId(position);
                            DBManager.getInstance(getActivity()).updateDeviceList(deviceInfos);
                            deviceInfos = DBManager.getInstance(getActivity()).queryDeviceList();
                            adapter.setData(deviceInfos);
                        }
                    });
                }else {

                    if (info.controlInfos.get(0).groupId > 0 || info.controlInfos.get(1).groupId > 0) {
                        showToast("该设备已经分组,不可以删除");
                        return;
                    }
                    MainShowDialog.ShowDialog(getActivity(), "删除阀控器", "是删除当前区域阀控器", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            info.setStatus(Entiy.DEVEICE_UNBIND);
                            info.controlInfos = new ArrayList<>();
                            info.controlInfos.add(new ControlInfo(0,"0"));
                            info.controlInfos.add(new ControlInfo(0,"1"));
                            info.setGroupId(position);
                            DBManager.getInstance(getActivity()).updateDeviceList(deviceInfos);
                            deviceInfos = DBManager.getInstance(getActivity()).queryDeviceList();
                            adapter.setData(deviceInfos);
                        }
                    });
                }
            }
        });
        EventBus.getDefault().register(this);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        deviceInfos = DBManager.getInstance(getActivity()).queryDeviceList();
        if (adapter != null)
        adapter.setData(deviceInfos);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        deviceInfos = DBManager.getInstance(getActivity()).queryDeviceList();
        if (adapter != null)
        adapter.setData(deviceInfos);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdapterUpdate(AdapterEvent event) {
        LogUtils.e("------", "Main");
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
