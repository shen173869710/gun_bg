package com.auto.di.guan.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.GroupStatusActivity;
import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.DialogListViewAdapter;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.floatWindow.FloatWindow;
import com.auto.di.guan.floatWindow.MoveType;
import com.auto.di.guan.floatWindow.Screen;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/25.
 */

public class FloatWindowUtil {

    private static FloatWindowUtil instance = new FloatWindowUtil();


    private ListView mListView;
    private DialogListViewAdapter adapter;
    private TextView textView;
    private ArrayList<CmdStatus> alist = new ArrayList<>();
    private View view;


    public static synchronized FloatWindowUtil getInstance() {
        return instance;
    }

    private final String TAG = "FloatWindowUtil";
    public void initFloatWindow(Context mContext) {
        view = View.inflate(BaseApp.getInstance(), R.layout.dialog_listview, null);
        mListView = (ListView) view.findViewById(R.id.listview);
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alist.clear();
                FloatWindow.destroy(TAG);
            }
        });
        alist = new ArrayList<>();
        adapter = new DialogListViewAdapter(mContext, alist);
        mListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(mListView);

    }

    public boolean isShow() {

        if (FloatWindow.get(TAG) == null) {
            return false;
        }
       return FloatWindow.get(TAG).isShowing();
    }

    public void show (){
        if (FloatWindow.get(TAG) == null) {
            FloatWindow.with(BaseApp.getInstance())
                    .setView(view)
                    .setWidth(Screen.width,0.3f)
                    .setHeight(Screen.height,0.3f)
                    .setX(Screen.width,0.4f)
                    .setY(Screen.height,0.5f)
                    .setDesktopShow(true)
                    .setFilter(true, MainActivity.class, GroupStatusActivity.class)
                    .setMoveType(MoveType.inactive)
                    .setTag(TAG)
                    .build();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alist.clear();
                    FloatWindow.destroy(TAG);
                }
            });
            FloatWindow.get(TAG).show();
        }else {
            if (!FloatWindow.get(TAG).isShowing()) {
                FloatWindow.get(TAG).show();
            }
        }
    }

    public void distory() {
        alist.clear();
        FloatWindow.destroy(TAG);
    }

    public void claenLise() {
        alist.clear();
    }

    public void onStatsuEvent(CmdStatus event) {
        LogUtils.e("------------------", new Gson().toJson(event));
        if (event != null) {
            int size = alist.size();
            boolean isHas = false;
            for (int i = 0; i < size; i++) {
                if (alist.get(i).control_id == event.control_id) {
                    alist.get(i).cmd_name = "控制阀"+ event.controlName+"号";
                    if (!TextUtils.isEmpty(event.cmd_start)) {
                        alist.get(i).cmd_start = event.cmd_start;
                    }
                    if (!TextUtils.isEmpty(event.cmd_end)) {
                        alist.get(i).cmd_end = event.cmd_end;
                    }

                    if(!TextUtils.isEmpty(event.cmd_read_start)) {
                        alist.get(i).cmd_read_start = event.cmd_read_start;
                    }

                    if (!TextUtils.isEmpty(event.cmd_read_middle)) {
                        alist.get(i).cmd_read_middle = event.cmd_read_middle;
                    }

                    if (!TextUtils.isEmpty(event.cmd_read_end)) {
                        alist.get(i).cmd_read_end = event.cmd_read_end;
                    }
                    isHas = true;
                }
            }

            if (!isHas) {
                if (alist.size() == 5) {
                    alist.clear();
                }
                alist.add(event);
            }
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(mListView);
        }
    }

    public  void setListViewHeightBasedOnChildren(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


}
