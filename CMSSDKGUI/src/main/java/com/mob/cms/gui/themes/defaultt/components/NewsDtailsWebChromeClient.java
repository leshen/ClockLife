package com.mob.cms.gui.themes.defaultt.components;

import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.Comment;
import com.mob.cms.News;
import com.mob.cms.QueryView;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.jimu.query.Condition;
import com.mob.jimu.query.Query;
import com.mob.jimu.query.Sort;
import com.mob.jimu.query.data.Text;
import com.mob.tools.utils.Hashon;

import java.util.HashMap;

public class NewsDtailsWebChromeClient extends WebChromeClient {

	private WebView view;
	private JavaHandler handler;
	
	public NewsDtailsWebChromeClient(WebView view, News news, UserBrief user) {
		this.view = view;
		handler = new JavaHandler();
		handler.setNews(news);
		handler.setUser(user);
		if (Build.VERSION.SDK_INT >= 17) {
			view.addJavascriptInterface(handler, "javaHandler");
		}
	}
	
	public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
		if (Build.VERSION.SDK_INT < 17 && message != null) {
			if ("_$#getNewsDetails#$_".equals(message)) {
				result.confirm(handler.getNewsDetails());
				return true;
			} else if ("_$#getComments#$_".equals(message)) {
				int offset;
				int size;
				try {
					HashMap<String, Object> map = new Hashon().fromJson(defaultValue);
					offset = Integer.parseInt(String.valueOf(map.get("offset")));
					size = Integer.parseInt(String.valueOf(map.get("size")));
				} catch (Throwable t) {
					offset = 0;
					size = 50;
				}
				result.confirm(handler.getComments(offset, size));
				return true;
			} else if ("_$#likeNews#$_".equals(message)) {
				result.confirm(handler.likeNews());
				return true;
			}
		}
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}
	
	public void addComment(Comment comment) {
		handler.addComment(view, comment);
	}
	
	public void toCommentposition() {
		handler.toCommentposition(view);
	}
	
	public void hasLike(boolean isPraised) {
		handler.hasLike(view, isPraised);
	}

	private class JavaHandler {
		private News news;
		private UserBrief user;
		
		public void setNews(News news) {
			this.news = news;
		}
		
		public void setUser(UserBrief user) {
			this.user = user;
		}

		@JavascriptInterface
		public String getNewsDetails() {
			return new Hashon().fromHashMap(news.parseToMap());
		}
		
		@JavascriptInterface
		public String getComments(final int offset, final int size) {
			final Object lock = new Object();
			final Object[] arr = new Object[1];
			synchronized (lock) {
				new Thread() {
					public void run() {
						synchronized (lock) {
							try {
								Comment c = new Comment();
								Query q = CMSSDK.getQuery(QueryView.COMMENT);
								q.condition(Condition.eq(c.news.getName(), Text.valueOf(news.id.get())));
								q.sort(Sort.desc(c.updateAt.getName()));
								q.offset(offset);
								q.size(size);
								HashMap<String, Object> res = new Hashon().fromJson(q.query());
								arr[0] = res.get("list");
							} catch (Throwable t) {
								
							}
							try {
								lock.notifyAll();
							} catch (Throwable t) {}
						}
					}
				}.start();
				try {
					lock.wait();
				} catch (Throwable t) {}
			}
			
			if (arr[0] != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("comments", arr[0]);
				return new Hashon().fromHashMap(map);
			}
			return null;
		}
		
		@JavascriptInterface
		public String likeNews() {
			if (user == null) {
				user = new UserBrief(UserBrief.USER_ANONYMOUS, null, null, null);
			}
			
			final Object lock = new Object();
			final Object[] arr = new Object[1];
			Callback<Void> cb = new Callback<Void>() {
				public void onSuccess(Void data) {
					arr[0] = true;
					//更新文章的喜欢次数
					int likes = news.likes.get() + 1;
					news.likes.set(likes);
					
					synchronized (lock) {
						try {
							lock.notifyAll();
						} catch (Throwable t) {}
					}
				}
				
				public void onCancel() {
					arr[0] = false;
					synchronized (lock) {
						try {
							lock.notifyAll();
						} catch (Throwable t) {}
					}
				}
				
				public void onFailed(Throwable t) {
					arr[0] = false;
					synchronized (lock) {
						try {
							lock.notifyAll();
						} catch (Throwable tt) {}
					}
				}
			};
			synchronized (lock) {
				switch (user.type) {
					case UserBrief.USER_UMSSDK: {
						CMSSDK.likeNewsFromUMSSDKUser(news, cb);
					} break;
					case UserBrief.USER_CUSTOM: {
						CMSSDK.likeNewsFromCustomUser(news, user.uid, user.nickname, user.avatarUrl, cb);
					} break;
					case UserBrief.USER_ANONYMOUS: {
						CMSSDK.likeNewsFromAnonymousUser(news, cb);
					} break;
				}
				try {
					lock.wait();
				} catch (Throwable t) {}
			}
			
			if (arr[0] != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("res", arr[0]);
				return new Hashon().fromHashMap(map);
			}
			return null;
		}
		
		public void addComment(WebView view, Comment comment) {
			String json = new Hashon().fromHashMap(comment.parseToMap());
			view.loadUrl("javascript:window.CMSSDKNative.addComment(" + json + ")");
		}

		public void hasLike(WebView view, boolean isPraised) {
			view.loadUrl("javascript:window.CMSSDKNative.isPraised(" + isPraised + ")");
		}
		
		public void toCommentposition(WebView view) {
			view.loadUrl("javascript:window.CMSSDKNative.toCommentposition()");
		}
		
	}
}
