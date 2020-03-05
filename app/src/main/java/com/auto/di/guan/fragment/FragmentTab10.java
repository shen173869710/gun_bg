package com.auto.di.guan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.auto.di.guan.R;

/**
 *
 */
public class FragmentTab10 extends BaseFragment {
	private Button login_out;
	private View view;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_10, null);
		login_out = (Button) view.findViewById(R.id.login_out);


		return view;
	}

	@Override
	public void refreshData() {

	}
}
