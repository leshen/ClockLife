package com.mob.cms.gui.themes.defaultt.components;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.Category;
import com.mob.cms.News;
import com.mob.cms.gui.Utils;
import com.mob.cms.gui.dialog.OKDialog;
import com.mob.cms.gui.dialog.ProgressDialog;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.cms.gui.pages.VideoDetailPage;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

public class VideoDetailListAdapter extends PullToRefreshHeaderFooterAdapter {
	private static final int PAGE_SIZE = 20;
	
	private VideoDetailPage page;
	private boolean hasNext;//是否有下一页数据
	private ArrayList<News> videoList;//数据列表
	private View noDataView;
	private UserBrief user;
	private Category category;
	private News videoNews;
	private VideoChangeListener listener;
	private ProgressDialog pd;
	
	public VideoDetailListAdapter(VideoDetailPage page, PullToRequestView view) {
		super(view);
		this.page = page;
		hasNext = true;
		videoList = new ArrayList<News>();
		
		getListView().setDivider(new ColorDrawable(0xffe9e9e9));
		getListView().setDividerHeight(1);
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
	
	public void setUser(UserBrief user) {
		this.user = user;
	}
	
	public void setData(Category category, News videoNews) {
		this.videoNews = videoNews;
		this.category = category;
	}
	
	public void setVideoChangeListener(VideoChangeListener listener) {
		this.listener = listener;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public News getItem(int position) {
		if (videoList != null) {
			return videoList.get(position);
		}
		return null;
	}
	
	public int getCount() {
		if (videoList != null) {
			return videoList.size();
		}
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new VideoViewItem1(parent.getContext());;
		}
		final News news = getItem(position);
		if (news != null) {
			VideoViewItem1 viewType1 = (VideoViewItem1) convertView;
			viewType1.tvVideoTitle.setText(news.title.get());
			String playTimes = getContext().getString(ResHelper.getStringRes(getContext(), "cmssdk_default_video_play_times"));
			viewType1.tvPlayTimes.setText(String.format(playTimes, news.reads.get()));
			
			int resId = ResHelper.getBitmapRes(getContext(), "cmssdk_default_video_bg");
			String imgUrl = null;
			if (news.displayImgs.get() != null && news.displayImgs.get().length > 0) {
				imgUrl = news.displayImgs.get()[0].imgUrl;
			}
			viewType1.aivVideoImg.execute(imgUrl, resId);
			viewType1.tvVideoDuration.setText(Utils.getVideoPlayTime(news.videoDuration.get()));
			//点击推荐视频时，更新视频列表
			viewType1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					pd = new ProgressDialog.Builder(getContext(), page.getTheme()).show();
					CMSSDK.getNewsDetails(news.id.get(), new Callback<News>() {
						public void onSuccess(News data) {
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							//把原来的移出的视频加到列表，然后移出当前播放的视频
							videoList.add(videoNews);
							videoList.remove(news);
							videoNews = data;
							if (listener != null) {
								listener.onVideoChange(videoNews);
							}
						}
						
						public void onFailed(Throwable t) {
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							OKDialog.Builder builder = new OKDialog.Builder(getContext(), page.getTheme());
							int resId = ResHelper.getStringRes(getContext(), "cmssdk_default_error");
							builder.setTitle(getContext().getString(resId));
							builder.setMessage(t.getMessage());
							builder.show();
						}
					});
				}
			});
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
					n.reads.getName(),
					n.videoDesc.getName(),
					n.likes.getName(),
					n.displayImgs.getName(),
					n.videoDuration.getName(),
					n.displayType.getName(),
					n.type.getName(),
					n.topNews.getName(),
					n.hotNews.getName(),
					n.comments.getName(),
					n.updateAt.getName(),
					n.imgSize.getName()
			};
			CMSSDK.getRecommendNews(videoNews.id.get(), offset, PAGE_SIZE, new Callback<ArrayList<News>>(){
				public void onSuccess(ArrayList<News> data) {
					if (offset == 0) {
						getParent().releasePullingUpLock();
						hasNext = true;
						videoList.clear();
					}
					
					ArrayList<News> videos = data;
					if (videos == null || videos.size() < PAGE_SIZE) {
						//最后一页，锁定上拉刷新
						hasNext = false;
						getParent().lockPullingUp();
					}

					if (videos != null && videos.size() > 0) {
						videoList.addAll(videos);
					}
					
					//去除列表中相同的视频
					int i = 0;
					while (i < videoList.size()) {
						News video = videoList.get(i);
						if (video.id.get() != null && video.id.get().equals(videoNews.id.get())) {
							videoNews = video;
							videoList.remove(video);
							break;
						}
						i++;
					}
					
					notifyDataSetChanged();
					increaseOffset();
				}
				
				public void onFailed(Throwable t) {
					notifyDataSetChanged();
				}
			});
		}
	}
	
	/**视频改变监听*/
	public interface VideoChangeListener {
		public void onVideoChange(News videoNews);
	}
}
