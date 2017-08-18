package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.User;

public class ProfilePage extends Page<ProfilePage> {
	private User target;
	
	public ProfilePage(Theme theme) {
		super(theme);
	}
	
	public void setUser(User target) {
		this.target = target;
	}
	
	public User getUer() {
		return target;
	}
	
}
