package com.mob.cms.gui;

import android.os.Handler.Callback;
import android.os.Message;

import com.mob.MobSDK;
import com.mob.cms.CMSSDK;
import com.mob.cms.gui.pages.NewsListPage;
import com.mob.cms.gui.pages.NewsListPage.UserBrief;
import com.mob.jimu.gui.Theme;
import com.mob.tools.utils.UIHandler;

public class CMSGUI {
	private static Theme theme;
	//#if def{lang} == cn
	/** 设置UI主题*/
	//#elif def{lang} == en
	/** Setting the theme of UI*/
	//#endif
	public static <T extends Theme> void setTheme(Class<T> theme) {
		try {
			CMSGUI.theme = theme.newInstance();
		} catch(Throwable t) {
			//t.printStackTrace();
		}
	}
	//#if def{lang} == cn
	/** 以UMSSDK的用户进入新闻列表 */
	//#elif def{lang} == en
	/** The user in UMSSDK show the NewsListPage */
	//#endif
	public static <T extends Theme> void showNewsListPageWithUMSSDKUser(Class<T> themeOfUMSSDK) {
		try {
			final com.mob.ums.OperationCallback cb = new com.mob.ums.OperationCallback() {
				public void onSuccess(Object user) {
					com.mob.ums.User loginUser = (com.mob.ums.User) user;
					NewsListPage page = new NewsListPage(theme);
					if (loginUser != null) {
						String avatar = loginUser.avatar.get() == null ? null : loginUser.avatar.get()[2];
						page.setUser(UserBrief.USER_UMSSDK, loginUser.id.get(), loginUser.nickname.get(), loginUser.avatar.get()[0]);
					} else {
						page.setUser(UserBrief.USER_UMSSDK, null, null, null);
					}
					page.show(MobSDK.getContext(), null);
				}
			};
			if (CMSSDK.hasLoginWithUMSSDK()) {
				UIHandler.sendEmptyMessage(0, new Callback() {
					public boolean handleMessage(Message msg) {
						com.mob.ums.UMSSDK.getLoginUser(cb);
						return false;
					}
				});
			} else {
				com.mob.ums.gui.UMSGUI.setTheme(themeOfUMSSDK);
				com.mob.ums.gui.UMSGUI.showLogin(cb);
			}
		} catch (Throwable t) {}
	}

	//#if def{lang} == cn
	/** 以当前应用的用户进入新闻列表 */
	//#elif def{lang} == en
	/** Using current params to show the NewsListPage*/
	//#endif
	public static void showNewsListPageWithCustomUser(String uid, String nickname, String avatarUrl) {
		NewsListPage page = new NewsListPage(theme);
		page.setUser(UserBrief.USER_CUSTOM, uid, nickname, avatarUrl);
		page.show(MobSDK.getContext(), null);
	}

	//#if def{lang} == cn
	/** 以匿名用户进入新闻列表 */
	//#elif def{lang} == en
	/** Anonymous users show the NewsListPage */
	//#endif
	public static void showNewsListPageWithAnonymousUser() {
		NewsListPage page = new NewsListPage(theme);
		page.setUser(UserBrief.USER_ANONYMOUS, null, null, null);
		page.show(MobSDK.getContext(), null);
	}
	
}
