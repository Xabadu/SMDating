package com.supermanket.supermanket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.ISideNavigationCallback;
import com.supermanket.utilities.MessageAdapter;
import com.supermanket.utilities.SideNavigationView;
import com.supermanket.utilities.UtilityBelt;
import com.supermanket.utilities.SideNavigationView.Mode;

public class MessagesList extends SherlockActivity implements ISideNavigationCallback {
	
	public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";

    private ImageView icon;
    private SideNavigationView sideNavigationView;
	
	ArrayList<HashMap<String, String>> messages = new ArrayList<HashMap<String, String>>();
	MessageAdapter adapter;
	ListView list;
	JSONArray contactsList;
	
	private Integer[] ids;
	private static SharedPreferences mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		setContentView(R.layout.activity_messages_list);
		
		icon = (ImageView) findViewById(android.R.id.icon);
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        if(mSharedPreferences.getString("USER_SEX", "female").equalsIgnoreCase("male")) {
        	sideNavigationView.setMenuItems(R.menu.side_navigation_male_menu);
        } else {
        	sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        }
        sideNavigationView.setMenuClickCallback(this);

        if (getIntent().hasExtra(EXTRA_TITLE)) {
            String title = getIntent().getStringExtra(EXTRA_TITLE);
            setTitle(title);
            sideNavigationView.setMode(Mode.LEFT);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        GetContacts contacts = new GetContacts(this);
        contacts.execute();
      	
	}
	
	public void loadInbox(final String data[]) {
		
		try {
			contactsList = new JSONArray(data[0]);
			ids = new Integer[contactsList.length()];
			if(contactsList.length() > 0) {
				for(int i = 0; i < contactsList.length(); i++) {
					JSONObject messageData = contactsList.getJSONObject(i);
					JSONArray messageList = messageData.getJSONArray("messages");
					HashMap<String, String> contact = new HashMap<String, String>();
					ids[i] = messageData.getInt("id");
					if(messageList.length() > 0) {
						JSONObject lastMessage = messageList.getJSONObject(messageList.length() - 1);
						contact.put("message", lastMessage.getString("content"));
						contact.put("date", lastMessage.getString("created_at"));
					} else {
						contact.put("message", "");
						contact.put("date", "");
					}
					contact.put("name", messageData.getString("username"));
					contact.put("image", messageData.getString("avatar"));
					messages.add(contact);
				}
			} else {
				Toast.makeText(this, "No tienes contactos", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		list = (ListView) findViewById(R.id.messageListList);
		 
        adapter = new MessageAdapter(this, messages, "list");
        list.setAdapter(adapter);
 
        list.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	Intent intent = new Intent(MessagesList.this, MessageDetail.class);
            	intent.putExtra("id", ids[position]);
            	startActivity(intent);
            }
        });
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sideNavigationView.toggleMenu();
                break;
            
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

    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch (itemId) {
            case R.id.side_navigation_menu_item1:
                invokeActivity(getString(R.string.menu_title1), R.drawable.ic_android1);
                break;

            case R.id.side_navigation_menu_item2:
                invokeActivity(getString(R.string.menu_title2), R.drawable.ic_android2);
                break;

            case R.id.side_navigation_menu_item3:
                invokeActivity(getString(R.string.menu_title3), R.drawable.ic_android3);
                break;

            case R.id.side_navigation_menu_item4:
                invokeActivity(getString(R.string.menu_title4), R.drawable.ic_android4);
                break;

            default:
                return;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        // hide menu if it shown
        if (sideNavigationView.isShown()) {
            sideNavigationView.hideMenu();
        } else {
            super.onBackPressed();
        }
    }

    private void invokeActivity(String title, int resId) {
        Intent intent = null;
        boolean action = true;
        if(title.equalsIgnoreCase("gente")) {
        	intent = new Intent(this, Dashboard.class);
        }
        if(title.equalsIgnoreCase("cercanos")) {
        	intent = new Intent(this, UsersMap.class);
        }
        if(title.equalsIgnoreCase("mensajes")) {
        	action = false;
        	MessagesList.this.finish();
        	startActivity(getIntent());
        }
        if(title.equalsIgnoreCase("perfil")) {
        	intent = new Intent(this, Account.class);
        }
        if(title.equalsIgnoreCase("cerrar sesion")) {
        	
        }
        
        if(action) {
        	intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_RESOURCE_ID, resId);
            intent.putExtra(EXTRA_MODE, sideNavigationView.getMode() == Mode.LEFT ? 0 : 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        
    }
	
	public class GetContacts extends AsyncTask<Void, Void, String[]> {
		
		MessagesList activityRef;
		ProgressDialog dialog;
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private AlertDialogs alert = new AlertDialogs();
		private String[] responses = new String[2];
		
		public GetContacts(MessagesList activityRef) {
			this.activityRef = activityRef;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(MessagesList.this, "", "Cargando mensajes", true);
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
		}
		
		@Override 
		protected String[] doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://demosmartphone.supermanket.cl/apim/contacts.json?app_key="
									+ api_key + "&signature=" + signature);
            get.setHeader("content-type", "application/json");
            
            try {
            	HttpResponse resp = client.execute(get);
				responses[0] = EntityUtils.toString(resp.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
			get = new HttpGet("http://demosmartphone.supermanket.cl/apim/profile.json?app_key="
									+ api_key + "&signature=" + signature);
            get.setHeader("content-type", "application/json");
            
            try {
            	HttpResponse resp = client.execute(get);
				responses[1] = EntityUtils.toString(resp.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
 
			return responses;
		}
		
		@Override
		protected void onPostExecute(String result[]) {
			super.onPostExecute(result);
			if(result == null) {
				alert.showAlertDialog(MessagesList.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Contacts", result[0]);
				dialog.dismiss();
				activityRef.loadInbox(result);
			}
		}
		
	}

}
