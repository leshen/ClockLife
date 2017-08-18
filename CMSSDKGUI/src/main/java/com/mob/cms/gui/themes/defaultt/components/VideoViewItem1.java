package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

public class VideoViewItem1 extends LinearLayout {
	public TextView tvVideoTitle;
	public AsyncImageView aivVideoImg;
	public TextView tvPlayTimes;
	public TextView tvVideoDuration;
	public VideoViewItem1(Context context) {
		super(context);
		initView(context);
	}

	public VideoViewItem1(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public VideoViewItem1(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		this.setOrientation(LinearLayout.HORIZONTAL);
		int dp15 = ResHelper.dipToPx(context, 15);
		int dp13 = ResHelper.dipToPx(context, 13);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setPadding(dp15, dp13, dp15, dp13);
		this.setLayoutParams(lp);
		
		//左边布局
		LinearLayout llLeft = new LinearLayout(context);
		llLeft.setOrientation(LinearLayout.VERTICAL);
		lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		this.addView(llLeft, lp);
		
		//标题
		tvVideoTitle = new TextView(context);
		tvVideoTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		tvVideoTitle.setMaxLines(2);
		tvVideoTitle.setTextColor(0xff222222);
		tvVideoTitle.setGravity(Gravity.CENTER_VERTICAL);
		tvVideoTitle.setEllipsize(TextUtils.TruncateAt.END);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llLeft.addView(tvVideoTitle, lp);
		
		//播放数
		tvPlayTimes = new TextView(context);
		tvPlayTimes.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		tvPlayTimes.setTextColor(0xff999999);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int dp12 = ResHelper.dipToPx(context, 12);
		lp.setMargins(0, 0, dp12, 0);
		llLeft.addView(tvPlayTimes, lp);
		
		//右边布局
		RelativeLayout rlRight = new RelativeLayout(context);
		int width = (ResHelper.getScreenWidth(context) - ResHelper.dipToPx(context, 36)) / 3;
		float ratio = ((float) width) / 230;
		int height = (int) (150 * ratio);
		lp = new LayoutParams(width, height);
		int dp25 = ResHelper.dipToPx(context, 25);
		lp.setMargins(dp25, 0, 0, 0);
		this.addView(rlRight, lp);
		
		//视频图片
		aivVideoImg = new AsyncImageView(context);
		aivVideoImg.setScaleToCropCenter(true);
		lp = new LayoutParams(width, height);
		rlRight.addView(aivVideoImg, lp);

		//视频时长
		tvVideoDuration = new TextView(context);
		tvVideoDuration.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
		tvVideoDuration.setGravity(Gravity.CENTER);
		tvVideoDuration.setTextColor(0xffffffff);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_playtime_bg");
		if (resId > 0) {
			tvVideoDuration.setBackgroundResource(resId);
		}
		int dp2 = ResHelper.dipToPx(context, 2);
		int dp5 = ResHelper.dipToPx(context, 5);
		int dp7 = ResHelper.dipToPx(context, 7);
		tvVideoDuration.setPadding(dp7, dp2, dp7, dp2);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlp.setMargins(0, 0, dp5, dp5);
		rlRight.addView(tvVideoDuration, rlp);
	}
	
}
