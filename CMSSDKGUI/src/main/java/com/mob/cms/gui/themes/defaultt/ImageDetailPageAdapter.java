package com.mob.cms.gui.themes.defaultt;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.cms.gui.pages.CommentListPage;
import com.mob.cms.gui.pages.ImageDetailPage;
import com.mob.cms.gui.themes.defaultt.components.CommentToolsView;
import com.mob.cms.gui.themes.defaultt.components.ImageDetailAdapter;
import com.mob.cms.gui.themes.defaultt.components.ToastUtils;
import com.mob.jimu.gui.PageAdapter;
import com.mob.tools.gui.MobViewPager;
import com.mob.tools.utils.ResHelper;

public class ImageDetailPageAdapter extends PageAdapter<ImageDetailPage> implements View.OnClickListener{
	private ImageView ivClose;
	private TextView tvImgDetail;
	private CommentToolsView commentToolsView;
	private ImageDetailPage page;
	private MobViewPager mvPage;
	private boolean hideOtherView;
	
	public void onCreate(ImageDetailPage page, Activity activity) {
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0x00000000));
		super.onCreate(page, activity);
		this.page = page;
		initPage(activity);
	}
	
	private void initPage(final Activity activity) {
		RelativeLayout rlPage = new RelativeLayout(activity);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		activity.setContentView(rlPage, lp);
				
		//评论工具栏
		commentToolsView = new CommentToolsView(activity);
		commentToolsView.setId(2);
		commentToolsView.ivMsgBg.setOnClickListener(this);
		commentToolsView.llInputComs.setOnClickListener(this);
		commentToolsView.setBlackBackground();
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlPage.addView(commentToolsView, lp);
		//评论数
		commentToolsView.setCommentsCount(page.getImgNews().comments.get());

		mvPage = new MobViewPager(activity);
		ImageDetailAdapter adapter = new ImageDetailAdapter(this, page.getImgNews());
		mvPage.setAdapter(adapter);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.ABOVE, 2);
		rlPage.addView(mvPage, lp);

		int dp5 = ResHelper.dipToPx(activity, 5);
		int dp14 = ResHelper.dipToPx(activity, 14);
		ivClose = new ImageView(activity);
		int resId = ResHelper.getBitmapRes(activity, "cmssdk_default_close");
		ivClose.setImageResource(resId);
		ivClose.setOnClickListener(this);
		ivClose.setPadding(dp5, dp5, dp5, dp5);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = dp5;
		lp.leftMargin = dp14;
		rlPage.addView(ivClose, lp);
		
		//图片描述
		tvImgDetail = new TextView(activity);
		int dp15 = ResHelper.dipToPx(activity, 15);
		tvImgDetail.setPadding(dp15, dp15, dp15, dp15);
		tvImgDetail.setBackgroundColor(0x80000000);
		tvImgDetail.setVerticalScrollBarEnabled(true);
		tvImgDetail.setMovementMethod(new ScrollingMovementMethod());
		tvImgDetail.setTextColor(0xffffffff);
		tvImgDetail.setEllipsize(TextUtils.TruncateAt.END);
		tvImgDetail.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		int width = ResHelper.getScreenWidth(activity);
		float ratio = ((float) width) / 750;
		int height = (int) (420 * ratio);
		height = (ResHelper.getScreenHeight(activity) - height) / 2;
		//图片描述的布局高为，（屏幕高-图片高）/ 2 - 评论工具栏高。
		tvImgDetail.setMaxHeight(height);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ABOVE, 2);
		rlPage.addView(tvImgDetail, lp);
		if (page.getImgNews() != null && page.getImgNews().imgList.get() != null && page.getImgNews().imgList.get().length > 0) {
			onScreenChange(1, page.getImgNews().imgList.get()[0].imgDesc);
		}
	}
	
	public void onScreenChange(int index, String imgDesc) {
		if (!TextUtils.isEmpty(imgDesc)) {
			String all = String.valueOf(page.getImgNews().imgList.get().length);
			SpannableStringBuilder sb = new SpannableStringBuilder(index + "/" + all + " " + imgDesc); // 包装字体内容 
			sb.setSpan(new RelativeSizeSpan(0.8f), 1, all.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tvImgDetail.setText(sb);
		}
	}

	public void onClick(View view) {
		if (view == ivClose) {
			finish();
		} else {
			if (!page.getImgNews().openingComment.get()) {
				int resId = ResHelper.getStringRes(getPage().getContext(), "cmssdk_default_donot_comment");
				ToastUtils.show(getPage().getContext(), resId, Toast.LENGTH_SHORT);
			}
			CommentListPage page = new CommentListPage(getPage().getTheme());
			page.setCategory(getPage().getCategory());
			page.setComNews(getPage().getImgNews());
			page.setUser(getPage().getUser());
			if (view == commentToolsView.llInputComs) {
				//点击评论输入框时，评论页自动打开评论输入框
				page.openCommendInputView();
			}
			page.show(MobSDK.getContext(), null);
		}
	}
	
	public void setHideOtherView() {
		if (hideOtherView) {
			hideOtherView = false;
			ivClose.setVisibility(View.INVISIBLE);
			tvImgDetail.setVisibility(View.INVISIBLE);
			commentToolsView.setVisibility(View.INVISIBLE);
		} else {
			hideOtherView = true;
			ivClose.setVisibility(View.VISIBLE);
			tvImgDetail.setVisibility(View.VISIBLE);
			commentToolsView.setVisibility(View.VISIBLE);
		}
	}
}
