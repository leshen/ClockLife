package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

public class ImageViewItem extends RelativeLayout {
	
	public AsyncImageView asyncImageView;
	
	public ImageViewItem(Context context) {
		super(context);
		initView(context);
	}

	public ImageViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ImageViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	private void initView(Context context) {
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(lp);

		final int width = ResHelper.getScreenWidth(context);
		asyncImageView = new AsyncImageView(context){
			public void setImageBitmap(Bitmap bm) {
				if (bm != null) {
					int h = bm.getHeight();
					int w = bm.getWidth();
					float ratio = ((float) width) / w;
					int height = (int) (h * ratio);
					ViewGroup.LayoutParams lp = getLayoutParams();
					lp.width = width;
					lp.height = height;
					setLayoutParams(lp);
					super.setImageBitmap(bm);
				}
			}
		};
		asyncImageView.setScaleToCropCenter(true);
	
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_IN_PARENT);
		this.addView(asyncImageView, lp);
	}
	
}
