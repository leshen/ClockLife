package com.mob.cms.gui.themes.defaultt;

import android.content.Context;

import com.mob.jimu.gui.DialogAdapter;
import com.mob.jimu.gui.PageAdapter;
import com.mob.jimu.gui.Theme;
import com.mob.tools.utils.ResHelper;

import java.util.Set;

public class DefaultTheme extends Theme {
	
	protected void initPageAdapters(Set<Class<? extends PageAdapter<?>>> adapters) {
		adapters.add(ImageDetailPageAdapter.class);
		adapters.add(NewsDetailPageAdapter.class);
		adapters.add(NewsListPageAdapter.class);
		adapters.add(VideoDetailPageAdapter.class);
		adapters.add(CommentListPageAdapter.class);
	}
	
	protected void initDialogAdapters(Set<Class<? extends DialogAdapter<?>>> adapters) {
		adapters.add(DeleteCommentDialogAdapter.class);
		adapters.add(SendCommentDialogAdapter.class);
		adapters.add(ProgressDialogAdapter.class);
		adapters.add(OKDialogAdapter.class);
	}

	public int getDialogStyle(Context context) {
		return ResHelper.getStyleRes(context, "cmssdk_default_DialogStyle");
	}
}
