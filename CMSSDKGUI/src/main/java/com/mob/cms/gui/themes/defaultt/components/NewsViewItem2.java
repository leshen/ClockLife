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

/** ListView 中的文章显示类型——图右一 */
public class NewsViewItem2 extends LinearLayout {
	public AsyncImageView aivNewsImg;
	public TextView tvTop;
	public TextView tvHot;
	public TextView tvNewsTime;
	public TextView tvVideoDuration;
	private TextView tvNewsTitle;
	private TextView tvComsCount;
	
	public NewsViewItem2(Context context) {
		super(context);
		initView(context);
	}

	public NewsViewItem2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public NewsViewItem2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		this.setOrientation(LinearLayout.HORIZONTAL);
		int dp15 = ResHelper.dipToPx(context, 15);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setPadding(dp15, dp15, dp15, dp15);
		this.setLayoutParams(lp);
		
		//左边布局
		LinearLayout llLeft = new LinearLayout(context);
		llLeft.setOrientation(LinearLayout.VERTICAL);
		lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		this.addView(llLeft, lp);
		
		//标题
		tvNewsTitle = new TextView(context);
		tvNewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		tvNewsTitle.setMaxLines(2);
		tvNewsTitle.setTextColor(0xff222222);
		tvNewsTitle.setLineSpacing(0, 1.15f);
		tvNewsTitle.setEllipsize(TextUtils.TruncateAt.END);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llLeft.addView(tvNewsTitle, lp);
		
		//置顶，评论，时间
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llLeft.addView(ll, lp);

		//置顶
		tvTop = new TextView(context);
		tvTop.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
		tvTop.setGravity(Gravity.CENTER);
		tvTop.setTextColor(0xfff05b5b);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_tv_red_bg");
		if (resId > 0) {
			tvTop.setBackgroundResource(resId);
		}
		int strId = ResHelper.getStringRes(context, "cmssdk_default_set_top");
		if (strId > 0) {
			tvTop.setText(strId);
		} else {
			tvTop.setText("");
		}
		int dp27 = ResHelper.dipToPx(context, 27);
		lp = new LayoutParams(dp27, dp15);
		int dp7 = ResHelper.dipToPx(context, 7);
		lp.setMargins(0, 0, dp7, 0);
		ll.addView(tvTop, lp);
		
		//热门
		tvHot = new TextView(context);
		tvHot.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
		tvHot.setGravity(Gravity.CENTER);
		tvHot.setTextColor(0xfff05b5b);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_tv_red_bg");
		if (resId > 0) {
			tvHot.setBackgroundResource(resId);
		}
		strId = ResHelper.getStringRes(context, "cmssdk_default_set_hot");
		if (strId > 0) {
			tvHot.setText(strId);
		} else {
			tvHot.setText("");
		}
		int dp17 = ResHelper.dipToPx(context, 17);
		lp = new LayoutParams(dp17, dp15);
		lp.setMargins(0, 0, dp7, 0);
		ll.addView(tvHot, lp);
		
		//评论数
		tvComsCount = new TextView(context);
		tvComsCount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		tvComsCount.setTextColor(0xff999999);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, dp7, 0);
		ll.addView(tvComsCount, lp);
		
		//新闻时间
		tvNewsTime = new TextView(context);
		tvNewsTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		tvNewsTime.setTextColor(0xff999999);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll.addView(tvNewsTime, lp);
		
		//右边图片布局
		RelativeLayout rlRight = new RelativeLayout(context);
		int width = (ResHelper.getScreenWidth(context) -  ResHelper.dipToPx(context, 36)) / 3;
		float ratio = ((float) width) / 230;
		int height = (int) (150 * ratio);
		lp = new LayoutParams(width, height);
		lp.setMargins(dp15, 0, 0, 0);
		this.addView(rlRight, lp);
		
		aivNewsImg = new AsyncImageView(context);
		aivNewsImg.setScaleToCropCenter(true);	
		lp = new LayoutParams(width, height);
		rlRight.addView(aivNewsImg, lp);
		
		//如果是视频类型时，显示视频时长
		tvVideoDuration = new TextView(context);
		tvVideoDuration.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
		tvVideoDuration.setGravity(Gravity.CENTER);
		tvVideoDuration.setTextColor(0xffffffff);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_playtime_bg");
		if (resId > 0) {
			tvVideoDuration.setBackgroundResource(resId);
		}
		int dp2 = ResHelper.dipToPx(context, 2);
		int dp5 = ResHelper.dipToPx(context, 5);
		tvVideoDuration.setPadding(dp7, dp2, dp7, dp2);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlp.setMargins(0, 0, dp5, dp5);
		rlRight.addView(tvVideoDuration, rlp);
	}
	
	public void setComsCount(boolean openCom, int count) {
		if (!openCom) {
			tvComsCount.setVisibility(GONE);
			return;
		}
		tvComsCount.setVisibility(VISIBLE);
		int resId = ResHelper.getStringRes(getContext(), "cmssdk_default_news_comments");
		if (resId > 0) {
			String comStr = getContext().getString(resId);
			comStr = String.format(comStr, count);
			tvComsCount.setText(comStr);
		}
	}

	public void setNewsTitle(String title, boolean hasReaded) {
		tvNewsTitle.setTextColor(0xff222222);
		if (hasReaded) {
			tvNewsTitle.setTextColor(0xff888888);
		}
		tvNewsTitle.setText(title);
	}
}
