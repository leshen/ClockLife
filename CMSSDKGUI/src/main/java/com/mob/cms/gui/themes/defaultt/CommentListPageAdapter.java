package com.mob.cms.gui.themes.defaultt;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.mob.cms.Callback;
import com.mob.cms.Category;
import com.mob.cms.Comment;
import com.mob.cms.News;
import com.mob.cms.gui.dialog.SendCommentDialog;
import com.mob.cms.gui.pages.CommentListPage;
import com.mob.cms.gui.themes.defaultt.components.CommentListAdapter;
import com.mob.cms.gui.themes.defaultt.components.CommentToolsView;
import com.mob.cms.gui.themes.defaultt.components.ToastUtils;
import com.mob.jimu.gui.PageAdapter;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

public class CommentListPageAdapter extends PageAdapter<CommentListPage> implements View.OnClickListener{
	private News comNews;
	private Category category;
	private ImageView vBack;
	private CommentListAdapter adapter;
	private CommentToolsView commentToolsView;
	
	public void onCreate(CommentListPage page, Activity activity) {
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0xffffffff));
		super.onCreate(page, activity);
		comNews = page.getComNews();
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
		rllp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
		rllp.addRule(RelativeLayout.BELOW, 1);
		rlPage.addView(v, rllp);
		
		//评论的滑动列表
		PullToRequestView requestView = new PullToRequestView(activity);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		int dp49 = ResHelper.dipToPx(activity, 49);
		rllp.setMargins(0, 0, 0, dp49);
		rllp.addRule(RelativeLayout.BELOW, 2);
		rlPage.addView(requestView, rllp);
		
		adapter = new CommentListAdapter(requestView, getPage().getTheme());
		adapter.setComNews(comNews);
		requestView.setAdapter(adapter);
		requestView.performPullingDown(true);

		//下面的评论框
		commentToolsView = new CommentToolsView(activity);
		commentToolsView.hideMsgView();
		commentToolsView.setId(3);
		commentToolsView.llInputComs.setOnClickListener(this);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlPage.addView(commentToolsView, rllp);
		
		if(getPage().isOpenCommendInputView()) {
			showCommentInputDialog();
		}
	}

	public void onClick(View view) {
		if (view == vBack) {
			finish();
		} else if (view == commentToolsView.llInputComs) {
			showCommentInputDialog();
		} 
	}
	
	private void showCommentInputDialog() {
		SendCommentDialog.Builder builder = new SendCommentDialog.Builder(getPage().getContext(), getPage().getTheme());
		builder.setCancelable(true);
		builder.setCanceledOnTouchOutside(true);
		builder.setUser(getPage().getUser());
		builder.setCommentNews(comNews);
		builder.setCallback(new Callback<Comment>() {
			public void onSuccess(Comment data) {
				if (data != null) {
					Comment newCom = data;
					newCom.updateAt.set(System.currentTimeMillis());
					adapter.addComment(newCom);
				}
				ToastUtils.showSendComSuc(getPage().getContext());
			}

			public void onFailed(Throwable t) {
				//t.printStackTrace();
				ToastUtils.showSendComFail(getPage().getContext());
			}
		});
		builder.show();
	}
}
