package com.mob.cms.gui.themes.defaultt.components;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mob.cms.News;
import com.mob.cms.News.Image;
import com.mob.cms.gui.themes.defaultt.ImageDetailPageAdapter;
import com.mob.tools.gui.ViewPagerAdapter;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

public class ImageDetailAdapter extends ViewPagerAdapter implements View.OnClickListener{
	
	private ArrayList<Integer> screenList;
	private Image[] imageList;
	private Context context;
	private ImageDetailPageAdapter imageDetailPage;
	
	public ImageDetailAdapter(ImageDetailPageAdapter imageDetailPage, News imgNews) {
		this.imageDetailPage = imageDetailPage;
		this.context = imageDetailPage.getPage().getContext();
		screenList = new ArrayList<Integer>();
		imageList = imgNews.imgList.get();
	}
	
	
	public int getCount() {
		return imageList.length;
	}
	
	public void onScreenChange(int currentScreen, int lastScreen) {
		super.onScreenChange(currentScreen, lastScreen);
		if (imageDetailPage != null) {
			imageDetailPage.onScreenChange(currentScreen + 1, imageList[currentScreen].imgDesc);
		}
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new ImageViewItem(parent.getContext());
		}
		
		if (!screenList.contains(index)) {
			ImageViewItem imgView = (ImageViewItem) convertView;
			imgView.asyncImageView.execute(imageList[index].imgUrl, ResHelper.getBitmapRes(parent.getContext(), "cmssdk_default_image_default_bg"));
			if (screenList.size() >= 3) {
				screenList.remove(0);
			}
			imgView.setOnClickListener(this);
			screenList.add(index);
		}
		
		return convertView;
	}

	public void onClick(View v) {
		imageDetailPage.setHideOtherView();
	}
}
