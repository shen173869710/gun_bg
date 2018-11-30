package com.auto.di.guan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.auto.di.guan.R;
import com.auto.di.guan.utils.CalculateUtil;
import com.auto.di.guan.utils.StringUtils;


public class ClearEditText extends EditText implements
        OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;
    private boolean isNeedLimitInputCount = false;
    private int maxInputCount = 0; 
    private int right_type = 0;
    private TextChangeCallBack mChangeCallBack;
    private int rightTextColor = Color.GRAY;
    private final int RIGHT_TYPE_CLEAR  = 0;
    private final int RIGHT_TYPE_NUMBER = 1;
    
    public ClearEditText(Context context) {
    	this(context, null); 
    } 
 
    public ClearEditText(Context context, AttributeSet attrs) {
    	this(context, attrs, android.R.attr.editTextStyle); 
    } 
    
    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        maxInputCount = mTypedArray.getInteger(R.styleable.ClearEditText_limitnumber, 0);
        isNeedLimitInputCount = mTypedArray.getBoolean(R.styleable.ClearEditText_limitinput, false);
        right_type = mTypedArray.getInteger(R.styleable.ClearEditText_righttype, RIGHT_TYPE_CLEAR);
        rightTextColor = mTypedArray.getColor(R.styleable.ClearEditText_righttextcolor, Color.GRAY);
        init();
        mTypedArray.recycle();
    }
    
    
    private void init() { 
    	Drawable mSearchDrawable = getCompoundDrawables()[0];
    	if(mSearchDrawable != null){
    		mSearchDrawable.setBounds(CalculateUtil.dip2px(getContext(), 5), 0, 
    				CalculateUtil.dip2px(getContext(), 5)+mSearchDrawable.getIntrinsicWidth(), mSearchDrawable.getIntrinsicHeight());
    		setCompoundDrawables(mSearchDrawable, null, null, null);
    	}
    	if(right_type == RIGHT_TYPE_CLEAR){
    		mClearDrawable = getCompoundDrawables()[2]; 
            if (mClearDrawable == null) { 
            	mClearDrawable = getResources() 
                        .getDrawable(R.drawable.search_clean); 
            } 
            mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
            setPadding(0, 0, mClearDrawable.getIntrinsicWidth(), 0);
            setClearIconVisible(false); 
    	}else{
    		if(maxInputCount > 0){
    			setRightTextDrawable(String.valueOf(maxInputCount));
    		}
    	}
    	
        setOnFocusChangeListener(this); 
        addTextChangedListener(this); 
    } 
 
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null && right_type == RIGHT_TYPE_CLEAR) { 
            if (event.getAction() == MotionEvent.ACTION_UP) {
            	boolean touchable = event.getX() > (getWidth() 
                        - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) 
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) { 
                    this.setText(""); 
                } 
            } 
        } 
 
        return super.onTouchEvent(event); 
    } 
 
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    	if(right_type == RIGHT_TYPE_CLEAR){
            if (hasFocus) { 
                setClearIconVisible(getText().length() > 0); 
            } else { 
                setClearIconVisible(false); 
            }
    	}else{
    		if(maxInputCount > 0){
    			setRightTextDrawable(getLeftNumberToString(getText().toString()));
    		}
    	}
    } 
 
 
    protected void setClearIconVisible(boolean visible) { 
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], 
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]); 
    } 
     
    
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
    	if(right_type == RIGHT_TYPE_CLEAR){
    		setClearIconVisible(s.length() > 0); 
    	}else{
    		setRightTextDrawable(getLeftNumberToString(s.toString()));
    	}
        if(mChangeCallBack!=null){
        	mChangeCallBack.onTextChanged(s, start, count, after);
        }
    } 
 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    	 if(mChangeCallBack!=null){
         	mChangeCallBack.onTextChanged(s, start, count, after);
         }
    } 
 
    @Override
    public void afterTextChanged(Editable s) {
    	if(isNeedLimitInputCount){
		    boolean nOverMaxLength = false;
		    //按照产品定义，一个中文字抵四个英文字符，这里默认按产品设计的限制字符数乘4
		    nOverMaxLength = (StringUtils.getLengthEx(s.toString()) > maxInputCount * 4) ? true : false;
		    if(nOverMaxLength){
			    setTextKeepState(getNewString(s.toString()));
		    }
    	}
    } 
    
   
    public void setShakeAnimation(){
    	this.setAnimation(shakeAnimation(5));
    }
    
    
    public static Animation shakeAnimation(int counts){
    	Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
    	translateAnimation.setInterpolator(new CycleInterpolator(counts));
    	translateAnimation.setDuration(1000);
    	return translateAnimation;
    }
 
    /**
     * 设置最大输入字符数，只需填入产品要求的字符数即可
     * 按照产品定义，一个中文字抵四个英文字符，需要限制输入是默认按产品设计的限制字符数乘4
     * @param count
     */
    public void setLimitInputNumber(int count){
    	isNeedLimitInputCount = true;
    	this.maxInputCount = count;
    	if(right_type == RIGHT_TYPE_NUMBER){
    		setRightTextDrawable(String.valueOf(count));
    	}
    }
    
    public void setCallBack(TextChangeCallBack back){
    	this.mChangeCallBack = back;
    }
    
    public abstract interface TextChangeCallBack{
    	public void onTextChanged(CharSequence s, int start, int count,
                                  int after);
    	public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after);
    }

	@Override
	public void setText(CharSequence text, BufferType type) {
		String charS = text.toString();
		if(right_type == RIGHT_TYPE_NUMBER){
			charS = getNewString(text.toString());
		}
		super.setText(charS, type);
	}
	
    private String getNewString(String s) {
    	String result = s;
    	while(StringUtils.getLengthEx(result) > maxInputCount * 4){
    		result = result.substring(0, result.length()-1);
    	}
		return result;
    }

	@Override
	public void setSelection(int index) {
		if(index > getText().toString().length()){
			index = getText().toString().length();
		}
		super.setSelection(index);
	}


	public void setRightType(int type){
		this.right_type = type;
	}
	
	public void setRightTextColor(int color){
		this.rightTextColor = color;
	}
	
	private void setRightTextDrawable(String text){
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], 
				TextToDrawable(text), getCompoundDrawables()[3]);
	}
	
	private Drawable TextToDrawable(String text) {
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    paint.setTextSize(CalculateUtil.dip2px(getContext(), getTextSize())); 
	    paint.setTextAlign(Align.LEFT);
	    paint.setColor(rightTextColor);  
	    String s = text;
	    FontMetrics fm = paint.getFontMetrics();
		int w = 10 + CalculateUtil.dip2px(getContext(), getTextSize()) * (text.length());
		int h = (int) (fm.bottom - fm.top);
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap);
	    canvas.drawText(s, 10, - (fm.bottom + fm.top), paint);  
	    canvas.save();  
	    Drawable drawableright = new BitmapDrawable(bitmap);
	    drawableright.setBounds(0, 0, drawableright.getIntrinsicWidth(),  
	            drawableright.getIntrinsicHeight());  
	    return drawableright;  
	} 
	
	public int getLeftNumber(String s){
		int maxInput = maxInputCount * 4;
		int number = (maxInput - StringUtils.getLengthEx(s.toString()))/4;
		return number;
	}
	
	public String getLeftNumberToString(String s){
		return String.valueOf(getLeftNumber(s));
	}
}
