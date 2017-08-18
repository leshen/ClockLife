package com.mob.cms.gui.themes.defaultt;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.cms.Comment;
import com.mob.cms.gui.dialog.SendCommentDialog;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.cms.gui.themes.defaultt.components.ScrollUpAndDownDialogAdapter;
import com.mob.tools.utils.ResHelper;

public class SendCommentDialogAdapter extends ScrollUpAndDownDialogAdapter<SendCommentDialog> implements OnClickListener {
	private SendCommentDialog dialog;
	private TextView tvSend;
	private EditText etWriteComment;
	
	public void init(SendCommentDialog dialog) {
		this.dialog = dialog;
		super.init(dialog);
	}

	protected void initBodyView(LinearLayout llBody) {
		Context context = dialog.getContext();
		llBody.setBackgroundColor(0xfff4f5f6);

		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		int dp44 = ResHelper.dipToPx(context, 44);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp44);
		int dp15 = ResHelper.dipToPx(context, 15);
		lp.leftMargin = dp15;
		lp.rightMargin = dp15;
		llBody.addView(ll, lp);

		TextView tv = new TextView(context);
		tv.setId(1);
		tv.setOnClickListener(this);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 15));
		tv.setTextColor(0xff3b3947);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText(ResHelper.getStringRes(context, "cmssdk_default_cancel"));	
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		ll.addView(tv, lp);

		tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 16));
		tv.setTextColor(0xff3b3947);
		tv.setGravity(Gravity.CENTER);
		tv.setText(ResHelper.getStringRes(context, "cmssdk_default_write_comment"));
		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		ll.addView(tv, lp);

		tvSend = new TextView(context);
		tvSend.setId(2);
		tvSend.setOnClickListener(this);
		tvSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 15));
		tvSend.setTextColor(0xff999999);
		tvSend.setGravity(Gravity.CENTER);
		tvSend.setText(ResHelper.getStringRes(context, "cmssdk_default_send"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		ll.addView(tvSend, lp);

		etWriteComment = new EditText(context);
		etWriteComment.setGravity(Gravity.LEFT);
		etWriteComment.setBackgroundColor(0xffffffff);
		etWriteComment.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
			}

			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
			}

			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					tvSend.setTextColor(0xff999999);
					tvSend.setEnabled(false);
				} else {
					tvSend.setTextColor(0xff3b3947);
					tvSend.setEnabled(true);
				}
			}
		});
		int dp16 = ResHelper.dipToPx(context, 16);
		int dp100 = ResHelper.dipToPx(context, 100);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp100);
		lp.setMargins(dp15, 0, dp15, dp16);
		llBody.addView(etWriteComment, lp);
	}

	public void onClick(View view) {
		if (view == tvSend) {
			final Comment comment = new Comment();
			comment.content.set(etWriteComment.getText().toString());
			UserBrief user = dialog.getUser();
			if (user == null) {
				user = new UserBrief(UserBrief.USER_ANONYMOUS, null, null, null);
			}
			switch (user.type) {
				case UserBrief.USER_UMSSDK: {
					comment.uid.set(user.uid);
					comment.nickname.set(user.nickname);
					comment.avatar.set(user.avatarUrl);
					CMSSDK.commentNewsFromUMSSDKUser(dialog.getCommentNews(), comment, new Callback<Void>(){
						public void onSuccess(Void data) {
							dialog.getCallback().onSuccess(comment);
						}

						public void onFailed(Throwable t) {
							dialog.getCallback().onFailed(t);
						}
					});
				} break;
				case UserBrief.USER_CUSTOM: {
					comment.uid.set(user.uid);
					comment.nickname.set(user.nickname);
					comment.avatar.set(user.avatarUrl);
					CMSSDK.commentNewsFromCustomUser(dialog.getCommentNews(), comment, new Callback<Void>(){
						public void onSuccess(Void data) {
							dialog.getCallback().onSuccess(comment);
						}

						public void onFailed(Throwable t) {
							dialog.getCallback().onFailed(t);
						}
					});
				} break;
				case UserBrief.USER_ANONYMOUS: {
					CMSSDK.commentNewsFromAnonymousUser(dialog.getCommentNews(), comment, new Callback<Void>(){
						public void onSuccess(Void data) {
							dialog.getCallback().onSuccess(comment);
						}

						public void onFailed(Throwable t) {
							dialog.getCallback().onFailed(t);
						}
					});
				} break;
			}
		}
		//隐藏输入法
		InputMethodManager imm = (InputMethodManager)dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		//关闭Dialog
		dialog.dismiss();
	}
}
