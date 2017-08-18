package com.mob.cms.gui.themes.defaultt;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.Category;
import com.mob.cms.Comment;
import com.mob.cms.News;
import com.mob.cms.gui.dialog.SendCommentDialog;
import com.mob.cms.gui.pages.NewsDetailPage;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.cms.gui.themes.defaultt.components.CommentToolsView;
import com.mob.cms.gui.themes.defaultt.components.NewsDtailsWebChromeClient;
import com.mob.cms.gui.themes.defaultt.components.ToastUtils;
import com.mob.jimu.gui.PageAdapter;
import com.mob.tools.utils.ResHelper;

public class NewsDetailPageAdapter extends PageAdapter<NewsDetailPage> implements View.OnClickListener{
	private News news;
	private UserBrief user;
	private Category category;
	private NewsDtailsWebChromeClient webChromeClient;
	private View vProgress; //进度条
	public ImageView vBack;
	public CommentToolsView commentToolsView;
	
	public void onCreate(NewsDetailPage page, Activity activity) {
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0xffffffff));
		super.onCreate(page, activity);
		news = page.getNews();
		user = page.getUser();
		category = page.getCategory();
		initPage(activity);
	}
	
	private void initPage(final Activity activity) {
		RelativeLayout rlPage = new RelativeLayout(activity);
		LayoutParams rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		activity.setContentView(rlPage, rllp);
		
		LinearLayout ll = new LinearLayout(activity);
		ll.setId(1);
		ll.setBackgroundColor(0xfffafafa);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rlPage.addView(ll, rllp);
		
		//返回键
		vBack = new ImageView(activity);
		int dp5 = ResHelper.dipToPx(activity, 5);
		vBack.setPadding(dp5, dp5, dp5, dp5);
		vBack.setOnClickListener(this);
		LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int dp7 = ResHelper.dipToPx(activity, 7);
		int dp10 = ResHelper.dipToPx(activity, 10);
		lllp.setMargins(dp10, dp7, 0, dp7);
		int resId = ResHelper.getBitmapRes(activity, "cmssdk_default_black_back");
		if (resId > 0) {
			vBack.setImageResource(resId);
		}
		ll.addView(vBack, lllp);

		//分割线
		View v = new View(activity);
		v.setId(2);
		v.setBackgroundColor(0xffb4b4b4);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		rllp.addRule(RelativeLayout.BELOW, 1);
		rlPage.addView(v, rllp);

		vProgress = new View(activity);
		vProgress.setId(3);
		vProgress.setVisibility(View.GONE);
		vProgress.setBackgroundColor(0xfff85959);
		final int dp1 = ResHelper.dipToPx(activity, 1);
		rllp = new LayoutParams(dp1, dp1);
		rllp.addRule(RelativeLayout.BELOW, 2);
		rlPage.addView(vProgress, rllp);
		
		//滑动的Webview 新闻页
		WebView newsWebView = new WebView(activity);
		newsWebView.setVerticalScrollBarEnabled(false);
		newsWebView.getSettings().setJavaScriptEnabled(true);
		newsWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		int dp8 = ResHelper.dipToPx(activity, 8);
		rllp.addRule(RelativeLayout.BELOW, 3);
		
		//外链的文章，客户端自己实现
		if (news.type.get() == News.ArticleType.LINK) {
			newsWebView.setWebViewClient(new WebViewClient() {
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}
			});
			newsWebView.setPadding(0, 0, 0, dp10);
			rllp.setMargins(dp8, 0, dp8, 0);
			rlPage.addView(newsWebView, rllp);
			newsWebView.loadUrl(news.content.get());
			return;
		}

		int marginBot = 0;
		if (news.openingComment.get()) {
			marginBot = ResHelper.dipToPx(activity, 49);
		}
		rllp.setMargins(dp8, 0, dp8, marginBot);
		rlPage.addView(newsWebView, rllp);
		newsWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				getLikeStatus();
			}
		});
		//外链文章需要网页配合
		webChromeClient = new NewsDtailsWebChromeClient(newsWebView, news, user) {
			public void onProgressChanged(WebView view, final int newProgress) {
				LayoutParams lpProgress = (LayoutParams) vProgress.getLayoutParams();
				lpProgress.width = ResHelper.getScreenWidth(activity) * newProgress / 100;
				vProgress.setLayoutParams(lpProgress);
				if (newProgress > 0 && newProgress < 100) {
					vProgress.setVisibility(View.VISIBLE);
				} else {
					vProgress.setVisibility(View.GONE);
				}
			}
		};
		newsWebView.setWebChromeClient(webChromeClient);
		newsWebView.loadUrl("file:///android_asset/index.html");

		//视频下面的评论框
		commentToolsView = new CommentToolsView(activity);
		//评论数，点击评论数时，调到网页的评论列表
		commentToolsView.ivMsgBg.setOnClickListener(this);
		commentToolsView.setCommentsCount(news.comments.get());
		commentToolsView.llInputComs.setOnClickListener(this);
		rllp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlPage.addView(commentToolsView, rllp);
	}

	public void onClick(View view) {
		if (view == vBack) {
			finish();
		} else if (view == commentToolsView.llInputComs) {
			//不能评论时，给提示
			if (!news.openingComment.get()) {
				int resId = ResHelper.getStringRes(getPage().getContext(), "cmssdk_default_donot_comment");
				ToastUtils.show(getPage().getContext(), resId, Toast.LENGTH_SHORT);
				return;
			}
			SendCommentDialog.Builder builder = new SendCommentDialog.Builder(getPage().getContext(), getPage().getTheme());
			builder.setCancelable(true);
			builder.setCanceledOnTouchOutside(true);
			builder.setUser(getPage().getUser());
			builder.setCommentNews(news);
			builder.setCallback(new Callback<Comment>() {
				public void onSuccess(Comment data) {
					if (data != null) {
						//更新评论列表
						Comment comment = data;
						comment.updateAt.set(System.currentTimeMillis());
						comment.avatar.set(getPage().getUser().avatarUrl);
						String nickName = comment.nickname.get();
						if (TextUtils.isEmpty(nickName)) {
							int resId = ResHelper.getStringRes(getPage().getContext(), "cmssdk_default_visitor");
							if (resId > 0) {
								nickName = getPage().getContext().getString(resId);
								comment.nickname.set(nickName);
							}
						}
						int coms = news.comments.get() + 1;
						news.comments.set(coms);
						webChromeClient.addComment(comment);
					}
					ToastUtils.showSendComSuc(getPage().getContext());
				}

				public void onFailed(Throwable t) {
					ToastUtils.showSendComFail(getPage().getContext());
				}
			});
			builder.show();
		} else if (view == commentToolsView.ivMsgBg) {
			webChromeClient.toCommentposition();
		}
	}
	
	private void getLikeStatus() {
		if (user == null) {
			user = new UserBrief(UserBrief.USER_ANONYMOUS, null, null, null);
		}
		Callback<Boolean> callback = new Callback<Boolean>(){
			public void onSuccess(Boolean like) {
				//回调到网页，更新网页状态
				webChromeClient.hasLike(like);
			}

			public void onFailed(Throwable t) {
				t.printStackTrace();
				webChromeClient.hasLike(false);
			}
		};
		switch (user.type) {
			case UserBrief.USER_UMSSDK: CMSSDK.hasUMSSDKUserLikedTheNews(news, callback); break;
			case UserBrief.USER_CUSTOM: CMSSDK.hasCustomUserLikedTheNews(news, user.uid, callback); break;
			case UserBrief.USER_ANONYMOUS: CMSSDK.hasAnonymousUserLikedTheNews(news, callback); break;
		}
	}
}
