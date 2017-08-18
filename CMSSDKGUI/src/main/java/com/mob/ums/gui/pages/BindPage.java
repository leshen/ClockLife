package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;

import java.util.ArrayList;
import java.util.HashMap;

/** bind phone number and social network platforms */
public class BindPage extends Page<BindPage> {
	private HashMap<Integer, HashMap<String, Object>> platforms;
	
	public BindPage(Theme theme) {
		super(theme);
		this.platforms = new HashMap<Integer, HashMap<String, Object>>();
	}
	
	public void setPlatforms(ArrayList<HashMap<String, Object>> platforms) {
		if (platforms != null) {
			for (HashMap<String, Object> platform : platforms) {
				this.platforms.put((Integer) platform.get("bindType"), platform);
			}
		}
	}
	
	public HashMap<Integer, HashMap<String, Object>> getPlatforms() {
		return platforms;
	}
	
}
