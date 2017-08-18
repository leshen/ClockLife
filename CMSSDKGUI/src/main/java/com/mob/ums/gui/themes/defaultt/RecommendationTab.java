package com.mob.ums.gui.themes.defaultt;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mob.jimu.query.Query;
import com.mob.jimu.query.Sort;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import com.mob.ums.QueryView;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.pages.MainPage;
import com.mob.ums.gui.pages.ProfilePage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.tabs.Tab;
import com.mob.ums.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.ums.gui.themes.defaultt.components.ItemUserView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RecommendationTab implements Tab {
	private MainPage page;
	private LinearLayout llPage;
	private PullToRequestView ptr;
	private RecentLoginAdapter adapter;

	public RecommendationTab(MainPage page) {
		this.page = page;
	}

	public String getTitleResName() {
		return "umssdk_default_recommendation";
	}

	public int getSelectedTitleColor() {
		return 0xffe4554c;
	}

	public int getUnselectedTitleColor() {
		return 0xff999999;
	}

	public String getSelectedIconResName() {
		return "umssdk_default_tab_recommend_sel";
	}

	public String getUnselectedIconResName() {
		return "umssdk_default_tab_recommend_unsel";
	}

	public View getTabView(Context context) {
		if (llPage == null) {
			llPage = new LinearLayout(context);
			llPage.setOrientation(LinearLayout.VERTICAL);

			// title
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			TitleView titleView = new TitleView(context) {
				protected boolean isNoPadding() {
					return true;
				}

				protected void onReturn() {
					page.finish();
				}

				protected String getTitleResName() {
					return "umssdk_default_recent_login";
				}
			};
			titleView.disableReturn();
			llPage.addView(titleView, lp);

			//body
			ptr = new PullToRequestView(context);
			ptr.setBackgroundColor(0xfff7f7f7);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			llPage.addView(ptr, lp);
		}
		return llPage;
	}

	public void onSelected() {
		if (adapter == null) {
			adapter = new RecentLoginAdapter(ptr, page);
			ptr.setAdapter(adapter);
			ptr.performPullingDown(true);
		}
	}

	public void onUnselected() {

	}

	private static class RecentLoginAdapter extends DefaultRTRListAdapter implements OnItemClickListener {
		private static final int PAGE_SIZE = 20;

		private MainPage page;
		private ArrayList<User> users;
		private int offset;
		private int[] formatRes;

		public RecentLoginAdapter(PullToRequestView view, MainPage page) {
			super(view);
			this.page = page;
			users = new ArrayList<User>();
			formatRes = new int[]{
					ResHelper.getStringRes(page.getContext(), "umssdk_default_x_minute"),
					ResHelper.getStringRes(page.getContext(), "umssdk_default_x_hour"),
					ResHelper.getStringRes(page.getContext(), "umssdk_default_x_day"),
					ResHelper.getStringRes(page.getContext(), "umssdk_default_x_month"),
					ResHelper.getStringRes(page.getContext(), "umssdk_default_x_year")
			};
			getListView().setOnItemClickListener(this);
		}

		public long getItemId(int position) {
			return position;
		}

		public Object getItem(int position) {
			return null;
		}

		protected void onRequest(boolean firstPage) {
			if (firstPage) {
				getUsers(0);
			} else {
				getUsers(offset + PAGE_SIZE);
			}
		}

		private void getUsers(final int offset) {
			new Thread() {
				public void run() {
					try {
						Query q = UMSSDK.getQuery(QueryView.USERS);
						q.sort(Sort.desc(new User().lastLogin.getName()));
						q.offset(offset);
						q.size(PAGE_SIZE);
						String json = q.query();

						HashMap<String, Object> resMap = new Hashon().fromJson(json);
						@SuppressWarnings("unchecked")
						ArrayList<HashMap<String, Object>> list
								= (ArrayList<HashMap<String, Object>>) resMap.get("list");
						ArrayList<User> us = new ArrayList<User>();
						if (list != null) {
							for (HashMap<String, Object> raw : list) {
								User u = new User();
								u.parseFromMap(raw);
								us.add(u);
							}
						}
						Message msg = new Message();
						msg.obj = us;
						UIHandler.sendMessage(msg, new Callback() {
							public boolean handleMessage(Message msg) {
								@SuppressWarnings("unchecked")
								ArrayList<User> us = (ArrayList<User>) msg.obj;
								if (us.isEmpty()) {
									getParent().lockPullingUp();
								} else {
									getParent().releasePullingUpLock();
									if (offset == 0) {
										users.clear();
									}
									users.addAll(us);
								}
								RecentLoginAdapter.this.offset = offset;
								notifyDataSetChanged();
								return false;
							}
						});
					} catch (final Throwable t) {
//						t.printStackTrace();
						UIHandler.sendEmptyMessage(0, new Callback() {
							public boolean handleMessage(Message msg) {
								ErrorDialog.Builder builder = new ErrorDialog.Builder(
										getParent().getContext(), page.getTheme());
								int resId = ResHelper.getStringRes(getContext(), "umssdk_default_recommendation");
								builder.setTitle(getContext().getString(resId));
								builder.setThrowable(t);
								builder.setMessage(t.getMessage());
								builder.show();
								notifyDataSetChanged();
								return false;
							}
						});
					}
				}
			}.start();
		}

		public int getCount() {
			return users == null ? 0 : users.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new ItemUserView(getContext());
			}

			User user = users.get(position);
			ItemUserView view = (ItemUserView) convertView;
			view.setUser(user);
			view.setRightText(timeToString(user.lastLogin.get().getTime()));
			return convertView;
		}

		private String timeToString(long time) {
			int[] val = ResHelper.covertTimeInYears(time);
			int res;

			if (val[1] > 3 || (val[1] == 3 && val[0] > 7)) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				return simpleDateFormat.format(new Date(time));
			} else if (val[1] == 0) {
				res = ResHelper.getStringRes(getContext(), "umssdk_default_less_than_1_minute");
			} else {
				res = formatRes[val[1] - 1];
			}

			return getContext().getString(res, val[0]);
		}

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ProfilePage page = new ProfilePage(this.page.getTheme());
			page.setUser(users.get(position));
			page.show(getContext(), null);
		}
	}

}
