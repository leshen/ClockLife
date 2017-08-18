package com.mob.cms.gui;

import android.content.Context;

import com.mob.cms.CMSSDK;
import com.mob.cms.Callback;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.Random;

public class Utils {
	
	//#if def{lang} == cn
	/**
	 * 视频时长转String<br/>
	 * 如，80秒，转换后变成 01:20
	 * @param videoTime
	 */
	//#elif def{lang} == en
	/** Returns a time converted to a string <br/> 
	 *  eg. 80 seconds converted to "01:20"
	 * */
	//#endif
	public static String getVideoPlayTime(int videoTime) {
		int minute = videoTime / 60;
		int second = videoTime % 60;
		String playTime;
		if (minute < 10) {
			playTime = "0" + minute;
		} else {
			playTime = String.valueOf(minute);
		}

		playTime += ":";
		if (second < 10) {
			playTime += "0" + second;
		} else {
			playTime += second;
		}

		return playTime;
	}

	//#if def{lang} == cn
	/**
	 * 时间转多久前<br/>
	 * 如，2小时前
	 * @param newsUpdateAt
	 */
	//#elif def{lang} == en
	/**	 Returns a date converted to a string </br>
	 * eg.return 2 hours ago
	 * */
	//#endif
	public static String getTimeInYears(Context context, long newsUpdateAt) {
		int[] timeInYear = ResHelper.covertTimeInYears(newsUpdateAt);
		int resId = 0;
		String timeStr = "";
		switch (timeInYear[1]){
			case 0:{
				resId = ResHelper.getStringRes(context, "cmssdk_default_date_justnow");
			} break;
			case 1:{
				resId = ResHelper.getStringRes(context, "cmssdk_default_date_minute");
			} break;
			case 2:{
				resId = ResHelper.getStringRes(context, "cmssdk_default_date_hour");
			} break;
			case 3:{
				resId = ResHelper.getStringRes(context, "cmssdk_default_date_day");
			} break;
			case 4:{
				resId = ResHelper.getStringRes(context, "cmssdk_default_date_month");
			} break;
			case 5:{
				resId = ResHelper.getStringRes(context, "cmssdk_default_date_year");
			} break;
		}
		if (resId > 0) {
			timeStr = context.getString(resId);
			timeStr = String.format(timeStr, timeInYear[0]);
		}
		return timeStr;
	}


}
