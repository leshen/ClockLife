package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.User;

/** setting page */
public class SettingsPage extends Page<SettingsPage> {

	private User user;
	
	public SettingsPage(Theme theme) {
		super(theme);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
