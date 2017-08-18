package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.FriendRequest;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.pages.FriendRequestPage;
import com.mob.ums.gui.pages.ProfilePage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.ums.gui.themes.defaultt.components.ItemUserView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FriendRequestPageAdapter extends DefaultThemePageAdapter<FriendRequestPage> {
	private PullToRequestView pullView;
	private FriendRequestListAdapter friendRequestListAdapter;

	public void onCreate(FriendRequestPage page, Activity activity) {
		super.onCreate(page, activity);
		final LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);

		llPage.addView(new TitleView(getPage().getContext()) {
			protected void onReturn() {
				finish();
			}

			protected String getTitleResName() {
				return "umssdk_default_new_friends";
			}

			protected boolean isNoPadding() {
				return true;
			}
		}, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		pullView = new PullToRequestView(activity);
		pullView.setBackgroundColor(0xfff7f7f7);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
				.WRAP_CONTENT);
		lp.weight = 1;
		llPage.addView(pullView, lp);

		friendRequestListAdapter = new FriendRequestListAdapter(pullView);
		pullView.setAdapter(friendRequestListAdapter);

		pullView.performPullingDown(true);
	}

	private class FriendRequestListAdapter extends DefaultRTRListAdapter {
		private Set<User> users;
		private ArrayList<FriendRequest> friendRequests;
		private HashMap<String, User> userMap = new HashMap<String, User>();
		private static final int PAGE_SIZE = 20;
		private int pageIndex = 0;
		private ProgressDialog pd;

		public FriendRequestListAdapter(PullToRequestView view) {
			super(view);
			users = new HashSet<User>();
			friendRequests = new ArrayList<FriendRequest>();
		}

		@Override
		protected void onRequest(boolean firstPage) {
			if (firstPage) {
				pageIndex = 0;
			}
			getAddFriendRequests();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new ItemUserView(parent.getContext());
			}
			ItemUserView itemUserView = (ItemUserView) convertView;
			itemUserView.clearAction();
			final FriendRequest friendRequest = friendRequests.get(position);
			final User user = userMap.get(friendRequest.requesterId.get());
			if (user == null){
				return convertView;
			}
			itemUserView.setUser(user);
			itemUserView.hideGender();
			itemUserView.hideLocation();
			String requestMessage=friendRequest.message.get();
			if(requestMessage != null && requestMessage.length() > 8){
				requestMessage = requestMessage.substring(0,8);
			}
			itemUserView.setSignature(requestMessage);
			itemUserView.clearAction();

			switch (friendRequest.status.get()) {
				case ACCEPT:
					itemUserView.addAction("umssdk_default_request_accepted", 0xff969696, null, null);
				break;
				case UNPROCESSED:
					itemUserView.addAction("umssdk_default_request_accept", 0xffffffff,
							"umssdk_default_request_accept_btn", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									replyFriendRequest(position, friendRequest.requesterId.get(), true);
								}
							});
					itemUserView.addAction("umssdk_default_request_refuse", 0xff969696,
							"umssdk_default_remove_black_btn", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									replyFriendRequest(position, friendRequest.requesterId.get(), false);
								}
							});
				break;
				case REFUSE:
					itemUserView.addAction("umssdk_default_request_refused", 0xff969696, null, null);
				break;
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
		public long getItemId(int i) {
			return i;
		}

		@Override
		public Object getItem(int i) {
			return friendRequests.get(i);
		}

		@Override
		public int getCount() {
			return friendRequests == null ? 0 : friendRequests.size();
		}

		private void getAddFriendRequests() {
			UMSSDK.getAddFriendRequests(pageIndex * PAGE_SIZE, PAGE_SIZE, new
					OperationCallback<ArrayList<FriendRequest>>() {
						@Override
						public void onSuccess(ArrayList<FriendRequest> data) {
							super.onSuccess(data);
							if (pageIndex == 0) {
								friendRequests.clear();
								users.clear();
								userMap.clear();
							}
							friendRequests.addAll(data);
							getUsers(data);
						}

						@Override
						public void onFailed(Throwable t) {
							super.onFailed(t);
							getParent().stopPulling();
							ErrorDialog.Builder builder = new ErrorDialog.Builder(getContext(), getPage().getTheme());
							int resId = ResHelper.getStringRes(getContext(), "umssdk_default_new_friends");
							builder.setTitle(getContext().getString(resId));
							builder.setThrowable(t);
							builder.setMessage(t.getMessage());
							builder.show();
						}
					});

		}

		private void getUsers(final ArrayList<FriendRequest> friendRequests) {
			if (friendRequests == null) {
				return;
			}
			String[] ids = new String[friendRequests.size()];
			for (int i = 0; i < friendRequests.size(); i++) {
				ids[i] = friendRequests.get(i).requesterId.get();
			}
			UMSSDK.getUserListByIDs(ids, new OperationCallback<ArrayList<User>>() {
				@Override
				public void onSuccess(ArrayList<User> data) {
					super.onSuccess(data);
					users.addAll(data);
					for (User user : users) {
						userMap.put(user.id.get(), user);
					}
					notifyDataSetChanged();
					if (friendRequests == null || friendRequests.isEmpty()) {
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
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_new_friends");
					builder.setTitle(getContext().getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
				}
			});
		}

		private void replyFriendRequest(final int poistion, String requesterId, final boolean accept) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (pd == null) {
				pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
			}
			UMSSDK.replyFriendRequesting(requesterId, accept, new OperationCallback<Void>() {
				@Override
				public void onSuccess(Void data) {
					super.onSuccess(data);
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					if (accept) {
						friendRequests.get(poistion).status.set(FriendRequest.RequestStatus.ACCEPT);
					} else {
						friendRequests.get(poistion).status.set(FriendRequest.RequestStatus.REFUSE);
					}
					notifyDataSetChanged();
				}

				@Override
				public void onFailed(Throwable t) {
					super.onFailed(t);
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					getParent().stopPulling();
					ErrorDialog.Builder builder = new ErrorDialog.Builder(getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_new_friends");
					builder.setTitle(getContext().getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
				}
			});
		}
	}
}
