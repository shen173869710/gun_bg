package com.auto.di.guan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.auto.di.guan.R;


public class FragmentTab8 extends BaseFragment {

	private TextView read_desc;
	private Button read_data;
	private View view;
	private Activity activity;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_8, null);
//		read_desc = (TextView)view.findViewById(R.id.read_desc);
//		read_data = (Button)view.findViewById(R.id.read_desc);
		return view;
	}

}
