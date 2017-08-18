package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.jimu.gui.Page;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.User;
import com.mob.ums.gui.pages.FansPage;
import com.mob.ums.gui.pages.FansPage.PageType;

import java.util.Locale;

public abstract class UserBriefView<P extends Page<P>> extends RelativeLayout {
	private P page;
	private AsyncImageView ivAvatar;
	private TextView tvFollowing;
	private TextView tvFan;
	private TextView tvRFriend;
	private TextView tvNick;
	private TextView tvGender;
	private TextView tvArea;
	private ImageView ivSetting;
	private ImageView ivReturn;
	private User user;
	
	public UserBriefView(P page) {
		super(page.getContext());
		this.page = page;
		init(getContext());
	}
	
	private void init(Context context) {
		setBackgroundColor(0xffea6860);

		RelativeLayout  rlAvatar = new RelativeLayout(context);
		rlAvatar.setId(1);
		int dp60 = ResHelper.dipToPx(context, 60);
		LayoutParams lp = new LayoutParams(dp60, dp60);
		lp.addRule(CENTER_HORIZONTAL);
		lp.topMargin = ResHelper.dipToPx(context, 38);
		addView(rlAvatar,lp);

		View bordView = new View(context);
		bordView.setBackgroundResource(ResHelper.getBitmapRes(context,"umssdk_default_avatar_round_bg"));
		lp = new LayoutParams(dp60,dp60);
		rlAvatar.addView(bordView,lp);

		ivAvatar = new AsyncImageView(context);
		ivAvatar.setId(2);
		ivAvatar.setRound(dp60 / 2f);
		ivAvatar.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_avatar"));
		int dp5 = ResHelper.dipToPx(context, 2);
		lp = new LayoutParams(dp60 - dp5, dp60 - dp5);
		lp.addRule(CENTER_IN_PARENT);
		rlAvatar.addView(ivAvatar, lp);

		LinearLayout llBar = new LinearLayout(context);
		llBar.setId(3);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 50));
		lp.addRule(ALIGN_PARENT_BOTTOM);
		addView(llBar, lp);

		LinearLayout llRela = new LinearLayout(context);
		llRela.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lpll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.weight = 1;
		llBar.addView(llRela, lpll);
		llRela.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goFollowing();
			}
		});
		
		tvFollowing = new TextView(context);
		tvFollowing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvFollowing.setTextColor(0xffffffff);
		tvFollowing.setMaxLines(1);
		tvFollowing.setText("0");
		tvFollowing.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		lpll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.weight = 1;
		llRela.addView(tvFollowing, lpll);
		
		TextView tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
		tv.setTextColor(0xffffffff);
		tv.setMaxLines(1);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_following"));
		tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		lpll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.weight = 1;
		llRela.addView(tv, lpll);
		
		View vSep = new View(context);
		int dp30 = ResHelper.dipToPx(context, 20);
		vSep.setBackgroundColor(0xffffffff);
		lpll = new LinearLayout.LayoutParams(1, dp30);
		lpll.gravity = Gravity.CENTER_VERTICAL;
		llBar.addView(vSep, lpll);
		
		llRela = new LinearLayout(context);
		llRela.setOrientation(LinearLayout.VERTICAL);
		lpll = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.weight = 1;
		llBar.addView(llRela, lpll);
		llRela.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goFans();
			}
		});
		
		tvFan = new TextView(context);
		tvFan.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvFan.setTextColor(0xffffffff);
		tvFan.setMaxLines(1);
		tvFan.setText("0");
		tvFan.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		lpll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.weight = 1;
		llRela.addView(tvFan, lpll);
		
		tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
		tv.setTextColor(0xffffffff);
		tv.setMaxLines(1);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_fans"));
		tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		lpll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.weight = 1;
		llRela.addView(tv, lpll);
		
		vSep = new View(context);
		vSep.setBackgroundColor(0xffffffff);
		lpll = new LinearLayout.LayoutParams(1, dp30);
		lpll.gravity = Gravity.CENTER_VERTICAL;
		llBar.addView(vSep, lpll);
		
		llRela = new LinearLayout(context);
		llRela.setOrientation(LinearLayout.VERTICAL);
		lpll = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.leftMargin=ResHelper.dipToPx(context,1);
		lpll.weight = 1;
		llBar.addView(llRela, lpll);
		llRela.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goRFriends();
			}
		});
		
		tvRFriend = new TextView(context);
		tvRFriend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvRFriend.setTextColor(0xffffffff);
		tvRFriend.setMaxLines(1);
		tvRFriend.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		tvRFriend.setText("0");
		lpll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.weight = 1;
		llRela.addView(tvRFriend, lpll);
		
		tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
		tv.setTextColor(0xffffffff);
		tv.setMaxLines(1);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_rfriend"));
		tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		lpll = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lpll.weight = 1;
		llRela.addView(tv, lpll);
		
		LinearLayout llCenter = new LinearLayout(context);
		llCenter.setOrientation(LinearLayout.VERTICAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW, rlAvatar.getId());
		lp.addRule(ABOVE, llBar.getId());
		lp.addRule(CENTER_HORIZONTAL);
		addView(llCenter, lp);
		
		tvNick = new TextView(context);
		tvNick.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tvNick.setTextColor(0xffffffff);
		tvNick.setMaxLines(1);
		tvNick.setEllipsize(TextUtils.TruncateAt.END);
		tvNick.setGravity(Gravity.BOTTOM);
		tvNick.setPadding(dp30,0,dp30,0);
		lpll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lpll.gravity = Gravity.CENTER;
		lpll.weight = 1;
		llCenter.addView(tvNick, lpll);
		
		LinearLayout ll = new LinearLayout(context);
		lpll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lpll.gravity = Gravity.CENTER;
		lpll.weight = 1;
		llCenter.addView(ll, lpll);
		
		tvGender = new TextView(context);
		tvGender.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		tvGender.setTextColor(0xffffffff);
		int dp10 = ResHelper.dipToPx(context, 10);
		tvGender.setPadding(0, 0, dp10, 0);
		tvGender.setGravity(Gravity.TOP);
		lpll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		ll.addView(tvGender, lpll);
		
		tvArea = new TextView(context);
		tvArea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		tvArea.setTextColor(0xffffffff);
		tvArea.setGravity(Gravity.TOP);
		lpll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		ll.addView(tvArea, lpll);
		
		ivSetting = new ImageView(context);
		ivSetting.setImageResource(ResHelper.getBitmapRes(context, "umssdk_defalut_profile_setting"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_TOP);
		lp.addRule(ALIGN_PARENT_RIGHT);
		int dp15 = ResHelper.dipToPx(context, 15);
		lp.topMargin = dp15;
		lp.rightMargin = lp.topMargin;
		addView(ivSetting, lp);
		ivSetting.setVisibility(GONE);
		ivSetting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goSettings(v.getContext());
			}
		});
		
		ivReturn = new ImageView(context);
		ivReturn.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_return_white"));
		ivReturn.setScaleType(ScaleType.CENTER_INSIDE);
		int dp32 = ResHelper.dipToPx(context, 32);
		ivReturn.setPadding(dp15, 0, dp32, 0);
		int dp43 = ResHelper.dipToPx(context, 43);
		lp = new LayoutParams(dp43 + dp15, dp43);
		addView(ivReturn, lp);
		ivReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onReturn();
			}
		});
	}
	
	public void setUser(User user) {
		String unEdit=getContext().getString(ResHelper.getStringRes(getContext(),"umssdk_default_hint_space"));
		this.user = user;
		if (!user.nickname.isNull()) {
			if(user.nickname.get().length() > 12){
				tvNick.setText(user.nickname.get().substring(0,6) + "..." + user.nickname.get().substring(user.nickname.get().length() - 6));
			} else {
				tvNick.setText(user.nickname.get());
			}
		} else {
			tvNick.setText(unEdit);
		}
		if (!user.gender.isNull()) {
			tvGender.setText(ResHelper.getStringRes(getContext(), user.gender.get().resName()));
		} else {
			tvGender.setText(unEdit);
		}
		String area = "";
		Context context = getContext();
		if ("zh".equals(Locale.getDefault().getLanguage())) {
			if (!user.country.isNull()) {
				area += context.getString(ResHelper.getStringRes(context, user.country.get().resName()));
			}
			if (!user.province.isNull()) {
				if (area.length() > 0) {
					area += "";
				}
				area += context.getString(ResHelper.getStringRes(context, user.province.get().resName()));
			}
			if (!user.city.isNull()) {
				if (area.length() > 0) {
					area += "";
				}
				area += context.getString(ResHelper.getStringRes(context, user.city.get().resName()));
			}
		} else {
			if (!user.city.isNull()) {
				area += context.getString(ResHelper.getStringRes(context, user.city.get().resName()));
			}
			if (!user.province.isNull()) {
				if (area.length() > 0) {
					area += " ";
				}
				area += context.getString(ResHelper.getStringRes(context, user.province.get().resName()));
			}
			if (!user.country.isNull()) {
				if (area.length() > 0) {
					area += " ";
				}
				area += context.getString(ResHelper.getStringRes(context, user.country.get().resName()));
			}
		}
		if (!TextUtils.isEmpty(area)) {
			tvArea.setText(area);
		} else {
			tvArea.setText(unEdit);
		}
		if (!user.avatar.isNull()) {
			ivAvatar.execute(user.avatar.get()[0], ResHelper.getBitmapRes(context, "umssdk_default_avatar"));
		}
		if (!user.followings.isNull()) {
			tvFollowing.setText(String.valueOf(user.followings.get()));
		}
		if (!user.fans.isNull()) {
			tvFan.setText(String.valueOf(user.fans.get()));
		}
		if (!user.rFriends.isNull()) {
			tvRFriend.setText(String.valueOf(user.rFriends.get()));
		}
	}
	
	public void setTabMob() {
		ivSetting.setVisibility(VISIBLE);
		ivReturn.setVisibility(GONE);
	}
	
	protected abstract void goSettings(Context context);
	
	protected abstract void onReturn();
	
	private void goFollowing() {
		if (user != null && user.followings.get() > 0) {
			FansPage page = new FansPage(this.page.getTheme());
			page.setUser(user);
			page.showPage(PageType.FOLLOWINGS);
		}
	}
	
	private void goFans() {
		if (user != null && user.fans.get() > 0) {
			FansPage page = new FansPage(this.page.getTheme());
			page.setUser(user);
			page.showPage(PageType.FANS);
		}
	}
	
	private void goRFriends() {
		if (user != null && user.rFriends.get() > 0) {
			FansPage page = new FansPage(this.page.getTheme());
			page.setUser(user);
			page.showPage(PageType.R_FRIENDS);
		}
	}
}
