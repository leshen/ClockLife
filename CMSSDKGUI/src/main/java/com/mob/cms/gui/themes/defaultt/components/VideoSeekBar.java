package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VideoSeekBar extends View implements VideoSeekable {
	private int max;
	private int sbProgress;
	private int barHeight;
	private int filledColor;
	private int backgroundColor;
	private int iconColor;
	private OnSrecSeekBarChangeListener listener;
	private boolean onMove;
	
	public VideoSeekBar(Context context) {
		super(context);
	}

	public VideoSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VideoSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public synchronized void setMax(int m) {
		if (m < 0) {
			m = 0;
		}
		
		if (m != max) {
			max = m;
			if (sbProgress > m) {
				sbProgress = m;
			}
			postInvalidate();
		}
	}
	
	public synchronized int getMax() {
		return max;
	}
	
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			progress = 0;
		}

		if (progress > max) {
			progress = max;
		}

		if (progress != sbProgress) {
			setProgress(progress, false);
			postInvalidate();
		}
	}
	
	private void setProgress(int progress, boolean fromUser) {
		sbProgress = progress;
		if (listener != null) {
			listener.onProgressChanged(this, sbProgress, fromUser);
		}
	}
	
	public synchronized void setBarHeight(int height) {
		barHeight = height;
		postInvalidate();
	}
	
	public synchronized void setFilledColor(int color) {
		filledColor = color;
		postInvalidate();
	}
	
	public synchronized void setBackgroundColor(int color) {
		backgroundColor = color;
		postInvalidate();
	}
	
	public synchronized void setIconColor(int color) {
		iconColor = color;
		postInvalidate();
	}
	
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		reinitBarHeight(height);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(backgroundColor);
		float top = ((float) (height - barHeight)) / 2;
		RectF rectf = new RectF(0, top, width, top + barHeight);
		float round = ((float) barHeight) / 2;
		canvas.drawRoundRect(rectf, round, round, paint);
		
		if (max > 0) {
			paint.setColor(filledColor);
			float filledWidth = ((float) width) * sbProgress / max;
			rectf = new RectF(0, top, filledWidth, top + barHeight);
			canvas.drawRoundRect(rectf, round, round, paint);
			
			paint.setColor(iconColor);
			float radius = ((float) height) / 2;
			float progressWidth = width - height;
			float progressLeft = radius + progressWidth * sbProgress / max;
			rectf = new RectF(progressLeft - radius, 0, progressLeft + radius, height);
			canvas.drawArc(rectf, 0, 360, true, paint);
		}
	}
	
	private void reinitBarHeight(int height) {
		if (barHeight <= 0) {
			barHeight = 1;
		}
		
		if (barHeight > height) {
			barHeight = height;
		}
	}
	
	public void setOnSeekBarChangeListener(OnSrecSeekBarChangeListener l) {
		listener = l;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				float x = event.getX();
				updateProgress((int) (max * x / getWidth()));
				return true;
			}
			case MotionEvent.ACTION_MOVE: {
				if (!onMove) {
					onMove = true;
					if (listener != null) {
						listener.onStartTrackingTouch(this);
					}
				}
				
				float x = event.getX();
				updateProgress((int) (max * x / getWidth()));
			} break;
			case MotionEvent.ACTION_UP: 
			case MotionEvent.ACTION_CANCEL: {
				if (onMove) {
					onMove = false;
					if (listener != null) {
						listener.onStopTrackingTouch(this);
					}
				}
			} break;
		}
		return super.onTouchEvent(event);
	}
	
	private void updateProgress(int progress) {
		if (progress < 0) {
			progress = 0;
		}

		if (progress > max) {
			progress = max;
		}
		
		if (progress != sbProgress) {
			setProgress(progress, true);
			postInvalidate();
		}
	}
	
	public int getProgress() {
		return sbProgress;
	}

	/**
	 * A callback that notifies clients when the progress level has been
	 * changed. This includes changes that were initiated by the user through a
	 * touch gesture or arrow key/trackball as well as changes that were initiated
	 * programmatically.
	 */
	public interface OnSrecSeekBarChangeListener {

		/**
		 * Notification that the progress level has changed. Clients can use the fromUser parameter
		 * to distinguish user-initiated changes from those that occurred programmatically.
		 *
		 * @param seekBar The SeekBar whose progress has changed
		 * @param progress The current progress level. This will be in the range 0..max where max
		 *		was set by {@link VideoSeekBar#setMax(int)}. (The default value for max is 100.)
		 * @param fromUser True if the progress change was initiated by the user.
		 */
		void onProgressChanged(VideoSeekable seekBar, int progress, boolean fromUser);

		/**
		 * Notification that the user has started a touch gesture. Clients may want to use this
		 * to disable advancing the seekbar. 
		 * @param seekBar The SeekBar in which the touch gesture began
		 */
		void onStartTrackingTouch(VideoSeekable seekBar);

		/**
		 * Notification that the user has finished a touch gesture. Clients may want to use this
		 * to re-enable advancing the seekbar. 
		 * @param seekBar The SeekBar in which the touch gesture began
		 */
		void onStopTrackingTouch(VideoSeekable seekBar);

	}
}
