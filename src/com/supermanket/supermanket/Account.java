package com.supermanket.supermanket;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.supermanket.supermanket.Login.ErrorDialogFragment;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.AutoCompleteDbAdapter;
import com.supermanket.utilities.ConectivityTools;
import com.supermanket.utilities.ISideNavigationCallback;
import com.supermanket.utilities.LocationAdapter;
import com.supermanket.utilities.SideNavigationView;
import com.supermanket.utilities.UtilityBelt;
import com.supermanket.utilities.SideNavigationView.Mode;

public class Account extends SherlockActivity implements ISideNavigationCallback {
	
	public static final String EXTRA_TITLE = "com.devspark.sidenavigation.sample.extra.MTGOBJECT";
    public static final String EXTRA_RESOURCE_ID = "com.devspark.sidenavigation.sample.extra.RESOURCE_ID";
    public static final String EXTRA_MODE = "com.devspark.sidenavigation.sample.extra.MODE";
    private ImageView icon;
    private SideNavigationView sideNavigationView;
	AutoCompleteTextView personalFormLocationText;
	Button personalFormBirthdayBtn;
	Button accountSaveBtn;
	Button accountBackBtn;
	CheckBox flavors[] = new CheckBox[13];
	static int flavorsIds[]={R.id.flavors1,R.id.flavors2,R.id.flavors3,R.id.flavors4,
	    R.id.flavors5,R.id.flavors6,R.id.flavors7,R.id.flavors8,R.id.flavors9,R.id.flavors10, R.id.flavors11, 
	    R.id.flavors12,R.id.flavors13};
	static int packagesIds[]={R.id.packages1,R.id.packages2,R.id.packages3,R.id.packages4,
	    R.id.packages5,R.id.packages6,R.id.packages7,R.id.packages8,R.id.packages9,R.id.packages10,R.id.packages11, 
	    R.id.packages12,R.id.packages13,R.id.packages14,R.id.packages15,R.id.packages16};
	CheckBox bonusPack[] = new CheckBox[11];
	static int bonusPackIds[]={R.id.bonusPack1,R.id.bonusPack2,R.id.bonusPack3,R.id.bonusPack4,
	    R.id.bonusPack5,R.id.bonusPack6,R.id.bonusPack7,R.id.bonusPack8,R.id.bonusPack9,R.id.bonusPack10, R.id.bonusPack11};
	CheckBox packages[] = new CheckBox[16];
	EditText personalFormNameText;
	EditText personalFormWeightText;
	EditText personalFormHeightText;
	EditText aboutFormDescriptionText;
	EditText aboutFormCrazyText;
	EditText nutritionalFormLanguagesText;
	EditText nutritionalFormFavoriteAlbumText;
	EditText nutritionalFormFavoriteBookText;
	EditText nutritionalFormFavoriteArtistText;
	EditText nutritionalFormFavoriteMovieText;
	EditText nutritionalFormJokeText;
	EditText nutritionalFormDrinkText;
	EditText nutritionalFormFavoriteCountryText;
	EditText nutritionalFormQuoteText;
	EditText nutritionalFormHobbyText;
	EditText nutritionalFormFavoriteDateText;
	EditText nutritionalFormSportsText;
	EditText nutritionalFormFavoriteFoodText;
	EditText accessoriesMaleChildrenText;
	EditText accessoriesFemaleChildrenText;
	EditText effectsDefectsText;
	EditText effectsHatesText;
	EditText effectsSexPosesText;
	EditText effectsSexToysText;
	EditText effectsGuiltyPleasureText;
	EditText effectsSexualFantasyText;
	EditText effectsFetishesText;
	EditText accountInfoUsernameText;
	EditText accountInfoEmailText;
	EditText accountInfoPasswordText;
	EditText accountInfoConfirmPasswordText;
	ImageView accountUserAvatar;
	RelativeLayout accountLayout;
	Spinner nutritionalFormReligionSpinner;
	Spinner nutritionalFormOccupationSpinner;
	Spinner nutritionalFormPoliticsSpinner;
	Spinner nutritionalFormZodiacSignSpinner;
	Spinner accessoriesLivesWithSpinner;
	Spinner accessoriesSalarySpinner;
	Spinner accessoriesHomeTypeSpinner;
	Spinner accessoriesBeachHouseSpinner;
	Spinner accessoriesPoolSpinner;
	Spinner accessoriesJacuzziSpinner;
	Spinner accessoriesMotorcycleSpinner;
	Spinner accessoriesBikeSpinner;
	Spinner accessoriesCarSpinner;
	Spinner accessoriesPetSpinner;
	Spinner effectsDrinksSpinner;
	Spinner effectsSmokesSpinner;
	Spinner effectsFavoriteDrugSpinner;
	TableRow accountPersonalInfoRow;
	TableRow accountAboutMeRow;
	TableRow accountNutritionalInfoRow;
	TableRow accountAccessoriesRow;
	TableRow accountSideEffectsRow;
	TableRow accountInfoRow;
	TableRow accountFlavorsRow;
	TableRow accountPackagesRow;
	TableRow accountBonusPackRow;
	TextView accountNutritionalInfoRowText;
	static TextView personalLocationId;
	View flavorsSeparator;
	View packagesSeparator;
	View bonusPackSeparator;
	
	ImageLoader imageLoader = ImageLoader.getInstance();
	
	private int day;
	private int month;
	private int year;
	final Calendar c = Calendar.getInstance();
	static final int DATE_DIALOG_ID = 999;
	String[] localBday = new String[3];
	String imagesList;
	ProgressDialog pDialog;
	ConectivityTools ct;
	
	UserData userData;
	
	private static SharedPreferences mSharedPreferences;
	
	static final String SERVICE_BASE_URL = "http://www.supermanket.com/apim/";
	
	LocationAdapter locationAdapter;
	AutoCompleteDbAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		initView();
	}
	
	public void initView() {
		setContentView(R.layout.activity_account);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		icon = (ImageView) findViewById(android.R.id.icon);
		sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
		if(mSharedPreferences.getString("USER_SEX", "female").equalsIgnoreCase("male")) {
        	sideNavigationView.setMenuItems(R.menu.side_navigation_male_menu);
        } else {
        	sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        }
	    sideNavigationView.setMenuClickCallback(this);
	    
	    accountLayout = (RelativeLayout) findViewById(R.id.accountLayout);
	    accountLayout.setVisibility(View.INVISIBLE);
	    
	    ct = new ConectivityTools(getApplicationContext());
	    
	    if (getIntent().hasExtra(EXTRA_TITLE)) {
	    	String title = getIntent().getStringExtra(EXTRA_TITLE);
	        setTitle(title);
	        sideNavigationView.setMode(getIntent().getIntExtra(EXTRA_MODE, 0) == 0 ? Mode.LEFT : Mode.RIGHT);
	    }
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
		if (!ct.isConnectingToInternet()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
        	userData = new UserData(this);
    		userData.execute();
        }
		
	}
	
	public void loadProfile(final String[] result) {
			    
		pDialog = ProgressDialog.show(this, "", "Cargando imágenes");
		
		accountUserAvatar = (ImageView) findViewById(R.id.accountAvatarImage);
		accountPersonalInfoRow = (TableRow) findViewById(R.id.accountPersonalInfoRow);
		accountAboutMeRow = (TableRow) findViewById(R.id.accountAboutMeRow);
		accountNutritionalInfoRow = (TableRow) findViewById(R.id.accountNutritionalInfoRow);
		accountAccessoriesRow = (TableRow) findViewById(R.id.accountAccessoriesRow);
		accountSideEffectsRow = (TableRow) findViewById(R.id.accountSideEffectsRow);
		accountInfoRow = (TableRow) findViewById(R.id.accountInfoRow);
		accountFlavorsRow = (TableRow) findViewById(R.id.accountFlavorsRow);
		accountPackagesRow = (TableRow) findViewById(R.id.accountPackagesRow);
		accountBonusPackRow = (TableRow) findViewById(R.id.accountBonusPackRow);
		accountNutritionalInfoRowText = (TextView) findViewById(R.id.accountNutritionalInfoText);
		
		JSONObject resultObj;
		String url = null;
		try {
			resultObj = new JSONObject(result[0]);
			JSONArray photos = resultObj.getJSONArray("photos"); 
			if(!resultObj.getString("sex").equalsIgnoreCase("male")) {
				accountFlavorsRow.setVisibility(View.GONE);
				accountPackagesRow.setVisibility(View.GONE);
				accountBonusPackRow.setVisibility(View.GONE);
				flavorsSeparator = (View) findViewById(R.id.flavorsSeparator);
				packagesSeparator = (View) findViewById(R.id.packagesSeparator);
				bonusPackSeparator = (View) findViewById(R.id.bonusPackSeparator);
				flavorsSeparator.setVisibility(View.GONE);
				packagesSeparator.setVisibility(View.GONE);
				bonusPackSeparator.setVisibility(View.GONE);
				accountNutritionalInfoRowText.setText(R.string.lbl_profile_information_female);
			}
			accountLayout.setVisibility(View.VISIBLE);
			for(int i = 0; i < photos.length(); i++) {
				JSONObject picInfo = photos.getJSONObject(i);
				if(picInfo.getBoolean("avatar?")) {
					url = picInfo.getString("medium");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		DisplayImageOptions options = new DisplayImageOptions.Builder().build();
		imageLoader.displayImage(url, accountUserAvatar, options, new ImageLoadingListener() {
	        @Override
	        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	        	pDialog.dismiss();
	        }

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {				
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1,
					FailReason arg2) {				
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
			}
	    });
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		int avatarWidth = 0;
		int avatarHeight = 0;
		int density = displaymetrics.densityDpi;
		
		switch(density) {
			
			case DisplayMetrics.DENSITY_LOW:
				 avatarWidth = 113;
				 avatarHeight = 113;
				 break;
			
			case DisplayMetrics.DENSITY_MEDIUM:
				 avatarWidth = 150;
				 avatarHeight = 150;
				 break;
			
			case DisplayMetrics.DENSITY_HIGH:
				 avatarWidth = 225;
				 avatarHeight = 225;
				 break;
			
			case DisplayMetrics.DENSITY_XHIGH:
				 avatarWidth = 300;
				 avatarHeight = 300;
				 break;
		}
		
		accountUserAvatar.getLayoutParams().height = avatarWidth;
		accountUserAvatar.getLayoutParams().width = avatarHeight;
		accountUserAvatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
		accountUserAvatar.setVisibility(View.VISIBLE);
		
		accountUserAvatar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Account.this, UserImageGallery.class);
				intent.putExtra("from", "account");
				intent.putExtra("images", result[1]);
				startActivity(intent);
			}
		});
		
		accountPersonalInfoRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("personal", result[0]);
			}
		});
		
		accountAboutMeRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("about", result[0]);
			}
		});
		
		accountNutritionalInfoRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("nutritional", result[0]);
			}
		});
		
		accountFlavorsRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("flavors", result[0]);
			}
		});
		
		accountPackagesRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("packages", result[0]);
			}
		});
		
		accountBonusPackRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("bonuspack", result[0]);
			}
		});
		
		accountAccessoriesRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("accessories", result[0]);
			}
		});
		
		accountSideEffectsRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("effects", result[0]);
			}
		});
		
		accountInfoRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("info", result[0]);
			}
		});
	}
	
	public void editUserData(String type, final String data) {
		
		if(type.equalsIgnoreCase("personal")) {
			
			setContentView(R.layout.activity_account_personal);
						
			personalFormNameText = (EditText) findViewById(R.id.personalFormNameText);
			personalFormWeightText = (EditText) findViewById(R.id.personalFormWeightText);
			personalFormHeightText = (EditText) findViewById(R.id.personalFormHeightText);
			personalFormLocationText = (AutoCompleteTextView) findViewById(R.id.personalFormLocationText);
			personalLocationId = (TextView) findViewById(R.id.personalFormLocationId);
			personalFormBirthdayBtn = (Button) findViewById(R.id.personalFormBirthdayBtn);
			accountSaveBtn = (Button) findViewById(R.id.personalFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.personalFormBackBtn);
			
			dbAdapter = new AutoCompleteDbAdapter(this);
			locationAdapter = new LocationAdapter(dbAdapter, this, "account");
			personalFormLocationText.setAdapter(locationAdapter);
			personalFormLocationText.setOnItemClickListener(locationAdapter);
			 
			try {
				JSONObject resultObject = new JSONObject(data);
				if(!resultObject.isNull("name")) {
					personalFormNameText.setText(resultObject.getString("name"));
				}
				if(!resultObject.isNull("weight")) {
					personalFormWeightText.setText(Integer.toString(resultObject.getInt("weight")));
				}
				if(!resultObject.isNull("height")) {
					personalFormHeightText.setText(resultObject.getString("height"));
				}
				if(!resultObject.isNull("birthday")) {
					localBday = resultObject.getString("birthday").split("-");
					personalFormBirthdayBtn.setText(localBday[2] + "/" + localBday[1] + "/" + localBday[0]);
				}
				if(!resultObject.isNull("city")) {
					personalFormLocationText.setText(resultObject.getString("city"));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(!localBday[2].equals("")){
				day = Integer.parseInt(localBday[2]);
			} else {
				day = c.get(Calendar.DAY_OF_MONTH);
			}
			if(!localBday[1].equals("")){
				month = Integer.parseInt(localBday[1]) - 1;
			} else {
				month = c.get(Calendar.MONTH);
			}
			if(!localBday[0].equals("")){
				year = Integer.parseInt(localBday[0]);
			} else {
				year = c.get(Calendar.YEAR);
			}
			
			personalFormBirthdayBtn.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("deprecation")
				public void onClick(View v) {
					showDialog(DATE_DIALOG_ID);
				}
			});
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!ct.isConnectingToInternet()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
			        	UpdateAccount account = new UpdateAccount("personal");
						account.execute();
			        }
					
				}
			});
			
		}
		
		if(type.equalsIgnoreCase("about")) {
			setContentView(R.layout.activity_account_about);
			
			aboutFormDescriptionText = (EditText) findViewById(R.id.aboutDescriptionText);
			aboutFormCrazyText = (EditText) findViewById(R.id.aboutCrazyText);
			
			accountSaveBtn = (Button) findViewById(R.id.aboutFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.aboutFormBackBtn);
			
			try {
				JSONObject resultObject = new JSONObject(data);
				if(!resultObject.isNull("short_description")) {
					aboutFormDescriptionText.setText(resultObject.getString("short_description"));
				}
				if(!resultObject.isNull("crazy_experience")) {
					aboutFormCrazyText.setText(resultObject.getString("crazy_experience"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!ct.isConnectingToInternet()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
			        	UpdateAccount account = new UpdateAccount("about");
						account.execute();
			        }
				}
			});

		}
		
		if(type.equalsIgnoreCase("nutritional")) {
			setContentView(R.layout.activity_account_nutritional);
			
			nutritionalFormLanguagesText = (EditText) findViewById(R.id.nutritionalLanguagesText);
			nutritionalFormFavoriteAlbumText = (EditText) findViewById(R.id.nutritionalFavoriteAlbumText);
			nutritionalFormFavoriteBookText = (EditText) findViewById(R.id.nutritionalFavoriteBookText);
			nutritionalFormFavoriteArtistText = (EditText) findViewById(R.id.nutritionalFavoriteArtistText);
			nutritionalFormFavoriteMovieText = (EditText) findViewById(R.id.nutritionalFavoriteMovieText);
			nutritionalFormJokeText = (EditText) findViewById(R.id.nutritionalJokeText);
			nutritionalFormDrinkText = (EditText) findViewById(R.id.nutritionalDrinkText);
			nutritionalFormFavoriteCountryText = (EditText) findViewById(R.id.nutritionalFavoriteCountryText);
			nutritionalFormQuoteText = (EditText) findViewById(R.id.nutritionalQuoteText);
			nutritionalFormHobbyText = (EditText) findViewById(R.id.nutritionalHobbyText);
			nutritionalFormFavoriteDateText = (EditText) findViewById(R.id.nutritionalFavoriteDateText);
			nutritionalFormSportsText = (EditText) findViewById(R.id.nutritionalSportsText);
			nutritionalFormFavoriteFoodText = (EditText) findViewById(R.id.nutritionalFavoriteFoodText);
			nutritionalFormReligionSpinner = (Spinner) findViewById(R.id.nutritionalReligionSpinner);
			nutritionalFormOccupationSpinner = (Spinner) findViewById(R.id.nutritionalOccupationSpinner);
			nutritionalFormPoliticsSpinner = (Spinner) findViewById(R.id.nutritionalPoliticsSpinner);
			nutritionalFormZodiacSignSpinner = (Spinner) findViewById(R.id.nutritionalZodiacSignSpinner);
			
			ArrayAdapter<CharSequence> adapterReligion = ArrayAdapter.createFromResource(this,
			        R.array.religions, android.R.layout.simple_spinner_item);
			adapterReligion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			nutritionalFormReligionSpinner.setAdapter(adapterReligion);
			
			ArrayAdapter<CharSequence> adapterOccupation = ArrayAdapter.createFromResource(this,
			        R.array.occupation, android.R.layout.simple_spinner_item);
			adapterOccupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			nutritionalFormOccupationSpinner.setAdapter(adapterOccupation);
			
			ArrayAdapter<CharSequence> adapterPolitics = ArrayAdapter.createFromResource(this,
			        R.array.politics, android.R.layout.simple_spinner_item);
			adapterPolitics.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			nutritionalFormPoliticsSpinner.setAdapter(adapterPolitics);
			
			ArrayAdapter<CharSequence> adapterZodiacSign = ArrayAdapter.createFromResource(this,
			        R.array.zodiac, android.R.layout.simple_spinner_item);
			adapterZodiacSign.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			nutritionalFormZodiacSignSpinner.setAdapter(adapterZodiacSign);
			
			
			try {
				JSONObject resultObject = new JSONObject(data);
				nutritionalFormReligionSpinner.setSelection(adapterReligion.getPosition(resultObject.getString("religion")));
				nutritionalFormOccupationSpinner.setSelection(adapterOccupation.getPosition(resultObject.getString("occupation")));
				nutritionalFormPoliticsSpinner.setSelection(adapterPolitics.getPosition(resultObject.getString("political_belief")));
				nutritionalFormZodiacSignSpinner.setSelection(adapterZodiacSign.getPosition(resultObject.getString("zodiac_sign")));
				if(!resultObject.isNull("languages")) {
					nutritionalFormLanguagesText.setText(resultObject.getString("languages"));
				}
				if(!resultObject.isNull("favorite_album")) {
					nutritionalFormFavoriteAlbumText.setText(resultObject.getString("favorite_album"));
				}
				if(!resultObject.isNull("favorite_book")) {
					nutritionalFormFavoriteBookText.setText(resultObject.getString("favorite_book"));
				}
				if(!resultObject.isNull("favorite_artist")) {
					nutritionalFormFavoriteArtistText.setText(resultObject.getString("favorite_artist"));
				}
				if(!resultObject.isNull("favorite_movie")) {
					nutritionalFormFavoriteMovieText.setText(resultObject.getString("favorite_movie"));
				}
				if(!resultObject.isNull("a_joke")) {
					nutritionalFormJokeText.setText(resultObject.getString("a_joke"));
				}
				if(!resultObject.isNull("a_drink")) {
					nutritionalFormDrinkText.setText(resultObject.getString("a_drink"));
				}
				if(!resultObject.isNull("favorite_country")) {
					nutritionalFormFavoriteCountryText.setText(resultObject.getString("favorite_country"));
				}
				if(!resultObject.isNull("a_quote")) {
					nutritionalFormQuoteText.setText(resultObject.getString("a_quote"));
				}
				if(!resultObject.isNull("a_hobby")) {
					nutritionalFormHobbyText.setText(resultObject.getString("a_hobby"));
				}
				if(!resultObject.isNull("favorite_date")) {
					nutritionalFormFavoriteDateText.setText(resultObject.getString("favorite_date"));
				}
				if(!resultObject.isNull("sport")) {
					nutritionalFormSportsText.setText(resultObject.getString("sport"));
				}
				if(!resultObject.isNull("favorite_food")) {
					nutritionalFormFavoriteFoodText.setText(resultObject.getString("favorite_food"));
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.nutritionalFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.nutritionalFormBackBtn);
			
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!ct.isConnectingToInternet()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
			        	UpdateAccount account = new UpdateAccount("nutritional");
						account.execute();
			        }
				}
			});
			
		}
		
		if(type.equalsIgnoreCase("flavors")) {
			setContentView(R.layout.activity_account_flavors);
			
			for(int i = 0; i < flavors.length ; i++) {
				flavors[i] = (CheckBox) findViewById(flavorsIds[i]);
			}
			
			try {
				JSONObject resultObject = new JSONObject(data);
				JSONArray flavorData = resultObject.getJSONArray("flavor_ids");
				for(int i = 0; i < flavorData.length(); i++) {
					int id = flavorData.getInt(i);
					flavors[id-1].setChecked(true);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.flavorsFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.flavorsFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					UpdateAccount account = new UpdateAccount("flavors");
					account.execute();
				}
			});
		}
		
		if(type.equalsIgnoreCase("packages")) {
			setContentView(R.layout.activity_account_packages);
			
			
			for(int i = 0; i < packages.length ; i++) {
				packages[i] = (CheckBox) findViewById(packagesIds[i]);
			}
			
			try {
				JSONObject resultObject = new JSONObject(data);
				JSONArray packageData = resultObject.getJSONArray("packaging_ids");
				for(int i = 0; i < packageData.length(); i++) {
					int id = packageData.getInt(i);
					packages[id-1].setChecked(true);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.packagesFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.packagesFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!ct.isConnectingToInternet()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
			        	UpdateAccount account = new UpdateAccount("packages");
						account.execute();
			        }
				}
			});
			
		}
		
		if(type.equalsIgnoreCase("bonuspack")) {
			setContentView(R.layout.activity_account_bonuspack);
			
			for(int i = 0; i < bonusPack.length ; i++) {
				bonusPack[i] = (CheckBox) findViewById(bonusPackIds[i]);
			}
			
			try {
				JSONObject resultObject = new JSONObject(data);
				JSONArray bonusPackData = resultObject.getJSONArray("bonus_pack_ids");
				for(int i = 0; i < bonusPackData.length(); i++) {
					int id = bonusPackData.getInt(i);
					bonusPack[id-1].setChecked(true);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.bonusPackFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.bonusPackFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!ct.isConnectingToInternet()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
			        	UpdateAccount account = new UpdateAccount("bonuspack");
						account.execute();
			        }
				}
			});
			
		}
		
		
		if(type.equalsIgnoreCase("accessories")) {
			setContentView(R.layout.activity_account_accessories);
			
			accessoriesMaleChildrenText = (EditText) findViewById(R.id.accesoriesMaleChildrenText);
			accessoriesFemaleChildrenText = (EditText) findViewById(R.id.accesoriesFemaleChildrenText);
			accessoriesLivesWithSpinner = (Spinner) findViewById(R.id.accessoriesLivesWithSpinner);
			accessoriesSalarySpinner = (Spinner) findViewById(R.id.accessoriesSalarySpinner);
			accessoriesHomeTypeSpinner = (Spinner) findViewById(R.id.accessoriesHouseTypeSpinner);
			accessoriesBeachHouseSpinner = (Spinner) findViewById(R.id.accessoriesBeachHouseType);
			accessoriesPoolSpinner = (Spinner) findViewById(R.id.accessoriesPoolSpinner);
			accessoriesJacuzziSpinner = (Spinner) findViewById(R.id.accessoriesJacuzziSpinner);
			accessoriesMotorcycleSpinner = (Spinner) findViewById(R.id.accessoriesMotorcycleSpinner);
			accessoriesBikeSpinner = (Spinner) findViewById(R.id.accessoriesBikeSpinner);
			accessoriesCarSpinner = (Spinner) findViewById(R.id.accessoriesCarSpinner);
			accessoriesPetSpinner = (Spinner) findViewById(R.id.accessoriesPetSpinner);
			
			ArrayAdapter<CharSequence> adapterLivesWith = ArrayAdapter.createFromResource(this,
			        R.array.liveswith, android.R.layout.simple_spinner_item);
			adapterLivesWith.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesLivesWithSpinner.setAdapter(adapterLivesWith);
			
			ArrayAdapter<CharSequence> adapterSalary = ArrayAdapter.createFromResource(this,
			        R.array.salary, android.R.layout.simple_spinner_item);
			adapterSalary.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesSalarySpinner.setAdapter(adapterSalary);
			
			ArrayAdapter<CharSequence> adapterHomeType = ArrayAdapter.createFromResource(this,
			        R.array.housetype, android.R.layout.simple_spinner_item);
			adapterHomeType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesHomeTypeSpinner.setAdapter(adapterHomeType);
			
			ArrayAdapter<CharSequence> adapterBeachHouse = ArrayAdapter.createFromResource(this,
			        R.array.beachhouse, android.R.layout.simple_spinner_item);
			adapterBeachHouse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesBeachHouseSpinner.setAdapter(adapterBeachHouse);
			
			ArrayAdapter<CharSequence> adapterPool = ArrayAdapter.createFromResource(this,
			        R.array.pool, android.R.layout.simple_spinner_item);
			adapterPool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesPoolSpinner.setAdapter(adapterPool);
			
			ArrayAdapter<CharSequence> adapterJacuzzi = ArrayAdapter.createFromResource(this,
			        R.array.standardyesno, android.R.layout.simple_spinner_item);
			adapterJacuzzi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesJacuzziSpinner.setAdapter(adapterJacuzzi);
			
			ArrayAdapter<CharSequence> adapterMotorcycle = ArrayAdapter.createFromResource(this,
			        R.array.standardyesno, android.R.layout.simple_spinner_item);
			adapterMotorcycle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesMotorcycleSpinner.setAdapter(adapterMotorcycle);
			
			ArrayAdapter<CharSequence> adapterBike = ArrayAdapter.createFromResource(this,
			        R.array.standardyesno, android.R.layout.simple_spinner_item);
			adapterBike.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesBikeSpinner.setAdapter(adapterBike);
			
			ArrayAdapter<CharSequence> adapterCar = ArrayAdapter.createFromResource(this,
			        R.array.standardyesno, android.R.layout.simple_spinner_item);
			adapterCar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesCarSpinner.setAdapter(adapterCar);
			
			ArrayAdapter<CharSequence> adapterPet = ArrayAdapter.createFromResource(this,
			        R.array.pet, android.R.layout.simple_spinner_item);
			adapterPet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			accessoriesPetSpinner.setAdapter(adapterPet);
						
			accountSaveBtn = (Button) findViewById(R.id.accessoriesFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.accessoriesFormBackBtn);
			
			try {
				JSONObject resultObject = new JSONObject(data);
				if(!resultObject.isNull("male_childs")) {
					accessoriesMaleChildrenText.setText(resultObject.getString("male_childs"));
				}
				if(!resultObject.isNull("female_childs")) {
					accessoriesFemaleChildrenText.setText(resultObject.getString("female_childs"));
				}
				accessoriesLivesWithSpinner.setSelection(adapterLivesWith.getPosition(resultObject.getString("live_with")));
				accessoriesSalarySpinner.setSelection(adapterSalary.getPosition(resultObject.getString("salary")));
				accessoriesHomeTypeSpinner.setSelection(adapterHomeType.getPosition(resultObject.getString("home_type")));
				accessoriesBeachHouseSpinner.setSelection(adapterBeachHouse.getPosition(resultObject.getString("has_beach_house")));
				accessoriesPoolSpinner.setSelection(adapterPool.getPosition(resultObject.getString("has_pool")));
				accessoriesJacuzziSpinner.setSelection(adapterJacuzzi.getPosition(resultObject.getString("has_jacuzzi")));
				accessoriesMotorcycleSpinner.setSelection(adapterMotorcycle.getPosition(resultObject.getString("has_motorcycle")));
				accessoriesBikeSpinner.setSelection(adapterBike.getPosition(resultObject.getString("has_bike")));
				accessoriesCarSpinner.setSelection(adapterCar.getPosition(resultObject.getString("has_car")));
				accessoriesPetSpinner.setSelection(adapterPet.getPosition(resultObject.getString("has_pet")));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!ct.isConnectingToInternet()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
			        	UpdateAccount account = new UpdateAccount("accessories");
						account.execute();
			        }
				}
			});

		}

		if(type.equalsIgnoreCase("effects")) {
			setContentView(R.layout.activity_account_effect);
			
			effectsDefectsText = (EditText) findViewById(R.id.effectsDefectText);
			effectsHatesText = (EditText) findViewById(R.id.effectsHatesText);
			effectsSexPosesText = (EditText) findViewById(R.id.effectsSexPosesText);
			effectsSexToysText = (EditText) findViewById(R.id.effectsSexToysText);
			effectsGuiltyPleasureText = (EditText) findViewById(R.id.effectsGuiltyPleasureText);
			effectsSexualFantasyText = (EditText) findViewById(R.id.effectsSexualFantasyText);
			effectsFetishesText = (EditText) findViewById(R.id.effectsFetishesText);
			effectsDrinksSpinner = (Spinner) findViewById(R.id.effectsDrinksSpinner);
			effectsSmokesSpinner = (Spinner) findViewById(R.id.effectsSmokesSpinner);
			effectsFavoriteDrugSpinner = (Spinner) findViewById(R.id.effectsFavoriteDrugSpinner);
			
			ArrayAdapter<CharSequence> adapterDrinks = ArrayAdapter.createFromResource(this,
			        R.array.drinks, android.R.layout.simple_spinner_item);
			adapterDrinks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			effectsDrinksSpinner.setAdapter(adapterDrinks);
			
			ArrayAdapter<CharSequence> adapterSmokes = ArrayAdapter.createFromResource(this,
			        R.array.smokes, android.R.layout.simple_spinner_item);
			adapterSmokes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			effectsSmokesSpinner.setAdapter(adapterSmokes);
			
			ArrayAdapter<CharSequence> adapterDrugs = ArrayAdapter.createFromResource(this,
			        R.array.drugs, android.R.layout.simple_spinner_item);
			adapterDrugs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			effectsFavoriteDrugSpinner.setAdapter(adapterDrugs);
			
			try {
				JSONObject resultObject = new JSONObject(data);
				if(!resultObject.isNull("defect")) {
					effectsDefectsText.setText(resultObject.getString("defect"));
				}
				if(!resultObject.isNull("hates")) {
					effectsHatesText.setText(resultObject.getString("hates"));
				}
				if(!resultObject.isNull("sex_poses")) {
					effectsSexPosesText.setText(resultObject.getString("sex_poses"));
				}
				if(!resultObject.isNull("sex_toys")) {
					effectsSexToysText.setText(resultObject.getString("sex_toys"));
				}
				if(!resultObject.isNull("guilty_pleasure")) {
					effectsGuiltyPleasureText.setText(resultObject.getString("guilty_pleasure"));
				}
				if(!resultObject.isNull("sexual_fantasy")) {
					effectsSexualFantasyText.setText(resultObject.getString("sexual_fantasy"));
				}
				if(!resultObject.isNull("fetishes")) {
					effectsFetishesText.setText(resultObject.getString("fetishes"));
				}
				effectsDrinksSpinner.setSelection(adapterDrinks.getPosition(resultObject.getString("drinks")));
				effectsSmokesSpinner.setSelection(adapterSmokes.getPosition(resultObject.getString("smokes")));
				effectsFavoriteDrugSpinner.setSelection(adapterDrugs.getPosition(resultObject.getString("favourite_drug")));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.effectsFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.effectsFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!ct.isConnectingToInternet()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
			        	UpdateAccount account = new UpdateAccount("effects");
						account.execute();
			        }
				}
			});
			
		}
		if(type.equalsIgnoreCase("info")) {
			setContentView(R.layout.activity_account_information);
			
			accountInfoUsernameText = (EditText) findViewById(R.id.accountInfoUsernameText);
			accountInfoEmailText = (EditText) findViewById(R.id.accountInfoEmailText);
			accountInfoPasswordText = (EditText) findViewById(R.id.accountInfoPasswordText);
			accountInfoConfirmPasswordText = (EditText) findViewById(R.id.accountInfoConfirmPasswordText);
			
			try {
				JSONObject resultObject = new JSONObject(data);
				if(!resultObject.isNull("username")) {
					accountInfoUsernameText.setText(resultObject.getString("username"));
				}
				if(!resultObject.isNull("email")) {
					accountInfoEmailText.setText(resultObject.getString("email"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.accountInfoFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.accountInfoFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!ct.isConnectingToInternet()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
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
			        	UpdateAccount account = new UpdateAccount("info");
						account.execute();
			        }
				}
			});
			
		}
		
		accountBackBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				initView();
			}
		});
		
	}
	
	public static void setId(int id) {
		personalLocationId.setText(Integer.toString(id));
	}
	

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         year, month,day);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			personalFormBirthdayBtn.setText(Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year));
		}
	};
	
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
	                	Intent intent = new Intent(Account.this, Login.class);
	                	startActivity(intent);
	                	Account.this.finish();
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
        // hide menu if it shown
        if (sideNavigationView.isShown()) {
            sideNavigationView.hideMenu();
        }
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	accountUserAvatar = (ImageView) findViewById(R.id.accountAvatarImage);
    	accountUserAvatar.setVisibility(View.INVISIBLE);
    	initView();
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
        	action = false;
        	Account.this.finish();
        	startActivity(getIntent());
        	
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
	
	private class UserData extends AsyncTask<Integer, Integer, String[]> {
		
		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private Account activityRef;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private String[] responses = new String[2];
		
		public UserData(Account activityRef) {
			this.activityRef = activityRef;
		}
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
			
			dialog = ProgressDialog.show(Account.this, "", "Cargando información personal...", true);
			
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
		}
		
		@Override
		protected String[] doInBackground(Integer... params) {
		    
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(SERVICE_BASE_URL + "profile.json?app_key="+ api_key + "&signature=" + signature);
            get.setHeader("content-type", "application/json");
            
            try {
            	HttpResponse resp = client.execute(get);
				responses[0] = EntityUtils.toString(resp.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
			HttpGet getImages = new HttpGet(SERVICE_BASE_URL + "photos.json?app_key="
									+ api_key + "&signature=" + signature);
            getImages.setHeader("content-type", "application/json");
            
            try {
            	HttpResponse resp = client.execute(getImages);
				responses[1] = EntityUtils.toString(resp.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
 
			return responses;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			
			if(result == null) {
				alert.showAlertDialog(Account.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
			} else {
				activityRef.loadProfile(result);
				dialog.dismiss();

			}
			
		}
		
	}
	
	private class UpdateAccount extends AsyncTask<Integer, Integer, String> {
		
		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private String reference;
		private String[] birthdayText;
		JSONObject user = new JSONObject();
		JSONObject usuario = new JSONObject();
		
		public UpdateAccount(String ref) {
			reference = ref;
		}
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
			
			dialog = ProgressDialog.show(Account.this, "", "Actualizando datos...", true);
						
			if(reference.equalsIgnoreCase("personal")) {
								
				birthdayText = personalFormBirthdayBtn.getText().toString().split("/");
				
				try {
					user.put("name", personalFormNameText.getText().toString());
					user.put("weight", personalFormWeightText.getText().toString());
					user.put("height", personalFormHeightText.getText().toString());
					user.put("birthday(3i)", birthdayText[0]);
					user.put("birthday(2i)", birthdayText[1]);
					user.put("birthday(1i)", birthdayText[2]);
					//user.put("city", personalLocationId.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			} else if(reference.equalsIgnoreCase("about")) {
				
				try {
					user.put("short_description", aboutFormDescriptionText.getText().toString());
					user.put("crazy_experience", aboutFormCrazyText.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			} else if(reference.equalsIgnoreCase("nutritional")) {
				
				try {
					user.put("languages", nutritionalFormLanguagesText.getText().toString());
					user.put("favorite_album", nutritionalFormFavoriteAlbumText.getText().toString());
					user.put("favorite_book", nutritionalFormFavoriteBookText.getText().toString());
					user.put("favorite_artist", nutritionalFormFavoriteArtistText.getText().toString());
					user.put("favorite_movie", nutritionalFormFavoriteMovieText.getText().toString());
					user.put("a_joke", nutritionalFormJokeText.getText().toString());
					user.put("a_drink", nutritionalFormDrinkText.getText().toString());
					user.put("favorite_country", nutritionalFormFavoriteCountryText.getText().toString());
					user.put("a_quote", nutritionalFormQuoteText.getText().toString());
					user.put("a_hobby", nutritionalFormHobbyText.getText().toString());
					user.put("favorite_date", nutritionalFormFavoriteDateText.getText().toString());
					user.put("sport", nutritionalFormSportsText.getText().toString());
					user.put("favorite_food", nutritionalFormFavoriteFoodText.getText().toString());
					user.put("religion", nutritionalFormReligionSpinner.getSelectedItem().toString());
					user.put("occupation", nutritionalFormOccupationSpinner.getSelectedItem().toString());
					user.put("political_belief", nutritionalFormPoliticsSpinner.getSelectedItem().toString());
					user.put("zodiac_sign", nutritionalFormZodiacSignSpinner.getSelectedItem().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			} else if(reference.equalsIgnoreCase("flavors")) {
				String flavorsChecked = "";
				for(int i = 0; i < flavors.length ; i++) {
					if(flavors[i].isChecked()) {
						flavorsChecked += Integer.toString(i+1);
						if(i < flavors.length - 1) {
							flavorsChecked += ",";
						}
					}
				}
				try {
					user.put("flavor_ids", flavorsChecked);
				} catch(JSONException e) {
					e.printStackTrace();
				}
			} else if(reference.equalsIgnoreCase("packages")) {
				String packagesChecked = "";
				for(int i = 0; i < packages.length ; i++) {
					if(packages[i].isChecked()) {
						packagesChecked += Integer.toString(i+1);
						if(i < packages.length - 1) {
							packagesChecked += ",";
						}
					}
				}
				try {
					user.put("packaging_ids", packagesChecked);
					Log.d("Envases", packagesChecked);
				} catch(JSONException e) {
					e.printStackTrace();
				}
			} else if(reference.equalsIgnoreCase("bonuspack")) {
				String bonusPackChecked = "";
				for(int i = 0; i < bonusPack.length; i++) {
					if(bonusPack[i].isChecked()) {
						bonusPackChecked += Integer.toString(i+1);
						if(i < bonusPack.length - 1) {
							bonusPackChecked += ",";
						}
					}
				}
				try {
					user.put("bonus_pack_ids", bonusPackChecked);
				} catch(JSONException e) {
					e.printStackTrace();
				}
			} else if(reference.equalsIgnoreCase("accessories")) {
				try {
					user.put("male_childs", accessoriesMaleChildrenText.getText().toString());
					user.put("female_childs", accessoriesFemaleChildrenText.getText().toString());
					user.put("live_with", accessoriesLivesWithSpinner.getSelectedItem().toString());
					user.put("salary", accessoriesSalarySpinner.getSelectedItem().toString());
					user.put("home_type", accessoriesHomeTypeSpinner.getSelectedItem().toString());
					user.put("has_beach_house", accessoriesBeachHouseSpinner.getSelectedItem().toString());
					user.put("has_pool", accessoriesPoolSpinner.getSelectedItem().toString());
					user.put("has_jacuzzi", accessoriesJacuzziSpinner.getSelectedItem().toString());
					user.put("has_motorcycle", accessoriesMotorcycleSpinner.getSelectedItem().toString());
					user.put("has_bike", accessoriesBikeSpinner.getSelectedItem().toString());
					user.put("has_car", accessoriesCarSpinner.getSelectedItem().toString());
					user.put("has_pet", accessoriesPetSpinner.getSelectedItem().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			} else if(reference.equalsIgnoreCase("effects")) {
				try {
					user.put("defect", effectsDefectsText.getText().toString());
					user.put("hates", effectsHatesText.getText().toString());
					user.put("sex_poses", effectsSexPosesText.getText().toString());
					user.put("sex_toys", effectsSexToysText.getText().toString());
					user.put("guilty_pleasure", effectsGuiltyPleasureText.getText().toString());
					user.put("sexual_fantasy", effectsSexualFantasyText.getText().toString());
					user.put("fetishes", effectsFetishesText.getText().toString());
					user.put("drinks", effectsDrinksSpinner.getSelectedItem().toString());
					user.put("smokes", effectsSmokesSpinner.getSelectedItem().toString());
					user.put("favourite_drug", effectsFavoriteDrugSpinner.getSelectedItem().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if(reference.equalsIgnoreCase("account")) {
				try {
					user.put("username", accountInfoUsernameText.getText().toString());
					user.put("email", accountInfoEmailText.getText().toString());
					if(!accountInfoPasswordText.getText().toString().equals("")) {
						user.put("password", accountInfoPasswordText.getText().toString());
						user.put("password_confirmation", accountInfoConfirmPasswordText.getText().toString());
					}
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			
			try {
				usuario.put("user", user);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
		}
		
		@Override
		protected String doInBackground(Integer... params) {
		    
			HttpClient client = new DefaultHttpClient();
			HttpPut put = new HttpPut(SERVICE_BASE_URL + "profile.json?app_key="
									+ api_key + "&signature=" + signature);
            put.setHeader("content-type", "application/json");
            
            try {
            	StringEntity entity = new StringEntity(usuario.toString(), HTTP.UTF_8);
            	put.setEntity(entity);
            	HttpResponse resp = client.execute(put);
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
				alert.showAlertDialog(Account.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				dialog.dismiss();
			}
			
		}
		
	}
	
	
}
