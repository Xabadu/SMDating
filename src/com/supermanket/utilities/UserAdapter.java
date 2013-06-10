package com.supermanket.utilities;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.supermanket.supermanket.R;

public class UserAdapter extends BaseAdapter{
	
	private Context mContext;
	private boolean imageCaption;
	private boolean userStatus;
	private int imageWidth;
	private int imageHeight;
	private int paddingLeft;
	private int paddingTop;
	private int paddingRight;
	private int paddingBottom;
	private int totalUsers = 0;
	private ArrayList<JSONArray> usersInfo;
	private JSONArray userImages;
	public String mThumbsURL[];
	public String mUsernames[];
	public ImageLoader imageLoader;
	
	public  UserAdapter(Context c, boolean caption, boolean status, int left, int top, int right, int bottom, int width, int height) {
	    mContext = c;
	    imageCaption = caption;
	    imageWidth = width;
	    imageHeight = height;
	    paddingLeft = left;
	    paddingTop = top;
	    paddingRight = right;
	    paddingBottom = bottom;
	    userStatus = status;
	}
	
	public UserAdapter(Context c, boolean caption, boolean status, int left, int top, 
			int right, int bottom, int width, int height, ArrayList<JSONArray> users) {
	    
		mContext = c;
	    imageCaption = caption;
	    imageWidth = width;
	    imageHeight = height;
	    paddingLeft = left;
	    paddingTop = top;
	    paddingRight = right;
	    paddingBottom = bottom;
	    userStatus = status;
	    usersInfo = users;
	    
	    for(int i = 0; i < usersInfo.size(); i++) {
	    	JSONArray userData = usersInfo.get(i);
	    	totalUsers += userData.length();
	    }
	    
	    mUsernames = new String[totalUsers];
	    mThumbsURL = new String[totalUsers];
	    
	    for(int i = 0; i < usersInfo.size(); i++) {
	    	
	    	JSONArray userData = usersInfo.get(i);
	    	
	    	for(int j = 0; j < userData.length(); j++) {
	    		JSONObject userObject;
				try {
					userObject = userData.getJSONObject(j);
					mUsernames[j+(16*i)] = userObject.getString("username");
		    		mThumbsURL[j+(16*i)] = userObject.getString("avatar_medium");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	
	    }

	    imageLoader = new ImageLoader(c);
	    
	}

	public int getCount() {
		int usersCount = 0;
		for(int i = 0; i < usersInfo.size(); i++) {
	    	JSONArray userInformation = usersInfo.get(i);
	    	usersCount += userInformation.length();
	    }
		return usersCount;
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
			view = inflater.inflate(R.layout.dashboard_grid_item, null);
		} else {
			view = convertView;
		}
		View overlay = (View) view.findViewById(R.id.dashboardItemOverlayView);
		ImageView userImage = (ImageView) view.findViewById(R.id.dashboardItemUserImage);
		userImage.getLayoutParams().height = imageWidth;
		userImage.getLayoutParams().width = imageHeight;
		userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		userImage.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		imageLoader.DisplayImage(mThumbsURL[position], userImage);
		 
		if(userStatus) {
			Random generator = new Random();
			int statusValue  = generator.nextInt(4) +1;
			if(statusValue == 2) {
				ImageView statusIcon = (ImageView) view.findViewById(R.id.dashboardItemStatusImage);
				statusIcon.setImageResource(R.drawable.ic_status_online);
				statusIcon.setPadding(4, 2, 0, 0);
			}
		}
		
		TextView username = (TextView) view.findViewById(R.id.dashboardItemNameText);
		 
		if(imageCaption) {
			overlay.setVisibility(View.VISIBLE);
			username.setText(mUsernames[position]);
		} else {
			username.setBackgroundColor(Color.TRANSPARENT);
			overlay.setVisibility(View.GONE);
		}

		return view;
		 
	 }

}
