package com.supermanket.supermanket;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.supermanket.utilities.LazyAdapter;

public class MessagesList extends Activity {
	
	ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
	LazyAdapter adapter;
	ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_list);
		
		for (int i = 0; i < 5; i++) {
            HashMap<String, String> map = new HashMap<String, String>();

            map.put("name", "Paul Onga " + Integer.toString(i));
            map.put("message", "Weeeena dude!");
 
            messageList.add(map);
        }
		
		list = (ListView) findViewById(R.id.list);
		 
        adapter = new LazyAdapter(this, messageList);
        list.setAdapter(adapter);
 
        list.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	Intent intent = new Intent(MessagesList.this, MessageDetail.class);
            	startActivity(intent);
            }
        });
		
	}

}
