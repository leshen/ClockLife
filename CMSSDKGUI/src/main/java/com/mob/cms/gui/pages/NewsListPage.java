package com.mob.cms.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;

public class NewsListPage extends Page<NewsListPage> {
	private UserBrief user;
	
	public NewsListPage(Theme theme) {
		super(theme);
	}
	
	public void setUser(int type, String uid, String nickname, String avatarUrl) {
		this.user = new UserBrief(type, uid, nickname, avatarUrl);
	}
	
	public UserBrief getUser() {
		return user;
	}
	
	public static class UserBrief {
		public static final int USER_UMSSDK = 0;
		public static final int USER_CUSTOM = 1;
		public static final int USER_ANONYMOUS = 2;
		
		public final int type;
		public final String uid;
		public final String nickname;
		public final String avatarUrl;
		
		public UserBrief(int type, String uid, String nickname, String avatarUrl) {
			this.type = type;
			this.uid = uid;
			this.nickname = nickname;
			this.avatarUrl = avatarUrl;
		}
	}
	
}
