package com.mob.ums.gui.themes.defaultt;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.pages.dialog.AddFriendDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.ItemUserView;

public class AddFriendDialogAdapter extends DefaultDialogAdapter<AddFriendDialog> {
	private AddFriendDialog dialog;
	private EditText etMessage;
	private ProgressDialog pd;
	
	public void init(final AddFriendDialog dialog) {
		this.dialog = dialog;
		Context context = dialog.getContext();
		final User user = dialog.getUser();
		
		//setup view
		LinearLayout containLayout = new LinearLayout(context);
		containLayout.setBackgroundResource(ResHelper.getBitmapRes(context, "umssdk_default_dialog_prompt"));
		containLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.setContentView(containLayout, lp);
		
		// body
		LinearLayout llBody = new LinearLayout(context);
		llBody.setOrientation(LinearLayout.VERTICAL);
		int dp54 = ResHelper.dipToPx(context, 27);
		int dp20 = ResHelper.dipToPx(context, 20);
		llBody.setPadding(dp20, dp54, dp20, dp20);
		containLayout.addView(llBody);
		
		// user item
		ItemUserView vUser = new ItemUserView(context);
		vUser.setBackgroundColor(0);
		vUser.setPadding(0, 0, 0, 0);
		vUser.setUser(user);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.bottomMargin = ResHelper.dipToPx(context, 23);
		llBody.addView(vUser, lp);
		
		// message
		etMessage = new EditText(context);
		etMessage.setBackgroundResource(ResHelper.getBitmapRes(context, "umssdk_defalut_add_friend_message"));
		etMessage.setTextColor(0xff333333);
		etMessage.setHintTextColor(0xff969696);
		etMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		etMessage.setHint(ResHelper.getStringRes(context, "umssdk_default_enter_message"));
		etMessage.setGravity(Gravity.LEFT | Gravity.TOP);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 94));
		llBody.addView(etMessage, lp);
		
		//line
		View vLine = new View(context);
		vLine.setBackgroundColor(0x7f979797);
		int dp1 = ResHelper.dipToPx(context, 1);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp1);
		containLayout.addView(vLine, lp);
		
		LinearLayout llBottom = new LinearLayout(context);
		llBottom.setBackgroundResource(ResHelper.getStringRes(context, "umssdk_default_dialog_button"));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 43));
		containLayout.addView(llBottom, lp);
		
		//btnCancel
		TextView tvCancel = new TextView(context);
		tvCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvCancel.setTextColor(0xffc4554c);
		tvCancel.setGravity(Gravity.CENTER);
		tvCancel.setBackgroundResource(ResHelper.getStringRes(context, "umssdk_default_dialog_btn_left"));
		tvCancel.setText(ResHelper.getStringRes(context, "umssdk_default_cancel"));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llBottom.addView(tvCancel, lp);
		tvCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		vLine = new View(context);
		vLine.setBackgroundColor(0x7f979797);
		lp = new LayoutParams(dp1, LayoutParams.MATCH_PARENT);
		llBottom.addView(vLine, lp);
		
		//btnOK
		TextView tvOK = new TextView(context);
		tvOK.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvOK.setTextColor(0xffc4554c);
		tvOK.setGravity(Gravity.CENTER);
		tvOK.setBackgroundResource(ResHelper.getStringRes(context, "umssdk_default_dialog_btn_right"));
		tvOK.setText(ResHelper.getStringRes(context, "umssdk_default_add_as_friend"));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llBottom.addView(tvOK, lp);
		tvOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				addFriend(user, etMessage.getText().toString().trim());
			}
		});
	}
	
	private void addFriend(User user, String message) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(dialog.getContext(), dialog.getTheme()).show();
		final OperationCallback<Void> cb = dialog.getCallback();
		UMSSDK.addFriend(user, message, new OperationCallback<Void>() {
			public void onSuccess(Void data) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				if (cb != null) {
					cb.onSuccess(data);
				}
			}
			
			public void onFailed(Throwable t) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				if (cb != null) {
					cb.onFailed(t);
				}
			}
		});
	}
	
	public void onDismiss(AddFriendDialog dialog, Runnable afterDismiss) {
		DeviceHelper.getInstance(dialog.getContext()).hideSoftInput(etMessage);
		super.onDismiss(dialog, afterDismiss);
	}
	
}
