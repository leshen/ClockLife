package slmodule.shenle.com.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.app.Dialog;

import slmodule.shenle.com.R;

public class LoadingDialog extends Dialog {
	public LoadingDialog(Context context) {
		super(context);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_loading_dialog);
		ImageView loadingIv = (ImageView) findViewById(R.id.iv_loading);
		AnimationDrawable animationDrawable = (AnimationDrawable) loadingIv
				.getDrawable();
		animationDrawable.start();
	}
	
}
