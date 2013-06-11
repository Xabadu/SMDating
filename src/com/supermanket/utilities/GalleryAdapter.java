package com.supermanket.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GalleryAdapter extends PagerAdapter {
	Context context;
	private JSONArray userImages;
	private String picsURL[];
	ImageLoader imageLoader;
	
	public GalleryAdapter(Context context, JSONArray images){
		this.context=context;
		userImages = images;
		picsURL = new String[userImages.length()];
		imageLoader = new ImageLoader(context);
		
		for(int i = 0; i < userImages.length(); i++) {
			try {
				JSONObject imageInfo = userImages.getJSONObject(i);
				picsURL[i] = imageInfo.getString("original");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public int getCount() {
		return userImages.length();
	}
 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}
 
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageLoader.DisplayImage(picsURL[position], imageView);
		((ViewPager) container).addView(imageView, 0);
		return imageView;
	}
	

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}
}