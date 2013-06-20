package com.supermanket.supermanket;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
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
	
	private static SharedPreferences mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	NavUtils.navigateUpFromSameTask(this);
    			return true;
            case R.id.logout:
            	Editor e = mSharedPreferences.edit();
                e.putBoolean("LOGGED_IN", false);
                e.commit();
            	Intent intent = new Intent(this, Login.class);
            	startActivity(intent);
            	this.finish();
            	break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
