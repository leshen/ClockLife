package com.mob.cms.gui.themes.defaultt;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

import com.mob.cms.gui.dialog.ProgressDialog;
import com.mob.jimu.gui.DialogAdapter;
import com.mob.tools.utils.ResHelper;

public class ProgressDialogAdapter extends DialogAdapter<ProgressDialog> {
	
	public void onCreate(ProgressDialog dialog, Bundle savedInstanceState) {
		Context context = dialog.getContext();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.CENTER;
		int dp50 = ResHelper.dipToPx(context, 50);
		wlp.width = context.getResources().getDisplayMetrics().widthPixels - dp50 * 2;
	}
	
	public void init(ProgressDialog dialog) {
		Context context = dialog.getContext();
		FrameLayout fl = new FrameLayout(context);
		LayoutParams lpfl = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.setContentView(fl, lpfl);
		
		ProgressBar pb = new ProgressBar(context);
		int dp10 = ResHelper.dipToPx(context, 10);
		pb.setPadding(dp10, dp10, dp10, dp10);
		int dp80 = ResHelper.dipToPx(context, 80);
		lpfl = new LayoutParams(dp80, dp80);
		lpfl.gravity = Gravity.CENTER;
		fl.addView(pb);
	}
	
}
