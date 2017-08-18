package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

//#if def{lang} == cn
/** 在At好友页面中，下拉刷新列表头部的旋转箭头 */
//#elif def{lang} == en
/** the rotating arrow icon of the header view of pull-to-refresh list in the “at” page */
//#endif
public class RotateImageView extends ImageView {
	private float rotation;

	public RotateImageView(Context context) {
		super(context);
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
		invalidate();
	}

	protected void onDraw(Canvas canvas) {
		canvas.rotate(rotation, getWidth() / 2, getHeight() / 2);
		super.onDraw(canvas);
	}

}