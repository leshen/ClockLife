package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

public class CommentToolsView extends RelativeLayout {
	private Context context;
	private View line;
	private ImageView ivPan;
	private TextView tvInputComs;
	private TextView tvComsCount;
	private RelativeLayout rlItem;
	public ImageView ivMsgBg;//暴露给外部设置监听
	public LinearLayout llInputComs;//暴露给外部设置监听
	
	public CommentToolsView(Context context) {
		super(context);
		initView(context);
	}

	public CommentToolsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public CommentToolsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	private void initView(Context context) {
		this.context = context.getApplicationContext();
		rlItem = new RelativeLayout(context);
		rlItem.setBackgroundColor(0xffffff);
		int dp49 = ResHelper.dipToPx(context, 49);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp49);
		addView(rlItem, lp);
		
		//分割线
		line = new View(context);
		line.setBackgroundColor(0xffe8e8e8);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rlItem.addView(line, lp);
		
		//评论信息图片
		ivMsgBg = new ImageView(context);
		ivMsgBg.setId(1);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_comment_black_msg");
		if (resId > 0) {
			ivMsgBg.setImageResource(resId);
		}
		int dp23 = ResHelper.dipToPx(context, 23);
		lp = new LayoutParams(dp23, dp23);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		int dp10 = ResHelper.dipToPx(context, 10);
		int dp20 = ResHelper.dipToPx(context, 20);
		int dp5 = ResHelper.dipToPx(context, 5);
		lp.setMargins(dp5, 0, dp20, 0);
		rlItem.addView(ivMsgBg, lp);

		//评论工具栏的输入框
		llInputComs = new LinearLayout(context);
		int dp15 = ResHelper.dipToPx(context, 15);
		llInputComs.setOrientation(LinearLayout.HORIZONTAL);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_input_comment_white_bg");
		if (resId > 0) {
			llInputComs.setBackgroundResource(resId);
		}
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.LEFT_OF, 1);
		int dp7 = ResHelper.dipToPx(context, 7);
		lp.setMargins(dp15, dp7, dp15, dp7);
		rlItem.addView(llInputComs, lp);

		//输入框里面的图片和文字
		ivPan = new ImageView(context);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_input_comment_black_pan");
		if (resId > 0) {
			ivPan.setImageResource(resId);
		}
		LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(dp15, dp15);
		lllp.gravity = Gravity.CENTER_VERTICAL;
		lllp.leftMargin = dp20;
		llInputComs.addView(ivPan, lllp);
		
		tvInputComs = new TextView(context);
		tvInputComs.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		tvInputComs.setPadding(dp10, 0, 0, 0);
		tvInputComs.setTextColor(0xff222222);
		resId = ResHelper.getStringRes(context, "cmssdk_default_write_comments");
		if (resId > 0) {
			tvInputComs.setText(resId);
		}
		lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lllp.gravity = Gravity.CENTER_VERTICAL;
		llInputComs.addView(tvInputComs, lllp);

		//评论信息数
		tvComsCount = new TextView(context);
		tvComsCount.setTextColor(0xffffffff);
		tvComsCount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
		tvComsCount.setPadding(dp5, 0, dp5, 0);
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_comment_count_bg");
		if (resId > 0) {
			tvComsCount.setBackgroundResource(resId);
		}
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.topMargin = dp7;
		lp.rightMargin = dp15;
		rlItem.addView(tvComsCount, lp);
	}
	
	/** 在评论页隐藏评论数量的图标 */
	public void hideMsgView() {
		ivMsgBg.setVisibility(GONE);
		tvComsCount.setVisibility(GONE);
	}
	
	public void setCommentsCount(int count) {
		if (count == 0) {
			tvComsCount.setVisibility(GONE);
		} else {
			tvComsCount.setVisibility(VISIBLE);
			tvComsCount.setText(String.valueOf(count));
		}
	}
	
	/**修改评论工具栏的背景*/
	public void setBlackBackground() {
		line.setVisibility(GONE);
		//修改黑色背景
		rlItem.setBackgroundColor(0x00000000);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_input_comment_black_bg");
		if (resId > 0) {
			llInputComs.setBackgroundResource(resId);
		}
		//修改输入框的笔
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_input_comment_white_pan");
		if (resId > 0) {
			ivPan.setImageResource(resId);
		}
		//修改输入框的字体颜色
		tvInputComs.setTextColor(0xffffffff);
		//信息提示框的颜色
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_comment_white_msg");
		if (resId > 0) {
			ivMsgBg.setImageResource(resId);
		}
	}
	
}
