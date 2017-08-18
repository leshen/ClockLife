package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

public class CommentViewItem extends RelativeLayout {
	public AsyncImageView aivUserIcon;
	public TextView tvUserName;
	public TextView tvComTime;
	public TextView tvComText;
	public TextView tvDeleteCom;
	public CommentViewItem(Context context) {
		super(context);
		initView(context);
	}

	public CommentViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public CommentViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int dp20 = ResHelper.dipToPx(context, 20);
		int dp10 = ResHelper.dipToPx(context, 10);
		this.setPadding(dp20, dp10, dp20, dp10);
		this.setLayoutParams(lp);

		//评论者头像
		aivUserIcon = new AsyncImageView(context);
		aivUserIcon.setId(1);
		int dp28 = ResHelper.dipToPx(context, 28);
		aivUserIcon.setRound(dp28);
		aivUserIcon.setScaleToCropCenter(true);
		int dp40 = ResHelper.dipToPx(context, 40);
		lp = new LayoutParams(dp40, dp40);
		int dp4 = ResHelper.dipToPx(context, 4);
		lp.rightMargin = dp10;
		lp.topMargin = dp4;
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		this.addView(aivUserIcon, lp);
		
		//昵称
		tvUserName = new TextView(context);
		tvUserName.setId(2);
		tvUserName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		tvUserName.setTextColor(0xff406599);
		tvUserName.setEllipsize(TextUtils.TruncateAt.END);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = ResHelper.dipToPx(context, 6);;
		lp.bottomMargin = dp10;
		lp.addRule(RelativeLayout.RIGHT_OF, 1);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		this.addView(tvUserName, lp);

		//删除
		tvDeleteCom = new TextView(context);
		tvDeleteCom.setVisibility(GONE);
		tvDeleteCom.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		tvDeleteCom.setTextColor(0xffe66159);
		tvDeleteCom.setEllipsize(TextUtils.TruncateAt.END);
		int resId = ResHelper.getStringRes(context, "cmssdk_default_detele");
		if (resId > 0) {
			tvDeleteCom.setText(resId);
		}
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.rightMargin = dp4;
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		this.addView(tvDeleteCom, lp);
		
		//评论内容
		tvComText = new TextView(context);
		tvComText.setId(3);
		tvComText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
		tvComText.setTextColor(0xff222222);
		tvComText.setLineSpacing(0, 1.4f);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int dp8 = ResHelper.dipToPx(context, 8);
		lp.bottomMargin = dp8;
		lp.addRule(RelativeLayout.RIGHT_OF, 1);
		lp.addRule(RelativeLayout.BELOW, 2);
		this.addView(tvComText, lp);
		
		//评论时间
		tvComTime = new TextView(context);
		tvComTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		tvComTime.setTextColor(0xff222222);
		tvComTime.setEllipsize(TextUtils.TruncateAt.END);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.RIGHT_OF, 1);
		lp.addRule(RelativeLayout.BELOW, 3);
		this.addView(tvComTime, lp);
	}
	
}
