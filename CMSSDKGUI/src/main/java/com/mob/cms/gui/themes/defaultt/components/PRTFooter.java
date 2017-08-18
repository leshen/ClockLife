package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mob.tools.utils.ResHelper;

public class PRTFooter extends RelativeLayout {
	
	private ProgressBar pbRefreshing;
	
	public PRTFooter(Context context) {
		super(context);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(lp);
		
		pbRefreshing = new ProgressBar(context);
		int dp5 = ResHelper.dipToPx(context, 5);
		pbRefreshing.setPadding(dp5, dp5, dp5, dp5);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_progressbar");
		Drawable pbdrawable = context.getResources().getDrawable(resId);
		pbRefreshing.setIndeterminateDrawable(pbdrawable);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(pbRefreshing, lp);
		pbRefreshing.setVisibility(View.INVISIBLE);
	}
	
	public void onRequest() {
		pbRefreshing.setVisibility(View.VISIBLE);
	}
	
	public void reverse() {
		pbRefreshing.setVisibility(View.INVISIBLE);
	}
}
