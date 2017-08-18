package com.mob.ums.gui.pages;

import com.mob.MobSDK;
import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.ums.User;

public class FansPage extends Page<FansPage> {
	private PageType fanPage;
	private User user;

	public FansPage(Theme theme) {
		super(theme);
	}

	public PageType getFanPage() {
		return fanPage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void showPage(PageType fanPage) {
		this.fanPage = fanPage;
		show(MobSDK.getContext(), null);
	}

	public enum PageType implements PublicMemberKeeper {
		FANS(1, "umssdk_default_my_fans"),
		FOLLOWINGS(2, "umssdk_default_my_follow"),
		R_FRIENDS(3, "umssdk_default_rfriend"),
		BLOCKINGS(4, "umssdk_default_black"),
		TFANS(5, "umssdk_default_t_fans"),
		TFOLLOWINGS(6, "umssdk_default_t_follow");

		private int code;
		private String resName;

		private PageType(int code, String resName) {
			this.code=code;
			this.resName=resName;
		}

		public int getCode(){
			return code;
		}

		public String getResName(){
			return resName;
		}
	}


}
