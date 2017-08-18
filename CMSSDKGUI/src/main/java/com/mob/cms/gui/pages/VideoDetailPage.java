package com.mob.cms.gui.pages;

import com.mob.cms.Category;
import com.mob.cms.News;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;

public class VideoDetailPage extends Page<VideoDetailPage> {
	private Category category;
	private News videoNews;
	private UserBrief user;

	public VideoDetailPage(Theme theme){
		super(theme);
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setVideoNews(News videoNews) {
		this.videoNews = videoNews;
	}

	public News getVideoNews() {
		return videoNews;
	}
	
	public void setUser(UserBrief user) {
		this.user = user;
	}
	
	public UserBrief getUser() {
		return user;
	}
}
