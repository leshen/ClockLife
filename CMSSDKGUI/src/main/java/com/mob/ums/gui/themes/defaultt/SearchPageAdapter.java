package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.jimu.query.data.Text;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.pages.ProfilePage;
import com.mob.ums.gui.pages.SearchPage;
import com.mob.ums.gui.pages.dialog.AddFriendDialog;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.OKDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.ums.gui.themes.defaultt.components.ItemUserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SearchPageAdapter extends DefaultThemePageAdapter<SearchPage> implements View.OnClickListener {
	private PullToRequestView pullView;
	private SearchListAdapter searchListAdapter;
	private ImageView ivClear;
	private EditText etSearch;
	private TextView tvCancel;

	public void onCreate(SearchPage page, Activity activity) {
		super.onCreate(page, activity);
		initPage(activity);
	}

	private void initPage(Activity activity) {
		int dp43 = ResHelper.dipToPx(activity, 43);
		int dp10 = ResHelper.dipToPx(activity, 10);
		int dp5 = ResHelper.dipToPx(activity, 5);

		LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		llPage.setBackgroundColor(0xffffffff);
		activity.setContentView(llPage);

		LinearLayout llSearchHead = new LinearLayout(activity);
		llSearchHead.setOrientation(LinearLayout.HORIZONTAL);
		llSearchHead.setBackgroundColor(0xffffffff);
		llSearchHead.setPadding(dp10, dp5, 0, dp5);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp43 + 1);
		llPage.addView(llSearchHead, lp);

		RelativeLayout rlEdit = new RelativeLayout(activity);
		rlEdit.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_default_search_edit_bg"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llSearchHead.addView(rlEdit, lp);

		ImageView ivSearch = new ImageView(activity);
		ivSearch.setId(1);
		ivSearch.setImageResource(ResHelper.getBitmapRes(activity, "umssdk_default_search"));
		ivSearch.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlp.leftMargin = dp10;
		rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rlp.addRule(RelativeLayout.CENTER_VERTICAL);
		rlEdit.addView(ivSearch, rlp);

		ivClear = new ImageView(activity);
		ivClear.setId(2);
		ivClear.setPadding(dp10, 0, dp10, 0);
		ivClear.setImageResource(ResHelper.getBitmapRes(activity, "umssdk_defalut_clear"));
		ivClear.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlp.addRule(RelativeLayout.CENTER_VERTICAL);
		rlEdit.addView(ivClear, rlp);
		ivClear.setOnClickListener(this);

		etSearch = new EditText(activity);
		etSearch.setBackground(null);
		etSearch.setHint(ResHelper.getStringRes(activity, "umssdk_default_search_edit_hint"));
		etSearch.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		etSearch.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		etSearch.setGravity(Gravity.CENTER_VERTICAL);
		etSearch.setPadding(dp10, 0, 0, 0);
		etSearch.setMaxLines(1);
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (TextUtils.isEmpty(etSearch.getText().toString())) {
						return true;
					}
					searchListAdapter.search(etSearch.getText().toString());
				}
				return false;
			}
		});
		rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams
				.MATCH_PARENT);
		rlp.addRule(RelativeLayout.RIGHT_OF, ivSearch.getId());
		rlp.addRule(RelativeLayout.LEFT_OF, ivClear.getId());
		rlp.addRule(RelativeLayout.CENTER_VERTICAL);
		rlEdit.addView(etSearch, rlp);

		tvCancel = new TextView(activity);
		tvCancel.setText(ResHelper.getStringRes(activity, "umssdk_default_cancel"));
		tvCancel.setTextColor(0xff000000);
		tvCancel.setGravity(Gravity.CENTER_VERTICAL);
		tvCancel.setPadding(dp10, 0, dp10, 0);
		tvCancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		llSearchHead.addView(tvCancel, lp);
		tvCancel.setOnClickListener(this);

		pullView = new PullToRequestView(activity);
		pullView.setBackgroundColor(0xfff7f7f7);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
				.WRAP_CONTENT);
		lp.weight = 1;
		llPage.addView(pullView, lp);

		searchListAdapter = new SearchListAdapter(pullView);
		pullView.setAdapter(searchListAdapter);
		pullView.lockPullingDown();
	}

	@Override
	public void onClick(View v) {
		if (v.equals(ivClear)) {
			etSearch.setText("");
		} else if (v.equals(tvCancel)) {
			finish();
		}
	}

	private class SearchListAdapter extends DefaultRTRListAdapter {
		private String keyword = "";
		private ArrayList<User> users;
		private HashMap<String, String> friendIdMap;
		private static final int PAGE_SIZE = 20;
		private int pageIndex = 0;
		private ProgressDialog pd;

		public SearchListAdapter(PullToRequestView view) {
			super(view);
			users = new ArrayList<User>();
			friendIdMap = new HashMap<String, String>();
		}

		@Override
		protected void onRequest(boolean firstPage) {
			if (TextUtils.isEmpty(keyword)) {
				getParent().lockPullingDown();
			} else {
				getParent().releasePullingDownLock();
			}
			if (firstPage) {
				pageIndex = 0;
			}
			getUsers();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new ItemUserView(parent.getContext());
			}
			ItemUserView itemUserView = (ItemUserView) convertView;
			itemUserView.clearAction();
			final User user = users.get(position);
			itemUserView.setUser(user);
			itemUserView.clearAction();
			if (!UMSSDK.isMe(user) && friendIdMap.get(user.id.get()) == null) {
				itemUserView.addAction("umssdk_default_add_friend", 0xff969696, "umssdk_default_remove_black_btn", new
						View.OnClickListener() {
							@Override
							public void onClick(View v) {
								addFriend(user);
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
		public long getItemId(int i) {
			return i;
		}

		@Override
		public Object getItem(int i) {
			return users.get(i);
		}

		@Override
		public int getCount() {
			return users == null ? 0 : users.size();
		}

		public void search(String keyword) {
			this.keyword = keyword;
			users.clear();
			friendIdMap.clear();
			onRequest(true);
		}

		private void getUsers() {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (pd == null) {
				pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
			}
			UMSSDK.search(keyword, pageIndex * PAGE_SIZE, PAGE_SIZE, new OperationCallback<ArrayList<User>>() {
				@Override
				public void onSuccess(ArrayList<User> data) {
					super.onSuccess(data);
					isMyFriends(data);
				}

				@Override
				public void onFailed(Throwable t) {
					super.onFailed(t);
					pd.dismiss();
					getParent().stopPulling();
					ErrorDialog.Builder builder = new ErrorDialog.Builder(getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_search");
					builder.setTitle(getContext().getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
				}
			});

		}

		private void isMyFriends(final ArrayList<User> userList) {
			Text[] ids = new Text[users.size()];
			for (int i = 0; i < users.size(); i++) {
				ids[i] = Text.valueOf(users.get(i).id.get());
			}
			UMSSDK.isMyFriends(ids, new OperationCallback<Set<String>>() {
				public void onSuccess(Set<String> data) {
					pd.dismiss();
					if (pageIndex == 0) {
						users.clear();
					}
					users.addAll(userList);
					for (String fId : data) {
						friendIdMap.put(fId, fId);
					}
					notifyDataSetChanged();

					if (userList == null || userList.isEmpty()) {
						getParent().lockPullingUp();
					} else {
						pageIndex++;
						getParent().releasePullingUpLock();
					}
				}

				public void onFailed(Throwable t) {
					pd.dismiss();
					if (pageIndex == 0) {
						users.clear();
					}
					users.addAll(userList);
					notifyDataSetChanged();
					getParent().stopPulling();
					ErrorDialog.Builder builder = new ErrorDialog.Builder(getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_search");
					builder.setTitle(getContext().getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
				}
			});
		}

		private void addFriend(User user) {
			AddFriendDialog.Builder builder = new AddFriendDialog.Builder(
					getPage().getContext(), getPage().getTheme());
			builder.setUser(user);
			builder.setCallback(new OperationCallback<Void>() {
				public void onSuccess(Void data) {
					Context ctx = getPage().getContext();
					OKDialog.Builder b = new OKDialog.Builder(ctx, getPage().getTheme());
					int resId = ResHelper.getStringRes(ctx, "umssdk_default_add_as_friend");
					b.setTitle(ctx.getString(resId));
					resId = ResHelper.getStringRes(ctx, "umssdk_default_request_sent");
					b.setMessage(ctx.getString(resId));
					b.show();
				}

				public void onFailed(Throwable t) {
//						t.printStackTrace();
					ErrorDialog.Builder builder = new ErrorDialog.Builder(
							getPage().getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_add_as_friend");
					builder.setTitle(getPage().getContext().getString(resId));
					builder.setThrowable(t);
					builder.setMessage(t.getMessage());
					builder.show();
				}
			});
			builder.show();
		}
	}
}
