package com.auto.di.guan.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.auto.di.guan.GroupStatusActivity;
import com.auto.di.guan.MainActivity;
import com.auto.di.guan.MyApplication;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.DialogListViewAdapter;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.floatWindow.FloatWindow;
import com.auto.di.guan.floatWindow.MoveType;
import com.auto.di.guan.floatWindow.Screen;

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
        view = View.inflate(MyApplication.getInstance(), R.layout.dialog_listview, null);
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


    }

    public boolean isShow() {

        if (FloatWindow.get(TAG) == null) {
            return false;
        }
       return FloatWindow.get(TAG).isShowing();
    }

    public void show (){
        if (FloatWindow.get(TAG) == null) {
            FloatWindow.with(MyApplication.getInstance())
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
        if (event != null) {
            int size = alist.size();
            boolean isHas = false;
            for (int i = 0; i < size; i++) {
                if (alist.get(i).control_id == event.control_id) {
                    alist.get(i).cmd_name = "控制阀"+ event.control_id+"号";
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
                alist.add(event);
            }
            adapter.notifyDataSetChanged();
        }
    }

}
