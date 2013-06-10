package com.supermanket.supermanket;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.UtilityBelt;

public class Account extends Activity {
	
	AutoCompleteTextView personalFormCountryText;
	AutoCompleteTextView personalFormRegionText;
	AutoCompleteTextView personalFormCountyText;
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
	
	ImageLoader imageLoader = ImageLoader.getInstance();
	
	private int day;
	private int month;
	private int year;
	final Calendar c = Calendar.getInstance();
	static final int DATE_DIALOG_ID = 999;
	String[] localBday = new String[3];
	ProgressDialog pDialog;
	
	UserData userData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
		userData = new UserData(this);
		userData.execute();
	}
	
	public void loadProfile(final String result) {
		setContentView(R.layout.activity_account);
		
		pDialog = ProgressDialog.show(this, "", "Cargando im�genes");
		
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
		
		JSONObject resultObj;
		String url = null;
		try {
			resultObj = new JSONObject(result);
			JSONArray photos = resultObj.getJSONArray("photos"); 
			if(!resultObj.getString("sex").equalsIgnoreCase("male")) {
				accountFlavorsRow.setVisibility(View.INVISIBLE);
				accountPackagesRow.setVisibility(View.INVISIBLE);
				accountBonusPackRow.setVisibility(View.INVISIBLE);
			}
			for(int i = 0; i < photos.length(); i++) {
				JSONObject picInfo = photos.getJSONObject(i);
				if(picInfo.getBoolean("avatar?")) {
					url = picInfo.getString("medium");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1,
					FailReason arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
	    });
		accountUserAvatar.getLayoutParams().height = 150;
		accountUserAvatar.getLayoutParams().width = 150;
		accountUserAvatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
		accountUserAvatar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Account.this, UserImageGallery.class);
				intent.putExtra("from", "account");
				intent.putExtra("images", result);
				startActivity(intent);
			}
		});
		
		accountPersonalInfoRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("personal", result);
			}
		});
		
		accountAboutMeRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("about", result);
			}
		});
		
		accountNutritionalInfoRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("nutritional", result);
			}
		});
		
		accountFlavorsRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("flavors", result);
			}
		});
		
		accountPackagesRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("packages", result);
			}
		});
		
		accountBonusPackRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("bonuspack", result);
			}
		});
		
		accountAccessoriesRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("accessories", result);
			}
		});
		
		accountSideEffectsRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("effects", result);
			}
		});
		
		accountInfoRow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editUserData("info", result);
			}
		});
	}
	
	public void editUserData(String type, final String data) {
		
		if(type.equalsIgnoreCase("personal")) {
			
			setContentView(R.layout.activity_account_personal);
						
			personalFormNameText = (EditText) findViewById(R.id.personalFormNameText);
			personalFormWeightText = (EditText) findViewById(R.id.personalFormWeightText);
			personalFormHeightText = (EditText) findViewById(R.id.personalFormHeightText);
			personalFormCountryText = (AutoCompleteTextView) findViewById(R.id.personalFormCountryText);
			personalFormRegionText = (AutoCompleteTextView) findViewById(R.id.personalFormRegionText);
			personalFormCountyText = (AutoCompleteTextView) findViewById(R.id.personalFormCountyText);
			personalFormBirthdayBtn = (Button) findViewById(R.id.personalFormBirthdayBtn);
			accountSaveBtn = (Button) findViewById(R.id.personalFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.personalFormBackBtn);
			
			String[] COUNTRIES = getResources().getStringArray(R.array.countries);
			String[] STATES = getResources().getStringArray(R.array.states);
			String[] COUNTIES = getResources().getStringArray(R.array.counties);
			
			ArrayAdapter<String> adapterCountries = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, COUNTRIES);
			ArrayAdapter<String> adapterStates = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, STATES);
			ArrayAdapter<String> adapterCounties = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, COUNTIES);
			
			personalFormCountryText.setAdapter(adapterCountries);
			personalFormRegionText.setAdapter(adapterStates);
			personalFormCountyText.setAdapter(adapterCounties);
			 
			try {
				JSONObject resultObject = new JSONObject(data);
				personalFormNameText.setText(resultObject.getString("name"));
				personalFormWeightText.setText(Integer.toString(resultObject.getInt("weight")));
				personalFormHeightText.setText(resultObject.getString("height"));
				localBday = resultObject.getString("birthday").split("-");
				personalFormBirthdayBtn.setText(localBday[2] + "/" + localBday[1] + "/" + localBday[0]);
				personalFormCountryText.setText(resultObject.getString("country"));
				personalFormRegionText.setText(resultObject.getString("state"));
				personalFormCountyText.setText(resultObject.getString("county"));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
			
		}
		
		if(type.equalsIgnoreCase("about")) {
			setContentView(R.layout.activity_account_about);
			
			aboutFormDescriptionText = (EditText) findViewById(R.id.aboutDescriptionText);
			aboutFormCrazyText = (EditText) findViewById(R.id.aboutCrazyText);
			
			accountSaveBtn = (Button) findViewById(R.id.aboutFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.aboutFormBackBtn);
			
			try {
				JSONObject resultObject = new JSONObject(data);
				aboutFormDescriptionText.setText(resultObject.getString("short_description"));
				aboutFormCrazyText.setText(resultObject.getString("crazy_experience"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
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
				nutritionalFormLanguagesText.setText(resultObject.getString("languages"));
				nutritionalFormFavoriteAlbumText.setText(resultObject.getString("favorite_album"));
				nutritionalFormFavoriteBookText.setText(resultObject.getString("favorite_book"));
				nutritionalFormFavoriteArtistText.setText(resultObject.getString("favorite_artist"));
				nutritionalFormFavoriteMovieText.setText(resultObject.getString("favorite_movie"));
				nutritionalFormJokeText.setText(resultObject.getString("a_joke"));
				nutritionalFormDrinkText.setText(resultObject.getString("a_drink"));
				nutritionalFormFavoriteCountryText.setText(resultObject.getString("favorite_country"));
				nutritionalFormQuoteText.setText(resultObject.getString("a_quote"));
				nutritionalFormHobbyText.setText(resultObject.getString("a_hobby"));
				nutritionalFormFavoriteDateText.setText(resultObject.getString("favorite_date"));
				nutritionalFormSportsText.setText(resultObject.getString("sport"));
				nutritionalFormFavoriteFoodText.setText(resultObject.getString("favorite_food"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.nutritionalFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.nutritionalFormBackBtn);
			
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			

			accountSaveBtn = (Button) findViewById(R.id.flavorsFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.flavorsFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.packagesFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.packagesFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.bonusPackFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.bonusPackFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
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
				accessoriesMaleChildrenText.setText(resultObject.getString("male_childs"));
				accessoriesFemaleChildrenText.setText(resultObject.getString("female_childs"));
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
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
				effectsDefectsText.setText(resultObject.getString("defect"));
				effectsHatesText.setText(resultObject.getString("hates"));
				effectsSexPosesText.setText(resultObject.getString("sex_poses"));
				effectsSexToysText.setText(resultObject.getString("sex_toys"));
				effectsGuiltyPleasureText.setText(resultObject.getString("guilty_pleasure"));
				effectsSexualFantasyText.setText(resultObject.getString("sexual_fantasy"));
				effectsFetishesText.setText(resultObject.getString("fetishes"));
				effectsDrinksSpinner.setSelection(adapterDrinks.getPosition(resultObject.getString("drinks")));
				effectsSmokesSpinner.setSelection(adapterSmokes.getPosition(resultObject.getString("smokes")));
				effectsFavoriteDrugSpinner.setSelection(adapterDrugs.getPosition(resultObject.getString("favourite_drug")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.effectsFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.effectsFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
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
				accountInfoUsernameText.setText(resultObject.getString("username"));
				accountInfoEmailText.setText(resultObject.getString("email"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			accountSaveBtn = (Button) findViewById(R.id.accountInfoFormSaveBtn);
			accountBackBtn = (Button) findViewById(R.id.accountInfoFormBackBtn);
			
			accountSaveBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
				}
			});
			
		}
		
		accountBackBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userData = new UserData(Account.this);
				userData.execute();
			}
		});
		
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

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			
			personalFormBirthdayBtn.setText(Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year));

		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	private class UserData extends AsyncTask<Integer, Integer, String> {
		
		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private Account activityRef;
		private UtilityBelt utilityBelt = new UtilityBelt();
		
		public UserData(Account activityRef) {
			this.activityRef = activityRef;
		}
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
			
			dialog = ProgressDialog.show(Account.this, "", "Cargando informaci�n personal...", true);
			
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
		}
		
		@Override
		protected String doInBackground(Integer... params) {
		    
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://demosmartphone.supermanket.cl/apim/profile.json?app_key="
									+ api_key + "&signature=" + signature);
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
				alert.showAlertDialog(Account.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				activityRef.loadProfile(result);
				dialog.dismiss();

			}
			
		}
		
	}

}