package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.MyFragmentAdapter;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;

import java.util.ArrayList;
/**

 *
 */
public class FragmentTab3 extends BaseFragment {
	private TextView option_title_1,option_title_2;
	private View option_title_1_drive, option_title_2_drive;
	private View view;
	private ViewPager option_viewpage;
	private MyFragmentAdapter adapter;
	private ArrayList<Fragment> fragments = new ArrayList<>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_3, null);
		option_title_1 = (TextView) view.findViewById(R.id.option_title_1);
		option_title_2 = (TextView)view.findViewById(R.id.option_title_2);
		option_title_1_drive = view.findViewById(R.id.option_title_1_drive);
		option_title_2_drive = view.findViewById(R.id.option_title_2_drive);
		option_title_2_drive.setVisibility(View.INVISIBLE);
		fragments.add(new FragmentTab31());
		fragments.add(new FragmentTab32());
		option_title_1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				option_viewpage.setCurrentItem(0);
			}
		});
		option_title_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				option_viewpage.setCurrentItem(1);
			}
		});
		option_viewpage = (ViewPager)view.findViewById(R.id.option_viewpage);
		adapter = new MyFragmentAdapter(getChildFragmentManager(), fragments);
		option_viewpage.setAdapter(adapter);
		option_viewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					option_title_1_drive.setVisibility(View.VISIBLE);
					option_title_2_drive.setVisibility(View.INVISIBLE);
				}else {
					option_title_1_drive.setVisibility(View.INVISIBLE);
					option_title_2_drive.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		return view;
	}


	@Override
	public void refreshData() {
		LogUtils.e("-------------", "333333");
		int size = fragments.size();
		if (size == 2) {
			FragmentTab31 fragmentTab31 = (FragmentTab31) fragments.get(0);
			fragmentTab31.refreshData();
			FragmentTab32 fragmentTab32 = (FragmentTab32) fragments.get(1);
			fragmentTab32.refreshData();
		}
	}
}
