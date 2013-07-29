package com.supermanket.supermanket;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.supermanket.utilities.AutoCompleteDbAdapter;
import com.supermanket.utilities.ISideNavigationCallback;
import com.supermanket.utilities.LocationAdapter;
import com.supermanket.utilities.SearchAdapter;
import com.supermanket.utilities.SideNavigationView;
import com.supermanket.utilities.SideNavigationView.Mode;

public class Search extends SherlockActivity implements ISideNavigationCallback {
	
	List<String> groupList;
    List<String> childList;
    Map<String, List<String>> paramCollection;
    ExpandableListView expListView;
    
    public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";

    private ImageView icon;
    private SideNavigationView sideNavigationView;
    
    private static SharedPreferences mSharedPreferences;
    
    static TextView locationId;
    
    LocationAdapter locationAdapter;
	AutoCompleteDbAdapter dbAdapter;
	
	private static boolean[] flavorsFlags = new boolean[13];
	private static boolean[] packageFlags = new boolean[16];
	private static boolean[] bonusPackFlags = new boolean[11];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		paramsScreen();
	}
	
	private void paramsScreen() {
		
		locationId = (TextView) findViewById(R.id.personalFormLocationId);
		
		createGroupList();
		 
        createCollection();
 
        expListView = (ExpandableListView) findViewById(R.id.searchList);
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
    
    public static void changeParam(int position, boolean value) {
    	/*if(type.equalsIgnoreCase("flavor")) {
    		if(value) {
    			flavorsFlags[position] = true;
    		} else {
    			flavorsFlags[position] = false;
    		}
    	} else if(type.equalsIgnoreCase("package")) {
    		if(value) {
    			packageFlags[position] = true;
    		} else {
    			packageFlags[position] = false;
    		}
    	} else if(type.equalsIgnoreCase("bonuspack")) {
    		if(value) {
    			bonusPackFlags[position] = true;
    		} else {
    			bonusPackFlags[position] = false;
    		}
    	}*/
    	Log.d("Position", Integer.toString(position));
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
        if(title.equalsIgnoreCase("gente")) {
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

}
