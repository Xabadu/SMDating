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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.supermanket.supermanket.MessagesList.GetContacts;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.ConectivityTools;
import com.supermanket.utilities.ISideNavigationCallback;
import com.supermanket.utilities.MessageAdapter;
import com.supermanket.utilities.SideNavigationView;
import com.supermanket.utilities.UtilityBelt;
import com.supermanket.utilities.SideNavigationView.Mode;
import com.supermanket.utilities.WatchersAdapter;

public class Watchers extends SherlockActivity implements ISideNavigationCallback {

	SharedPreferences mSharedPreferences;
	public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";

    private ImageView icon;
    private SideNavigationView sideNavigationView;
    WatchersAdapter adapter;
    ListView list;
    ConectivityTools ct;
    ArrayList<HashMap<String, String>> watchers = new ArrayList<HashMap<String, String>>();
    JSONArray watchersList;

    static final String SERVICE_BASE_URL = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		if(!mSharedPreferences.getBoolean("LOGGED_IN", false)) {
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
			this.finish();
		} else {
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
	        ct = new ConectivityTools(getApplicationContext());
	        if (!ct.isConnectingToInternet()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Watchers.this);
				builder.setTitle(R.string.alert_attention_title);
				builder.setMessage(R.string.alert_internet);
				builder.setPositiveButton(R.string.btn_settings, new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int id) {
	    				Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
	    				startActivity(intent);
	    			}
	    		});
				builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
	        } else {
	        	GetWatchers watchers = new GetWatchers(this);
		        watchers.execute();
	        }

		}
	}

	public void loadWatchers(final String data) {

		try {
			JSONObject responseObj = new JSONObject(data);
			watchersList = responseObj.getJSONArray("visits");
			if(watchersList.length() > 0) {
				for(int i = 0; i < watchersList.length(); i++) {
					JSONObject watchersData = watchersList.getJSONObject(i);
					HashMap<String, String> contact = new HashMap<String, String>();

					if(watchersData.getBoolean("is_hidde")) {
						contact.put("name", Watchers.this.getResources().getString(R.string.unknown_user));
						contact.put("image", "");

					} else {
						contact.put("name", watchersData.getString("username"));
						contact.put("image", watchersData.getString("avatar"));
					}
					if(watchersData.getBoolean("is_new")) {
						contact.put("new", Watchers.this.getResources().getString(R.string.new_visit));
					} else {
						contact.put("new", "");
					}
					contact.put("when", watchersData.getString("distance_of_time_in_words_to_now"));
					contact.put("count", watchersData.getString("visits_count"));


					watchers.add(contact);
				}
			} else {
				Toast.makeText(this, "No tienes visitas", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		list = (ListView) findViewById(R.id.messageListList);

        adapter = new WatchersAdapter(this, watchers);
        list.setAdapter(adapter);

        final JSONArray blockInfo = watchersList;

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	try {
					JSONObject blockObj = blockInfo.getJSONObject(position);
					if(!blockObj.getBoolean("is_hidde")) {
						Intent intent = new Intent(Watchers.this, UserProfileWatchers.class);
		            	intent.putExtra("id", blockObj.getInt("id"));
						intent.putExtra("pic", blockObj.getString("avatar"));
						intent.putExtra("username", blockObj.getString("username"));
						startActivity(intent);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
	            	AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle(R.string.alert_attention_title);
					builder.setMessage(R.string.alert_logout);
					builder.setPositiveButton(R.string.btn_logout, new DialogInterface.OnClickListener() {
		    			@Override
		    			public void onClick(DialogInterface dialog, int id) {
		    				Editor e = mSharedPreferences.edit();
		                	e.remove("LOGGED_IN");
		                    e.commit();
		                	Intent intent = new Intent(Watchers.this, Login.class);
		                	startActivity(intent);
		                	Watchers.this.finish();
		    			}
		    		});
					builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
					AlertDialog alert = builder.create();
					alert.show();

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
	                invokeActivity(getString(R.string.menu_title1), R.drawable.ic_social_group);
	                break;

	            case R.id.side_navigation_menu_item2:
	                invokeActivity(getString(R.string.menu_title2), R.drawable.ic_location_place);
	                break;

	            case R.id.side_navigation_menu_item3:
	                invokeActivity(getString(R.string.menu_title3), R.drawable.ic_content_email);
	                break;

	            case R.id.side_navigation_menu_item4:
	                invokeActivity(getString(R.string.menu_title4), R.drawable.ic_action_settings);
	                break;

	            case R.id.side_navigation_menu_item6:
	            	invokeActivity(getString(R.string.menu_title6), R.drawable.ic_action_search);
	            	break;

	            case R.id.side_navigation_menu_item7:
	            	invokeActivity(getString(R.string.menu_title7), R.drawable.ic_alert_warning);

	            default:
	                return;
	        }
	        finish();
	    }

	    @Override
	    public void onBackPressed() {
	        if (sideNavigationView.isShown()) {
	            sideNavigationView.hideMenu();
	        }
	    }

	    private void invokeActivity(String title, int resId) {
	        Intent intent = null;
	        boolean action = true;
	        if(title.equalsIgnoreCase(this.getResources().getString(R.string.menu_title1))) {
	        	intent = new Intent(this, Dashboard.class);
	        }
	        if(title.equalsIgnoreCase(this.getResources().getString(R.string.menu_title2))) {
	        	intent = new Intent(this, UsersMap.class);
	        }
	        if(title.equalsIgnoreCase(this.getResources().getString(R.string.menu_title3))) {
	        	intent = new Intent(this, MessagesList.class);
	        }
	        if(title.equalsIgnoreCase(this.getResources().getString(R.string.menu_title4))) {
	        	intent = new Intent(this, Account.class);
	        }
	        if(title.equalsIgnoreCase(this.getResources().getString(R.string.menu_title6))) {
	        	intent = new Intent(this, Search.class);
	        }
	        if(title.equalsIgnoreCase(this.getResources().getString(R.string.menu_title7))) {
	        	action = false;
	        	Watchers.this.finish();
	        	startActivity(getIntent());
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


	    public class GetWatchers extends AsyncTask<Void, Void, String> {

			Watchers activityRef;
			ProgressDialog dialog;
			private String api_key;
			private String api_secret;
			private String signature;
			private SharedPreferences mSharedPreferences;
			private UtilityBelt utilityBelt = new UtilityBelt();
			private AlertDialogs alert = new AlertDialogs();

			public GetWatchers(Watchers activityRef) {
				this.activityRef = activityRef;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = ProgressDialog.show(Watchers.this, "", Watchers.this.getResources().getString(R.string.progress_loading), true);
				mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
				api_key = mSharedPreferences.getString("API_KEY", "");
				api_secret = mSharedPreferences.getString("API_SECRET", "");
				signature = utilityBelt.md5("app_key" + api_key + api_secret);
			}

			@Override
			protected String doInBackground(Void... params) {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(SERVICE_BASE_URL + "visits.json?app_key="
										+ api_key + "&signature=" + signature);
	            get.setHeader("content-type", "application/json");

	            try {
	            	HttpResponse resp = client.execute(get);
					return EntityUtils.toString(resp.getEntity());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if(result == null) {
					alert.showAlertDialog(Watchers.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
					dialog.dismiss();
				} else {
					Log.d("Watchers", result);
					dialog.dismiss();
					activityRef.loadWatchers(result);
				}
			}

		}

}
