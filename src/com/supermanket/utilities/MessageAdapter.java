package com.supermanket.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import com.supermanket.supermanket.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private String referral;
    public ImageLoader imageLoader; 
 
    public MessageAdapter(Activity a, ArrayList<HashMap<String, String>> d, String from) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        referral = from;
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
    		if(referral.equalsIgnoreCase("list")) {
    			vi = inflater.inflate(R.layout.message_list_row, null);
    		} else if(referral.equalsIgnoreCase("detail")) {
    			vi = inflater.inflate(R.layout.message_detail_row, null);
    		}
    	}
           
        TextView userName = (TextView) vi.findViewById(R.id.messageListNameText); // title
        TextView lastMessage = (TextView) vi.findViewById(R.id.messageListLastMessageText); // artist name
        TextView messageDate = (TextView) vi.findViewById(R.id.messageListDateTimeText); // duration
        ImageView userImage = (ImageView) vi.findViewById(R.id.messageListUserImage); // thumb image
 
        HashMap<String, String> message = new HashMap<String, String>();
        message = data.get(position);
 
        userName.setText(message.get("name"));
        lastMessage.setText(message.get("message"));
        if(message.get("read").equalsIgnoreCase("false")) {
        	lastMessage.setTypeface(null, Typeface.BOLD);
        }
        messageDate.setText(message.get("date"));
        imageLoader.DisplayImage(message.get("image"), userImage);
        
        return vi;
    
    }
	

}
