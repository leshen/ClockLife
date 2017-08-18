package com.mob.cms.gui.themes.defaultt;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.Category;
import com.mob.cms.News;
import com.mob.cms.gui.pages.CommentListPage;
import com.mob.cms.gui.pages.NewsListPage;
import com.mob.cms.gui.pages.VideoDetailPage;
import com.mob.cms.gui.themes.defaultt.components.CommentToolsView;
import com.mob.cms.gui.themes.defaultt.components.ToastUtils;
import com.mob.cms.gui.themes.defaultt.components.VideoDetailListAdapter;
import com.mob.cms.gui.themes.defaultt.components.VideoPlayView;
import com.mob.cms.gui.themes.defaultt.components.VideoPlayView.OnProgressChangeListener;
import com.mob.cms.gui.themes.defaultt.components.VideoPlayerController;
import com.mob.cms.gui.themes.defaultt.components.VideoSeekBar;
import com.mob.cms.gui.themes.defaultt.components.VideoSeekable;
import com.mob.cms.gui.themes.defaultt.components.VideoViewItem0;
import com.mob.jimu.gui.PageAdapter;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.ResHelper;

public class VideoDetailPageAdapter extends PageAdapter<VideoDetailPage> implements View.OnClickListener,
		OnPreparedListener, OnProgressChangeListener, OnCompletionListener, Handler.Callback, VideoSeekBar.OnSrecSeekBarChangeListener {
	private static final int MSG_HIDE = 1;
	private static final int AUTO_HIDE_INTERVAL = 3000; // 3s后自动隐藏

	private View vProgress;
	private VideoPlayView vvPlayer;
	private VideoPlayerController controller;
	private PullToRequestView requestView;
	private CommentToolsView commentToolsView;
	private RelativeLayout rlVideo;
	private VideoViewItem0 videoDesView;
	
	private Handler handler;
	private boolean vvPrepare;
	private boolean fullScreen;
	private Activity activity;

	private News videoNews;
	private Category category;
	private VideoDetailPage page;

	public void onCreate(VideoDetailPage page, Activity activity) {
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0xffffffff));
		super.onCreate(page, activity);
		this.activity = activity;
		videoNews = page.getVideoNews();
		category = page.getCategory();
		this.page = page;
		initPage(activity);
	}

	private void initPage(final Activity activity) {
		RelativeLayout rl = new RelativeLayout(activity);
		LayoutParams rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		activity.setContentView(rl, rllp);
		
		//视频播放的ViewGroup
		rlVideo = new RelativeLayout(activity);
		rlVideo.setBackgroundColor(0xff000000);
		rlVideo.setId(1);
		float ratio = ((float)ResHelper.getScreenWidth(activity)) / 750;
		int height = (int) (420 * ratio);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rl.addView(rlVideo, rllp);

		//视频播放器
		vvPlayer = new VideoPlayView(activity);
		vvPlayer.setOnPreparedListener(this);
		vvPlayer.setOnProgressChangeListener(this);
		vvPlayer.setOnCompletionListener(this);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
		rlVideo.addView(vvPlayer, rllp);
		
		//隐藏视频控制bar后，显示的进度条
		int dp2 = ResHelper.dipToPx(activity, 2);
		RelativeLayout rlPro = new RelativeLayout(activity);
		rlPro.setBackgroundColor(0xffbfbfbf);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, dp2);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlVideo.addView(rlPro, rllp);
		//进度条
		vProgress = new View(activity);
		vProgress.setBackgroundColor(0xffd43d3d);
		vProgress.setVisibility(View.GONE);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, dp2);
		rlPro.addView(vProgress, rllp);
		
		//视频播放的控制bar
		controller = new VideoPlayerController(activity);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
		rlVideo.addView(controller, rllp);
		controller.setOnClickListener(this);
		controller.ivPlay.setOnClickListener(this);
		controller.ivClose.setOnClickListener(this);
		controller.sbProgress.setOnSeekBarChangeListener(this);
		controller.ivFullScr.setOnClickListener(this);
		controller.ivPlay.setVisibility(View.INVISIBLE);
		controller.pb.setVisibility(View.INVISIBLE);
		
		handler = new Handler(this);
		handler.removeMessages(MSG_HIDE);
		handler.sendEmptyMessageDelayed(MSG_HIDE, AUTO_HIDE_INTERVAL);
		
		videoDesView = new VideoViewItem0(activity);
		videoDesView.setId(2);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.BELOW, 1);
		rl.addView(videoDesView, rllp);
		videoDesView.tvVideoTitle.setText(videoNews.title.get());
		videoDesView.setViedoPlayTimes(videoNews.reads.get());
		videoDesView.setVideoDetail(videoNews.updateAt.get(), videoNews.videoDesc.get());
		videoDesView.setLikeStatus(getPage().getUser(), videoNews);
		videoDesView.tvLikeCounts.setText(String.valueOf(videoNews.likes.get()));
		videoDesView.ivLike.setOnClickListener(this);
		
		//视频下部分的滑动列表
		requestView = new PullToRequestView(activity);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		if (videoNews.openingComment.get()) {
			int dp49 = ResHelper.dipToPx(activity, 49);
			rllp.bottomMargin = dp49;
		}
		rllp.addRule(RelativeLayout.BELOW, 2);
		rl.addView(requestView, rllp);

		final VideoDetailListAdapter adapter = new VideoDetailListAdapter(getPage(), requestView);
		adapter.setUser(getPage().getUser());
		adapter.setData(category, videoNews);
		adapter.setVideoChangeListener(new VideoDetailListAdapter.VideoChangeListener() {
			public void onVideoChange(News videoNews) {
				onVideoChangeListener(videoNews);
				adapter.notifyDataSetChanged();
			}
		});
		requestView.setAdapter(adapter);
		requestView.performPullingDown(true);

		//设置视频地址
		final String videoUrl = videoNews.videoUrl.get();//"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
		if (!TextUtils.isEmpty(videoUrl)) {
			try {
				controller.pb.setVisibility(View.VISIBLE);
				vvPlayer.setVideo(Uri.parse(videoUrl));
				vvPlayer.start();
			} catch (Throwable t) {

			} finally {
				controller.pb.setVisibility(View.INVISIBLE);
			}
		}
		
		if (!videoNews.openingComment.get()) {
			return;//文章不能评论时，不添加评论框
		}
		//视频下面的评论框
		commentToolsView = new CommentToolsView(activity);
		commentToolsView.setCommentsCount(videoNews.comments.get());
		commentToolsView.ivMsgBg.setOnClickListener(this);
		commentToolsView.llInputComs.setOnClickListener(this);
		rllp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rl.addView(commentToolsView, rllp);
	}

	public void onClick(View view) {
		if (view == controller.ivPlay) {
			try {
				if (vvPlayer.isPlaying()) {
					vvPlayer.pause();
					controller.ivPlay.setImageBitmap(VideoPlayerController.bmStop);
				} else {
					vvPlayer.resume();
					controller.ivPlay.setImageBitmap(VideoPlayerController.bmPlay);
				}
			} catch (Throwable t) {}
		} else if (view == controller.ivClose) {
			if (fullScreen) {
				fullScreen = !fullScreen;
				setOtherViewsVisibility(fullScreen);
			} else {
				finish();
			}
		} else if (view == controller.ivFullScr) {
			fullScreen = !fullScreen;
			setOtherViewsVisibility(fullScreen);
		} else if (view == controller) {
			if (controller.llBar.getVisibility() != View.VISIBLE ){
				showControllerBar(true);
			} else {
				showControllerBar(false);
			}
		} else if (view == commentToolsView.ivMsgBg || view == commentToolsView.llInputComs) {
			if (!videoNews.openingComment.get()) {
				int resId = ResHelper.getStringRes(getPage().getContext(), "cmssdk_default_donot_comment");
				ToastUtils.show(getPage().getContext(), resId, Toast.LENGTH_SHORT);
				return;
			}
			CommentListPage page = new CommentListPage(getPage().getTheme());
			page.setCategory(category);
			page.setComNews(videoNews);
			page.setUser(getPage().getUser());
			if (view == commentToolsView.llInputComs) {
				//点击输入框时，跳转到评论页面，并且打开评论输入框
				page.openCommendInputView();
			}
			page.show(MobSDK.getContext(), null);
		} else if (view == videoDesView.ivLike) {
			like();
		}
	}

	public void onVideoChangeListener (News video) {
		this.videoNews = video;
		if (videoNews != null) {
			String videoUrl = videoNews.videoUrl.get();
			if (!TextUtils.isEmpty(videoUrl)) {
				controller.pb.setVisibility(View.VISIBLE);
				try {
					vvPlayer.stop();
					vvPlayer.seekTo(0);
					vvPlayer.setVideo(Uri.parse(videoUrl));
					vvPlayer.start();
				} catch (Throwable t) {
					
				} finally {
					controller.ivPlay.setImageBitmap(VideoPlayerController.bmPlay);
					controller.pb.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	public void onCompletion(MediaPlayer mp) {
		if (mp.isPlaying()) {
			mp.pause();
		}
		try {
			vvPlayer.seekTo(0);
		} catch (Throwable t) {}
		controller.tvTime.setText(progressToTime(0));
		controller.sbProgress.setProgress(0);
		controller.ivPlay.setImageBitmap(VideoPlayerController.bmStop);
		vProgress.setVisibility(View.GONE);
	}

	public void onPrepared(MediaPlayer mp) {
		vvPrepare = true;
		controller.pb.setVisibility(View.INVISIBLE);

		int w = mp.getVideoWidth();
		while (w <= 0) {
			try {
				Thread.sleep(10);
			} catch (Throwable t) {}
			w = mp.getVideoWidth();
		}
		int[] src = new int[] {mp.getVideoWidth(), mp.getVideoHeight()};
		int[] target = new int[] {vvPlayer.getWidth(), vvPlayer.getHeight()};
		int[] dst = BitmapHelper.fixRect(src, target);
		vvPlayer.resize(dst[0], dst[1]);

		int total = mp.getDuration();
		int progress = mp.getCurrentPosition();
		controller.tvTime.setText(progressToTime(progress));
		controller.sbProgress.setProgress(progress);
		controller.sbProgress.setMax(total);
		controller.tvDuration.setText(progressToTime(total));
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case MSG_HIDE: {
				showControllerBar(false);
			} break;
		}
		return false;
	}

	public void onProgressChange(int progress) {
		controller.tvTime.setText(progressToTime(progress));
		controller.sbProgress.setProgress(progress);
		
		int max = controller.sbProgress.getMax();
		float percent = ((float)progress) / max;
		LayoutParams lpProgress = (LayoutParams) vProgress.getLayoutParams();
		lpProgress.width = (int) (ResHelper.getScreenWidth(activity) * percent);
		vProgress.setLayoutParams(lpProgress);
		if (percent > 0 && percent < 1) {
			vProgress.setVisibility(View.VISIBLE);
		} else {
			vProgress.setVisibility(View.GONE);
		}
	}

	private void setOtherViewsVisibility(boolean fullScreen) {
		Bitmap bm = fullScreen ? VideoPlayerController.bmToView : VideoPlayerController.bmFullScr;
		controller.ivFullScr.setImageBitmap(bm);
		if (!fullScreen) {
			page.requestFullScreen(false);
			page.requestPortraitOrientation();
			requestView.setVisibility(View.VISIBLE);
			commentToolsView.setVisibility(View.VISIBLE);
			float ratio = ((float) ResHelper.getScreenWidth(activity)) / 750;
			int height = (int) (420 * ratio);
			rlVideo.getLayoutParams().height = height;
			vvPlayer.getLayoutParams().height = height;
			resize(ResHelper.getScreenWidth(activity), height);
			rlVideo.requestLayout();
		} else {
			page.requestFullScreen(true);
			page.requestLandscapeOrientation();
			requestView.setVisibility(View.GONE);
			commentToolsView.setVisibility(View.GONE);
			rlVideo.getLayoutParams().height = LayoutParams.MATCH_PARENT;
			vvPlayer.getLayoutParams().height = LayoutParams.MATCH_PARENT;
			resize(ResHelper.getScreenWidth(activity), ResHelper.getScreenHeight(activity));
			rlVideo.requestLayout();
		}
	}

	public void resize(int width, int height) {
		vvPlayer.resize(width, height);
		LayoutParams lp = (LayoutParams) controller.getLayoutParams();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = height;
		controller.setLayoutParams(lp);
	}

	/** 进度条改变的监听 */
	public void onProgressChanged(VideoSeekable seekBar, int progress, boolean fromUser) {
		controller.tvTime.setText(progressToTime(progress));
	}

	/** 开始拖动进度条的监听 */
	public void onStartTrackingTouch(VideoSeekable seekBar) {
		handler.removeMessages(MSG_HIDE);
		handler.sendEmptyMessageDelayed(MSG_HIDE, AUTO_HIDE_INTERVAL);
	}

	public void onStopTrackingTouch(VideoSeekable seekBar) {
		try {
			vvPlayer.seekTo(controller.sbProgress.getProgress());
		} catch (Throwable t) {}
	}

	public void onResume(VideoDetailPage page, Activity activity) {
		try {
			if (vvPrepare && !vvPlayer.isPlaying()) {
				vvPlayer.resume();
				showControllerBar(true);
			}
		} catch (Throwable t) {}
	}

	public void onPause(VideoDetailPage page, Activity activity) {
		try {
			if (vvPlayer.isPlaying()) {
				vvPlayer.pause();
			}
		} catch (Throwable t) {}
	}

	public void onDestroy(VideoDetailPage page, Activity activity) {
		try {
			if (vvPlayer.isPlaying()) {
				vvPlayer.pause();
			}
			vvPlayer.stop();
			vvPlayer.release();
		} catch (Throwable t) {}
	}

	/** 显示视频控制栏 */
	private void showControllerBar(boolean show) {
		if (show) {
			controller.ivClose.setVisibility(View.VISIBLE);
			controller.llBar.setVisibility(View.VISIBLE);
			controller.ivPlay.setVisibility(View.VISIBLE);
			handler.removeMessages(MSG_HIDE);
			handler.sendEmptyMessageDelayed(MSG_HIDE, AUTO_HIDE_INTERVAL);
		} else {
			controller.ivPlay.setVisibility(View.INVISIBLE);
			controller.ivClose.setVisibility(View.INVISIBLE);
			controller.llBar.setVisibility(View.INVISIBLE);
		}
	}


	private void like() {
		NewsListPage.UserBrief user = getPage().getUser();
		if (user == null) {
			user = new NewsListPage.UserBrief(NewsListPage.UserBrief.USER_ANONYMOUS, null, null, null);
		}
		switch (user.type) {
			case NewsListPage.UserBrief.USER_UMSSDK: {
				CMSSDK.likeNewsFromUMSSDKUser(videoNews, new Callback<Void>(){
					public void onSuccess(Void data) {
						int reads = videoNews.likes.get() + 1;
						videoNews.likes.set(reads);
						videoDesView.ivLike.setSelected(true);
						videoDesView.tvLikeCounts.setText(String.valueOf(reads));
					}
				});
			} break;
			case NewsListPage.UserBrief.USER_CUSTOM: {
				CMSSDK.likeNewsFromCustomUser(videoNews, user.uid, user.nickname, user.avatarUrl,
						new Callback<Void>(){
							public void onSuccess(Void data) {
								int reads = videoNews.likes.get() + 1;
								videoNews.likes.set(reads);
								videoDesView.ivLike.setSelected(true);
								videoDesView.tvLikeCounts.setText(String.valueOf(reads));
							}
						});
			} break;
			case NewsListPage.UserBrief.USER_ANONYMOUS: {
				CMSSDK.likeNewsFromAnonymousUser(videoNews, new Callback<Void>(){
					public void onSuccess(Void data) {
						int reads = videoNews.likes.get() + 1;
						videoNews.likes.set(reads);
						videoDesView.ivLike.setSelected(true);
						videoDesView.tvLikeCounts.setText(String.valueOf(reads));
					}
				});
			} break;
		}
	}


	private String progressToTime(int position) {
		int sec = position / 1000;
		int min = sec / 60;
		sec = sec % 60;
		if (sec < 10) {
			return min + ":0" + sec;
		} else {
			return min + ":" + sec;
		}
	}

}