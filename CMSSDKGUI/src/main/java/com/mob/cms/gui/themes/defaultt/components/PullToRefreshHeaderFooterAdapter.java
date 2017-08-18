package com.mob.cms.gui.themes.defaultt.components;

import android.view.View;

import com.mob.tools.gui.PullToRequestView;

public abstract class PullToRefreshHeaderFooterAdapter extends PullToRefreshEmptyAdapter {
	//#if def{lang} == cn
	/** 下拉刷新的头部View */
	//#elif def{lang} == en
	/** the header view of friends list page*/
	//#endif
	private PRTHeader llHeader;
	private PRTFooter footerView;
	private int offset;
	private int pageSize;
	private boolean type;
	
	public PullToRefreshHeaderFooterAdapter(PullToRequestView view) {
		super(view);
		pageSize = getPageSize();
	}
	
	// =======================
	
	public View getHeaderView() {
		if (llHeader == null) {
			llHeader = new PRTHeader(getContext());
		}
		return llHeader;
	}
	
	public void onPullDown(int percent) {
		llHeader.onPullDown(percent);
	}
	
	// request the 1st page data
	public void onRefresh() {
		type = true;
		llHeader.onRequest();
		offset = 0;
		onRequest(offset);
	}
	
	// =======================
	
	public View getFooterView() {
		if (footerView == null) {
			footerView = new PRTFooter(getContext());
		}
		return footerView;
	}
	
	public void onRequestNext() {
		type = false;
		footerView.onRequest();
		onRequest(offset);
	}
	
	// =======================
	
	public void onReversed() {
		if (type) {
			llHeader.reverse();
		} else {
			footerView.reverse();
		}
	}
	
	protected abstract int getPageSize();
	
	protected abstract void onRequest(int offset);
	
	protected void increaseOffset() {
		offset += pageSize;
	}
	
}
