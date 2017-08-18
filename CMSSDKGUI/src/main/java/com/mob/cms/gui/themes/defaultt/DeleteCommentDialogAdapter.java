package com.mob.cms.gui.themes.defaultt;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.cms.gui.dialog.DeleteCommentDialog;
import com.mob.cms.gui.themes.defaultt.components.ScrollUpAndDownDialogAdapter;
import com.mob.tools.utils.ResHelper;

public class DeleteCommentDialogAdapter extends ScrollUpAndDownDialogAdapter<DeleteCommentDialog> implements OnClickListener {
	private DeleteCommentDialog dialog;
	
	public void init(DeleteCommentDialog dialog) {
		this.dialog = dialog;
		super.init(dialog);
	}

	protected void initBodyView(LinearLayout llBody) {
		Context context = dialog.getContext();
		int dp10 = ResHelper.dipToPx(context, 10);
		llBody.setPadding(dp10, dp10, dp10, dp10);

		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundResource(ResHelper.getBitmapRes(context, "cmssdk_default_dialog_delete_comment_bg"));
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llBody.addView(ll, lp);

		TextView tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 14));
		tv.setTextColor(0xff3b3947);
		tv.setGravity(Gravity.CENTER);
		tv.setText(ResHelper.getStringRes(context, "cmssdk_default_if_detele_comment"));
		int dp43 = ResHelper.dipToPx(context, 43);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp43);
		ll.addView(tv, lp);

		View vLine = new View(context);
		vLine.setBackgroundColor(0xffeeeeee);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
		ll.addView(vLine, lp);

		tv = new TextView(context);
		tv.setId(1);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 16));
		tv.setTextColor(0xffe66159);
		tv.setGravity(Gravity.CENTER);
		tv.setText(ResHelper.getStringRes(context, "cmssdk_default_detele_comment"));
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp43);
		ll.addView(tv, lp);
		tv.setOnClickListener(this);

		tv = new TextView(context);
		tv.setId(2);
		tv.setBackgroundResource(ResHelper.getBitmapRes(context, "cmssdk_default_dialog_delete_comment_bg"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 14));
		tv.setTextColor(0xff3b3947);
		tv.setGravity(Gravity.CENTER);
		tv.setText(ResHelper.getStringRes(context, "cmssdk_default_cancel"));
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp43);
		lp.topMargin = ResHelper.dipToPx(context, 15);
		llBody.addView(tv, lp);
		tv.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == 1) {
			dialog.getCallback().onSuccess(null);
		}
		dialog.dismiss();
	}

}
