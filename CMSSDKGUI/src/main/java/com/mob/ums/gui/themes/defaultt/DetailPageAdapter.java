package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mob.jimu.query.Condition;
import com.mob.jimu.query.Query;
import com.mob.jimu.query.data.Text;
import com.mob.tools.FakeActivity;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import com.mob.ums.OperationCallback;
import com.mob.ums.QueryView;
import com.mob.ums.SocialNetwork;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.datatype.City;
import com.mob.ums.datatype.Constellation;
import com.mob.ums.datatype.Country;
import com.mob.ums.datatype.Gender;
import com.mob.ums.datatype.Province;
import com.mob.ums.datatype.Zodiac;
import com.mob.ums.gui.pages.BindPage;
import com.mob.ums.gui.pages.DetailPage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.pickers.ImagePicker;
import com.mob.ums.gui.pickers.ImagePicker.OnImageGotListener;
import com.mob.ums.gui.pickers.SingleValuePicker.Choice;
import com.mob.ums.gui.themes.defaultt.components.SingleChoiceView;
import com.mob.ums.gui.themes.defaultt.components.SingleLineView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class DetailPageAdapter extends DefaultThemePageAdapter<DetailPage> {
	private AsyncImageView ivAvatar;
	private LinearLayout llBind;
	private SingleLineView llNick;
	private SingleChoiceView llGender;
	private SingleChoiceView llBirthday;
	private SingleChoiceView llAge;
	private SingleChoiceView llConstellation;
	private SingleChoiceView llZodiac;
	private SingleChoiceView llArea;
	private SingleLineView llSignature;
	private SingleLineView llEmail;
	private SingleLineView llAddress;
	private SingleLineView llZipCode;
	private ProgressDialog pd;

	public void onCreate(DetailPage page, Activity activity) {
		super.onCreate(page, activity);
		initPage(activity, page.getUser());
		refreshPage(activity, page.getUser());
		getBindedPlatforms();
	}

	private void initPage(final Activity activity, final User me) {
		LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);

		// title
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(new TitleView(activity) {
			@Override
			protected boolean isNoPadding() {
				return true;
			}

			protected void onReturn() {
				finish();
			}

			protected String getTitleResName() {
				return "umssdk_default_user_profile";
			}
		}, lp);

		// user info items
		ScrollView sv = new ScrollView(activity);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		llPage.addView(sv, lp);

		LinearLayout llBody = new LinearLayout(activity);
		llBody.setOrientation(LinearLayout.VERTICAL);
		ScrollView.LayoutParams lpsv = new ScrollView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		sv.addView(llBody, lpsv);

		// avatar
		LinearLayout llAvatar = new LinearLayout(activity);
		int dp15 = ResHelper.dipToPx(activity, 15);
		llAvatar.setPadding(dp15, 0, dp15, 0);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 54));
		llBody.addView(llAvatar, lp);

		TextView tv = new TextView(activity);
		tv.setMinEms(4);
		tv.setTextColor(0xff3b3947);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_avatar"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 1;
		llAvatar.addView(tv, lp);

		ivAvatar = new AsyncImageView(activity);
		ivAvatar.setImageResource(ResHelper.getBitmapRes(activity, "umssdk_default_infor_details_avatar"));
		int dp38 = ResHelper.dipToPx(activity, 38);
		ivAvatar.setRound(dp38);
		lp = new LayoutParams(dp38, dp38);
		lp.gravity = Gravity.CENTER_VERTICAL;
		llAvatar.addView(ivAvatar, lp);
		llAvatar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ImagePicker.Builder builder = new ImagePicker.Builder(activity, getPage().getTheme());
				builder.setOnImageGotListener(new OnImageGotListener() {
					public void onOmageGot(String id, String[] url) {
						if (url != null && url.length > 0) {
							int resId = ResHelper.getBitmapRes(activity, "umssdk_default_infor_details_avatar");
							ivAvatar.execute(url[0], resId);
						}
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(me.avatarId.getName(), id);
						map.put(me.avatar.getName(), url);
						updateUserInfo(map);
					}
				});
				builder.show();
			}
		});

		View vSep = new View(activity);
		int resid = ResHelper.getBitmapRes(activity, "umssdk_defalut_list_sep");
		vSep.setBackground(activity.getResources().getDrawable(resid));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		llBody.addView(vSep, lp);

		// nickname
		llNick = new SingleLineView(getPage()) {
			protected void onTextChange() {
				String text = getText();
				if (!TextUtils.isEmpty(text) && (me.nickname.isNull() || !me.nickname.get().equals(text) )) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(me.nickname.getName(), text.trim());
					updateUserInfo(map);
				}
			}
		};
		llNick.showNext();
		llNick.setTitleResName("umssdk_default_nickname");
		llNick.setHintResName("umssdk_default_unedit");
		llNick.setMinLenght(2);
		llNick.setMaxLenght(30);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llNick, lp);

		// gender
		llGender = new SingleChoiceView(getPage()) {
			protected void onSelectionsChange() {
				Choice[] choice = getSelections();
				if (choice != null && choice.length > 0) {
					Gender gender = (Gender) choice[0].ext;
					if (gender != null && (me.gender.isNull() || me.gender.get().code() != gender.code())) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(me.gender.getName(), gender);
						updateUserInfo(map);
					}
				}
			}
		};
		llGender.showNext();
		int resId = ResHelper.getStringRes(activity, "umssdk_default_gender");
		llGender.setTitle(activity.getString(resId));
		ArrayList<Choice> choices = SingleChoiceView.createChoice(Gender.class);
		llGender.setChoices(choices);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llGender, lp);

		// birthday
		llBirthday = new SingleChoiceView(getPage()) {
			protected void onSelectionsChange() {
				Choice[] choice = getSelections();
				if (choice != null && choice.length == 3) {
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, (Integer) choice[0].ext);
					cal.set(Calendar.MONTH, ((Integer) choice[1].ext) - 1);
					cal.set(Calendar.DAY_OF_MONTH, (Integer) choice[2].ext);
					String sel = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
					String bid = me.birthday.isNull()
							? null : new SimpleDateFormat("yyyy-MM-dd").format(me.birthday.get());
					if (!sel.equals(bid)) {
						if (cal.getTimeInMillis() > System.currentTimeMillis()) {
							ErrorDialog.Builder builder = new ErrorDialog.Builder(activity, getPage().getTheme());
							int resId = ResHelper.getStringRes(activity, "umssdk_default_user_profile");
							builder.setTitle(activity.getString(resId));
							resId = ResHelper.getStringRes(activity, "umssdk_default_beyond_current_date");
							builder.setMessage(activity.getString(resId));
							builder.show();
							refreshPage(getPage().getContext(), me);
						} else {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put(me.birthday.getName(), cal.getTime());
							updateUserInfo(map);
						}
					}
				}
			}
		};
		llBirthday.showNext();
		resId = ResHelper.getStringRes(activity, "umssdk_default_birthday");
		llBirthday.setTitle(activity.getString(resId));
		llBirthday.setSeparator("");
		choices = getDateChoices();
		llBirthday.setChoices(choices);
		llBirthday.showTitle();
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llBirthday, lp);

		// age
		llAge = new SingleChoiceView(getPage()) {
			protected void onSelectionsChange() {
				Choice[] choice = getSelections();
				if (choice != null && choice.length > 0) {
					int age = (Integer) choice[0].ext;
					if (me.age.isNull() || me.age.get() != age) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(me.age.getName(), age);
						updateUserInfo(map);
					}
				}
			}
		};
		llAge.showNext();
		resId = ResHelper.getStringRes(activity, "umssdk_default_age");
		llAge.setTitle(activity.getString(resId));
		choices = new ArrayList<Choice>();
		for (int i = 1; i <= 150; i++) {
			Choice c = new Choice();
			c.ext = i;
			c.text = String.valueOf(i);
			choices.add(c);
		}
		llAge.setChoices(choices);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llAge, lp);

		// constellation
		llConstellation = new SingleChoiceView(getPage()) {
			protected void onSelectionsChange() {
				Choice[] choice = getSelections();
				if (choice != null && choice.length > 0) {
					Constellation c = (Constellation) choice[0].ext;
					if (c != null && (me.constellation.isNull() || me.constellation.get().code() != c.code())) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(me.constellation.getName(), c);
						updateUserInfo(map);
					}
				}
			}
		};
		llConstellation.showNext();
		resId = ResHelper.getStringRes(activity, "umssdk_default_constellation");
		llConstellation.setTitle(activity.getString(resId));
		choices = SingleChoiceView.createChoice(Constellation.class);
		llConstellation.setChoices(choices);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llConstellation, lp);

		// zodiac
		llZodiac = new SingleChoiceView(getPage()) {
			protected void onSelectionsChange() {
				Choice[] choice = getSelections();
				if (choice != null && choice.length > 0) {
					Zodiac zodiac = (Zodiac) choice[0].ext;
					if (zodiac != null && (me.zodiac.isNull() || me.zodiac.get().code() != zodiac.code())) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(me.zodiac.getName(), zodiac);
						updateUserInfo(map);
					}
				}
			}
		};
		llZodiac.showNext();
		resId = ResHelper.getStringRes(activity, "umssdk_default_zodiac");
		llZodiac.setTitle(activity.getString(resId));
		choices = SingleChoiceView.createChoice(Zodiac.class);
		llZodiac.setChoices(choices);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llZodiac, lp);

		// area
		llArea = new SingleChoiceView(getPage()) {
			protected void onSelectionsChange() {
				Choice[] choice = getSelections();
				if (choice != null && choice.length == 3) {
					Country country = choice[0] == null ? null : (Country) choice[0].ext;
					Province p = choice[1] == null ? null : (Province) choice[1].ext;
					City city = choice[2] == null ? null : (City) choice[2].ext;
					HashMap<String, Object> map = new HashMap<String, Object>();
					if (country != null && (me.country.isNull() || me.country.get().code() != country.code())) {
						map.put(me.country.getName(), country);
					}
					if (p != null && (me.province.isNull() || me.province.get().code() != p.code())) {
						map.put(me.province.getName(), p);
					}
					if (city != null && (me.city.isNull() || me.city.get().code() != city.code())) {
						map.put(me.city.getName(), city);
					}
					if (map.size() > 0) {
						updateUserInfo(map);
					}
				}
			}
		};
		llArea.showNext();
		resId = ResHelper.getStringRes(activity, "umssdk_default_where_are_you");
		llArea.setTitle(activity.getString(resId));
		choices = SingleChoiceView.createChoice(Country.class);
		llArea.setChoices(choices);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llArea, lp);

		// signature
		llSignature = new SingleLineView(getPage()) {
			protected void onTextChange() {
				String text = getText();
				if (!TextUtils.isEmpty(text) && (me.signature.isNull() || !me.signature.get().equals(text))) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					if (text.trim().length() > 0) {
						map.put(me.signature.getName(), text);
					} else {
						map.put(me.signature.getName(), text.trim());
					}
					updateUserInfo(map);
				}
			}
		};
		llSignature.showNext();
		llSignature.setTitleResName("umssdk_default_signature");
		llSignature.setHintResName("umssdk_default_unedit");
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llSignature, lp);

		// email
		llEmail = new SingleLineView(getPage()) {
			protected void onTextChange() {
				String text = getText();
				if (!TextUtils.isEmpty(text) && (me.email.isNull() || !me.email.get().equals(text))) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(me.email.getName(), text.trim());
					updateUserInfo(map);
				}
			}
		};
		llEmail.setEmail();
		llEmail.showNext();
		llEmail.setTitleResName("umssdk_default_email");
		llEmail.setHintResName("umssdk_default_unedit");
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llEmail, lp);

		// address
		llAddress = new SingleLineView(getPage()) {
			protected void onTextChange() {
				String text = getText();
				if (!TextUtils.isEmpty(text) && (me.address.isNull() || !me.address.get().equals(text))) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(me.address.getName(), text.trim());
					updateUserInfo(map);
				}
			}
		};
		llAddress.showNext();
		llAddress.setTitleResName("umssdk_default_address");
		llAddress.setHintResName("umssdk_default_unedit");
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llAddress, lp);

		// zipcode
		llZipCode = new SingleLineView(getPage()) {
			protected void onTextChange() {
				String text = getText();
				if (!TextUtils.isEmpty(text) && !text.equals(String.valueOf(me.zipCode.get()))) {
					long code = -1;
					try {
						code = Long.parseLong(text.trim());
					} catch (Throwable t) {}
					if (code == -1) {
						ErrorDialog.Builder builder = new ErrorDialog.Builder(activity, getPage().getTheme());
						int resId = ResHelper.getStringRes(activity, "umssdk_default_user_profile");
						builder.setTitle(activity.getString(resId));
						resId = ResHelper.getStringRes(activity, "umssdk_default_zipcode_incorrect");
						builder.setMessage(activity.getString(resId));
						builder.show();
						refreshPage(getPage().getContext(), me);
					} else {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(me.zipCode.getName(), code);
						updateUserInfo(map);
					}
				}
			}
		};
		llZipCode.showNext();
		llZipCode.setNumeral();
		llZipCode.setTitleResName("umssdk_default_zip_code");
		llZipCode.setHintResName("umssdk_default_unedit");
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llZipCode, lp);

		// bind social platform
		llBind = new LinearLayout(activity);
		llBind.setPadding(dp15, 0, dp15, 0);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 44));
		llBody.addView(llBind, lp);

		tv = new TextView(activity);
		tv.setMinEms(4);
		tv.setTextColor(0xff3b3947);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_bind_social_platform"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 1;
		llBind.addView(tv, lp);

		ArrayList<SocialNetwork> sns = new ArrayList<SocialNetwork>();
		sns.addAll(Arrays.asList(UMSSDK.getAvailableSocialLoginWays()));
		sns.add(0, null);
		int dp32 = ResHelper.dipToPx(activity, 20);
		lp = new LayoutParams(dp32, dp32);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.leftMargin = ResHelper.dipToPx(activity, 5);
		lp.rightMargin = lp.leftMargin;
		for (SocialNetwork sn : sns) {
			String resName;
			if (sn == null) {
				resName = "umssdk_default_vocde";
			} else {
				resName = "umssdk_default_" + sn.getName().toLowerCase();
			}
			ImageView iv = new ImageView(activity);
			iv.setImageResource(ResHelper.getBitmapRes(activity, resName));
			iv.setTag(sn);
			llBind.addView(iv, lp);
		}
		ImageView iv = new ImageView(activity);
		iv.setImageResource(ResHelper.getBitmapRes(activity, "umssdk_default_go_details"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.leftMargin = ResHelper.dipToPx(activity, 5);
		llBind.addView(iv, lp);
	}

	private void refreshPage(Context context, final User me) {
		if (!me.avatar.isNull()) {
			int resId = ResHelper.getBitmapRes(context, "umssdk_default_infor_details_avatar");
			ivAvatar.execute(me.avatar.get()[0], resId);
		}

		if (!me.nickname.isNull()) {
			llNick.setText(me.nickname.get());
		}

		ArrayList<Choice> choices = llGender.getChoices();
		if (!me.gender.isNull()) {
			for (int i = 0; i < choices.size(); i++) {
				if (choices.get(i).ext.equals(me.gender.get())) {
					llGender.setSelections(new int[]{i});
					break;
				}
			}
		}

		choices = llBirthday.getChoices();
		if (!me.birthday.isNull()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			int year = Integer.parseInt(sdf.format(me.birthday.get()));
			sdf = new SimpleDateFormat("MM");
			int month = Integer.parseInt(sdf.format(me.birthday.get()));
			sdf = new SimpleDateFormat("dd");
			int day = Integer.parseInt(sdf.format(me.birthday.get()));

			int[] sel = new int[3];
			for (int y = 0; y < choices.size(); y++) {
				if (year == (Integer) choices.get(y).ext) {
					sel[0] = y;
					choices = choices.get(y).children;
					for (int m = 0; m < choices.size(); m++) {
						if (month == (Integer) choices.get(m).ext) {
							sel[1] = m;
							choices = choices.get(m).children;
							for (int d = 0; d < choices.size(); d++) {
								if (day == (Integer) choices.get(d).ext) {
									sel[2] = d;
									break;
								}
							}
							break;
						}
					}
					break;
				}
			}
			llBirthday.setSelections(sel);
		}

		if (!me.age.isNull() && me.age.get() <= 150) {
			if (me.age.get() - 1 >= 0) {
				llAge.setSelections(new int[] {me.age.get() - 1});
			}
		}

		choices = llConstellation.getChoices();
		if (!me.constellation.isNull()) {
			for (int i = 0; i < choices.size(); i++) {
				if (choices.get(i).ext.equals(me.constellation.get())) {
					llConstellation.setSelections(new int[] {i});
					break;
				}
			}
		}

		choices = llZodiac.getChoices();
		if (!me.zodiac.isNull()) {
			for (int i = 0; i < choices.size(); i++) {
				if (choices.get(i).ext.equals(me.zodiac.get())) {
					llZodiac.setSelections(new int[] {i});
					break;
				}
			}
		}

		Country country = me.country.get();
		Province province = me.province.get();
		City city = me.city.get();
		choices = llArea.getChoices();
		if (country != null) {
			int[] sel = new int[3];
			for (int co = 0; co < choices.size(); co++) {
				if (country.code() == ((Country) choices.get(co).ext).code()) {
					sel[0] = co;
					choices = choices.get(co).children;
					if (province != null) {
						for (int p = 0; p < choices.size(); p++) {
							if (province.code() == ((Province) choices.get(p).ext).code()) {
								sel[1] = p;
								choices = choices.get(p).children;
								if (city != null) {
									for (int ci = 0; ci < choices.size(); ci++) {
										if (city.code() == ((City) choices.get(ci).ext).code()) {
											sel[2] = ci;
											break;
										}
									}
								}
								break;
							}
						}
					}
					break;
				}
			}
			llArea.setSelections(sel);
		}

		if (!me.signature.isNull()) {
			llSignature.setText(me.signature.get());
		}

		if (!me.email.isNull()) {
			llEmail.setText(me.email.get());
		}

		if (!me.address.isNull()) {
			llAddress.setText(me.address.get());
		}

		if (!me.zipCode.isNull() && me.zipCode.get() != 0) {
			llZipCode.setText(String.valueOf(me.zipCode.get()));
		}
	}

	private ArrayList<Choice> getDateChoices() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		int thisYear = cal.get(Calendar.YEAR);
		ArrayList<Choice> years = new ArrayList<Choice>();
		int resid = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_year");
		String strYear = getPage().getContext().getString(resid);
		resid = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_month");
		String strMonth = getPage().getContext().getString(resid);
		resid = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_day");
		String strDay = getPage().getContext().getString(resid);
		int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		for (int year = thisYear - 150; year <= thisYear; year++) {
			Choice cYear = new Choice();
			cYear.ext = year;
			cYear.text = year + strYear;
			years.add(cYear);
			daysOfMonth[1] = ResHelper.isLeapYear(year) ? 29 : 28;
			for (int month = 1; month <= 12; month++) {
				Choice cMonth = new Choice();
				cMonth.ext = month;
				if(month < 10){
					cMonth.text = "0" + month + strMonth;
				} else {
					cMonth.text = month + strMonth;
				}
				cYear.children.add(cMonth);
				for (int day = 1; day <= daysOfMonth[month - 1]; day++) {
					Choice cDay = new Choice();
					cDay.ext = day;
					if(day < 10){
						cDay.text = "0" + day + strDay;
					} else {
						cDay.text = day + strDay;
					}
					cMonth.children.add(cDay);
				}
			}
		}
		return years;
	}

	private void updateUserInfo(final HashMap<String, Object> update) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
		UMSSDK.updateUserInfo(update, new OperationCallback<Void>() {
			public void onSuccess(Void data) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				User me = getPage().getUser();
				me.parseFromMap(update);
				if (update.containsKey(me.birthday.getName())) {
					synchronizeBirthday();
				}
				refreshPage(getPage().getContext(), me);
			}

			public void onFailed(Throwable t) {
//				t.printStackTrace();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				ErrorDialog.Builder builder = new ErrorDialog.Builder(getPage().getContext(), getPage().getTheme());
				int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_user_profile");
				builder.setTitle(getPage().getContext().getString(resId));
				builder.setThrowable(t);
				builder.setMessage(t.getMessage());
				builder.show();
			}
		});
	}

	private void synchronizeBirthday() {
		User me = getPage().getUser();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int curYear = Integer.parseInt(sdf.format(cal.getTime()));
		int curDateOfYear = cal.get(Calendar.DAY_OF_YEAR);
		cal.clear();
		cal.setTime(me.birthday.get());
		int birthYear = Integer.parseInt(sdf.format(me.birthday.get()));
		int birthDateOfYear = cal.get(Calendar.DAY_OF_YEAR);
		int age = curYear - birthYear;
		if (curDateOfYear < birthDateOfYear) {
			age--;
		}
		me.age.set(age);

		int zodiacCode;
		if(birthYear < 1900){
			zodiacCode = 12 - (1900 - birthYear) % 12 + 1;
		} else {
			zodiacCode = ((birthYear - 1900) % 12) + 1;
		}
		me.zodiac.set(Zodiac.valueOf(zodiacCode));

		cal.clear();
		cal.setTime(me.birthday.get());
		int birthday = cal.get(Calendar.DAY_OF_YEAR);
		int[] yearArr;
		if (ResHelper.isLeapYear(birthYear)) {
			yearArr = new int[] {20, 50, 81, 112, 142, 174, 205, 236, 267, 297, 327, 357};
		} else {
			yearArr = new int[] {20, 50, 80, 111, 141, 173, 204, 235, 266, 296, 326, 356};
		}
		if (birthday <= yearArr[0] || birthday > yearArr[11]) {
			me.constellation.set(Constellation.valueOf(10));
		} else if (birthday <= yearArr[1]) {
			me.constellation.set(Constellation.valueOf(11));
		} else if (birthday <= yearArr[2]) {
			me.constellation.set(Constellation.valueOf(12));
		} else {
			for (int i = 3; i < yearArr.length; i++) {
				int dayOfYear = yearArr[i];
				if (birthday < dayOfYear) {
					me.constellation.set(Constellation.valueOf(i - 2));
					break;
				}
			}
		}
	}

	private void getBindedPlatforms() {
		refreshBindedPlatforms(null);
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
		new Thread() {
			public void run() {
				try {
					Query q = UMSSDK.getQuery(QueryView.BINDED_LOGIN_METOHDS);
					User me = getPage().getUser();
					q.condition(Condition.eq(me.id.getName(), Text.valueOf(me.id.get())));
					String res = q.query();
					HashMap<String, Object> resMap = new Hashon().fromJson(res);
					@SuppressWarnings("unchecked")
					final ArrayList<HashMap<String, Object>> list
							= (ArrayList<HashMap<String, Object>>) resMap.get("list");
					UIHandler.sendEmptyMessage(0, new Callback() {
						public boolean handleMessage(Message msg) {
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							refreshBindedPlatforms(list);
							return false;
						}
					});
				} catch (final Throwable t) {
					UIHandler.sendEmptyMessage(0, new Callback() {
						public boolean handleMessage(Message msg) {
//							t.printStackTrace();
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							Context cxt = getPage().getContext();
							ErrorDialog.Builder builder = new ErrorDialog.Builder(cxt, getPage().getTheme());
							int resId = ResHelper.getStringRes(cxt, "umssdk_default_user_profile");
							builder.setTitle(cxt.getString(resId));
							builder.setThrowable(t);
							builder.setMessage(t.getMessage());
							builder.show();
							return false;
						}
					});
				}
			}
		}.start();
	}

	private void refreshBindedPlatforms(final ArrayList<HashMap<String, Object>> platforms) {
		if (platforms != null && !platforms.isEmpty()) {
			HashMap<Integer, ImageView> idToViews = new HashMap<Integer, ImageView>();
			HashMap<Integer, String> idToResName = new HashMap<Integer, String>();
			for (int i = 1; i < llBind.getChildCount() - 1; i++) {
				ImageView iv = (ImageView) llBind.getChildAt(i);
				SocialNetwork sn = (SocialNetwork) iv.getTag();
				if (sn == null) {
					idToViews.put(0, iv);
					idToResName.put(0, "umssdk_default_vcode_binded");
				} else {
					idToViews.put(sn.getId(), iv);
					idToResName.put(sn.getId(), "umssdk_default_" + sn.getName().toLowerCase() + "_binded");
				}
			}

			for (HashMap<String, Object> item : platforms) {
				int bindType = (Integer) item.get("bindType");
				ImageView iv = idToViews.get(bindType);
				if (iv != null) {
					iv.setImageResource(ResHelper.getBitmapRes(iv.getContext(), idToResName.get(bindType)));
				}
			}
		}

		llBind.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BindPage page = new BindPage(getPage().getTheme());
				page.setPlatforms(platforms);
				page.showForResult(getPage().getContext(), null, new FakeActivity() {
					public void onResult(HashMap<String, Object> data) {
						if (data != null) {
							llBind.setOnClickListener(null);
							getBindedPlatforms();
						}
					}
				});
			}
		});
	}

}
