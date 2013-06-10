package com.supermanket.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.supermanket.supermanket.R;

public class PictureAdapter extends BaseAdapter{
	
	private Context mContext;
	private int imageWidth;
	private int imageHeight;
	private int paddingLeft;
	private int paddingTop;
	private int paddingRight;
	private int paddingBottom;
	private JSONArray userImages;
	public String mThumbsURL[];
	public ImageLoader imageLoader;
	
	public PictureAdapter(Context c, int left, int top, 
			int right, int bottom, int width, int height, JSONArray images) {
	    
		mContext = c;
	    imageWidth = width;
	    imageHeight = height;
	    paddingLeft = left;
	    paddingTop = top;
	    paddingRight = right;
	    paddingBottom = bottom;
	    userImages = images;
	    mThumbsURL = new String[userImages.length()];
	    
	    for(int i = 0; i < userImages.length(); i++) {
	    	
	    	try {
				JSONObject pictureInfo = userImages.getJSONObject(i);
				mThumbsURL[i] = pictureInfo.getString("thumb");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    }

	    imageLoader = new ImageLoader(c);
	    
	}

	public int getCount() {
		return userImages.length();
	}

	public Object getItem(int position) {
	    return null;
	}

	public long getItemId(int position) {
	    return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	     
		View view;

		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.userprofile_grid_item, null);
		} else {
			view = convertView;
		}

		ImageView userImage = (ImageView) view.findViewById(R.id.userProfileItemUserImage);
		userImage.getLayoutParams().height = imageWidth;
		userImage.getLayoutParams().width = imageHeight;
		userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		userImage.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		imageLoader.DisplayImage(mThumbsURL[position], userImage);
		
		return view;
		 
	 }

}
