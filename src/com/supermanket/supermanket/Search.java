package com.supermanket.supermanket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.AutoCompleteDbAdapter;
import com.supermanket.utilities.ISideNavigationCallback;
import com.supermanket.utilities.LocationAdapter;
import com.supermanket.utilities.SearchAdapter;
import com.supermanket.utilities.SideNavigationView;
import com.supermanket.utilities.SideNavigationView.Mode;
import com.supermanket.utilities.UserAdapter;
import com.supermanket.utilities.UtilityBelt;

public class Search extends SherlockActivity implements ISideNavigationCallback {
	
	List<String> groupList;
    List<String> childList;
    Map<String, List<String>> paramCollection;
    ImageButton searchBtn;
    ImageButton searchAgainBtn;
    ExpandableListView expListView;
    AutoCompleteTextView locationAutoCompleteField;
    EditText ageFromField;
    EditText ageToField;
    static TextView searchLocationId;
    RelativeLayout searchLayout;
    
    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";

    private ImageView icon;
    private SideNavigationView sideNavigationView;
    
    private static SharedPreferences mSharedPreferences;
    
    static final String SERVICE_BASE_URL = "http://www.supermanket.com/apim/";
    
    static TextView locationId;
    
    LocationAdapter locationAdapter;
	AutoCompleteDbAdapter dbAdapter;
	
	private static boolean[] flavorsFlags = new boolean[13];
	private static boolean[] packageFlags = new boolean[16];
	private static boolean[] bonusPackFlags = new boolean[11];
	
	private ArrayList<JSONArray> usersContainer = new ArrayList<JSONArray>();
	private GridView mGridView;
    private PullToRefreshGridView mPullRefreshGridView;
    private UserAdapter uAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		searchLayout = (RelativeLayout) findViewById(R.id.searchBgLayout);
		searchLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hideSoftKeyboard(Search.this);
			}
        });
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
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
        mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
    	if(mSharedPreferences.getBoolean("ACTIVE_SEARCH", false)) {
    		fillGrid(mSharedPreferences.getString("ACTIVE_DATA", ""), mSharedPreferences.getString("AGE_FROM", ""), 
    				mSharedPreferences.getString("AGE_TO", ""), mSharedPreferences.getString("FLAVORS", ""), 
    				mSharedPreferences.getString("PACKAGES", ""), mSharedPreferences.getString("BONUS_PACKS", ""));
    	} else {
    		paramsScreen("create", "", "", "", "", "");
    	}
	}
	
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	private void paramsScreen(String from, String ageFrom, String ageTo, String flavors,
			String packages, String bonuspacks) {
		
		if(from.equalsIgnoreCase("results")) {
			setContentView(R.layout.activity_search);
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
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
	        searchLayout = (RelativeLayout) findViewById(R.id.searchBgLayout);
			searchLayout.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					hideSoftKeyboard(Search.this);
				}
	        });

	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		locationId = (TextView) findViewById(R.id.personalFormLocationId);
		locationAutoCompleteField = (AutoCompleteTextView) findViewById(R.id.searchLocationFieldText);
		searchLocationId = (TextView) findViewById(R.id.searchLocationId);
		expListView = (ExpandableListView) findViewById(R.id.searchList);
		searchBtn = (ImageButton) findViewById(R.id.searchBtn);
		ageFromField = (EditText) findViewById(R.id.searchAgeFromText);
		ageToField = (EditText) findViewById(R.id.searchAgeToText);
		
		if(!ageFrom.equals("")) {
			ageFromField.setText(ageFrom);
		}
		if(!ageTo.equals("")) {
			ageToField.setText(ageTo);
		}
		
		if(!flavors.equals("")) {
			String allTheFlavors[] = flavors.split(",");
			for(int i = 0; i < allTheFlavors.length; i++) {
				flavorsFlags[Integer.parseInt(allTheFlavors[i])] = true;
			}
    	} 
		
		if(!packages.equals("")) {
			String allThePackages[] = packages.split(",");
			for(int i = 0; i < allThePackages.length; i++) {
				packageFlags[Integer.parseInt(allThePackages[i])] = true;
			}
    	} 
		
		if(!bonuspacks.equals("")) {
			String allTheBonusPacks[] = bonuspacks.split(",");
			for(int i = 0; i < allTheBonusPacks.length; i++) {
				bonusPackFlags[Integer.parseInt(allTheBonusPacks[i])] = true;
			}
    	} 	
		
		
		/* LOCACTION OCULTADA - ACTIVAR EN FUTURA VERSION OJO CON INTEGRACION BD */
		
		locationAutoCompleteField.setVisibility(View.GONE);
		
		if(mSharedPreferences.getString("USER_SEX", "female").equalsIgnoreCase("male")) {
			expListView.setVisibility(View.GONE);
        } else {
        	createGroupList();
	        createCollection();
	       
	        final SearchAdapter expListAdapter = new SearchAdapter(
	                this, groupList, paramCollection);
	        expListView.setAdapter(expListAdapter);
	  
	        expListView.setOnChildClickListener(new OnChildClickListener() {
	 
	            public boolean onChildClick(ExpandableListView parent, View v,
	                    int groupPosition, int childPosition, long id) {
	                final String selected = (String) expListAdapter.getChild(
	                        groupPosition, childPosition);
	                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
	                        .show();
	                return true;
	            }
	        });
        }
		
        /*
        dbAdapter = new AutoCompleteDbAdapter(this);
		locationAdapter = new LocationAdapter(dbAdapter, this, "search");
		locationAutoCompleteField.setAdapter(locationAdapter);
		locationAutoCompleteField.setOnItemClickListener(locationAdapter);
        */
		
		searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//hideSoftKeyboard(Search.this);
				SearchUsers search = new SearchUsers(Search.this);
				search.execute();
			}
		});
		
	}
	
	public static void setId(int id) {
		searchLocationId.setText(Integer.toString(id));
	}
	
	public void fillGrid(String data, final String ageFrom, final String ageTo, final String flavors, 
			final String packages, final String bonuspacks) {
		
		setContentView(R.layout.activity_search_results);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
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
		searchAgainBtn = (ImageButton) findViewById(R.id.searchAgainBtn);
		
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean("ACTIVE_SEARCH", true);
		editor.putString("ACTIVE_DATA", data);
		editor.putString("AGE_FROM", ageFrom);
		editor.putString("AGE_TO", ageTo);
		editor.putString("FLAVORS", flavors);
		editor.putString("PACKAGES", packages);
		editor.putString("BONUS_PACKS", bonuspacks);
		editor.commit();
		
		JSONObject resultObject;
    	JSONArray usersInfo = null;
    	usersContainer.clear();
		try {
			resultObject = new JSONObject(data);
			usersInfo = resultObject.getJSONArray("users");
			usersContainer.add(usersInfo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.searchResultsGrid);
    	
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
		
		uAdapter = new UserAdapter(Search.this, true, true, 4, 0, 4, 0, width, height, usersContainer);
		mGridView.setAdapter(uAdapter);

		uAdapter.notifyDataSetChanged();

		mPullRefreshGridView.onRefreshComplete();

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
					Intent intent = new Intent(Search.this, UserProfileSearch.class);
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
		
		searchAgainBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
				Editor editor = mSharedPreferences.edit();
				editor.remove("ACTIVE_SEARCH");
				editor.remove("ACTIVE_DATA");
				editor.remove("AGE_FROM");
				editor.remove("AGE_TO");
				editor.remove("FLAVORS");
				editor.remove("PACKAGES");
				editor.remove("BONUS_PACKS");
				editor.commit();
				paramsScreen("results", ageFrom, ageTo, flavors, packages, bonuspacks);
			}
		});
		
	}

	private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Sabores");
        groupList.add("Envases");
        groupList.add("Bonus Pack");
    }
 
    private void createCollection() {
        String[] sabores = { "Intelectual", "Ejecutivo", "Carretero", "Deportista", "Aventurero",
        		"Artista", "Tímido", "Geek", "Gamer", "Tuerca", "Tallero", "Hipster", "Fixero"};
        String[] envases = { "Rockero", "Metalero", "Casual", "Uniformado", "Skater",
        		"Hip-Hop", "Otaku", "Surfista", "Verde", "Gótico", "Motoquero", "Reggaetonero", 
        		"Zorrón", "Parrillero", "Lana", "Pokemón"};
        String[] bonusPack = { "Chef", "Vegetariano", "Six Pack", "Fumador", "No fumador",  "Romántico",
        		"Músico", "Musculoso", "Millonario y muriendo", "Cantante", "Maestro chasquilla"};        
 
        paramCollection = new LinkedHashMap<String, List<String>>();
 
        for (String param : groupList) {
            if (param.equals("Sabores")) {
                loadChild(sabores);
            } else if (param.equals("Envases"))
                loadChild(envases);
            else
                loadChild(bonusPack);
 
            paramCollection.put(param, childList);
        }
    }
 
    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }
 
    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
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
	                	Intent intent = new Intent(Search.this, Login.class);
	                	startActivity(intent);
	                	Search.this.finish();
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
    
    public static void changeParam(int position, boolean value, int group) {
    	if(group == 0) {
    		flavorsFlags[position] = value;
    	} else if(group == 1) {
    		packageFlags[position] = value;
    	} else if(group == 2) {
    		bonusPackFlags[position] = value;
    	}
    	Log.d("Position", Integer.toString(position));
    }
    
    public static boolean isChecked(int group, int position) {
    	if(group == 0) {
    		return flavorsFlags[position];
    	} else if(group == 1) {
    		return packageFlags[position];
    	} else if(group == 2) {
    		return bonusPackFlags[position];
    	}
    	return false;
    }
    
    @Override
    protected void onRestart() {
    	//super.onRestart();
    	mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
    	if(mSharedPreferences.getBoolean("ACTIVE_SEARCH", false)) {
    		fillGrid(mSharedPreferences.getString("ACTIVE_DATA", ""), mSharedPreferences.getString("AGE_FROM", ""), 
    				mSharedPreferences.getString("AGE_TO", ""), mSharedPreferences.getString("FLAVORS", ""), 
    				mSharedPreferences.getString("PACKAGES", ""), mSharedPreferences.getString("BONUS_PACKS", ""));
    	} else {
    		paramsScreen("results", "", "", "", "", "");
    	}
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
        	intent = new Intent(this, Dashboard.class);
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
        	action = false;
        	Search.this.finish();
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
    
    private class SearchUsers extends AsyncTask<Void, Void, String> {

		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private Search activityRef;
		private UtilityBelt utilityBelt = new UtilityBelt();
		String ageFrom;
		String ageTo;
		String location;
		String flavors = "";
		String packages = "";
		String bonuspacks = "";

		public SearchUsers(Search activityRef) {
			this.activityRef = activityRef;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			dialog = ProgressDialog.show(Search.this, "", "Buscando...", true);
			
			ageFrom = ageFromField.getText().toString();
			ageTo = ageToField.getText().toString();
			location = searchLocationId.getText().toString();
			
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
			if(ageFrom.equals("") || Integer.parseInt(ageFrom) < 18) {
				ageFrom = "18";
			}
			
			if(ageTo.equals("") || Integer.parseInt(ageTo) < 18) {
				ageTo = "99";
			}
			
			if(Integer.parseInt(ageTo) < Integer.parseInt(ageFrom)) {
				String temp = ageFrom;
				ageFrom = ageTo;
				ageTo = temp;
			}
			
			if(!mSharedPreferences.getString("USER_SEX", "female").equalsIgnoreCase("male")) {
	        	
				for(int i = 0; i < flavorsFlags.length; i++) {
					if(flavorsFlags[i]) {
						flavors += Integer.toString(i);
						if(i < flavorsFlags.length - 1) {
							flavors += ",";
						}
					}
				}
				
				for(int i = 0; i < packageFlags.length; i++) {
					if(packageFlags[i]) {
						packages += Integer.toString(i);
						if(i < packageFlags.length - 1) {
							packages += ",";
						}
					}
				}
				
				for(int i = 0; i < bonusPackFlags.length; i++) {
					if(bonusPackFlags[i]) {
						bonuspacks += Integer.toString(i);
						if(i < bonusPackFlags.length - 1) {
							bonuspacks += ",";
						}
					}
				}
				
	        }

		}

		@Override
		protected String doInBackground(Void... params) {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "search.json?app_key="
					+ api_key + "&signature=" + signature);
            post.setHeader("content-type", "application/json");
            
            JSONObject q = new JSONObject();
            JSONObject query = new JSONObject();
            
            Log.d("age from", ageFrom);
            Log.d("age to", ageTo);
            Log.d("Location", location);
            Log.d("Flavors", flavors);
            Log.d("Packages", packages);
            Log.d("Bonus Pack", bonuspacks);
            
            try {
				q.put("age_gteq", ageFrom);
				q.put("age_lteq", ageTo);
				if(!flavors.equals("")) {
					q.put("flavor_ids_eq", flavors);
				}
				if(!packages.equals("")) {
					q.put("packaging_ids_eq", packages);
				}
				if(!bonuspacks.equals("")) {
					q.put("bonus_pack_ids_eq", bonuspacks);
				}
				if(!location.equals("")) {
					q.put("city_id", location);
				}
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

            try {
            	query.put("q", q);
            } catch (JSONException e1) {
				e1.printStackTrace();
			}
            
            try {
				StringEntity entity = new StringEntity(query.toString());
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
            
            try {
            	HttpResponse resp = client.execute(post);
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
			searchLocationId.setText("");
			if(result == null) {
				alert.showAlertDialog(Search.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				activityRef.fillGrid(result, ageFrom, ageTo, flavors, packages, bonuspacks);
				dialog.dismiss();

			}

		}

	}

}
