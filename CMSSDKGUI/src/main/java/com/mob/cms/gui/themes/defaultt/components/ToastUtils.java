package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.utils.ResHelper;

public class ToastUtils {

	/**
	 * 发送评论失败的提示
	 */
	public static void showSendComFail(Context context) {
		String sendSuc = "";
		int resId = ResHelper.getStringRes(context, "cmssdk_default_send_fail");
		if (resId > 0) {
			sendSuc = context.getString(resId);
		}
		show(context, sendSuc, 0, Toast.LENGTH_SHORT);
	}

	/**
	 * 发送评论成功的提示
	 */
	public static void showSendComSuc(Context context) {
		String sendSuc = "";
		int resId = ResHelper.getStringRes(context, "cmssdk_default_send_success");
		if (resId > 0) {
			sendSuc = context.getString(resId);
		}
		resId = ResHelper.getBitmapRes(context, "cmssdk_default_send_comment_suc_bg");
		show(context, sendSuc, resId, Toast.LENGTH_SHORT);
	}
	
	/**  */
	public static void show(Context context, int resId, int duration) {
		String text = "";
		if (resId > 0) {
			text = context.getString(resId);
		}
		show(context, text, 0, duration);
	}
	
	/**
	 * 发送评论的Dialog
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void show(Context context, CharSequence text, int imgResId, int duration) {
		//没有图片和文字时，不弹窗
		if (imgResId <= 0 && TextUtils.isEmpty(text)) {
			return;
		}
		RelativeLayout rlView = new RelativeLayout(context);
		int resId = ResHelper.getBitmapRes(context, "cmssdk_default_send_comment_toast_bg");
		if (resId > 0) {
			rlView.setBackgroundResource(resId);
		}
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		rlView.setLayoutParams(lp);
		
		//有图片时，显示
		if (imgResId > 0) {
			ImageView iv = new ImageView(context);
			iv.setId(1);
			int dp25 = ResHelper.dipToPx(context, 25);
			int dp45 = ResHelper.dipToPx(context, 45);
			iv.setPadding(dp45, dp25, dp45, 0);
			iv.setImageResource(imgResId);
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			rlView.addView(iv, lp);
		}
		
		//文字
		TextView tv = new TextView(context);
		tv.setText(text);
		tv.setTextColor(0xffffffff);
		int dp10 = ResHelper.dipToPx(context, 10);
		int dp23 = ResHelper.dipToPx(context, 23);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (imgResId > 0) { // 有图片时的配置
			lp.addRule(RelativeLayout.BELOW, 1);
			tv.setPadding(0, dp10, 0, dp23);
		} else {
			int dp45 = ResHelper.dipToPx(context, 45);
			tv.setPadding(dp45, dp23, dp45, dp23);
		}
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlView.addView(tv, lp);
		
		Toast toast = new Toast(context);
		toast.setView(rlView);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(duration);
		toast.show();
	}
}
