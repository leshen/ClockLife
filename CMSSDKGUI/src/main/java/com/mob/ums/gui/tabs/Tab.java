package com.mob.ums.gui.tabs;

import android.content.Context;
import android.view.View;

public interface Tab {
	
	public String getSelectedIconResName();
	
	public String getUnselectedIconResName();
	
	public String getTitleResName();
	
	public int getSelectedTitleColor();
	
	public int getUnselectedTitleColor();
	
	public View getTabView(Context context);
	
	public void onSelected();
	
	public void onUnselected();
	
}
