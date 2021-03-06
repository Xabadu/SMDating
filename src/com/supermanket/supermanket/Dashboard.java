package com.supermanket.supermanket;

import java.io.IOException;
import java.util.ArrayList;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.ConectivityTools;
import com.supermanket.utilities.ISideNavigationCallback;
import com.supermanket.utilities.SideNavigationView;
import com.supermanket.utilities.SideNavigationView.Mode;
import com.supermanket.utilities.UserAdapter;
import com.supermanket.utilities.UtilityBelt;

public class Dashboard extends SherlockActivity implements ISideNavigationCallback {


	public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";

    private ImageView icon;
    private SideNavigationView sideNavigationView;
    private GridView mGridView;
    private PullToRefreshGridView mPullRefreshGridView;
    private UserAdapter uAdapter;
    private ArrayList<JSONArray> usersContainer = new ArrayList<JSONArray>();
    Integer currentPage[] = new Integer[1];

    private static SharedPreferences mSharedPreferences;

    ImageButton dashboardNearByUsersBtn;
    RelativeLayout dashboardBottomLayout;
    ConectivityTools ct;

    getUsers users;

    static final String SERVICE_BASE_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
        setContentView(R.layout.activity_dashboard);

		dashboardNearByUsersBtn = (ImageButton) findViewById(R.id.dashboardNearByUsersBtn);
		dashboardBottomLayout = (RelativeLayout) findViewById(R.id.dashboardBottomLayout);

		if(mSharedPreferences.getString("USER_SEX", "female").equalsIgnoreCase("male")) {
			dashboardBottomLayout.setVisibility(View.GONE);
		} else {
			dashboardBottomLayout.setVisibility(View.VISIBLE);
		}

		dashboardNearByUsersBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Dashboard.this, UsersMap.class);
				startActivity(intent);
			}
		});

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
            sideNavigationView.setMode(getIntent().getIntExtra(EXTRA_MODE, 0) == 0 ? Mode.LEFT : Mode.RIGHT);
            sideNavigationView.setMode(Mode.LEFT);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentPage[0] = 1;

        ct = new ConectivityTools(getApplicationContext());

        if (!ct.isConnectingToInternet()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
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
        	users = new getUsers(this);
            users.execute(currentPage);
        }


    }

    public void fillGrid(String result, int page) {

    	JSONObject resultObject;
    	JSONArray usersInfo = null;
		try {
			resultObject = new JSONObject(result);
			usersInfo = resultObject.getJSONArray("users");
			usersContainer.add(usersInfo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	page++;
    	currentPage[0] = page;

    	mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.dashboardUsersGrid);

		mGridView = mPullRefreshGridView.getRefreshableView();

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		int width = 0;
		int height = 0;
		int density = displaymetrics.densityDpi;

		switch(density) {

			case DisplayMetrics.DENSITY_LOW:
				 width = 71;
				 height = 71;
				 break;

			case DisplayMetrics.DENSITY_MEDIUM:
				 width = 95;
				 height = 95;
				 break;

			case DisplayMetrics.DENSITY_HIGH:
				 width = 142;
				 height = 142;
				 break;

			case DisplayMetrics.DENSITY_XHIGH:
				 width = 190;
				 height = 190;
				 break;
		}

		uAdapter = new UserAdapter(Dashboard.this, true, true, 4, 0, 4, 0, width, height, usersContainer);
		mGridView.setAdapter(uAdapter);

		uAdapter.notifyDataSetChanged();

		// Call onRefreshComplete when the list has been refreshed.
		mPullRefreshGridView.onRefreshComplete();

		if(page > 2) {
			int actualPage = (page - 2)*15;
			Log.d("Position", Integer.toString(actualPage));
			mGridView.setSelection(actualPage);
		}

		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {



			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				usersContainer = new ArrayList<JSONArray>();
				currentPage[0] = 1;
				if (!ct.isConnectingToInternet()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
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
		        	users = new getUsers(Dashboard.this);
			        users.execute(currentPage);
		        }

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				if (!ct.isConnectingToInternet()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
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
		        	users = new getUsers(Dashboard.this);
			        users.execute(currentPage);
		        }

			}

		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				int arrayNumber;

				if(position < 16) {
					arrayNumber = 0;
				} else {
					arrayNumber = (int) Math.floor(position/16);
				}

				JSONArray userArray = usersContainer.get(arrayNumber);
				try {
					JSONObject userPosition;
					int userId;
					String picURL;
					String userName = null;
					if(position < 16) {
						userPosition = userArray.getJSONObject(position);
						userId = userPosition.getInt("id");
						picURL = userPosition.getString("avatar_medium");
						userName = userPosition.getString("username");
					} else {
						Log.d("Position", Integer.toString(position));
						Log.d("Array", Integer.toString(arrayNumber));
						userPosition = userArray.getJSONObject(position-(16*arrayNumber));
						userId = userPosition.getInt("id");
						picURL = userPosition.getString("avatar_medium");
						userName = userPosition.getString("username");
					}
					Intent intent = new Intent(Dashboard.this, UserProfile.class);
					intent.putExtra("id", userId);
					intent.putExtra("pic", picURL);
					intent.putExtra("username", userName);
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

    }

    @Override
   	protected void onResume() {
   		synchronized (GcmBroadcastReceiver.CURRENTACTIVITYLOCK) {
               GcmBroadcastReceiver.currentActivity = this;
   		}
   		super.onResume();
   	}

   	@Override
    protected void onPause() {
   		synchronized (GcmBroadcastReceiver.CURRENTACTIVITYLOCK) {
   			GcmBroadcastReceiver.currentActivity = null;
        }
        super.onPause();
    }

   	@Override
   	protected void onRestart() {
   		super.onRestart();
   		usersContainer.clear();
   		currentPage[0] = 1;
   		getUsers users = new getUsers(this);
        users.execute(currentPage);
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
	                	Intent intent = new Intent(Dashboard.this, Login.class);
	                	startActivity(intent);
	                	Dashboard.this.finish();
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
        if(title.equalsIgnoreCase("inicio")) {
        	action = false;
        	Dashboard.this.finish();
        	startActivity(getIntent());
        }
        if(title.equalsIgnoreCase("cercanos")) {
        	intent = new Intent(this, UsersMap.class);
        }
        if(title.equalsIgnoreCase("mensajes")) {
        	intent = new Intent(this, MessagesList.class);
        }
        if(title.equalsIgnoreCase("perfil")) {
        	intent = new Intent(this, Account.class);
        }
        if(title.equalsIgnoreCase("buscar gente")) {
        	intent = new Intent(this, Search.class);
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

    private class getUsers extends AsyncTask<Integer, Integer, String> {

		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private Dashboard activityRef;
		private UtilityBelt utilityBelt = new UtilityBelt();

		public getUsers(Dashboard activityRef) {
			this.activityRef = activityRef;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			dialog = ProgressDialog.show(Dashboard.this, "", "Cargando usuarios...", true);

			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + "page" + Integer.toString(currentPage[0]) + api_secret);

		}

		@Override
		protected String doInBackground(Integer... params) {

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(SERVICE_BASE_URL + "users.json?app_key="
									+ api_key + "&page=" + Integer.toString(currentPage[0]) + "&signature=" + signature);
            get.setHeader("content-type", "application/json");

            try {
            	HttpResponse resp = client.execute(get);
				return EntityUtils.toString(resp.getEntity());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(result == null) {
				alert.showAlertDialog(Dashboard.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				activityRef.fillGrid(result, currentPage[0]);
				dialog.dismiss();

			}

		}

	}


}
