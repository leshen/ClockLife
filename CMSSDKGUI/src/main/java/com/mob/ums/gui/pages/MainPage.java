package com.mob.ums.gui.pages;

import com.mob.MobSDK;
import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.User;

public class MainPage extends Page<MainPage> {
	public enum MainPageTabs {
		RECOMMENDATION (0),
		PROFILE (1);
		
		private int value;
		
		private MainPageTabs(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	private MainPageTabs enterTab;
	private User me;
	
	public MainPage(Theme theme) {
		super(theme);
	}
	
	public void setUser(User me) {
		this.me = me;
	}
	
	public User getUser() {
		return me;
	}
	
	public void showForProfile() {
		enterTab = MainPageTabs.PROFILE;
		show(MobSDK.getContext(), null);
	}
	
	public void showForRecommendation() {
		enterTab = MainPageTabs.RECOMMENDATION;
		show(MobSDK.getContext(), null);
	}
	
	public MainPageTabs getEnterTab() {
		return enterTab;
	}
	
}
