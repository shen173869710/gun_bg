package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.PumpLeftAdapter;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.PumpInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FragmentTab6 extends BaseFragment {
	private RecyclerView list;
	private PumpLeftAdapter adapter;
	private View view;
	private List<PumpInfo> infos = new ArrayList<>();

	private Button pump_open;
	private Button pump_close;
	private TextView pump_pram_1;
	private TextView pump_pram_2;
	private TextView pump_pram_3;
	private TextView pump_pram_4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_6, null);
		list = view.findViewById(R.id.pump_list);

		pump_open = view.findViewById(R.id.pump_open);
		pump_close = view.findViewById(R.id.pump_close);
		pump_pram_1 = view.findViewById(R.id.pump_pram_1);
		pump_pram_2 = view.findViewById(R.id.pump_pram_2);
		pump_pram_3 = view.findViewById(R.id.pump_pram_3);
		pump_pram_4 = view.findViewById(R.id.pump_pram_4);


		list.setLayoutManager(new LinearLayoutManager(activity));
		for (int i = 1; i < 6; i++) {
			PumpInfo info = new PumpInfo();
			info.setPumpIndex(i);
			info.setPumpName("水泵" + i);
			infos.add(info);
		}
		adapter = new PumpLeftAdapter(infos);
		list.setAdapter(adapter);
		setData(infos.get(0));
		adapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				setData(infos.get(position));
			}
		});
		return view;
	}


	public void setData(PumpInfo info) {
		pump_open.setText(info.getPumpName() + "开启");
		pump_close.setText(info.getPumpName() + "关闭");
		pump_pram_1.setText(Entiy.PUMP_PRAM[0]+ "");
		pump_pram_2.setText(Entiy.PUMP_PRAM[1] + "");
		pump_pram_3.setText(Entiy.PUMP_PRAM[2] + "");
		pump_pram_4.setText(Entiy.PUMP_PRAM[3] + "");
	}

	@Override
	public void refreshData() {

	}
}
