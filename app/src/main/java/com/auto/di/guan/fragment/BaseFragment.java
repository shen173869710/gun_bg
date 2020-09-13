package com.auto.di.guan.fragment;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by Administrator on 2017/7/16.
 */

public abstract class BaseFragment extends Fragment implements AdapterListener{

    public Activity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public void setTitle(String title) {
        activity.setTitle(title);
    }




    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshData();
        }
    }

    public abstract void refreshData();



    public  void adapterUpdate(){

    }

    @Override
    public void AdapterUpdate() {

    }

    public void showToast(String message) {
        Toast.makeText(this.getActivity(),message,Toast.LENGTH_LONG).show();
    }
}
