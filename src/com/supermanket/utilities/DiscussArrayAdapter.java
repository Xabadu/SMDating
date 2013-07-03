package com.supermanket.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supermanket.supermanket.R;

public class DiscussArrayAdapter extends BaseAdapter {

	private TextView messageBubble;
	private LinearLayout wrapper;
	private ArrayList<HashMap<String, String>> messages = new ArrayList<HashMap<String, String>>();
	private Activity activity;
	private static LayoutInflater inflater = null;
	
	public void add(HashMap<String, String> message) {
		messages.add(message);
		notifyDataSetChanged();
	}
	
	public DiscussArrayAdapter(Activity a, ArrayList<HashMap<String, String>> m) {
		messages = m;
		activity = a;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return messages.size();
	}
	
	public void clearAdapter() {
        messages.clear();
        notifyDataSetChanged();
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		
		if (row == null) {
			row = inflater.inflate(R.layout.message_detail_bubble, null);
		}

		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
		messageBubble = (TextView) row.findViewById(R.id.comment);
		
		HashMap<String, String> messageData = new HashMap<String, String>();
		messageData = messages.get(position);
		messageBubble.setText(messageData.get("message"));
		
		if(messageData.get("who").equalsIgnoreCase("self")) {
			messageBubble.setBackgroundResource(R.drawable.bubble_green);
			wrapper.setGravity(Gravity.RIGHT);
		} else if(messageData.get("who").equalsIgnoreCase("other")) {
			messageBubble.setBackgroundResource(R.drawable.bubble_yellow);
			wrapper.setGravity(Gravity.LEFT);
		}

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}