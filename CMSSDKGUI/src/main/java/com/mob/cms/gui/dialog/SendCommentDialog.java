package com.mob.cms.gui.dialog;

import android.content.Context;

import com.mob.cms.Callback;
import com.mob.cms.Comment;
import com.mob.cms.News;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.jimu.gui.Dialog;
import com.mob.jimu.gui.Theme;

import java.util.HashMap;

public class SendCommentDialog extends Dialog<SendCommentDialog> {
	private News commentNews;
	private Callback<Comment> callback;
	private UserBrief user;
	
	public SendCommentDialog(Context context, Theme theme) {
		super(context, theme);
	}

	protected void applyParams(HashMap<String, Object> params) {
		commentNews = (News) params.get("CommentNews");
		callback = (Callback<Comment>) params.get("callback");
		user = (UserBrief) params.get("user");
	}
	
	public UserBrief getUser() {
		return user;
	}
	
	public Callback<Comment> getCallback() {
		return callback;
	}

	public News getCommentNews() {
		return commentNews;
	}
	
	public static class Builder extends Dialog.Builder<SendCommentDialog> {

		public Builder(Context context, Theme theme) {
			super(context, theme);
		}

		protected SendCommentDialog createDialog(Context context, Theme theme) {
			return new SendCommentDialog(context, theme);
		}
		
		public void setCallback(Callback<Comment> callback) {
			set("callback", callback);
		}
		
		public void setUser(UserBrief user) {
			set("user", user);
		}
		
		public void setCommentNews(News news) {
			set("CommentNews", news);
		}
	}
	
}
