package com.supermanket.supermanket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.supermanket.utilities.DiscussArrayAdapter;

public class MessageDetail extends Activity {
	
	private int position;
	private String data;
	private JSONArray contactsList;
	private JSONArray messageList;
	private JSONObject messageData;
	private ArrayList<HashMap<String, String>> messages = new ArrayList<HashMap<String, String>>();
	private ListView list;
	private DiscussArrayAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showMessages();
	}
	
	public void showMessages() {
		setContentView(R.layout.activity_message_detail);
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		data = intent.getStringExtra("data");
		try {
			contactsList = new JSONArray(data);
			messageData = contactsList.getJSONObject(position);
			messageList = messageData.getJSONArray("messages");
			if(messageList.length() > 0) {
				for(int i = 0; i < messageList.length(); i++) {
					HashMap<String, String> singleMessage = new HashMap<String, String>();
					JSONObject messageDetail = messageList.getJSONObject(i);
					singleMessage.put("message", messageDetail.getString("content"));
					if(messageDetail.getBoolean("mine")) {
						singleMessage.put("who", "self");
					} else {
						singleMessage.put("who", "other");
					}
					messages.add(singleMessage);
				}
			} else {
				Toast.makeText(this, "No tienes mensajes", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		list = (ListView) findViewById(R.id.messageDetailList);
		 
        adapter = new DiscussArrayAdapter(this, messages);
        list.setAdapter(adapter);
		
	}

}
