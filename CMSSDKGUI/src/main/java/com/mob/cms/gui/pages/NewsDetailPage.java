package com.mob.cms.gui.pages;

import com.mob.cms.Category;
import com.mob.cms.News;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;

public class NewsDetailPage extends Page<NewsDetailPage> {
	private Category category;
	private News news;
	private UserBrief user;

	public NewsDetailPage(Theme theme){
		super(theme);
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public News getNews() {
		return news;
	}

	public void setUser(NewsListPage.UserBrief user) {
		this.user = user;
	}

	public UserBrief getUser() {
		return user;
	}
	
}
