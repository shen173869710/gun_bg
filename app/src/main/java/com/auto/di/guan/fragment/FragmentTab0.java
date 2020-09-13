package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.MyGridAdapter;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
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
        deviceInfos = DeviceInfoSql.queryDeviceList();

        adapter = new MyGridAdapter(getActivity(), deviceInfos);
        mGridView.setAdapter(adapter);
        mGridView.setNumColumns(Entiy.GRID_COLUMNS);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final DeviceInfo info = deviceInfos.get(position);
                if (info.getDeviceStatus() == Entiy.DEVEICE_UNBIND) {
                    MainShowDialog.ShowDialog(getActivity(), "添加阀控器", "添加阀控器到当前区域", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            info.bindDevice();
                            DeviceInfoSql.updateDevice(info);
                            deviceInfos = DeviceInfoSql.queryDeviceList();
                            adapter.setData(deviceInfos);
                        }
                    });
                }else {
                    if (info.getValveDeviceSwitchList().get(0).getValve_group_id() > 0
                            || info.getValveDeviceSwitchList().get(1).getValve_group_id() > 0) {
                        showToast("该设备已经分组,不可以删除");
                        return;
                    }
                    MainShowDialog.ShowDialog(getActivity(), "删除阀控器", "是删除当前区域阀控器", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            info.unBindDevice();
                            DeviceInfoSql.updateDevice(info);
                            deviceInfos = DeviceInfoSql.queryDeviceList();
                            adapter.setData(deviceInfos);
                        }
                    });
                }
            }
        });
        return view;
    }


    @Override
    public void refreshData() {
        if (adapter != null) {
            adapter.setData(DeviceInfoSql.queryDeviceList());
        }
    }
}
