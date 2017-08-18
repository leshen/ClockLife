package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.pages.FriendRequestPage;
import com.mob.ums.gui.pages.MyFriendsPage;
import com.mob.ums.gui.pages.ProfilePage;
import com.mob.ums.gui.pages.SearchPage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.ums.gui.themes.defaultt.components.ItemUserView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.ArrayList;

public class MyFriendsPageAdapter extends DefaultThemePageAdapter<MyFriendsPage> {
	private PullToRequestView ptr;

	public void onCreate(final MyFriendsPage page, final Activity activity) {
		super.onCreate(page, activity);
		final LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);

		llPage.addView(new TitleView(getPage().getContext()) {
			protected void onReturn() {
				finish();
			}

			protected String getTitleResName() {
				return "umssdk_default_my_friends";
			}

			protected String getRightLabelResName() {
				return "umssdk_default_add";
			}

			protected void onRightClick() {
				SearchPage page = new SearchPage(getPage().getTheme());
				page.setUser(getPage().getUser());
				page.show(getPage().getContext(), null);
			}

			protected boolean isNoPadding() {
				return true;
			}
		}, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		ptr = new PullToRequestView(activity);
		ptr.setBackgroundColor(0xfff7f7f7);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		llPage.addView(ptr, lp);

		MyFriendsAdapter adapter = new MyFriendsAdapter(page, ptr);
		ptr.setAdapter(adapter);

		ptr.performPullingDown(true);
	}

	private static class MyFriendsAdapter extends DefaultRTRListAdapter implements OnItemClickListener {
		private static final int PAGE_SIZE = 20;

		private MyFriendsPage page;
		private ArrayList<String> requests;
		private boolean hasNews;
		private ArrayList<User> friends;
		private int offset;

		public MyFriendsAdapter(MyFriendsPage page, PullToRequestView view) {
			super(view);
			this.page = page;
			friends = new ArrayList<User>();
			getListView().setOnItemClickListener(this);
			getListView().setDividerHeight(0);
			requests = new ArrayList<String>();
		}

		public long getItemId(int position) {
			return position;
		}

		public Object getItem(int position) {
			return null;
		}

		public int getCount() {
			return 1 + (friends == null ? 0 : friends.size());
		}

		public int getItemViewType(int position) {
			return position == 0 ? 0 : 1;
		}

		public int getViewTypeCount() {
			return 2;
		}

		protected void onRequest(boolean firstPage) {
			final int fOffset = firstPage ? 0 : (offset + PAGE_SIZE);
			UMSSDK.getFrinds(page.getUser(), fOffset, PAGE_SIZE, new OperationCallback<ArrayList<User>>() {
				public void onSuccess(ArrayList<User> data) {
					if (fOffset == 0) {
						friends.clear();
					}
					if (data == null || data.isEmpty()) {
						getParent().lockPullingUp();
					} else {
						getParent().releasePullingUpLock();
						friends.addAll(data);
					}
					offset = fOffset;
					getUnprocessedRequest();
				}

				public void onFailed(Throwable t) {
					ErrorDialog.Builder builder = new ErrorDialog.Builder(page.getContext(), page.getTheme());
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_my_friends");
					builder.setTitle(getContext().getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
					notifyDataSetChanged();
				}
			});
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				return getViewType0(convertView, parent);
			} else {
				return getViewType1(position - 1, convertView, parent);
			}
		}

		private View getViewType0(View convertView, ViewGroup parent) {
			if (convertView == null) {
				LinearLayout ll = new LinearLayout(getContext());
				ll.setOrientation(LinearLayout.VERTICAL);
				convertView = ll;

				LinearLayout llBody = new LinearLayout(getContext());
				llBody.setBackgroundColor(0xffffffff);
				int dp15 = ResHelper.dipToPx(getContext(), 15);
				llBody.setPadding(dp15, 0, dp15, 0);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(getContext(), 75));
				ll.addView(llBody, lp);

				View v = new View(getContext());
				v.setBackgroundResource(ResHelper.getBitmapRes(getContext(), "umssdk_default_find_friend"));
				int dp52 = ResHelper.dipToPx(getContext(), 52);
				lp = new LayoutParams(dp52, dp52);
				lp.gravity = Gravity.CENTER_VERTICAL;
				int dp10 = ResHelper.dipToPx(getContext(), 10);
				lp.rightMargin = dp10;
				llBody.addView(v, lp);

				TextView tv = new TextView(getContext());
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				tv.setTextColor(0xff3b3947);
				tv.setGravity(Gravity.CENTER_VERTICAL);
				tv.setText(ResHelper.getStringRes(getContext(), "umssdk_default_new_friends"));
				lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER_VERTICAL;
				lp.weight = 1;
				lp.rightMargin = dp10;
				llBody.addView(tv, lp);

				LinearLayout llRequest = new LinearLayout(getContext());
				lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER_VERTICAL;
				int dp5 = ResHelper.dipToPx(getContext(), 5);
				lp.rightMargin = dp5;
				llBody.addView(llRequest, lp);

				tv = new TextView(getContext());
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				tv.setTextColor(0xff969696);
				tv.setGravity(Gravity.CENTER_VERTICAL);
				lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER_VERTICAL;
				lp.rightMargin = ResHelper.dipToPx(getContext(), 4);
				lp.topMargin = lp.rightMargin / 4;
				lp.bottomMargin = lp.rightMargin / 4;
				llRequest.addView(tv, lp);

				v = new View(getContext());
				v.setBackgroundResource(ResHelper.getBitmapRes(getContext(), "umssdk_defalut_new_request"));
				lp = new LayoutParams(dp5, dp5);
				llRequest.addView(v, lp);
				v.setVisibility(View.INVISIBLE);

				ImageView ivNext = new ImageView(getContext());
				ivNext.setImageResource(ResHelper.getBitmapRes(getContext(), "umssdk_default_go_details"));
				lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER_VERTICAL;
				llBody.addView(ivNext, lp);

				View vSep = new View(getContext());
				lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp10);
				ll.addView(vSep, lp);
			}

			LinearLayout ll = (LinearLayout) convertView;
			LinearLayout llBody = (LinearLayout) ll.getChildAt(0);
			LinearLayout llRequest = (LinearLayout) llBody.getChildAt(2);
			TextView tv = (TextView) llRequest.getChildAt(0);
			tv.setText(String.valueOf(requests.size()));
			View v = llRequest.getChildAt(1);
			if (hasNews) {
				v.setVisibility(View.VISIBLE);
				tv.setVisibility(View.VISIBLE);
			} else {
				v.setVisibility(View.GONE);
				tv.setVisibility(View.GONE);
			}
			return convertView;
		}

		private View getViewType1(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LinearLayout ll = new LinearLayout(getContext());
				ll.setOrientation(LinearLayout.VERTICAL);
				convertView = ll;

				ItemUserView view = new ItemUserView(getContext());
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				ll.addView(view, lp);

				View vSep = new View(getContext());
				int resid = ResHelper.getBitmapRes(getContext(), "umssdk_defalut_list_sep");
				vSep.setBackground(getContext().getResources().getDrawable(resid));
				lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
				ll.addView(vSep, lp);
			}

			User user = friends.get(position);
			LinearLayout ll = (LinearLayout) convertView;
			ItemUserView view = (ItemUserView) ll.getChildAt(0);
			view.setUser(user);
			return convertView;
		}

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position == 0) {
				hasNews = false;
				notifyDataSetChanged();
				FriendRequestPage page = new FriendRequestPage(this.page.getTheme());
				page.show(this.page.getContext(), null);
			} else {
				ProfilePage page = new ProfilePage(this.page.getTheme());
				page.setUser(friends.get(position - 1));
				page.show(getContext(), null);
			}
		}

		private void getUnprocessedRequest() {
			UMSSDK.getNewFriendsCount( new OperationCallback<ArrayList<String>>() {
				@Override
				public void onSuccess(ArrayList<String> data) {
					super.onSuccess(data);
					requests.clear();
					requests.addAll(data);
					if (data != null && !data.isEmpty()) {
						hasNews = true;
					} else {
						hasNews=false;
					}
					notifyDataSetChanged();
				}

				@Override
				public void onFailed(Throwable t) {
					super.onFailed(t);
					notifyDataSetChanged();
					ErrorDialog.Builder builder = new ErrorDialog.Builder(page.getContext(), page.getTheme());
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_my_friends");
					builder.setTitle(getContext().getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
				}
			});
		}
	}

}
