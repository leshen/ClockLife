package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

/** 没有数据的默认图标 */
public class NoDataViewItem extends LinearLayout {
	
	public NoDataViewItem(Context context) {
		super(context);
		initView(context);
	}

	public NoDataViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public NoDataViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	private void initView(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);

		ImageView iv = new ImageView(getContext());
		iv.setImageResource(ResHelper.getBitmapRes(getContext(), "cmssdk_default_no_data"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		int dp50 = ResHelper.dipToPx(getContext(), 50); 
		lp.topMargin = dp50;
		this.addView(iv, lp);

		TextView tv = new TextView(getContext());
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(0xff999999);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		tv.setText(ResHelper.getStringRes(getContext(), "cmssdk_default_no_data"));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = ResHelper.dipToPx(getContext(), 20);
		this.addView(tv, lp);
	}
	
}
