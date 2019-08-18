package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
import com.auto.di.guan.fragment.FragmentTab8;
import com.auto.di.guan.fragment.FragmentTab9;
import com.auto.di.guan.adapter.MyListAdapter;
import com.auto.di.guan.entity.Entiy;

import java.util.ArrayList;

public class ArticleListFragment extends ListFragment {
	public MyListAdapter adapter;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private ArrayList<Fragment>fragments = new ArrayList<Fragment>(10);
	private MainActivity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = getFragmentManager();


		adapter = new MyListAdapter(getActivity(), Entiy.TAB_TITLE);
		setListAdapter(adapter);
		fragments.add(new FragmentTab0());
		fragments.add(new FragmentTab1());
		fragments.add(new FragmentTab2());
		fragments.add(new FragmentTab3());
		fragments.add(new FragmentTab4());
		fragments.add(new FragmentTab5());
		fragments.add(new FragmentTab6());
		fragments.add(new FragmentTab7());
		fragments.add(new FragmentTab8());
		fragments.add(new FragmentTab10());
		fragments.add(new FragmentTab11());
		fragments.add(new FragmentTab9());
		transaction = manager.beginTransaction();
		transaction.add(R.id.right, fragments.get(0), Entiy.TAB_TITLE[0]).show(fragments.get(0));
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Log.e("------", "----"+position);
		if (MyApplication.user.getLevel() != 9999){
			if (position != 0 && position != 3 && position != 4 && position != 5) {
				Toast.makeText(getContext(), "权限不足无法操作",Toast.LENGTH_SHORT).show();
				return;
			}
		}
		activity.setTitle(Entiy.TAB_TITLE[position]);
		super.onListItemClick(l, v, position, id);
		adapter.setSelectedPosition(position);
		String str = (String) adapter.getItem(position);
		transaction = manager.beginTransaction();
		for (int i = 0; i < fragments.size(); i++) {
			if (position  == i) {
                if (fragments.get(i).isAdded()) {
                    transaction.show(fragments.get(i));
                }else {
                    transaction.add(R.id.right, fragments.get(i)).show(fragments.get(i));
                }
			}else {
                if (fragments.get(i).isAdded()) {
                    transaction.hide(fragments.get(i));
                }else {
                    transaction.add(R.id.right, fragments.get(i)).hide(fragments.get(i));
                }
			}
		}
		transaction.commitAllowingStateLoss();
		adapter.notifyDataSetChanged();
	}


	public void showPosition() {
		adapter.setSelectedPosition(0);
		String str = (String) adapter.getItem(0);
		transaction = manager.beginTransaction();
		for (int i = 0; i < fragments.size(); i++) {
			if (0  == i) {
				if (fragments.get(i).isAdded()) {
					transaction.show(fragments.get(i));
				}else {
					transaction.add(R.id.right, fragments.get(i)).show(fragments.get(i));
				}
			}else {
				if (fragments.get(i).isAdded()) {
					transaction.hide(fragments.get(i));
				}else {
					transaction.add(R.id.right, fragments.get(i)).hide(fragments.get(i));
				}
			}
		}
		transaction.commitAllowingStateLoss();
		adapter.notifyDataSetChanged();
	}

	private Fragment addFragment(int position) {
		if (fragments.size() < position)

		if (fragments.get(position) == null) {
			fragments.add(position, new FragmentTab0());
		}
		return  fragments.get(position);

	}
}
