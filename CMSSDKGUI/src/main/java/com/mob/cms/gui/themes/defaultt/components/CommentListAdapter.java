package com.mob.cms.gui.themes.defaultt.components;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.Comment;
import com.mob.cms.News;
import com.mob.cms.gui.Utils;
import com.mob.jimu.gui.Theme;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

public class CommentListAdapter extends PullToRefreshHeaderFooterAdapter {
	private static final int PAGE_SIZE = 20;
	
	private Theme theme;
	private boolean hasNext;//是否有下一页数据
	private ArrayList<Comment> commentList;//数据列表
	private News comNews;
	private View noDataView;
	
	public CommentListAdapter(PullToRequestView view, Theme theme) {
		super(view);
		this.theme = theme;
		hasNext = true;
		commentList = new ArrayList<Comment>();
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
	
	public void setComNews(News news) {
		comNews = news;
	}
	
	public void addComment(Comment comment) {
		if (comment != null) {
			commentList.add(0, comment);
			notifyDataSetChanged();
		}
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public Comment getItem(int position) {
		if (commentList != null) {
			return commentList.get(position);
		}
		return null;
	}
	
	public int getCount() {
		if (commentList != null) {
			return commentList.size();
		}
		return 0;
	}
	
	protected void onRequest(final int offset) {
		if (offset != 0 && !hasNext) {
			getParent().stopPulling();
		} else {
			CMSSDK.getComments(comNews, offset, PAGE_SIZE, new Callback<ArrayList<Comment>>(){
				public void onSuccess(ArrayList<Comment> data) {
					if (offset == 0) {
						getParent().releasePullingUpLock();
						hasNext = true;
						commentList.clear();
					}
					
					ArrayList<Comment> comments = data;
					if (comments == null || comments.size() < PAGE_SIZE) {
						//最后一页，锁定上拉刷新
						hasNext = false;
						getParent().lockPullingUp();
						if (comments.size() > 0) {
							commentList.addAll(comments);
						}
					} else {
						commentList.addAll(comments);
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
	
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new CommentViewItem(parent.getContext());;
		}
		Comment comment = getItem(position);
		if (comment != null) {
			CommentViewItem viewItem = (CommentViewItem) convertView;
			String nickName = comment.nickname.get();
			if (TextUtils.isEmpty(nickName)) {
				int resId = ResHelper.getStringRes(getContext(), "cmssdk_default_visitor");
				if (resId > 0) {
					nickName = getContext().getString(resId);
				}
			}
			viewItem.tvUserName.setText(nickName);
			viewItem.tvComTime.setText(Utils.getTimeInYears(getContext(), comment.updateAt.get()));
			int resId = ResHelper.getBitmapRes(getContext(), "cmssdk_default_comment_icon");
			viewItem.aivUserIcon.execute(comment.avatar.get(), resId);
			viewItem.tvComText.setText(comment.content.get());
		}
		return convertView;
	}
	
}
