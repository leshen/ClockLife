package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.News;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.tools.utils.ResHelper;

import java.text.SimpleDateFormat;

public class VideoViewItem0 extends LinearLayout implements View.OnClickListener{
	
	public TextView tvVideoTitle;
	private TextView tvVideoPlayTimes;
	private TextView tvVideoDetail;
	public ImageView ivLike;
	public TextView tvLikeCounts;
	private ImageView arrowView; 
	private boolean open;
	
	public VideoViewItem0(Context context) {
		super(context);
		initView(context);
	}

	public VideoViewItem0(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public VideoViewItem0(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	private void initView(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		int dp15 = ResHelper.dipToPx(context, 15);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(lp);

		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int dp10 = ResHelper.dipToPx(context, 10);
		lp.setMargins(dp15, dp15, dp10, 0);
		this.addView(ll, lp);
		
		//视频标题
		tvVideoTitle = new TextView(context);
		tvVideoTitle.setEllipsize(TextUtils.TruncateAt.END);
		tvVideoTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		tvVideoTitle.setLineSpacing(0, 1.15f);
		tvVideoTitle.getPaint().setFakeBoldText(true);
		tvVideoTitle.setMaxLines(1);
		tvVideoTitle.setTextColor(0xff000000);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int dp5 = ResHelper.dipToPx(context, 5);
		lp.rightMargin = dp5;
		lp.weight = 1;
		ll.addView(tvVideoTitle, lp);
		
		//隐藏视频详情的按钮;
		arrowView = new ImageView(context);
		arrowView.setId(1);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_arrow_down");
		if (resId > 0) {
			arrowView.setImageResource(resId);
		}
		arrowView.setOnClickListener(this);
		arrowView.setPadding(dp5, dp5, dp5, dp5);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll.addView(arrowView, lp);
		
		//视频播放次数
		tvVideoPlayTimes = new TextView(context);
		tvVideoPlayTimes.setTextColor(0xff999999);
		tvVideoPlayTimes.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int dp7 = ResHelper.dipToPx(context, 7);
		lp.setMargins(dp15, dp5, 0, dp7);
		this.addView(tvVideoPlayTimes, lp);
		
		//视频详情
		tvVideoDetail = new TextView(context);
		tvVideoDetail.setTextColor(0xff999999);
		tvVideoDetail.setEllipsize(TextUtils.TruncateAt.END);
		tvVideoDetail.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		tvVideoDetail.setVisibility(GONE);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(dp15, 0, 0, 0);
		this.addView(tvVideoDetail, lp);

		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(dp15, 0, 0, dp15);
		this.addView(ll, lp);
		//赞
		ivLike = new ImageView(context);
		ivLike.setId(2);
		ivLike.setPadding(0, 0, dp7, 0);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_like_selector");
		if (resId > 0) {
			ivLike.setImageResource(resId);
		}
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll.addView(ivLike, lp);
		
		//赞的个数
		tvLikeCounts = new TextView(context);
		tvLikeCounts.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		tvLikeCounts.setTextColor(0xff505050);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll.addView(tvLikeCounts, lp);
		
		//分割线
		View v = new View(context);
		v.setBackgroundColor(0xffb4b4b4);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		this.addView(v, lp);
	}

	public void onClick(View view) {
		if (view == arrowView) {
			int resId = ResHelper.getBitmapRes(getContext(), "cmssdk_default_arrow_down");
			if (open) {
				open = false;
				tvVideoTitle.setMaxLines(1);
				tvVideoDetail.setVisibility(GONE);
			} else {
				open = true;
				tvVideoTitle.setMaxLines(2);
				tvVideoDetail.setVisibility(VISIBLE);
				resId = ResHelper.getBitmapRes(getContext(), "cmssdk_default_arrow_up");
			}
			if (resId > 0) {
				arrowView.setImageResource(resId);
			}
		}
	}
	
	/** 视频播放次数 */
	public void setViedoPlayTimes(int playC) {
		String playTimes = getContext().getString(ResHelper.getStringRes(getContext(), "cmssdk_default_video_play_times"));
		tvVideoPlayTimes.setText(String.format(playTimes, playC));
	}
	
	/** 没有视频详情时，隐藏箭头和视频详情View*/
	public void setVideoDetail(long videoTime, String detail) {
		if (TextUtils.isEmpty(detail)) {
			tvVideoTitle.setMaxLines(1);
			arrowView.setVisibility(GONE);
			tvVideoDetail.setVisibility(GONE);
		} else {
			String videoDetail = getContext().getString(ResHelper.getStringRes(getContext(), "cmssdk_default_video_detail"));
			int resId = ResHelper.getStringRes(getContext(), "cmssdk_default_video_update_time");
			if (videoTime <= 0 || resId <= 0) {
				tvVideoDetail.setText(detail);
			} else {
				String format = getContext().getString(resId);
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				;
				detail = String.format(videoDetail, sdf.format(videoTime)) + "\n" + detail;
				tvVideoDetail.setText(Html.fromHtml(detail));
			}
		}
		
	}
	
	public void setLikeStatus(UserBrief user, News videoNews) {
		if (user == null) {
			user = new UserBrief(UserBrief.USER_ANONYMOUS, null, null, null);
		}
		switch (user.type) {
			case UserBrief.USER_UMSSDK: {
				CMSSDK.hasUMSSDKUserLikedTheNews(videoNews, new Callback<Boolean>(){
					public void onSuccess(Boolean isLike) {
						if (isLike) {
							ivLike.setSelected(true);
							ivLike.setEnabled(false);
						}
					}

					public void onFailed(Throwable t) {
						//t.printStackTrace();
					}
				});
			} break;
			case UserBrief.USER_CUSTOM: {
				CMSSDK.hasCustomUserLikedTheNews(videoNews, user.uid, new Callback<Boolean>(){
					public void onSuccess(Boolean isLike) {
						if (isLike) {
							ivLike.setSelected(true);
							ivLike.setEnabled(false);
						}
					}

					public void onFailed(Throwable t) {
						//t.printStackTrace();
					}
				});
			} break;
			case UserBrief.USER_ANONYMOUS: {
				CMSSDK.hasAnonymousUserLikedTheNews(videoNews, new Callback<Boolean>(){
					public void onSuccess(Boolean isLike) {
						if (isLike) {
							ivLike.setSelected(true);
							ivLike.setEnabled(false);
						}
					}

					public void onFailed(Throwable t) {
						//t.printStackTrace();
					}
				});
			} break;
		}
	}
	
}
