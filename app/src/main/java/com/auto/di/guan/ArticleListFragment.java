package com.auto.di.guan;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.auto.di.guan.adapter.MyListAdapter;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.fragment.FragmentTab0;
import com.auto.di.guan.fragment.FragmentTab1;
import com.auto.di.guan.fragment.FragmentTab10;
import com.auto.di.guan.fragment.FragmentTab11;
import com.auto.di.guan.fragment.FragmentTab2;
import com.auto.di.guan.fragment.FragmentTab3;
import com.auto.di.guan.fragment.FragmentTab4;
import com.auto.di.guan.fragment.FragmentTab5;
import com.auto.di.guan.fragment.FragmentTab6;
import com.auto.di.guan.fragment.FragmentTab7;
import com.auto.di.guan.fragment.FragmentTab9;
import com.auto.di.guan.utils.LogUtils;

import java.util.ArrayList;

public class ArticleListFragment extends ListFragment {
	public MyListAdapter adapter;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private ArrayList<Fragment>fragments = new ArrayList<Fragment>(10);
	private MainActivity activity;

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		this.activity = (MainActivity)context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = getFragmentManager();

		adapter = new MyListAdapter(activity, Entiy.TAB_TITLE);
		setListAdapter(adapter);
		fragments.add(new FragmentTab0());
		fragments.add(new FragmentTab1());
		fragments.add(new FragmentTab2());
		fragments.add(new FragmentTab3());
		fragments.add(new FragmentTab4());
		fragments.add(new FragmentTab5());
		fragments.add(new FragmentTab6());
		fragments.add(new FragmentTab7());
//		fragments.add(new FragmentTab8());
		fragments.add(new FragmentTab10());
		fragments.add(new FragmentTab11());
		fragments.add(new FragmentTab9());
		transaction = manager.beginTransaction();
		transaction.add(R.id.right, fragments.get(0), Entiy.TAB_TITLE[0]).show(fragments.get(0));
		transaction.commitAllowingStateLoss();
		adapter.setSelectedPosition(0);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		activity.setTitle(Entiy.TAB_TITLE[position]);
		adapter.setSelectedPosition(position);
//
//		for (int i = 0; i < fragments.size(); i++) {
//			if (position  == i) {
//                if (fragments.get(i).isAdded()) {
//                    transaction.show(fragments.get(i));
//                }else {
//                    transaction.add(R.id.right, fragments.get(i)).show(fragments.get(i));
//                }
//			}else {
//                if (fragments.get(i).isAdded()) {
//                    transaction.hide(fragments.get(i));
//                }else {
//                    transaction.add(R.id.right, fragments.get(i)).hide(fragments.get(i));
//                }
//			}
//		}
//		transaction.commitAllowingStateLoss();
		LogUtils.e("ArticleListFragment", "start = "+System.currentTimeMillis());
		showFragment(fragments.get(position));
		adapter.notifyDataSetChanged();
	}


	private void showFragment(Fragment fragment) {
		if(null == fragment) {
			return;
		}
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		String fragmentTag = fragment.getClass().getSimpleName();
		addFragment(fragmentManager, fragment, fragmentTag);
		hideAllFragment(fragmentManager,fragment);
		fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
	}

	private void addFragment(FragmentManager fm, Fragment fragment, String tag) {
		if(null !=fm && !fm.isDestroyed() && null !=fragment && !fragment.isAdded() && null == fm.findFragmentByTag(tag) && !TextUtils.isEmpty(tag)){
			FragmentTransaction ft = fm.beginTransaction();
			if (null !=ft) {
				fm.executePendingTransactions();
				ft.add(R.id.right, fragment,tag);
				ft.commitAllowingStateLoss();
			}
		}
	}

	/**
	 *
	 * @param fm
	 * @param fragment  隐藏 其他 fragment
	 */
	private void hideAllFragment(FragmentManager fm, Fragment fragment) {
		if(null !=fm && !fm.isDestroyed() && null !=fragment &&!TextUtils.isEmpty(fragment.getTag()) && null !=fragments && fragments.size()>0){
			FragmentTransaction ft = fm.beginTransaction();
			if(null !=ft){
				for (Fragment frag : fragments) {
					if (!fragment.getTag().equals(frag.getTag())) {
						ft.hide(frag);
					}
				}
				ft.commitAllowingStateLoss();
			}
		}
	}
}
