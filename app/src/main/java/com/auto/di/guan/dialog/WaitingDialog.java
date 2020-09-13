package com.auto.di.guan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.auto.di.guan.R;

public class WaitingDialog extends Dialog{  
	  

    private ImageView iv;  
    private TextView tv;
    private AnimationDrawable mAnimationDrawable;  
      
    public WaitingDialog(Context context, int theme) {  
        super(context, theme);  
    }  
  
    protected WaitingDialog(Context context, boolean cancelable,
                            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);  
    }  
  
    public WaitingDialog(Context context) {  
        super(context);  
  
    }  
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
        setContentView(view);
//        iv = (ImageView) view.findViewById(R.id.oahprogressbar);
//        tv = (TextView) view.findViewById(R.id.loading_text);
//        tv.setVisibility(View.GONE);
//        iv.setImageResource(R.drawable.main_loading);
    }  
      
    @Override  
    public void show() {  
        // TODO Auto-generated method stub  
        super.show();  
//        mAnimationDrawable = (AnimationDrawable) iv.getDrawable();
//        if (null != mAnimationDrawable){
//        	mAnimationDrawable.start();
//        }
    }  
      
    @Override  
    public void dismiss() {  
        super.dismiss();  
//        mAnimationDrawable = (AnimationDrawable) iv.getDrawable();
//        if (null != mAnimationDrawable){
//        	mAnimationDrawable.stop();
//        }
//
    }  
      
    public void setMsg(String txt) {  
    	tv.setVisibility(View.VISIBLE);
    	tv.setText(txt);
    
    }  
      
}  
