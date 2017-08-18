package com.mob.cms.gui.pages;

import com.mob.cms.Category;
import com.mob.cms.News;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;

public class CommentListPage extends Page<CommentListPage> {
	private Category category;
	private News comNews;
	private UserBrief user;
	private boolean open;
	
	public CommentListPage(Theme theme) {
		super(theme);
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setComNews(News comNews) {
		this.comNews = comNews;
	}

	public News getComNews() {
		return comNews;
	}
	
	public void setUser(UserBrief user) {
		this.user = user;
	}
	
	public UserBrief getUser() {
		return user;
	}

	public void openCommendInputView() {
		this.open = true;
	}

	public boolean isOpenCommendInputView() {
		return open;
	}
}
