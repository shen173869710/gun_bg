package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.GunManagerAdapter;
import com.auto.di.guan.entity.GunManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class FragmentTab11 extends BaseFragment {
    RecyclerView fragment11List;
    private View view;
    GunManagerAdapter adapter;


    String [] titles = {
        "地表殇情",
            "气象信息",
            "气温",
            "气压",
            "日照",
            "风向",
            "风速",
            "降雨量"

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_11, null);
        List<GunManager> list = new ArrayList<>();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            list.add(new GunManager(titles[i], "XXXX"));
        }

        fragment11List = view.findViewById(R.id.fragment_11_list);
        adapter = new GunManagerAdapter(list);
        fragment11List.setLayoutManager(new LinearLayoutManager(getContext()));
        fragment11List.setAdapter(adapter);
        return view;
    }

    @Override
    public void refreshData() {

    }
}
