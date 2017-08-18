package com.mob.cms.gui.pages;

import com.mob.cms.Category;
import com.mob.cms.News;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;

public class ImageDetailPage extends Page<ImageDetailPage> {
	private Category category;
	private News imgNews;
	private UserBrief user;
	
	public ImageDetailPage(Theme theme){
		super(theme);
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setImgNews(News imgNews) {
		this.imgNews = imgNews;
	}

	public News getImgNews() {
		return imgNews;
	}
	
	public void setUser(UserBrief user) {
		this.user = user;
	}
	
	public UserBrief getUser() {
		return user;
	}
}
