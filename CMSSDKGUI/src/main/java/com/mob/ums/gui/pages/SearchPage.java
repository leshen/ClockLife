package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.User;

public class SearchPage extends Page<SearchPage> {
	private User me;
	
	public SearchPage(Theme theme) {
		super(theme);
	}
	
	public void setUser(User me) {
		this.me = me;
	}
	
	public User getUser() {
		return me;
	}
	
}
