package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pages.MainPage;
import com.mob.ums.gui.tabs.Tab;
import com.mob.ums.gui.tabs.TabHost;

import java.util.ArrayList;

public class MainPageAdapter extends DefaultThemePageAdapter<MainPage> implements TabHost {
	private ArrayList<Tab> tabs;
	private LinearLayout llPage;
	private LinearLayout llBody;
	private LinearLayout llBar;
	private int selected;
	
	public void onCreate(MainPage page, Activity activity) {
		super.onCreate(page, activity);
		tabs = new ArrayList<Tab>();
		selected = -1;
		activity.setContentView(getHostView(activity));
		addTab(new RecommendationTab(page));
		addTab(new ProfileTab(page));
		setSelection(page.getEnterTab().getValue());
	}
	
	public void addTab(Tab tab) {
		tabs.add(tab);
		
		Context context = getPage().getContext();
		LinearLayout llLogo = new LinearLayout(context);
		llLogo.setOrientation(LinearLayout.VERTICAL);
		final int index = llBar.getChildCount();
		llLogo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setSelection(index);
			}
		});
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llBar.addView(llLogo, lp);
		
		ImageView ivLogo = new ImageView(context);
		int resId = ResHelper.getBitmapRes(context, tab.getUnselectedIconResName());
		ivLogo.setImageResource(resId);
		ivLogo.setScaleType(ScaleType.CENTER_INSIDE);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 26));
		llLogo.addView(ivLogo, lp);
		
		TextView tvTitle = new TextView(context);
		resId = ResHelper.getStringRes(context, tab.getTitleResName());
		tvTitle.setText(resId);
		tvTitle.setTextColor(tab.getUnselectedTitleColor());
		tvTitle.setMaxLines(1);
		tvTitle.setGravity(Gravity.CENTER);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llLogo.addView(tvTitle, lp);
	}
	
	public void setSelection(int selection) {
		if (selected != -1) {
			Tab tabSel = tabs.get(selected);
			tabSel.onUnselected();
			llBody.removeAllViews();
			LinearLayout llIcon = (LinearLayout) llBar.getChildAt(selected);
			ImageView ivLogo = (ImageView) llIcon.getChildAt(0);
			TextView tvTitle = (TextView) llIcon.getChildAt(1);
			int resId = ResHelper.getBitmapRes(getPage().getContext(), tabSel.getUnselectedIconResName());
			ivLogo.setImageResource(resId);
			tvTitle.setTextColor(tabSel.getUnselectedTitleColor());
			selected = -1;
		}
		selected = selection;
		Tab tabSel = tabs.get(selection);
		llBody.addView(tabSel.getTabView(getPage().getContext()), new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		tabSel.onSelected();
		LinearLayout llIcon = (LinearLayout) llBar.getChildAt(selected);
		ImageView ivLogo = (ImageView) llIcon.getChildAt(0);
		TextView tvTitle = (TextView) llIcon.getChildAt(1);
		int resId = ResHelper.getBitmapRes(getPage().getContext(), tabSel.getSelectedIconResName());
		ivLogo.setImageResource(resId);
		tvTitle.setTextColor(tabSel.getSelectedTitleColor());
	}
	
	public View getHostView(Context context) {
		if (llPage == null) {
			llPage = new LinearLayout(context);
			llPage.setOrientation(LinearLayout.VERTICAL);
			
			llBody = new LinearLayout(context);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			llPage.addView(llBody, lp);
			
			View vSep = new View(context);
			vSep.setBackgroundColor(0xffedeff3);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
			llPage.addView(vSep, lp);
			
			llBar = new LinearLayout(context);
			int dp4 = ResHelper.dipToPx(context, 4);
			llBar.setPadding(0, dp4, dp4, 0);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 48));
			llPage.addView(llBar, lp);
		}
		return llPage;
	}
}
