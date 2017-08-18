package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.SocialNetwork;
import com.mob.ums.UMSSDK;
import com.mob.ums.gui.pages.BindPage;
import com.mob.ums.gui.pages.RegisterPage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BindPageAdapter extends DefaultThemePageAdapter<BindPage> implements OnClickListener {
	private HashMap<Integer, LinearLayout> idToView;
	private HashMap<View, SocialNetwork> viewToPlatform;
	private ProgressDialog pd;
	
	public void onCreate(BindPage page, Activity activity) {
		super.onCreate(page, activity);
		initPage(activity);
	}
	
	private void initPage(Activity activity) {
		LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);
		
		// title
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(new TitleView(activity) {
			protected void onReturn() {
				finish();
			}
			
			protected String getTitleResName() {
				return "umssdk_default_bind_social_platform";
			}
		}, lp);
		
		// platforms
		ArrayList<SocialNetwork> sns = new ArrayList<SocialNetwork>();
		sns.addAll(Arrays.asList(UMSSDK.getAvailableSocialLoginWays()));
		sns.add(0, null);
		idToView = new HashMap<Integer, LinearLayout>();
		viewToPlatform = new HashMap<View, SocialNetwork>();
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 43));
		for (int i = 0; i < sns.size(); i++) {
			int resId;
			if (sns.get(i) == null) {
				resId = ResHelper.getStringRes(activity, "umssdk_default_phone_number");
			} else {
				resId = ResHelper.getStringRes(activity, "umssdk_default_" + sns.get(i).getName().toLowerCase());
			}
			String title = activity.getString(resId);
			LinearLayout llItem = getItemView(activity, title);
			llPage.addView(llItem, lp);
			if (sns.get(i) == null) {
				idToView.put(0, llItem);
			} else {
				idToView.put(sns.get(i).getId(), llItem);
			}
			viewToPlatform.put(llItem, sns.get(i));
		}
		
		for (Integer id : idToView.keySet()) {
			LinearLayout llItem = idToView.get(id);
			TextView tv = (TextView) llItem.findViewById(1);
			if (getPage().getPlatforms().containsKey(id)) {
				tv.setTextColor(0xffe4554c);
				tv.setText(ResHelper.getStringRes(getPage().getContext(), "umssdk_default_binded"));
				tv.setBackgroundColor(0x00ffffff);
			} else {
				llItem.setOnClickListener(this);
			}
		}
	}
	
	public LinearLayout getItemView(Activity activity, String title) {
		LinearLayout llItem = new LinearLayout(activity);
		llItem.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout ll = new LinearLayout(activity);
		int dp15 = ResHelper.dipToPx(activity, 15);
		ll.setPadding(dp15, 0, dp15, 0);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		llItem.addView(ll, lp);
		
		TextView tv = new TextView(activity);
		tv.setTextColor(0xff3b3947);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setText(title);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 1;
		ll.addView(tv, lp);
		
		tv = new TextView(activity);
		tv.setId(1);
		tv.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_default_bind_platform"));
		tv.setTextColor(0xff979797);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_bind"));
		tv.setGravity(Gravity.CENTER);
		int dp8 = ResHelper.dipToPx(activity, 8);
		tv.setPadding(dp8, 0, dp8, 0);
		int dp23 = ResHelper.dipToPx(activity, 23);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, dp23);
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(tv, lp);
		
		View vSep = new View(activity);
		int resid = ResHelper.getBitmapRes(activity, "umssdk_defalut_list_sep");
		vSep.setBackground(activity.getResources().getDrawable(resid));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		llItem.addView(vSep, lp);
		
		return llItem;
	}
	
	public void onClick(final View v) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
		SocialNetwork sn = viewToPlatform.get(v);
		if (sn == null) {
			RegisterPage page = new RegisterPage(getPage().getTheme());
			page.setBindPhoneType();
			page.showForResult(v.getContext(), null, new FakeActivity() {
				public void onResult(HashMap<String, Object> data) {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					if (data != null) {
						TextView tv = (TextView) v.findViewById(1);
						tv.setTextColor(0xffe4554c);
						tv.setText(ResHelper.getStringRes(getPage().getContext(), "umssdk_default_binded"));
						tv.setBackgroundColor(0x00ffffff);
						v.setOnClickListener(null);
					}
				}
			});
		} else {
			UMSSDK.bindSocialAccount(sn, new OperationCallback<Void>() {
				public void onSuccess(Void data) {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					getPage().setResult(new HashMap<String, Object>());
					TextView tv = (TextView) v.findViewById(1);
					tv.setTextColor(0xffe4554c);
					tv.setText(ResHelper.getStringRes(getPage().getContext(), "umssdk_default_binded"));
					tv.setBackgroundColor(0x00ffffff);
					v.setOnClickListener(null);
				}
				
				public void onCancel() {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
				}
				
				public void onFailed(Throwable t) {
//					t.printStackTrace();
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					Context cxt = getPage().getContext();
					ErrorDialog.Builder builder = new ErrorDialog.Builder(cxt, getPage().getTheme());
					int resId = ResHelper.getStringRes(cxt, "umssdk_default_bind_social_platform");
					builder.setTitle(cxt.getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
				}
			});
		}
	}
}
