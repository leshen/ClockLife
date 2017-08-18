package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.User;

public class MyFriendsPage extends Page<MyFriendsPage> {
	private User me;
	
	public MyFriendsPage(Theme theme) {
		super(theme);
	}
	
	public void setUser(User me) {
		this.me = me;
	}
	
	public User getUser() {
		return me;
	}
	
}
