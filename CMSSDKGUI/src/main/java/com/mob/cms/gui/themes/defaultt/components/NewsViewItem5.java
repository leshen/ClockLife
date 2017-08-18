package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

/** ListView 中的视频文章显示类型——图下一 */
public class NewsViewItem5 extends LinearLayout {
	public AsyncImageView aivNewsImg;
	public TextView tvTop;
	public TextView tvHot;
	public TextView tvUpdateAt;
	public TextView tvPlayTime;
	private TextView tvNewsTitle;
	private TextView tvComsCount;
	
	public NewsViewItem5(Context context) {
		super(context);
		initView(context);
	}

	public NewsViewItem5(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public NewsViewItem5(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		int dp15 = ResHelper.dipToPx(context, 15);
		int dp12 = ResHelper.dipToPx(context, 12);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
		
		RelativeLayout rl = new RelativeLayout(context);
		int width = ResHelper.getScreenWidth(context);
		float ratio = ((float)(width - ResHelper.dipToPx(context, 30))) / 690;
		int height = (int) (390 * ratio);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		int dp5 = ResHelper.dipToPx(context, 5);
		int dp8 = ResHelper.dipToPx(context, 8);
		lp.setMargins(0, dp8, 0, dp8);
		this.addView(rl, lp);
		
		//视频图片
		aivNewsImg = new AsyncImageView(context);
		aivNewsImg.setScaleToCropCenter(true);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(width, height);
		rl.addView(aivNewsImg, rlp);
		
		//播放按钮
		View v = new View(context);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_play_icon");
		v.setBackgroundResource(resId);
		int dp41 = ResHelper.dipToPx(context, (int) (41 * ratio));
		rlp = new RelativeLayout.LayoutParams(dp41, dp41);
		rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(v, rlp);
		
		//视频时长
		tvPlayTime = new TextView(context);
		tvPlayTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
		tvPlayTime.setGravity(Gravity.CENTER);
		tvPlayTime.setTextColor(0xffffffff);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_playtime_bg");
		if (resId > 0) {
			tvPlayTime.setBackgroundResource(resId);
		}
		int dp3 = ResHelper.dipToPx(context, 3);
		tvPlayTime.setPadding(dp8, dp3, dp8, dp3);
		rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlp.setMargins(0, 0, dp5, dp5);
		rl.addView(tvPlayTime, rlp);
		
		//置顶，评论，时间
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.addView(ll, lp);
		
		//置顶
		tvTop = new TextView(context);
		tvTop.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
		tvTop.setMinHeight(dp24);
		tvTop.setGravity(Gravity.CENTER);
		tvTop.setTextColor(0xfff05b5b);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_tv_red_bg");
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
		tvUpdateAt = new TextView(context);
		tvUpdateAt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		tvUpdateAt.setTextColor(0xff999999);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll.addView(tvUpdateAt, lp);
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
