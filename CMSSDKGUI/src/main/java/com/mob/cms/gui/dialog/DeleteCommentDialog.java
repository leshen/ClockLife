package com.mob.cms.gui.dialog;

import android.content.Context;

import com.mob.cms.Callback;
import com.mob.jimu.gui.Dialog;
import com.mob.jimu.gui.Theme;

import java.util.HashMap;

public class DeleteCommentDialog extends Dialog<DeleteCommentDialog> {
	private Callback callback;
	public DeleteCommentDialog(Context context, Theme theme) {
		super(context, theme);
	}

	protected void applyParams(HashMap<String, Object> params) {
		callback = (Callback) params.get("callback");
	}
	
	public Callback getCallback() {
		return callback;
	}

	public static class Builder extends Dialog.Builder<DeleteCommentDialog> {

		public Builder(Context context, Theme theme) {
			super(context, theme);
		}

		protected DeleteCommentDialog createDialog(Context context, Theme theme) {
			return new DeleteCommentDialog(context, theme);
		}
		
		public void setCallback(Callback callback) {
			set("callback", callback);
		}
		
	}
	
}
