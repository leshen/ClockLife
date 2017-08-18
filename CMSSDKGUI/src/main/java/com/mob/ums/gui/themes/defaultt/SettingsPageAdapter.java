package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.gui.pages.ChangePasswordPage;
import com.mob.ums.gui.pages.FansPage;
import com.mob.ums.gui.pages.FansPage.PageType;
import com.mob.ums.gui.pages.SearchPage;
import com.mob.ums.gui.pages.SettingsPage;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.HashMap;

public class SettingsPageAdapter extends DefaultThemePageAdapter<SettingsPage> {
	private ProgressDialog pd;
	
	public void onCreate(SettingsPage page, Activity activity) {
		super.onCreate(page, activity);
		initPage(activity);
	}
	
	private void initPage(Activity activity) {
		final LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);
		
		// title
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(new TitleView(activity) {
			protected void onReturn() {
				finish();
			}
			
			protected String getTitleResName() {
				return "umssdk_default_setting";
			}
			
			protected boolean isNoPadding() {
				return true;
			}
		}, lp);
		
		LinearLayout llBody = new LinearLayout(activity);
		llBody.setOrientation(LinearLayout.VERTICAL);
		llBody.setBackgroundColor(0xfff7f7f7);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		llPage.addView(llBody, lp);
		
		View vsep = new View(activity);
		int dp10 = ResHelper.dipToPx(activity, 10);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp10);
		llBody.addView(vsep, lp);
		
		// change password
		final LinearLayout llChangePsw = new LinearLayout(activity);
		llChangePsw.setBackgroundColor(0xffffffff);
		int dp15 = ResHelper.dipToPx(activity, 15);
		llChangePsw.setPadding(dp15, 0, dp15, 0);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 45));
		llBody.addView(llChangePsw, lp);
		llChangePsw.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangePasswordPage page = new ChangePasswordPage(getPage().getTheme());
				page.show(getPage().getContext(), null);
			}
		});
		llChangePsw.setVisibility(View.GONE);
		
		final View v = new View(activity);
		int res = ResHelper.getBitmapRes(activity, "umssdk_defalut_list_sep");
		v.setBackground(activity.getResources().getDrawable(res));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 1));
		llBody.addView(v, lp);
		v.setVisibility(View.GONE);
		
		TextView tv = new TextView(activity);
		tv.setTextColor(0xff3b3947);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_change_password"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_VERTICAL;
		llChangePsw.addView(tv, lp);
		
		ImageView iv = new ImageView(activity);
		iv.setImageResource(ResHelper.getBitmapRes(activity, "umssdk_default_go_details"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llChangePsw.addView(iv, lp);
		
		// block list
		final LinearLayout llBlockList = new LinearLayout(activity);
		llBlockList.setBackgroundColor(0xffffffff);
		llBlockList.setPadding(dp15, 0, dp15, 0);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 45));
		llBody.addView(llBlockList, lp);
		llBlockList.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FansPage page = new FansPage(getPage().getTheme());
				page.setUser(getPage().getUser());
				page.showPage(PageType.BLOCKINGS);
			}
		});
		
		tv = new TextView(activity);
		tv.setTextColor(0xff3b3947);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_black"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_VERTICAL;
		llBlockList.addView(tv, lp);
		
		iv = new ImageView(activity);
		iv.setImageResource(ResHelper.getBitmapRes(activity, "umssdk_default_go_details"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llBlockList.addView(iv, lp);
		
		View vSep = new View(activity);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 25));
		llBody.addView(vSep, lp);
		
		// logout
		tv = new TextView(activity);
		tv.setBackgroundColor(0xffffffff);
		tv.setTextColor(0xff3b3947);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_logout"));
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 45));
		llBody.addView(tv, lp);
		tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UMSSDK.logout(null);
				getPage().setResult(new HashMap<String, Object>());
				finish();
			}
		});
		
		// check bind phone
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
		UMSSDK.isBindPhone(new OperationCallback<Boolean>() {
			public void onSuccess(Boolean data) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				if (data) {
					llChangePsw.setVisibility(View.VISIBLE);
					v.setVisibility(View.VISIBLE);
				}
			}
			
			public void onFailed(Throwable t) {
//				t.printStackTrace();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}
		});
		
	}
	
}
