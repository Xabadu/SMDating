package com.supermanket.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.supermanket.supermanket.R;

public class WatchersAdapter extends BaseAdapter {
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader; 
 
    public WatchersAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
    
    public void clearAdapter() {
        data.clear();
        notifyDataSetChanged();
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View vi = convertView;
        
    	if(convertView == null) {
    		vi = inflater.inflate(R.layout.watchers_detail_row, null);
    	}
           
        TextView userName = (TextView) vi.findViewById(R.id.watchersListUserName); // title
        TextView whenText = (TextView) vi.findViewById(R.id.watchersListWhenText); // duration
        ImageView userImage = (ImageView) vi.findViewById(R.id.watchersListUserImage); // thumb image
        
        HashMap<String, String> message = new HashMap<String, String>();
        message = data.get(position);
        userName.setText(message.get("name") + " " + message.get("new"));
        whenText.setText(message.get("count") + " - " + message.get("when"));
        
        imageLoader.DisplayImage(message.get("image"), userImage);
        
        return vi;
    
    }
}
