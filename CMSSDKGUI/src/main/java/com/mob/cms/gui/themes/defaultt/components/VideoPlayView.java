package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.RelativeLayout.LayoutParams;

import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.UIHandler;

public class VideoPlayView extends SurfaceView implements OnPreparedListener, Callback {
	private MediaPlayer player;
	private boolean pausedByDestroy;
	private boolean released;
	private OnPreparedListener opListener;
	private Uri video;
	private Handler.Callback updateCb;
	private OnProgressChangeListener opcListener;
	private OnCompletionListener ocListener;
	private OnErrorListener oeListener;
	private boolean pauseAfterPrepared;

	public VideoPlayView(Context context) {
		super(context);
		init(context);
	}

	public VideoPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public VideoPlayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		getHolder().addCallback(this);
		
		player = new MediaPlayer(){
			public void start() throws IllegalStateException {
				setBackgroundColor(0);
				super.start();
			}
		};
		player.setOnPreparedListener(this);
		
		updateCb = new Handler.Callback() {
			public boolean handleMessage(Message msg) {
				if (!released && opcListener != null) {
					if (isPlaying()) {
						opcListener.onProgressChange(player.getCurrentPosition());
						UIHandler.sendEmptyMessageDelayed(1, 1000, this);
					}
				}
				return false;
			}
		};
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		player.setDisplay(getHolder());
		if (pausedByDestroy) {
			player.start();
		}
		pausedByDestroy = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		player.setDisplay(getHolder());
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (!released) {
			if (player.isPlaying()) {
				player.pause();
				pausedByDestroy = true;
			}
			player.setDisplay(null);
		}
	}
	
	// =====================================
	
	public void setOnPreparedListener(OnPreparedListener l) {
		opListener = l;
	}
	
	public void setOnProgressChangeListener(OnProgressChangeListener l) {
		opcListener = l;
	}
	
	public void setOnCompletionListener(OnCompletionListener l) {
		ocListener = l;
		player.setOnCompletionListener(l);
	}
	
	public void setOnErrorListener(OnErrorListener l) {
		oeListener = l;
		player.setOnErrorListener(l);
	}
	
	// =====================================
	
	public void setVideo(Uri video) {
		this.video = video;
	}
	
	public void start() throws Throwable {
		if (released) {
			return;
		}
		
		if (!player.isPlaying()) {
			player.setDataSource(getContext(), video);
			player.prepareAsync();
		}
	}
	
	public void onPrepared(MediaPlayer mp) {
		if (opListener != null) {
			opListener.onPrepared(mp);
		}
		
		if (!pauseAfterPrepared) {
			pauseAfterPrepared = false;
			mp.start();
		}
		UIHandler.sendEmptyMessage(1, updateCb);
	}
	
	public void pause() throws Throwable {
		if (player.isPlaying()) {
			player.pause();
		}
	}
	
	public void resume() throws Throwable {
		if (!player.isPlaying()) {
			player.start();
		}
		UIHandler.sendEmptyMessage(1, updateCb);
	}
	
	public void stop() throws Throwable {
		if (player.isPlaying()) {
			player.pause();
		}
		player.stop();
	}
	
	public void release() throws Throwable {
		player.release();
		released = true;
	}
	
	public void seekTo(int msec) throws Throwable {
		boolean resume = false;
		if (player.isPlaying()) {
			player.pause();
			resume = true;
		}
		player.seekTo(msec);
		if (resume) {
			player.start();
		}
	}
	
	public boolean isPlaying() {
		return released ? false : player.isPlaying();
	}
	
	public int getDuration() {
		if (released) {
			return 0;
		}
		return player.getDuration();
	}
	
	public void resize(int width, int height) {
		if (player != null) {
			try {
				int[] videoSize = {player.getVideoWidth(), player.getVideoHeight()};
				int[] target = {width, height};
				int[] dst = BitmapHelper.fixRect(videoSize, target);
				LayoutParams lp = (LayoutParams) getLayoutParams();
				lp.width = dst[0];
				lp.height = dst[1];
				setLayoutParams(lp);
			} catch (Throwable t) {
			}
		}
	}
	
	public void setPauseAfterPrepared() {
		pauseAfterPrepared = true;
	}

	public interface OnProgressChangeListener {
		public void onProgressChange(int progress);
	}
	
}
