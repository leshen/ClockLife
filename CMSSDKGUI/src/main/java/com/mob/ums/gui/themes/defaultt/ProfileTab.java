package com.mob.ums.gui.themes.defaultt;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.jimu.gui.Theme;
import com.mob.tools.FakeActivity;
import com.mob.tools.gui.PullToRequestAdatper;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.gui.Scrollable;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;
import com.mob.ums.gui.pages.DetailPage;
import com.mob.ums.gui.pages.MainPage;
import com.mob.ums.gui.pages.MyFriendsPage;
import com.mob.ums.gui.pages.SettingsPage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.tabs.Tab;
import com.mob.ums.gui.themes.defaultt.components.HeaderFooterProvider;
import com.mob.ums.gui.themes.defaultt.components.ScrollableRelativeLayout;
import com.mob.ums.gui.themes.defaultt.components.UserBriefView;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileTab implements Tab {
	private MainPage page;
	private Theme theme;
	private PullToRequestView ptr;
	private ProfileAdapter adapter;
	private ProgressDialog pd;
	
	public ProfileTab(MainPage page) {
		this.page = page;
		this.theme = page.getTheme();
	}
	
	public String getTitleResName() {
		return "umssdk_default_my";
	}
	
	public int getSelectedTitleColor() {
		return 0xffe4554c;
	}
	
	public int getUnselectedTitleColor() {
		return 0xff999999;
	}
	
	public String getSelectedIconResName() {
		return "umssdk_default_tab_profile_sel";
	}
	
	public String getUnselectedIconResName() {
		return "umssdk_default_tab_profile_unsel";
	}
	
	public View getTabView(final Context context) {
		if (ptr == null) {
			ptr = new PullToRequestView(context);
			ptr.setBackgroundColor(0xffea6860);
			adapter = new ProfileAdapter(this, ptr);
			ptr.setAdapter(adapter);
			if (page.getUser() == null) {
				getUserInfo(context, true);
			} else {
				adapter.notifyDataSetChanged();
			}
		}
		return ptr;
	}
	
	private void getUserInfo(final Context context, final boolean showPd) {
		if (TextUtils.isEmpty(UMSSDK.getLoginUserToken())) {
			UMSGUI.showLogin(new OperationCallback<User>() {
				public void onSuccess(User data) {
					getUnprocessedRequest(context,data,showPd);
				}

				public void onFailed(Throwable t) {
//					t.printStackTrace();
					ErrorDialog.Builder builder = new ErrorDialog.Builder(context, theme);
					int resId = ResHelper.getStringRes(context, "umssdk_default_user_profile");
					builder.setTitle(context.getString(resId));
					builder.setMessage(t.getMessage());
					builder.setThrowable(t);
					builder.show();
				}
			});
			adapter.notifyDataSetChanged();
		} else {
			if (showPd) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				pd = new ProgressDialog.Builder(context, theme).show();
			}
			UMSSDK.getLoginUser(new OperationCallback<User>() {
				public void onSuccess(User data) {
					getUnprocessedRequest(context,data,showPd);
				}

				public void onFailed(Throwable t) {
//					t.printStackTrace();
					if (showPd && pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					adapter.notifyDataSetChanged();
					ErrorDialog.Builder builder = new ErrorDialog.Builder(context, theme);
					int resId = ResHelper.getStringRes(context, "umssdk_default_user_profile");
					builder.setTitle(context.getString(resId));
					builder.setMessage(t.getMessage());
					builder.setThrowable(t);
					builder.show();
				}
			});
		}
	}

	private void getUnprocessedRequest(final Context context, final User user, final boolean showPd) {
		UMSSDK.getNewFriendsCount( new OperationCallback<ArrayList<String>>() {
			@Override
			public void onSuccess(ArrayList<String> data) {
				super.onSuccess(data);
				if (showPd && pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				page.setUser(user);
				if(data != null && !data.isEmpty()){
					adapter.setHasNew(true);
				} else {
					adapter.setHasNew(false);
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailed(Throwable t) {
				super.onFailed(t);
				if (showPd && pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				page.setUser(user);
				adapter.notifyDataSetChanged();
				ErrorDialog.Builder builder = new ErrorDialog.Builder(page.getContext(), page.getTheme());
				int resId = ResHelper.getStringRes(context, "umssdk_default_my_friends");
				builder.setTitle(context.getString(resId));
				builder.setThrowable(t);
				builder.setMessage(t.getMessage());
				builder.show();
			}
		});
	}
	
	private void goMyFriends(Context context) {
		if (page.getUser() != null) {
			MyFriendsPage page = new MyFriendsPage(theme);
			page.setUser(this.page.getUser());
			page.show(context, null);
		}
	}
	
	private void goSettings(final Context context) {
		if (page.getUser() != null) {
			SettingsPage sPage = new SettingsPage(theme);
			sPage.setUser(page.getUser());
			sPage.showForResult(context, null, new FakeActivity() {
				public void onResult(HashMap<String, Object> data) {
					if (data != null) {
						UMSGUI.showLogin(new OperationCallback<User>() {
							public void onSuccess(User data) {
								MainPage page = new MainPage(theme);
								page.setUser(data);
								page.showForProfile();
							}
							
							public void onFailed(Throwable t) {
//								t.printStackTrace();
								ErrorDialog.Builder builder = new ErrorDialog.Builder(context, theme);
								int resId = ResHelper.getStringRes(context, "umssdk_default_user_profile");
								builder.setTitle(context.getString(resId));
								builder.setMessage(t.getMessage());
								builder.setThrowable(t);
								builder.show();
							}
						});
						ProfileTab.this.page.finish();
					}
				}
			});
		}
	}
	
	private void goDetails(final Context context) {
		if (page.getUser() != null) {
			DetailPage page = new DetailPage(theme);
			page.setUser(this.page.getUser());
			page.showForResult(context, null, new FakeActivity() {
				public void onResult(HashMap<String, Object> data) {
					// refresh user info
					adapter.notifyDataSetChanged();
				}
			});
		}
	}
	
	public void onSelected() {
		
	}
	
	public void onUnselected() {
		
	}
	
	private static class ProfileAdapter extends PullToRequestAdatper {
		private ProfileTab tab;
		private ScrollableRelativeLayout bodyView;
		private HeaderFooterProvider hfProvider;
		private LinearLayout llPage;
		private UserBriefView<MainPage> header;
		private TextView tvMyFriends;
		private View vNew;
		private boolean hasNew;
		
		public ProfileAdapter(ProfileTab tab, PullToRequestView view) {
			super(view);
			this.tab = tab;
			hfProvider = new HeaderFooterProvider(view.getContext());
			hfProvider.setWhiteStyle();
			bodyView = new ScrollableRelativeLayout(view.getContext());
			view.lockPullingUp();
			
			// 这里加入一个无用的listview，我发现不加入就不能按住空白处下来，可能下拉刷新控件有bug
			ListView lv = new ListView(view.getContext());
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			bodyView.addView(lv, lp);
			
			initPage(view.getContext());
			bodyView.addView(llPage, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		
		private void initPage(Context context) {
			llPage = new LinearLayout(context);
			llPage.setOrientation(LinearLayout.VERTICAL);
			
			// user brief
			header = new UserBriefView<MainPage>(tab.page) {
				protected void goSettings(Context context) {
					tab.goSettings(context);
				}
				
				protected void onReturn() {
					
				}
			};
			header.setTabMob();
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 215));
			llPage.addView(header, lp);
			
			LinearLayout llBody = new LinearLayout(context);
			llBody.setOrientation(LinearLayout.VERTICAL);
			llBody.setBackgroundColor(0xfff7f7f7);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			llPage.addView(llBody, lp);
			
			// sep
			View vsep = new View(context);
			int dp10 = ResHelper.dipToPx(context, 10);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp10);
			llBody.addView(vsep, lp);
			
			// my friends
			LinearLayout ll = new LinearLayout(context);
			ll.setBackgroundColor(0xffffffff);
			int dp15 = ResHelper.dipToPx(context, 15);
			ll.setPadding(dp15, 0, dp15, 0);
			int dp45 = ResHelper.dipToPx(context, 43);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp45);
			llBody.addView(ll, lp);
			ll.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					tab.goMyFriends(v.getContext());
				}
			});
			
			TextView tv = new TextView(context);
			tv.setTextColor(0xff3b3947);
			tv.setText(ResHelper.getStringRes(context, "umssdk_default_my_friends"));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			lp.gravity = Gravity.CENTER_VERTICAL;
			ll.addView(tv, lp);
			
			tvMyFriends = new TextView(context);
			tvMyFriends.setTextColor(0xff969696);
			tvMyFriends.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_VERTICAL;
			ll.addView(tvMyFriends, lp);

			vNew = new View(getContext());
			vNew.setBackgroundResource(ResHelper.getBitmapRes(getContext(), "umssdk_defalut_new_request"));
			int dp5 = ResHelper.dipToPx(context, 5);
			lp = new LayoutParams(dp5, dp5);
			int dp8 = ResHelper.dipToPx(context, 8);
			int dp2 = ResHelper.dipToPx(context, 2);
			int dp3 = ResHelper.dipToPx(context, 3);
			lp.topMargin=-dp8;
			lp.leftMargin=dp2;
			lp.rightMargin=dp2;
			lp.gravity=Gravity.CENTER_VERTICAL;
			ll.addView(vNew, lp);
			vNew.setVisibility(View.INVISIBLE);
			
			ImageView iv = new ImageView(context);
			iv.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_go_details"));
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_VERTICAL;
			ll.addView(iv, lp);
			
			vsep = new View(context);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 10));
			llBody.addView(vsep, lp);
			
			// details of profile
			ll = new LinearLayout(context);
			ll.setBackgroundColor(0xffffffff);
			ll.setPadding(dp15, 0, dp15, 0);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp45);
			llBody.addView(ll, lp);
			ll.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					tab.goDetails(v.getContext());
				}
			});
			
			tv = new TextView(context);
			tv.setTextColor(0xff3b3947);
			tv.setText(ResHelper.getStringRes(context, "umssdk_default_user_profile"));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			lp.gravity = Gravity.CENTER_VERTICAL;
			ll.addView(tv, lp);
			
			tv = new TextView(context);
			tv.setTextColor(0xff969696);
			tv.setText("13");
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_VERTICAL;
			lp.rightMargin = dp5;
			ll.addView(tv, lp);

			iv = new ImageView(context);
			iv.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_go_details"));
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_VERTICAL;
			ll.addView(iv, lp);
		}

		public void setHasNew(boolean hasNew){
			this.hasNew=hasNew;
		}

		public View getHeaderView() {
			return hfProvider.getHeaderView();
		}
		
		public Scrollable getBodyView() {
			return bodyView;
		}
		
		public void onPullDown(int percent) {
			hfProvider.onPullDown(percent);
		}
		
		public void onRefresh() {
			hfProvider.onRefresh();
			tab.getUserInfo(getContext(), false);
		}
		
		public void notifyDataSetChanged() {
			User me = tab.page.getUser();
			super.notifyDataSetChanged();
			if(me == null){
				return;
			}
			header.setUser(me);
			tvMyFriends.setText(String.valueOf(me.friends.isNull() ? "" : me.friends.get()));
			if(hasNew){
				vNew.setVisibility(View.VISIBLE);
			} else {
				vNew.setVisibility(View.INVISIBLE);
			}
		}
		
		public View getFooterView() {
			return hfProvider.getFooterView();
		}
		
		public void onReversed() {
			hfProvider.onReversed();
		}
		
		public boolean isPullDownReady() {
			return true;
		}
		
		public boolean isPullUpReady() {
			return false;
		}
	
	}
	
}
