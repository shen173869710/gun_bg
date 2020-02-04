package com.auto.di.guan.fragment;

import android.app.Activity;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.auto.di.guan.MainActivity;

/**
 * Created by Administrator on 2017/7/16.
 */

public class BaseFragment extends Fragment implements AdapterListener{

    public Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity =  activity;
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    public void setTitle(String title) {
        activity.setTitle(title);
    }

    public void showToast(String message) {
        Toast.makeText(this.getActivity(),message,Toast.LENGTH_LONG).show();
    }


    public  void adapterUpdate(){

    }

    @Override
    public void AdapterUpdate() {

    }
}
