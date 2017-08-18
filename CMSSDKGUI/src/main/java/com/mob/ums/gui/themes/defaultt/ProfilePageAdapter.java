package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mob.jimu.query.Condition;
import com.mob.jimu.query.Query;
import com.mob.jimu.query.data.Text;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import com.mob.ums.OperationCallback;
import com.mob.ums.QueryView;
import com.mob.ums.SocialNetwork;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;
import com.mob.ums.gui.pages.ProfilePage;
import com.mob.ums.gui.pages.dialog.AddFriendDialog;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.OKCancelDialog;
import com.mob.ums.gui.pages.dialog.OKDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.UserBriefView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class ProfilePageAdapter extends DefaultThemePageAdapter<ProfilePage> {
	private UserBriefView<ProfilePage> header;
	private LinearLayout llBind;
	private LinearLayout llFollow;
	private LinearLayout llFriend;
	private LinearLayout llBlock;
	private ProgressDialog pd;

	public void onCreate(ProfilePage page, Activity activity) {
		super.onCreate(page, activity);
		User user = page.getUer();
		initPage(user, activity);
		requestData(user);
	}

	private void initPage(User user, Activity activity) {
		LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);

		ScrollView sv = new ScrollView(activity);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		llPage.addView(sv, lp);

		LinearLayout llBody = new LinearLayout(activity);
		llBody.setOrientation(LinearLayout.VERTICAL);
		sv.addView(llBody, new ScrollView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// user brief
		header = new UserBriefView<ProfilePage>(getPage()) {
			protected void goSettings(Context context) {

			}

			protected void onReturn() {
				finish();
			}
		};
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 215));
		llBody.addView(header, lp);
		lp.bottomMargin = ResHelper.dipToPx(activity, 10);
		header.setUser(user);

		// create items
		ArrayList<View> items = new ArrayList<View>();

		String unEdit = getPage().getContext().getString(ResHelper.getStringRes(getPage().getContext(),"umssdk_default_unedit"));
		String birthday = "";
		if ( user.birthday.isNull() ) {
			birthday = unEdit;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			birthday = sdf.format(user.birthday.get());
		}
		items.add(getItemView("umssdk_default_birthday",birthday));

		items.add(getItemView("umssdk_default_age", String.valueOf(user.age.get())));

		int resid = 0;
		if (user.constellation.isNull()) {
			resid = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_unedit");
		} else {
			resid = ResHelper.getStringRes(getPage().getContext(), user.constellation.get().resName());
		}
		items.add(getItemView("umssdk_default_constellation", getPage().getContext().getString(resid)));


		int zodiacResId;
		if (user.zodiac.isNull()) {
			zodiacResId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_unedit");
		} else {
			zodiacResId = ResHelper.getStringRes(getPage().getContext(), user.zodiac.get().resName());
		}
		items.add(getItemView("umssdk_default_zodiac", getPage().getContext().getString(zodiacResId)));

		items.add(getItemView("umssdk_default_email", user.email.get()));

		items.add(getItemView("umssdk_default_address", user.address.get()));

		String zipCode = "";
		if(user.zipCode.isNull()){
			zipCode = unEdit;
		} else {
			zipCode = String.valueOf(user.zipCode.get());
		}
		items.add(getItemView("umssdk_default_zip_code", zipCode));

		llBind = getBindedView();
		items.add(llBind);

		items.add(getItemView("umssdk_default_signature", user.signature.get()));

		// add items
		if (items.size() > 0) {
			lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			llBody.addView(items.get(0), lp);
			int sepResId = ResHelper.getBitmapRes(getPage().getContext(), "umssdk_defalut_list_sep");
			Drawable d = getPage().getContext().getResources().getDrawable(sepResId);
			LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
			for (int i = 1; i < items.size(); i++) {
				View vsep = new View(getPage().getContext());
				vsep.setBackground(d);
				llBody.addView(vsep, lp1);
				llBody.addView(items.get(i), lp);
			}
		}

		// action bar
		View v = new View(activity);
		v.setBackgroundColor(0xffedeff3);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		llPage.addView(v, lp);

		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 49));
		llPage.addView(getActionBar(), lp);
	}

	private View getItemView(String titleResName, String data) {
		if(data == null){
			data=getPage().getContext().getString(ResHelper.getStringRes(getPage().getContext(),"umssdk_default_unedit"));
		}
		LinearLayout llItem = new LinearLayout(getPage().getContext());

		TextView tvTitle = new TextView(getPage().getContext());
		tvTitle.setTextColor(0xff969696);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvTitle.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams lp = new LayoutParams(ResHelper.dipToPx(getPage().getContext(), 110), ResHelper.dipToPx(getPage().getContext(), 39));
		lp.leftMargin = ResHelper.dipToPx(getPage().getContext(), 15);
		llItem.addView(tvTitle, lp);
		tvTitle.setText(ResHelper.getStringRes(getPage().getContext(), titleResName));

		TextView tvText = new TextView(getPage().getContext());
		tvText.setTextColor(0xff494949);
		tvText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 1;
		llItem.addView(tvText, lp);
		tvText.setText(data);

		return llItem;
	}

	private LinearLayout getBindedView() {
		LinearLayout llItem = new LinearLayout(getPage().getContext());

		TextView tvTitle = new TextView(getPage().getContext());
		tvTitle.setTextColor(0xff969696);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvTitle.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams lp = new LayoutParams(ResHelper.dipToPx(getPage().getContext(), 110), ResHelper.dipToPx(getPage().getContext(), 39));
		lp.leftMargin = ResHelper.dipToPx(getPage().getContext(), 15);
		llItem.addView(tvTitle, lp);
		tvTitle.setText(ResHelper.getStringRes(getPage().getContext(), "umssdk_default_bind_social_platform"));

		ArrayList<SocialNetwork> sns = new ArrayList<SocialNetwork>();
		sns.addAll(Arrays.asList(UMSSDK.getAvailableSocialLoginWays()));
		sns.add(0, null);
		int dp20 = ResHelper.dipToPx(getPage().getContext(), 18);
		for (SocialNetwork sn : sns) {
			String resName;
			if (sn == null) {
				resName = "umssdk_default_vocde";
			} else {
				resName = "umssdk_default_" + sn.getName().toLowerCase();
			}
			ImageView iv = new ImageView(getPage().getContext());
			iv.setImageResource(ResHelper.getBitmapRes(getPage().getContext(), resName));
			iv.setTag(sn);
			lp = new LayoutParams(dp20, dp20);
			lp.gravity = Gravity.CENTER_VERTICAL;
			lp.leftMargin = ResHelper.dipToPx(getPage().getContext(), 5);
			lp.rightMargin = lp.leftMargin;
			iv.setVisibility(View.GONE);
			llItem.addView(iv, lp);
		}

		if (llItem.getChildCount() > 1) {
			View v = llItem.getChildAt(1);
			lp = (LayoutParams) v.getLayoutParams();
			lp.leftMargin = 0;
			v.setLayoutParams(lp);

			v = llItem.getChildAt(llItem.getChildCount() - 1);
			lp = (LayoutParams) v.getLayoutParams();
			lp.rightMargin = 0;
			v.setLayoutParams(lp);
		}

		return llItem;
	}

	private View getActionBar() {
		Context context = getPage().getContext();
		LinearLayout llBar = new LinearLayout(context);
		llBar.setBackgroundColor(0xfff7f7f7);

		RelativeLayout rl = new RelativeLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llBar.addView(rl, lp);

		llFollow = new LinearLayout(context);
		RelativeLayout.LayoutParams lprl = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lprl.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(llFollow, lprl);
		llFollow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onFollow();
			}
		});

		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER_INSIDE);
		iv.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_add_follow"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llFollow.addView(iv, lp);

		TextView tv = new TextView(context);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_following"));
		tv.setTextColor(0xff333333);
		tv.setMaxLines(1);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llFollow.addView(tv, lp);

		View vSep = new View(context);
		vSep.setBackgroundColor(0xffe7e7e7);
		lp = new LayoutParams(1, ResHelper.dipToPx(context, 33));
		lp.gravity = Gravity.CENTER_VERTICAL;
		llBar.addView(vSep, lp);

		rl = new RelativeLayout(context);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llBar.addView(rl, lp);

		llFriend = new LinearLayout(context);
		lprl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lprl.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(llFriend, lprl);
		llFriend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onFriend();
			}
		});

		iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER_INSIDE);
		iv.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_add_friend"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llFriend.addView(iv, lp);

		tv = new TextView(context);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_add_as_friend"));
		tv.setTextColor(0xff333333);
		tv.setMaxLines(1);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llFriend.addView(tv, lp);

		vSep = new View(getPage().getContext());
		vSep.setBackgroundColor(0xffe7e7e7);
		lp = new LayoutParams(1, ResHelper.dipToPx(context, 33));
		lp.gravity = Gravity.CENTER_VERTICAL;
		llBar.addView(vSep, lp);

		rl = new RelativeLayout(context);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llBar.addView(rl, lp);

		llBlock = new LinearLayout(context);
		lprl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lprl.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(llBlock, lprl);
		llBlock.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBlock();
			}
		});

		iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER_INSIDE);
		iv.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_add_block"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llBlock.addView(iv, lp);

		tv = new TextView(context);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_block"));
		tv.setTextColor(0xff333333);
		tv.setMaxLines(1);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llBlock.addView(tv, lp);

		return llBar;
	}

	private void requestData(final User user) {
		showProgressDialog();
		getBindedPlatforms(user, new Runnable() {
			public void run() {
				if (UMSSDK.amILogin()) {
					final int[] count = new int[1];
					Runnable r = new Runnable() {
						public void run() {
							count[0]++;
							if (count[0] == 3) {
								dismissProgressDialog();
							}
						}
					};
					isMyFollowing(user, r);
					isMyFriend(user, r);
					isMyBlocking(user, r);
				} else {
					dismissProgressDialog();
				}
			}
		});
	}

	private void getBindedPlatforms(final User user, final Runnable nextStep) {
		new Thread() {
			public void run() {
				try {
					Query q = UMSSDK.getQuery(QueryView.BINDED_LOGIN_METOHDS);
					q.condition(Condition.eq(user.id.getName(), Text.valueOf(user.id.get())));
					String res = q.query();
					HashMap<String, Object> resMap = new Hashon().fromJson(res);
					@SuppressWarnings("unchecked")
					final ArrayList<HashMap<String, Object>> list
							= (ArrayList<HashMap<String, Object>>) resMap.get("list");
					UIHandler.sendEmptyMessage(0, new Callback() {
						public boolean handleMessage(Message msg) {
							nextStep.run();
							refreshBindedPlatforms(list);
							return false;
						}
					});
				} catch (final Throwable t) {
					UIHandler.sendEmptyMessage(0, new Callback() {
						public boolean handleMessage(Message msg) {
//							t.printStackTrace();
							nextStep.run();
							return false;
						}
					});
				}
			}
		}.start();
	}

	private void refreshBindedPlatforms(final ArrayList<HashMap<String, Object>> platforms) {
		if (platforms != null && !platforms.isEmpty()) {
			HashMap<Integer, ImageView> idToViews = new HashMap<Integer, ImageView>();
			HashMap<Integer, String> idToResName = new HashMap<Integer, String>();
			for (int i = 1; i < llBind.getChildCount(); i++) {
				ImageView iv = (ImageView) llBind.getChildAt(i);
				SocialNetwork sn = (SocialNetwork) iv.getTag();
				if (sn == null) {
					idToViews.put(0, iv);
					idToResName.put(0, "umssdk_default_vcode_binded");
				} else {
					idToViews.put(sn.getId(), iv);
					idToResName.put(sn.getId(), "umssdk_default_" + sn.getName().toLowerCase() + "_binded");
				}
				iv.setVisibility(View.GONE);
			}

			for (HashMap<String, Object> item : platforms) {
				int bindType = (Integer) item.get("bindType");
				ImageView iv = idToViews.get(bindType);
				if (iv != null) {
					iv.setVisibility(View.VISIBLE);
					iv.setImageResource(ResHelper.getBitmapRes(iv.getContext(), idToResName.get(bindType)));
				}
			}
		}
	}

	private void isMyFollowing(User user, final Runnable nextStep) {
		UMSSDK.isMyFollowing(user, new OperationCallback<Boolean>() {
			public void onSuccess(Boolean data) {
				refreshFollow(data);
				nextStep.run();
			}

			public void onFailed(Throwable t) {
//				t.printStackTrace();
				nextStep.run();
			}
		});
	}

	private void refreshFollow(boolean myFollowing) {
		View iv = llFollow.getChildAt(0);
		TextView tv = (TextView) llFollow.getChildAt(1);
		if (myFollowing) {
			iv.setVisibility(View.GONE);
			tv.setTextColor(0xff969696);
			tv.setText(ResHelper.getStringRes(tv.getContext(), "umssdk_default_stop_follow"));
		} else {
			iv.setVisibility(View.VISIBLE);
			tv.setTextColor(0xff333333);
			tv.setText(ResHelper.getStringRes(tv.getContext(), "umssdk_default_following"));
		}
	}

	private void isMyFriend(User user, final Runnable nextStep) {
		UMSSDK.isMyFriends(new Text[]{Text.valueOf(user.id.get())}, new OperationCallback<Set<String>>() {
			public void onSuccess(Set<String> data) {
				if (data == null || data.isEmpty()) {
					refreshFriend(false);
				} else {
					refreshFriend(true);
				}
				nextStep.run();
			}

			public void onFailed(Throwable t) {
//				t.printStackTrace();
				nextStep.run();
			}
		});
	}

	private void refreshFriend(boolean myFriend) {
		View iv = llFriend.getChildAt(0);
		TextView tv = (TextView) llFriend.getChildAt(1);
		if (myFriend) {
			iv.setVisibility(View.GONE);
			tv.setTextColor(0xff969696);
			tv.setText(ResHelper.getStringRes(tv.getContext(), "umssdk_default_delete_friend"));
		} else {
			iv.setVisibility(View.VISIBLE);
			tv.setTextColor(0xff333333);
			tv.setText(ResHelper.getStringRes(tv.getContext(), "umssdk_default_add_as_friend"));
		}
	}

	private void isMyBlocking(User user, final Runnable nextStep) {
		UMSSDK.isMyBlocking(user, new OperationCallback<Boolean>() {
			public void onSuccess(Boolean data) {
				refreshBlock(data);
				nextStep.run();
			}

			public void onFailed(Throwable t) {
//				t.printStackTrace();
				nextStep.run();
			}
		});
	}

	private void refreshBlock(boolean myBlocking) {
		View iv = llBlock.getChildAt(0);
		TextView tv = (TextView) llBlock.getChildAt(1);
		if (myBlocking) {
			iv.setVisibility(View.GONE);
			tv.setTextColor(0xff969696);
			tv.setText(ResHelper.getStringRes(tv.getContext(), "umssdk_default_stop_blocking"));
		} else {
			iv.setVisibility(View.VISIBLE);
			tv.setTextColor(0xff333333);
			tv.setText(ResHelper.getStringRes(tv.getContext(), "umssdk_default_block"));
		}
	}

	private void onFollow() {
		if (UMSSDK.isMe(getPage().getUer())) {
			initWarningDialog("umssdk_default_add_follow");
			return;
		}
		if (UMSSDK.amILogin()) {
			if (llFollow.getChildAt(0).getVisibility() == View.VISIBLE) {
				addFollow();
			} else {
				showConfirmDialog("umssdk_default_stop_follow","umssdk_default_check_stop_follow");
			}
		} else {
			UMSGUI.showLogin(new OperationCallback<User>() {
				public void onSuccess(User data) {
					requestData(getPage().getUer());
				}
			});
		}
	}

	private void onFriend() {
		if (UMSSDK.isMe(getPage().getUer())) {
			initWarningDialog("umssdk_default_add_friend");
			return;
		}
		if (UMSSDK.amILogin()) {
			if (llFriend.getChildAt(0).getVisibility() == View.VISIBLE) {
				addFriend();
			} else {
				showConfirmDialog("umssdk_default_delete_friend","umssdk_default_check_delete_friend");
			}
		} else {
			UMSGUI.showLogin(new OperationCallback<User>() {
				public void onSuccess(User data) {
					requestData(getPage().getUer());
				}
			});
		}
	}

	private void onBlock() {
		if (UMSSDK.isMe(getPage().getUer())) {
			initWarningDialog("umssdk_default_block");
			return;
		}
		if (UMSSDK.amILogin()) {
			if (llBlock.getChildAt(0).getVisibility() == View.VISIBLE) {
				showConfirmDialog("umssdk_default_block","umssdk_default_check_block");
			} else {
				stopBlock();
			}
		} else {
			UMSGUI.showLogin(new OperationCallback<User>() {
				public void onSuccess(User data) {
					requestData(getPage().getUer());
				}
			});
		}
	}

	private void addBlock(){
		UMSSDK.blockUser(getPage().getUer(), new OperationCallback<Void>() {
			public void onSuccess(Void data) {
				dismissProgressDialog();
				showOkDialog("umssdk_default_block","umssdk_default_block_success");
			}

			public void onFailed(Throwable t) {
//						t.printStackTrace();
				dismissProgressDialog();
				showErrorDialog(t,"umssdk_default_block");
			}
		});
	}

	private void stopBlock(){
		showProgressDialog();
		UMSSDK.stopBlock(getPage().getUer(), new OperationCallback<Void>() {
			public void onSuccess(Void data) {
				dismissProgressDialog();
				showOkDialog("umssdk_default_stop_blocking","umssdk_default_stop_block_success");
			}

			public void onFailed(Throwable t) {
//						t.printStackTrace();
				dismissProgressDialog();
				showErrorDialog(t,"umssdk_default_stop_blocking");
			}
		});
	}

	private void addFollow(){
		showProgressDialog();
		UMSSDK.followUser(getPage().getUer(), new OperationCallback<Void>() {
			public void onSuccess(Void data) {
				dismissProgressDialog();
				showOkDialog("umssdk_default_add_follow","umssdk_default_follow_success");
			}

			public void onFailed(Throwable t) {
//						t.printStackTrace();
				dismissProgressDialog();
				showErrorDialog(t,"umssdk_default_following");
			}
		});
	}

	private void stopFollow(){
		UMSSDK.stopFollow(getPage().getUer(), new OperationCallback<Void>() {
			public void onSuccess(Void data) {
				dismissProgressDialog();
				refreshFollow(false);
			}

			public void onFailed(Throwable t) {
//						t.printStackTrace();
				dismissProgressDialog();
				showErrorDialog(t,"umssdk_default_stop_follow");
			}
		});
	}

	private void addFriend(){
		AddFriendDialog.Builder builder = new AddFriendDialog.Builder(
				getPage().getContext(), getPage().getTheme());
		builder.setUser(getPage().getUer());
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
				showErrorDialog(t,"umssdk_default_add_as_friend");
			}
		});
		builder.show();
	}

	private void deleteFriend(){
		UMSSDK.deleteFriend(getPage().getUer(), new OperationCallback<Void>() {
			public void onSuccess(Void data) {
				dismissProgressDialog();
				refreshFriend(false);
			}

			public void onFailed(Throwable t) {
//						t.printStackTrace();
				dismissProgressDialog();
				showErrorDialog(t,"umssdk_default_delete_friend");
			}
		});
	}

	private void initWarningDialog(String tipRes) {
		OKDialog.Builder builder = new OKDialog.Builder(getPage().getContext(), getPage().getTheme());
		int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_tip");
		builder.setTitle(getPage().getContext().getString(resId));
		resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_tip_cannot_do");
		int tipResId = ResHelper.getStringRes(getPage().getContext(), tipRes);
		builder.setMessage(String.format(getPage().getContext().getString(resId), getPage().getContext().getString
				(tipResId)));
		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void showConfirmDialog(final String titleRes, final String confirmRes){
		Context context = getPage().getContext();
		OKCancelDialog.Builder builder = new OKCancelDialog.Builder(context,getPage().getTheme());
		int resId = ResHelper.getStringRes(context, titleRes);
		builder.setTitle(context.getString(resId));
		int tipResId = ResHelper.getStringRes(context, confirmRes);
		builder.setMessage(context.getString(tipResId));
		builder.noPadding();
		builder.setOnClickListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_POSITIVE) {
					showProgressDialog();
					if(confirmRes.equals("umssdk_default_check_block")){
						addBlock();
					} else if(confirmRes.equals("umssdk_default_check_stop_follow")){
						stopFollow();
					} else if(confirmRes.equals("umssdk_default_check_delete_friend")){
						deleteFriend();
					}
				}
				dialog.dismiss();
			}
		});
		if(confirmRes.equals("umssdk_default_check_block")){
			builder.setButtonOK(context.getString(ResHelper.getStringRes(context,"umssdk_default_block")));
		} else if(confirmRes.equals("umssdk_default_check_stop_follow")){
			builder.setButtonOK(context.getString(ResHelper.getStringRes(context,"umssdk_default_unfollow")));
		} else if(confirmRes.equals("umssdk_default_check_delete_friend")){
			builder.setButtonOK(context.getString(ResHelper.getStringRes(context,"umssdk_default_delete")));
		}
		builder.show();
	}

	private void showOkDialog(final String titleRes, String tipRes){
		OKDialog.Builder builder = new OKDialog.Builder(getPage().getContext(), getPage().getTheme());
		int resId = ResHelper.getStringRes(getPage().getContext(), titleRes);
		builder.setTitle(getPage().getContext().getString(resId));
		resId = ResHelper.getStringRes(getPage().getContext(), tipRes);
		builder.setMessage(getPage().getContext().getString(resId));
		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				dialog.dismiss();
				if(titleRes.equals("umssdk_default_stop_blocking")){
					refreshBlock(false);
				} else if(titleRes.equals("umssdk_default_block")){
					refreshBlock(true);
				} else if(titleRes.equals("umssdk_default_add_follow")){
					refreshFollow(true);
				}
			}
		});
		builder.show();
	}

	private void showErrorDialog(Throwable t,String titleRes){
		ErrorDialog.Builder builder = new ErrorDialog.Builder(
				getPage().getContext(), getPage().getTheme());
		int resId = ResHelper.getStringRes(getPage().getContext(), titleRes);
		builder.setTitle(getPage().getContext().getString(resId));
		builder.setThrowable(t);
		builder.setMessage(t.getMessage());
		builder.show();
	}

	private void showProgressDialog(){
		dismissProgressDialog();
		if(pd == null){
			pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
		} else {
			pd.show();
		}
	}
	private void dismissProgressDialog(){
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}

	@Override
	public void onDestroy(ProfilePage page, Activity activity) {
		super.onDestroy(page, activity);
		dismissProgressDialog();
		pd = null;
	}
}
