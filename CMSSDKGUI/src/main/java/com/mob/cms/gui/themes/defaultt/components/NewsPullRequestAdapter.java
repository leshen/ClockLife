package com.mob.cms.gui.themes.defaultt.components;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mob.MobSDK;
import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.Category;
import com.mob.cms.News;
import com.mob.cms.News.Image;
import com.mob.cms.gui.Utils;
import com.mob.cms.gui.dialog.OKDialog;
import com.mob.cms.gui.dialog.ProgressDialog;
import com.mob.cms.gui.pages.ImageDetailPage;
import com.mob.cms.gui.pages.NewsDetailPage;
import com.mob.cms.gui.pages.NewsListPage;
import com.mob.cms.gui.pages.VideoDetailPage;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class NewsPullRequestAdapter extends PullToRefreshHeaderFooterAdapter implements OnItemClickListener {
	private static final int PAGE_SIZE = 20;//每页多少条数据
	private static final int VIEW_TYPE_COUNT = 6;
	
	private NewsListPage page;
	private View noDataView;
	private Category category; //当前是第几屏幕，用于请求对于的数据
	private ArrayList<String> readedNewsId; //数据列表
	private ArrayList<News> newsData; //数据列表
	private boolean hasNext; //是否有下一页数据
	private ProgressDialog pd;
	
	public NewsPullRequestAdapter(NewsListPage page, PullToRequestView view) {
		super(view);
		this.page = page;
		newsData = new ArrayList<News>();
		
		getListView().setDivider(new ColorDrawable(0xffe9e9e9));
		getListView().setDividerHeight(1);
		getListView().setOnItemClickListener(this);
	}
	
	protected int getPageSize() {
		return PAGE_SIZE;
	}
	
	protected View getEmptyView() {
		if (noDataView == null) {
			noDataView = new NoDataViewItem(getContext());
		}
		return noDataView;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public void setHasReadedNewsIdList(ArrayList<String> hasReadedNewsIdList) {
		readedNewsId = hasReadedNewsIdList;
	}
	
	public int getCount() {
		if (newsData != null) {
			return newsData.size();
		}
		return 0;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public News getItem(int position) {
		if (newsData != null) {
			return newsData.get(position);
		}
		return null;
	}
	
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}
	
	public int getItemViewType(int position) {
		int displayType = getItem(position).displayType.get().code();
		if (displayType == 3 && getItem(position).type.get() == News.ArticleType.VIDEO) {
			return 5;
		}
		return displayType;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		switch (getItemViewType(position)){
			case 0: return getViewType0(position, convertView, parent); //无图
			case 1: return getViewType1(position, convertView, parent); //左一图
			case 2: return getViewType2(position, convertView, parent); //右一图
			case 3: return getViewType3(position, convertView, parent); //下图一
			case 4: return getViewType4(position, convertView, parent); //下三图
			default: return getViewType5(position, convertView, parent); //视频
		}
	}
	
	private View getViewType0(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new NewsViewItem0(parent.getContext());
		}
		News news = getItem(position);
		if (news != null) {
			NewsViewItem0 viewType0 = (NewsViewItem0) convertView;
			viewType0.setNewsTitle(news.title.get(), readedNewsId.contains(news.id.get()));
			viewType0.tvTop.setVisibility(news.topNews.get() ? View.VISIBLE : View.GONE);
			viewType0.tvHot.setVisibility(news.hotNews.get() ? View.VISIBLE : View.GONE);
			viewType0.setComsCount(news.openingComment.get(), news.comments.get());
			viewType0.tvNewsTime.setText(Utils.getTimeInYears(getContext(), news.updateAt.get()));
		}
		return convertView;
	}
	
	private View getViewType1(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new NewsViewItem1(parent.getContext());;
		}
		News news = getItem(position);
		if (news != null) {
			NewsViewItem1 viewType1 = (NewsViewItem1) convertView;
			viewType1.setNewsTitle(news.title.get(), readedNewsId.contains(news.id.get()));
			viewType1.tvTop.setVisibility(news.topNews.get() ? View.VISIBLE : View.GONE);
			viewType1.tvHot.setVisibility(news.hotNews.get() ? View.VISIBLE : View.GONE);
			viewType1.setComsCount(news.openingComment.get(), news.comments.get());
			viewType1.tvNewsTime.setText(Utils.getTimeInYears(getContext(), news.updateAt.get()));
			int resId = ResHelper.getBitmapRes(parent.getContext(), "cmssdk_default_news_img_bg");
			String imgUrl = null;
			if (news.displayImgs.get() != null && news.displayImgs.get().length > 0) {
				imgUrl = news.displayImgs.get()[0].imgUrl;
			}
			viewType1.aivNewsImg.execute(imgUrl, resId);
			//如果是视频时，显示视频时间
			viewType1.tvVideoDuration.setVisibility(View.GONE);
			if (news.type.get() == News.ArticleType.VIDEO) {
				viewType1.tvVideoDuration.setVisibility(View.VISIBLE);
				viewType1.tvVideoDuration.setText(Utils.getVideoPlayTime(news.videoDuration.get()));
			}
		}
		return convertView;
	}
	
	private View getViewType2(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new NewsViewItem2(parent.getContext());
		}
		News news = getItem(position);
		if (news != null) {
			NewsViewItem2 viewType2 = (NewsViewItem2) convertView;
			viewType2.setNewsTitle(news.title.get(), readedNewsId.contains(news.id.get()));
			viewType2.tvTop.setVisibility(news.topNews.get() ? View.VISIBLE : View.GONE);
			viewType2.tvHot.setVisibility(news.hotNews.get() ? View.VISIBLE : View.GONE);
			viewType2.setComsCount(news.openingComment.get(), news.comments.get());
			viewType2.tvNewsTime.setText(Utils.getTimeInYears(getContext(), news.updateAt.get()));
			int resId = ResHelper.getBitmapRes(parent.getContext(), "cmssdk_default_news_img_bg");
			String imgUrl = null;
			if (news.displayImgs.get() != null && news.displayImgs.get().length > 0) {
				imgUrl = news.displayImgs.get()[0].imgUrl;
			}
			viewType2.aivNewsImg.execute(imgUrl, resId);
			//如果是视频时，显示视频时间
			viewType2.tvVideoDuration.setVisibility(View.GONE);
			if (news.type.get() == News.ArticleType.VIDEO) {
				viewType2.tvVideoDuration.setVisibility(View.VISIBLE);
				viewType2.tvVideoDuration.setText(Utils.getVideoPlayTime(news.videoDuration.get()));
			}
		}
		return convertView;
	}
	
	private View getViewType3(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new NewsViewItem3(parent.getContext());
		}
		News news = getItem(position);
		if (news != null) {
			NewsViewItem3 viewType3 = (NewsViewItem3) convertView;
			viewType3.setNewsTitle(news.title.get(), readedNewsId.contains(news.id.get()));
			viewType3.tvTop.setVisibility(news.topNews.get() ? View.VISIBLE : View.GONE);
			viewType3.tvHot.setVisibility(news.hotNews.get() ? View.VISIBLE : View.GONE);
			viewType3.setComsCount(news.openingComment.get(), news.comments.get());
			viewType3.tvNewsTime.setText(Utils.getTimeInYears(getContext(), news.updateAt.get()));
			int resId = ResHelper.getBitmapRes(parent.getContext(), "cmssdk_default_image_default_bg");
			String imgUrl = null;
			if (news.displayImgs.get() != null && news.displayImgs.get().length > 0) {
				imgUrl = news.displayImgs.get()[0].imgUrl;
			}
			viewType3.aivNewsImg.execute(imgUrl, resId);
			viewType3.setImgCount(String.valueOf(news.imgSize.get()));
		}
		return convertView;
	}
	
	private View getViewType4(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new NewsViewItem4(parent.getContext());
		}
		News news = getItem(position);
		if (news != null) {
			NewsViewItem4 viewType4 = (NewsViewItem4) convertView;
			viewType4.setNewsTitle(news.title.get(), readedNewsId.contains(news.id.get()));
			viewType4.tvTop.setVisibility(news.topNews.get() ? View.VISIBLE : View.GONE);
			viewType4.tvHot.setVisibility(news.hotNews.get() ? View.VISIBLE : View.GONE);
			viewType4.setComsCount(news.openingComment.get(), news.comments.get());
			viewType4.tvNewsTime.setText(Utils.getTimeInYears(getContext(), news.updateAt.get()));
			int resId = ResHelper.getBitmapRes(parent.getContext(), "cmssdk_default_news_img_bg");
			Image[] imgs = news.displayImgs.get();
			if (imgs != null) {
				for (int i = 0; i < imgs.length; i++) {
					viewType4.aivNewsImgAry[i].execute(imgs[i].imgUrl, resId);
				}
			}
		}
		return convertView;
	}
	
	private View getViewType5(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new NewsViewItem5(parent.getContext());
		}
		News news = getItem(position);
		if (news != null) {
			NewsViewItem5 viewType5 = (NewsViewItem5) convertView;
			viewType5.setNewsTitle(news.title.get(), readedNewsId.contains(news.id.get()));
			viewType5.tvTop.setVisibility(news.topNews.get() ? View.VISIBLE : View.GONE);
			viewType5.tvHot.setVisibility(news.hotNews.get() ? View.VISIBLE : View.GONE);
			viewType5.setComsCount(news.openingComment.get(), news.comments.get());
			viewType5.tvUpdateAt.setText(Utils.getTimeInYears(getContext(), news.updateAt.get()));
			viewType5.tvPlayTime.setText(Utils.getVideoPlayTime(news.videoDuration.get()));
			int resId = ResHelper.getBitmapRes(parent.getContext(), "cmssdk_default_news_img_bg");
			String imgUrl = null;
			if (news.displayImgs.get() != null && news.displayImgs.get().length > 0) {
				imgUrl = news.displayImgs.get()[0].imgUrl;
			}
			viewType5.aivNewsImg.execute(imgUrl, resId);
		}
		return convertView;
	}
	
	protected void onRequest(final int offset) {
		if (offset != 0 && !hasNext) {
			getParent().stopPulling();
		} else {
			News n = new News();
			String[] fields = {
					n.id.getName(),
					n.title.getName(),
					n.topNews.getName(),
					n.hotNews.getName(),
					n.comments.getName(),
					n.updateAt.getName(),
					n.displayImgs.getName(),
					n.imgSize.getName(),
					n.videoDuration.getName(),
					n.displayType.getName(),
					n.type.getName(),
					n.openingComment.getName()
			};
			CMSSDK.getNews(category, null, fields, offset, PAGE_SIZE, new Callback<ArrayList<News>>(){
				public void onSuccess(ArrayList<News> data) {
					if (offset == 0) {
						getParent().releasePullingUpLock();
						hasNext = true;
						newsData.clear();
					}
					
					ArrayList<News> list = data;
					if (list == null || list.size() < PAGE_SIZE) {
						//最后一页，锁定上拉刷新
						hasNext = false;
						getParent().lockPullingUp();
						if (list.size() > 0) {
							newsData.addAll(list);
						}
					} else {
						newsData.addAll(list);
					}
					notifyDataSetChanged();
					increaseOffset();
				}
				
				public void onFailed(Throwable t) {
//					t.printStackTrace();
					getParent().stopPulling();
				}
			});
		}
	}
	
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(getContext(), page.getTheme()).show();
		News n = getItem(position);
		String newsId = n.id.get();
		changeClickItemTitleColor(view, position, newsId);
		CMSSDK.getNewsDetails(newsId, new Callback<News>() {
			public void onSuccess(News data) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				News.ArticleType articleType = data.type.get();
				if (articleType == News.ArticleType.PICTURES) {
					ImageDetailPage page = new ImageDetailPage(NewsPullRequestAdapter.this.page.getTheme());
					page.setCategory(category);
					page.setUser(NewsPullRequestAdapter.this.page.getUser());
					page.setImgNews(data);
					page.show(MobSDK.getContext(), null);
				} else if (articleType == News.ArticleType.VIDEO) {
					VideoDetailPage page = new VideoDetailPage(NewsPullRequestAdapter.this.page.getTheme());
					page.setCategory(category);
					page.setUser(NewsPullRequestAdapter.this.page.getUser());
					page.setVideoNews(data);
					page.show(MobSDK.getContext(), null);
				} else {
					NewsDetailPage page = new NewsDetailPage(NewsPullRequestAdapter.this.page.getTheme());
					page.setCategory(category);
					page.setUser(NewsPullRequestAdapter.this.page.getUser());
					page.setNews(data);
					page.show(MobSDK.getContext(), null);
				}
			}
			
			public void onFailed(Throwable t) {
//				t.printStackTrace();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				
				NewsListPage p = NewsPullRequestAdapter.this.page;
				OKDialog.Builder builder = new OKDialog.Builder(p.getContext(), p.getTheme());
				int resId = ResHelper.getStringRes(p.getContext(), "cmssdk_default_error");
				builder.setTitle(p.getContext().getString(resId));
				builder.setMessage(t.getMessage());
				builder.show();
			}
		});
	}
	
	/**点击新闻时，更改字体颜色，标志已阅读*/
	private void changeClickItemTitleColor(View clickView, int position, String newsId) {
		if (!readedNewsId.contains(newsId)) {
			readedNewsId.add(newsId);
			int type = getItemViewType(position);
			switch (type) {
				case 0:{
					NewsViewItem0 item0 = (NewsViewItem0) clickView;
					item0.setNewsTitle(getItem(position).title.get(), true);
				} break;
				case 1:{
					NewsViewItem1 item1 = (NewsViewItem1) clickView;
					item1.setNewsTitle(getItem(position).title.get(), true);
				} break;
				case 2:{
					NewsViewItem2 item2 = (NewsViewItem2) clickView;
					item2.setNewsTitle(getItem(position).title.get(), true);
				} break;
				case 3:{
					NewsViewItem3 item3 = (NewsViewItem3) clickView;
					item3.setNewsTitle(getItem(position).title.get(), true);
				} break;
				case 4:{
					NewsViewItem4 item4 = (NewsViewItem4) clickView;
					item4.setNewsTitle(getItem(position).title.get(), true);
				} break;
				case 5:{
					NewsViewItem5 item5 = (NewsViewItem5) clickView;
					item5.setNewsTitle(getItem(position).title.get(), true);
				} break;
			}
		}
	}
}
