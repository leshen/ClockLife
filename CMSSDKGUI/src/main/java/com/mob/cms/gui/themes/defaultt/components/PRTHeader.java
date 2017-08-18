package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

//#if def{lang} == cn
/** 下拉刷新的头部控件  */
//#elif def{lang} == en
/** the loading-layout of drop-down refresh */
//#endif
public class PRTHeader extends RelativeLayout {
	
	private TextView tvHeader;
	private RotateImageView ivArrow;
	private ProgressBar pbRefreshing;

	public PRTHeader(Context context) {
		super(context);

		int dp15 = ResHelper.dipToPx(context, 15);
		this.setPadding(0, dp15, 0, 0);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(lp);

		tvHeader = new TextView(getContext());
		tvHeader.setId(1);
		tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tvHeader.setPadding(dp15, 0, dp15, 0);
		tvHeader.setTextColor(0xffcccccc);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(tvHeader, lp);
		
		ivArrow = new RotateImageView(context);
		int dp5 = ResHelper.dipToPx(context, 5);
		ivArrow.setPadding(dp5, dp5, dp5, dp5);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_ptr_ptr");
		if (resId > 0) {
			ivArrow.setImageResource(resId);
		}
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.LEFT_OF, 1);
		this.addView(ivArrow, lp);

		pbRefreshing = new ProgressBar(context);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_progressbar");
		Drawable pbdrawable = context.getResources().getDrawable(resId);
		pbRefreshing.setIndeterminateDrawable(pbdrawable);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.LEFT_OF, 1);
		this.addView(pbRefreshing, lp);
		pbRefreshing.setVisibility(View.GONE);

	}

	public void onPullDown(int percent) {
		if (percent > 100) {
			int degree = (percent - 100) * 180 / 20;
			if (degree > 180) {
				degree = 180;
			}
			if (degree < 0) {
				degree = 0;
			}
			ivArrow.setRotation(degree);
		} else {
			ivArrow.setRotation(0);
		}

		if (percent < 100) {
			int resId = ResHelper.getStringRes(getContext(), "cmssdk_default_pull_to_refresh");
			if (resId > 0) {
				tvHeader.setText(resId);
			}
		} else {
			int resId = ResHelper.getStringRes(getContext(), "cmssdk_default_release_to_refresh");
			if (resId > 0) {
				tvHeader.setText(resId);
			}
		}
	}

	public void onRequest() {
		ivArrow.setVisibility(View.GONE);
		pbRefreshing.setVisibility(View.VISIBLE);
		int resId = ResHelper.getStringRes(getContext(), "cmssdk_default_refreshing");
		if (resId > 0) {
			tvHeader.setText(resId);
		}
	}

	public void reverse() {
		pbRefreshing.setVisibility(View.GONE);
		ivArrow.setRotation(180);
		ivArrow.setVisibility(View.VISIBLE);
	}

}
