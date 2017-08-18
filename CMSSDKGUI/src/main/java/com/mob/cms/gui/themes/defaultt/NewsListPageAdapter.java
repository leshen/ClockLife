package com.mob.cms.gui.themes.defaultt;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.Category;
import com.mob.cms.gui.dialog.OKDialog;
import com.mob.cms.gui.dialog.ProgressDialog;
import com.mob.cms.gui.pages.NewsListPage;
import com.mob.cms.gui.themes.defaultt.components.NewsListAdapter;
import com.mob.jimu.gui.PageAdapter;
import com.mob.tools.gui.MobViewPager;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

public class NewsListPageAdapter extends PageAdapter<NewsListPage> implements View.OnClickListener{
	private TextView tvSelect;
	private TextView[] tvCells;
	private MobViewPager mvPage;
	private ArrayList<Category> categories;
	private ProgressDialog pd;
	private HorizontalScrollView hsvTitleBar;
	
	public void onCreate(NewsListPage page, Activity activity) {
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0xffffffff));
		super.onCreate(page, activity);
		initPage(activity);
	}
	
	private void initPage(final Activity activity) {
		LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		activity.setContentView(llPage, lp);
		//标题
		int dp45 = ResHelper.dipToPx(activity, 45);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp45);
		TextView tvTitle = new TextView(activity);
		tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
		tvTitle.setGravity(Gravity.CENTER);
		tvTitle.setBackgroundColor(0xffd43d3d);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
		tvTitle.setTextColor(0xffffffff);
		tvTitle.setText(ResHelper.getStringRes(activity, "cmssdk_default_sdk"));
		llPage.addView(tvTitle, lp);
		
		//工具栏横向滚动条
		hsvTitleBar = new HorizontalScrollView(activity);
		hsvTitleBar.setSmoothScrollingEnabled(true);
		hsvTitleBar.setHorizontalScrollBarEnabled(false);
		int dp40 = ResHelper.dipToPx(activity, 40);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp40);
		llPage.addView(hsvTitleBar, lp);
		
		//工具栏布局
		final LinearLayout ll = new LinearLayout(activity);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		int dp5 = ResHelper.dipToPx(activity, 5);
		ll.setPadding(dp5, 0, dp5, 0);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		hsvTitleBar.addView(ll, lp);
		
		//分割线
		View line = new View(activity);
		line.setBackgroundColor(0xffe8e8e8);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		llPage.addView(line, lp);
		
		//左右滑动内容页
		mvPage = new MobViewPager(activity);
		mvPage.setVisibility(View.GONE);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llPage.addView(mvPage, lp);
		
		//没有数据的默认图标
		final LinearLayout llNotData = new LinearLayout(activity);
		llNotData.setOrientation(LinearLayout.VERTICAL);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.topMargin = ResHelper.getScreenHeight(activity) / 4;
		llPage.addView(llNotData, lp);
		
		ImageView iv = new ImageView(activity);
		iv.setImageResource(ResHelper.getBitmapRes(activity, "cmssdk_default_no_data"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		llNotData.addView(iv, lp);
		
		TextView tv = new TextView(activity);
		tv.setTextColor(0xff999999);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		tv.setText(ResHelper.getStringRes(activity, "cmssdk_default_no_data"));
		llNotData.addView(tv, lp);
				
		//设置adapter
		final NewsListAdapter adapter = new NewsListAdapter(getPage());
		adapter.setScreenChangeListener(new NewsListAdapter.ScreenChangeListener() {
			public void onScreenChange(int currentScreen, int lastScreen) {
				//滑屏监听，动态修改标题栏
				if (tvCells != null && tvCells.length >= currentScreen) {
					if (tvSelect != null) {
						tvSelect.setTextColor(0xff222222);
						tvSelect.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
					}
					tvSelect = tvCells[currentScreen];
					tvSelect.setTextColor(0xfff85959);
					tvSelect.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 21);
					//滑动标题栏
					scrollTitleView();
				}
			}
		});

		//获取列表数据后
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(activity, getPage().getTheme()).show();
		CMSSDK.getCategories(new Callback<ArrayList<Category>>(){
			public void onSuccess(ArrayList<Category> data) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				llNotData.setVisibility(View.GONE);
				categories = data;
				initTitleBar(activity, ll);
				adapter.setCategories(categories);
				mvPage.setAdapter(adapter);
				mvPage.setVisibility(View.VISIBLE);
			}

			public void onFailed(Throwable t) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
//				t.printStackTrace();
				OKDialog.Builder builder = new OKDialog.Builder(activity, getPage().getTheme());
				int resId = ResHelper.getStringRes(activity, "cmssdk_default_error");
				builder.setTitle(activity.getString(resId));
				builder.setMessage(t.getMessage());
				builder.show();
			}
		});
	}
	
	//初始化工具栏
	private void initTitleBar(Activity activity, LinearLayout ll) {
		if (categories == null || categories.size() <= 0) {
			return;
		}
		int dataSize = categories.size();
		tvCells = new TextView[dataSize];
		for (int i = 0; i < dataSize; i++) {
			tvCells[i] = new TextView(activity);
			int dp10 = ResHelper.dipToPx(activity, 10);
			tvCells[i].setPadding(dp10, 0, dp10, 0);
			tvCells[i].setText(categories.get(i).name.get());
			tvCells[i].setTextColor(0xff222222);
			tvCells[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
			tvCells[i].setGravity(Gravity.CENTER);
			tvCells[i].setOnClickListener(this);
			tvCells[i].setTag(i);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
			ll.addView(tvCells[i], lp);
		}
		
		if (tvCells[0] != null) {
			tvSelect = tvCells[0];
			tvSelect.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 21);
			tvSelect.setTextColor(0xfff85959);
		}
	}

	public void onClick(View view) {
		if (view instanceof TextView) {
			if (tvSelect != null) {
				tvSelect.setTextColor(0xff222222);
				tvSelect.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
			}
			tvSelect = (TextView) view;
			tvSelect.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 21);
			tvSelect.setTextColor(0xfff85959);
			
			//获取对应新闻数据，更新列表
			if (mvPage != null) {
				int index = (Integer) view.getTag();
				mvPage.scrollToScreen(index, true);
			}
		}
	}
	
	private void scrollTitleView() {
		int offset = hsvTitleBar.getScrollX();
		int scrollViewWidth = hsvTitleBar.getWidth();
		if ((scrollViewWidth + offset) < tvSelect.getRight()) {//需要向右移动
			int rightOffset = tvSelect.getRight() - (scrollViewWidth + offset);
			hsvTitleBar.smoothScrollBy(rightOffset, 0);
		} else if (offset > tvSelect.getLeft()) {//需要向左移动
			int leftOffset = tvSelect.getLeft() - offset;
			hsvTitleBar.smoothScrollBy(leftOffset, 0);
		}
	}
	
}
