package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

public class VideoPlayerController extends RelativeLayout {
	public static Bitmap bmStop;
	public static Bitmap bmPlay;
	public static Bitmap bmFullScr;
	public static Bitmap bmToView;
	
	public LinearLayout llBar;
	public ProgressBar pb;
	public ImageView ivClose;
	public ImageView ivPlay;
	public TextView tvTime;
	public VideoSeekBar sbProgress;
	public TextView tvDuration;
	public ImageView ivFullScr;

	public VideoPlayerController(Context context) {
		super(context);
		init(context);
	}

	public VideoPlayerController(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public VideoPlayerController(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		initBm(context);
		
		llBar = new LinearLayout(context);
		int dp41 = ResHelper.dipToPx(context, 41);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp41);
		lp.addRule(ALIGN_PARENT_BOTTOM);
		llBar.setBackgroundColor(0x88000000);
		addView(llBar, lp);
		initBar(context);
		
		pb = new ProgressBar(context);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_IN_PARENT);
		int dp10 = ResHelper.dipToPx(context, 10);
		pb.setPadding(dp10, dp10, dp10, dp10);
		addView(pb, lp);

		ivPlay = new ImageView(context);
		int dp50 = ResHelper.dipToPx(context, 50);
		lp = new LayoutParams(dp50, dp50);
		lp.addRule(CENTER_IN_PARENT);
		ivPlay.setPadding(dp10, dp10, dp10, dp10);
		ivPlay.setImageBitmap(bmPlay);
		addView(ivPlay, lp);
		
		ivClose = new ImageView(context);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_LEFT);
		lp.addRule(ALIGN_PARENT_TOP);
		int dp16 = ResHelper.dipToPx(context, 16);
		int dp13 = ResHelper.dipToPx(context, 13);
		int dp12 = ResHelper.dipToPx(context, 12);
		int dp15 = ResHelper.dipToPx(context, 15);
		ivClose.setPadding(dp15, dp15, dp12, dp16);
		ivClose.setScaleType(ScaleType.FIT_XY);
		ivClose.setImageResource(ResHelper.getBitmapRes(context, "cmssdk_default_white_back"));
		addView(ivClose, lp);
	}

	private void initBm(Context context) {
		int dp20 = ResHelper.dipToPx(context, 20);
		if (bmStop == null || bmStop.isRecycled()) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(), ResHelper.getBitmapRes(context, "cmssdk_default_vp_pause"));
			bmStop = Bitmap.createScaledBitmap(bm, bm.getWidth() * dp20 / bm.getHeight(), dp20, true);
		}
		
		if (bmPlay == null || bmPlay.isRecycled()) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(), ResHelper.getBitmapRes(context, "cmssdk_default_vp_play"));
			bmPlay = Bitmap.createScaledBitmap(bm, bm.getWidth() * dp20 / bm.getHeight(), dp20, true);
		}
		
		if (bmFullScr == null || bmFullScr.isRecycled()) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(), ResHelper.getBitmapRes(context, "cmssdk_default_vp_bar_fullscr"));
			bmFullScr = Bitmap.createScaledBitmap(bm, bm.getWidth() * dp20 / bm.getHeight(), dp20, true);
		}
		
		if (bmToView == null || bmToView.isRecycled()) {
			Bitmap bm = BitmapFactory.decodeResource(getResources(), ResHelper.getBitmapRes(context, "cmssdk_default_vp_bar_toview"));
			bmToView = Bitmap.createScaledBitmap(bm, bm.getWidth() * dp20 / bm.getHeight(), dp20, true);
		}
	}
	
	private void initBar(Context context) {
		tvTime = new TextView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		//tvTime.setMinEms(2);
		int dp15 = ResHelper.dipToPx(context, 15);
		tvTime.setPadding(dp15, 0, 0, 0);
		tvTime.setText("0:00");
		tvTime.setTextColor(0xffffffff);
		tvTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 14));
		llBar.addView(tvTime, lp);
		
		initProgress(context);
		
		tvDuration = new TextView(context);
		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		//tvDuration.setMinEms(2);
		int dp6 = ResHelper.dipToPx(context, 6);
		tvDuration.setPadding(dp6, 0, dp6, 0);
		tvDuration.setText("0:00");
		tvDuration.setTextColor(0xffffffff);
		tvDuration.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 14));
		llBar.addView(tvDuration, lp);
		
		ivFullScr = new ImageView(context);
		int dp25 = ResHelper.dipToPx(context, 25);
		lp = new LinearLayout.LayoutParams(dp25, dp25);
		int dp10 = ResHelper.dipToPx(context, 10);
		lp.rightMargin = dp10;
		lp.gravity = Gravity.CENTER_VERTICAL;
		int dp5 = ResHelper.dipToPx(context, 5);
		ivFullScr.setPadding(dp5, dp5, dp5, dp5);
		ivFullScr.setScaleType(ScaleType.CENTER_INSIDE);
		ivFullScr.setImageBitmap(bmFullScr);
		llBar.addView(ivFullScr, lp);
	}
	
	private void initProgress(Context context) {
		sbProgress = new VideoSeekBar(context);
		sbProgress.setBackgroundColor(0xffbfbfbf);
		sbProgress.setFilledColor(0xffd43d3d);
		sbProgress.setIconColor(0xffffffff);
		sbProgress.setBarHeight(ResHelper.dipToPx(context, 1));
		LinearLayout.LayoutParams lpll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResHelper.dipToPx(context, 14));
		lpll.gravity = Gravity.CENTER_VERTICAL;
		lpll.weight = 1;
		lpll.leftMargin = lpll.rightMargin = ResHelper.dipToPx(context, 8);
		llBar.addView(sbProgress, lpll);
	}
}
