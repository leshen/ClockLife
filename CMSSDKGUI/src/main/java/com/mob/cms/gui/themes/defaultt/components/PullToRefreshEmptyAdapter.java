package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.mob.tools.gui.PullToRequestListAdapter;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.gui.Scrollable;

public abstract class PullToRefreshEmptyAdapter extends PullToRequestListAdapter {
	private ScrollableRelativeLayout bodyView;
	private View emptyView;
	
	public PullToRefreshEmptyAdapter(PullToRequestView view) {
		super(view);
		bodyView = new ScrollableRelativeLayout(view.getContext());
		
		ListView lv = getListView();
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		bodyView.addView(lv, lp);
		
		emptyView = getEmptyView();
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		bodyView.addView(emptyView, lp);
		emptyView.setVisibility(View.INVISIBLE);
	}
	
	public Scrollable getBodyView() {
		return bodyView;
	}
	
	protected abstract View getEmptyView();
	
	public void notifyDataSetChanged() {
		if (emptyView != null) {
			if (getCount() > 0) {
				emptyView.setVisibility(View.INVISIBLE);
			} else {
				emptyView.setVisibility(View.VISIBLE);
			}
		}
		super.notifyDataSetChanged();
	}
	
	private class ScrollableRelativeLayout extends RelativeLayout implements Scrollable {
		
		public ScrollableRelativeLayout(Context context) {
			super(context);
		}
		
		public ScrollableRelativeLayout(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		
		public ScrollableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}
		
	}
}
