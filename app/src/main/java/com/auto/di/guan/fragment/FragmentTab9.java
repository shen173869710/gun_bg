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
public class FragmentTab9 extends BaseFragment {
	private Button login_out;
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
		view = inflater.inflate(R.layout.fragment_9, null);
		login_out = (Button) view.findViewById(R.id.login_out);

		login_out.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});

		return view;
	}

}
