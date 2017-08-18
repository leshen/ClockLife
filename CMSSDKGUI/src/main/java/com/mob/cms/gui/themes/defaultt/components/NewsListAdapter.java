package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.cms.Category;
import com.mob.cms.gui.pages.NewsListPage;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.gui.ViewPagerAdapter;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

public class NewsListAdapter extends ViewPagerAdapter {
	private ArrayList<Category> categories;
	private ArrayList<Integer> screenList;
	private ArrayList<String> hasReadedNewsIdList;
	private NewsListPage page;
	private Context context;
	private ScreenChangeListener screenChangeListener;
	
	public NewsListAdapter(NewsListPage page) {
		this.page = page;
		this.context = page.getContext();
		screenList = new ArrayList<Integer>();
		hasReadedNewsIdList = new ArrayList<String>();
	}

	public void setScreenChangeListener(ScreenChangeListener screenChangeListener) {
		this.screenChangeListener = screenChangeListener;
	}
	
	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}
	
	public int getCount() {
		if (categories != null) {
			return categories.size();
		}
		return 1;
	}
	
	public void onScreenChange(int currentScreen, int lastScreen) {
		super.onScreenChange(currentScreen, lastScreen);
		if (screenChangeListener != null) {
			screenChangeListener.onScreenChange(currentScreen, lastScreen);
		}
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		//没有数据时，显示默认页面
		if (categories == null) {
			if (convertView == null) {
				convertView = new NoDataViewItem(parent.getContext());
			}
			return  convertView;
		}
		
		if (convertView == null) {
			convertView = createPanel(parent.getContext());
		}
		if (!screenList.contains(index)) {
			PullToRequestView requestView = (PullToRequestView) convertView;
			NewsPullRequestAdapter pullRequestAdapter = new NewsPullRequestAdapter(this.page, requestView);
			pullRequestAdapter.setCategory(categories.get(index));
			pullRequestAdapter.setHasReadedNewsIdList(hasReadedNewsIdList);
			requestView.setAdapter(pullRequestAdapter);
			requestView.performPullingDown(true);
			if (screenList.size() >= 3) {
				screenList.remove(0);
			}
			screenList.add(index);
		}
		
		return convertView;
	}
	
	//创建下拉刷新列表
	private PullToRequestView createPanel(Context context) {
		PullToRequestView requestView = new PullToRequestView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		requestView.setLayoutParams(lp);
		return requestView;
	}
	
	/**页面滑动监听*/
	public interface ScreenChangeListener {
		public void onScreenChange(int currentScreen, int lastScreen);
	}
	
}
