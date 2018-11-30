package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.DialogListViewAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.ManualItem;
import com.auto.di.guan.entity.PollingEvent;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 满足两个按钮的dialog
 */
public class CustomListViewDialog extends Dialog{


    private Context mContext;
    private ListView mListView;
    private ArrayList<CmdStatus> alist = new ArrayList<>();
    private DialogListViewAdapter adapter;
    public CustomListViewDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
    }


    public void claenList() {
        alist.clear();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = View.inflate(context, R.layout.dialog_listview, null);
//        initView(view);
//        return view;
//    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.dialog_listview, null);
        mListView = (ListView) view.findViewById(R.id.listview);
        setContentView(view);
        EventBus.getDefault().register(this);
        alist = new ArrayList<>();
        adapter = new DialogListViewAdapter(mContext, alist);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        setHeight();
    }

    private void setHeight() {
        Window window = getWindow();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (window.getDecorView().getHeight() >= (int) (displayMetrics.heightPixels * 0.9)) {
            attributes.height = (int) (displayMetrics.heightPixels * 0.9);
        }
        window.setAttributes(attributes);
    }


    public static void ShowListDialog(final Activity context) {
        final CustomListViewDialog dialog = new CustomListViewDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        ViewGroup.LayoutParams lay = dialog.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();//获取屏幕分辨率
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);//
        Rect rect = new Rect();
        View view = context.getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.width = dm.widthPixels * 9/10;
        lay.height = dm.heightPixels* 9/10;
        dialog.show();
    }

    //
//    @Override
//    public void onResume() {
//        super.onResume();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatsuEvent(CmdStatus event) {
        LogUtils.e("-----", "onStatsuEvent id = "+event.control_id);
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
                    if (!TextUtils.isEmpty(event.cmd_read_start)) {
                        alist.get(i).cmd_read_start = event.cmd_read_start;
                    }
                    if (!TextUtils.isEmpty(event.cmd_read_middle)) {
                        alist.get(i).cmd_read_start = event.cmd_read_start;
                    }
                    if (!TextUtils.isEmpty(event.cmd_read_end)) {
                        alist.get(i).cmd_read_start = event.cmd_read_start;
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


    public void setListViewHeightBasedOnChildren(ListView listView, android.widget.BaseAdapter adapter) {
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

//    /**
//     * 判断弹窗是否显示
//     * @return
//     */
//    public boolean isShowing(){
//        return getDialog() != null && getDialog().isShowing();
//    }

//    /**
//     * 显示DialogFragment(注此方法会衍生出状态值问题(本人在正常使用时并未出现过))
//     * @param manager
//     * @param tag
//     * @param isResume 在Fragment中使用可直接传入isResumed()
//     *                 在FragmentActivity中使用可自定义全局变量 boolean isResumed 在onResume()和onPause()中分别传人判断为true和false
//     */
//    public void show(FragmentManager manager, String tag, boolean isResume){
//        if(!isShowing()){
//            if(isResume){
//                //正常显示
//                if(!isAdded()){
//                    show(manager, tag);
//                }else{
//                    FragmentTransaction ft = manager.beginTransaction();
//                    ft.show(this);
//                    ft.commit();
//                }
//            }else{
//                //注 此方法会衍生出一些状态问题,慎用（在原代码中 需要设置  mDismissed = false 和 mShownByMe = true 并未在此引用到,如果需要用到相关判断值,此方法不可用）
//                FragmentTransaction ft = manager.beginTransaction();
//                if(!isAdded()){
//                    ft.add(this, tag);
//                }else{
//                    ft.show(this);
//                }
//                ft.commitAllowingStateLoss();
//            }
//        }
//    }
//
//    /**
//     * 关闭DialogFragment
//     * @param isResume 在Fragment中使用可直接传入isResumed()
//     *                 在FragmentActivity中使用可自定义全局变量 boolean isResumed 在onResume()和onPause()中分别传人判断为true和false
//     */
//    public void dismiss(boolean isResume){
//        if(isResume){
//            dismiss();
//        }else{
//            dismissAllowingStateLoss();
//        }
//    }
//
//    @Override
//    public void dismiss() {
//        if(isShowing()){
//            super.dismiss();
//        }
//    }
//
//    @Override
//    public void dismissAllowingStateLoss() {
//        if(isShowing()){
//            super.dismissAllowingStateLoss();
//        }
//    }
//
//
//    public static CustomListViewDialog getInstance(Context mContext, FragmentManager fm) {
//        String tag = CustomListViewDialog.class.getName();
//        Fragment fragment = fm.findFragmentByTag(tag);
//        if (fragment == null) {
//            fragment = Fragment.instantiate(mContext, tag);
//            CustomListViewDialog dialogFragment = (CustomListViewDialog) fragment;
////            dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);//设置取消标题栏
////            dialogFragment.setCanceledOnTouchOutside(true);//外围点击 dismiss
//            return dialogFragment;
//        }else{
//            return  (CustomListViewDialog)fragment;
//        }
//    }
}
