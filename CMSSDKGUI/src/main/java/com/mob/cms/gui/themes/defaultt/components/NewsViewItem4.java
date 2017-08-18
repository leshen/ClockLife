package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

/** ListView 中的文章显示类型——图下三 */
public class NewsViewItem4 extends LinearLayout {
	public AsyncImageView[] aivNewsImgAry;
	public TextView tvTop;
	public TextView tvHot;
	public TextView tvNewsTime;
	private TextView tvNewsTitle;
	private TextView tvComsCount;
	
	public NewsViewItem4(Context context) {
		super(context);
		initView(context);
	}

	public NewsViewItem4(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public NewsViewItem4(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		int dp15 = ResHelper.dipToPx(context, 15);
		int dp12 = ResHelper.dipToPx(context, 12);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setPadding(dp15, dp12, dp15, dp12);
		this.setLayoutParams(lp);
		
		//标题
		tvNewsTitle = new TextView(context);
		tvNewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		tvNewsTitle.setMaxLines(2);
		tvNewsTitle.setTextColor(0xff222222);
		int dp24 = ResHelper.dipToPx(context, 24);
		tvNewsTitle.setMinHeight(dp24);
		tvNewsTitle.setLineSpacing(0, 1.15f);
		tvNewsTitle.setEllipsize(TextUtils.TruncateAt.END);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.addView(tvNewsTitle, lp);
		
		//图片列表
		aivNewsImgAry = new AsyncImageView[3];
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int dp8 = ResHelper.dipToPx(context, 8);
		lp.setMargins(0, dp8, 0, dp8);
		this.addView(ll, lp);

		int w = (ResHelper.getScreenWidth(context) - ResHelper.dipToPx(context, 36)) / 3;
		float ratio = ((float) w) / 226;
		int h = (int) (150 * ratio);
		int dp2 = ResHelper.dipToPx(context, 2);
		for (int i = 0; i < aivNewsImgAry.length; i++) {
			AsyncImageView aivNewsImg = new AsyncImageView(context);
			aivNewsImgAry[i] = aivNewsImg;
			aivNewsImg.setScaleToCropCenter(true);
			lp = new LayoutParams(w, h);
			if (i == 1) {
				lp.setMargins(dp2, 0, dp2, 0);
			}
			ll.addView(aivNewsImg, lp);
		}
			
		//置顶，评论，时间
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.addView(ll, lp);
		
		//置顶
		tvTop = new TextView(context);
		tvTop.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
		tvTop.setMinHeight(dp24);
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
		lp = new LayoutParams(dp15 + dp12, dp15);
		int dp7 = ResHelper.dipToPx(context, 7);
		lp.setMargins(0, 0, dp7, 0);
		ll.addView(tvTop, lp);

		//热门
		tvHot = new TextView(context);
		tvHot.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
		tvHot.setMinHeight(dp24);
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
