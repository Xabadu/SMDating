package com.supermanket.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.supermanket.supermanket.MessagesList;
import com.supermanket.supermanket.MessagesList.BlockContact;
import com.supermanket.supermanket.R;

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
        ImageButton blockBtn = (ImageButton) vi.findViewById(R.id.messageListBlockBtn);
        
        HashMap<String, String> message = new HashMap<String, String>();
        message = data.get(position);
        final String contact = message.get("contact");
        userName.setText(message.get("name"));
        lastMessage.setText(message.get("message"));
        if(message.get("read").equalsIgnoreCase("false")) {
        	lastMessage.setTypeface(null, Typeface.BOLD);
        }
        if(message.get("blocked").equalsIgnoreCase("true")) {
        	blockBtn.setImageResource(R.drawable.ic_unblock);
        }
        messageDate.setText(message.get("date"));
        imageLoader.DisplayImage(message.get("image"), userImage);
       
        blockBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		//MessagesList.showContact(contact);
        		MessagesList ml = new MessagesList();
        		ml.showContact(contact);
        	}
        });
        
        return vi;
    
    }
	
}
