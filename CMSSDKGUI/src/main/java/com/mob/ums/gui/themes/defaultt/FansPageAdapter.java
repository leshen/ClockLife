package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.pages.FansPage;
import com.mob.ums.gui.pages.FansPage.PageType;
import com.mob.ums.gui.pages.ProfilePage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.OKDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.ums.gui.themes.defaultt.components.ItemUserView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.ArrayList;
import java.util.List;

public class FansPageAdapter extends DefaultThemePageAdapter<FansPage> {
	private PullToRequestView pullView;
	private FansListAdapter fansListAdapter;
	private String titleResName;
	private User user;

	@Override
	public void onCreate(FansPage page, Activity activity) {
		user = page.getUser();
		initPage(activity);
		initList();
	}

	private void initPage(Activity activity) {
		LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		llPage.setBackgroundColor(0xffffffff);
		activity.setContentView(llPage);

		//title
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llPage.addView(new TitleView(activity) {
			@Override
			protected boolean isNoPadding() {
				return true;
			}

			protected void onReturn() {
				finish();
			}

			protected String getTitleResName() {
				titleResName = initTitle(getPage().getFanPage());
				return titleResName;
			}
		}, lp);

		//列表
		pullView = new PullToRequestView(activity);
		pullView.setBackgroundColor(0xfff7f7f7);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
				.WRAP_CONTENT);
		lp.weight = 1;
		llPage.addView(pullView, lp);

		fansListAdapter = new FansListAdapter(user, pullView, getPage().getFanPage(), titleResName);
		pullView.setAdapter(fansListAdapter);
	}

	private String initTitle(PageType fanPage) {
		switch (fanPage) {
			case FANS:
				if (!UMSSDK.isMe(user)) {
					return fanPage.TFANS.getResName();
				}
			return fanPage.FANS.getResName();
			case FOLLOWINGS:
				if (!UMSSDK.isMe(user)) {
					return fanPage.TFOLLOWINGS.getResName();
				}
			return fanPage.FOLLOWINGS.getResName();
			case R_FRIENDS:
			return fanPage.R_FRIENDS.getResName();
			case BLOCKINGS:
			return fanPage.BLOCKINGS.getResName();
		}
		return "";
	}

	private void initList() {
		if (user != null) {
			pullView.performPullingDown(true);
		} else {
			fansListAdapter.notifyDataSetChanged();
		}
	}

	private class FansListAdapter extends DefaultRTRListAdapter {
		private List<User> userList = new ArrayList<User>();
		private PageType fanPage;
		private String titleResName;
		private static final int PAGE_SIZE = 20;
		private int pageIndex = 0;
		private User user;
		private FansOperationCallback fansOperationCallback;

		public FansListAdapter(User user, PullToRequestView view, PageType fanPage, String titleResName) {
			super(view);
			this.fanPage = fanPage;
			this.titleResName = titleResName;
			this.user = user;
			fansOperationCallback = new FansOperationCallback();
		}

		@Override
		protected void onRequest(boolean firstPage) {
			if (firstPage) {
				pageIndex = 0;
			}
			getUserList();
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				convertView = new ItemUserView(parent.getContext());
			}
			ItemUserView itemUserView = (ItemUserView) convertView;
			itemUserView.clearAction();
			final User user = userList.get(position);
			itemUserView.setUser(user);
			if (getPage().getFanPage().getCode() == PageType.BLOCKINGS.getCode()) {
				itemUserView.addAction("umssdk_default_stop_block", 0xff969696, "umssdk_default_remove_black_btn",
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								removeBlack(user);
							}
						});
			}
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ProfilePage profilePage = new ProfilePage(getPage().getTheme());
					profilePage.setUser(user);
					profilePage.show(getContext(), null);
				}
			});
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return userList.get(position);
		}

		@Override
		public int getCount() {
			return userList == null ? 0 : userList.size();
		}

		private void getUserList() {
			switch (fanPage) {
				case FANS:
					UMSSDK.getFans(user, pageIndex * PAGE_SIZE, PAGE_SIZE, fansOperationCallback);
				break;
				case FOLLOWINGS:
					UMSSDK.getFollowings(user, pageIndex * PAGE_SIZE, PAGE_SIZE, fansOperationCallback);
				break;
				case R_FRIENDS:
					UMSSDK.getRFriends(user, pageIndex * PAGE_SIZE, PAGE_SIZE, fansOperationCallback);
				break;
				case BLOCKINGS:
					UMSSDK.getBlockings(user, pageIndex * PAGE_SIZE, PAGE_SIZE, fansOperationCallback);
				break;
			}
		}

		private void removeBlack(final User user) {
			UMSSDK.stopBlock(user, new OperationCallback<Void>() {
				@Override
				public void onSuccess(Void data) {
					super.onSuccess(data);
					final OKDialog.Builder builder = new OKDialog.Builder(getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_stop_block");
					builder.setTitle(getContext().getString(resId));
					resId = ResHelper.getStringRes(getContext(), "umssdk_default_stop_block_success");
					builder.setMessage(getContext().getString(resId));
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							userList.remove(user);
							notifyDataSetChanged();
							dialog.dismiss();
						}
					});
					builder.show();
				}

				@Override
				public void onFailed(Throwable t) {
					super.onFailed(t);
					ErrorDialog.Builder builder = new ErrorDialog.Builder(getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_stop_block");
					builder.setTitle(getContext().getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
				}
			});
		}


		private class FansOperationCallback extends OperationCallback<ArrayList<User>> {
			@Override
			public void onSuccess(ArrayList<User> data) {
				super.onSuccess(data);
				if (pageIndex == 0) {
					userList.clear();
				}
				userList.addAll(data);
				notifyDataSetChanged();
				if (data == null || data.isEmpty()) {
					getParent().lockPullingUp();
				} else {
					pageIndex++;
					getParent().releasePullingUpLock();
				}
			}

			@Override
			public void onFailed(Throwable t) {
				super.onFailed(t);

				getParent().stopPulling();
				ErrorDialog.Builder builder = new ErrorDialog.Builder(getContext(), getPage().getTheme());
				int resId = ResHelper.getStringRes(getContext(), titleResName);
				builder.setTitle(getContext().getString(resId));
				builder.setThrowable(t);
				builder.setMessage(t.getMessage());
				builder.show();
			}
		}
	}
}

