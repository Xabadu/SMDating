package com.supermanket.supermanket;

import java.io.IOException;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemClickListener;
import com.jess.ui.TwoWayGridView;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.ImageLoader;
import com.supermanket.utilities.PictureAdapter;
import com.supermanket.utilities.UtilityBelt;

public class UserProfile extends Activity {

	private TwoWayGridView mImageGrid;
	private PictureAdapter imageAdapter;
	public UserInfo userInfo;
	public Integer[] currentUser = new Integer[1];
	private ImageLoader imageLoader;
	private JSONArray resultImages = null;
	private UtilityBelt utilityBelt = new UtilityBelt();
	String[] flavorsList = {"Intelectual", "Ejecutivo", "Carretero", "Deportista", "Aventurero", "Artista",
			"Tímido", "Geek", "Gamer", "Tuerca", "Tallero", "Hipster", "Fixero"};
	String[] packagesList = {"Rockero", "Metalero", "Casual", "Uniformado", "Skater", "Hip-Hop", "Otaku", 
			"Surfista", "Verde", "Gótico", "Motoquero", "Reggaetonero", "Zorrón", "Parrillero", "Lana", "Pokemón"};
	String[] bonusPackList = {"Chef", "Vegetariano", "Six Pack", "Fumador", "No fumador", "Romántico", "Músico", 
			"Musculoso", "Millonario y muriendo", "Cantante", "Maestro chasquilla"};
	
	// UI Elements
	Button userProfileOfferBtn;
	Button userProfileSendMessageBtn;
	Button userProfileUnblockInfoBtn;
	ImageView userProfileUserImage;
	TableRow flavorsRow;
	TableRow packagesRow;
	TableRow bonusPackRow;
	TextView userProfileNameText;
	TextView userProfileAgeText;
	TextView userProfileHeightText;
	TextView userProfileWeightText;
	TextView userProfileCountryText;
	TextView userProfileFlavorsText;
	TextView userProfilePackagesText;
	TextView userProfileBonusPackText;
	TextView userProfileAboutMeText;
	TextView userProfileCrazyText;
	TextView userProfileReligionText;
	TextView userProfileLanguagesText;
	TextView userProfileOccupationText;
	TextView userProfileFavoriteAlbumText;
	TextView userProfileFavoriteBookText;
	TextView userProfileFavoriteArtistText;
	TextView userProfileFavoriteMovieText;
	TextView userProfileAJokeText;
	TextView userProfileADrinkText;
	TextView userProfileAQuoteText;
	TextView userProfileFavoriteCountryText;
	TextView userProfileAHobbyText;
	TextView userProfileAFavoriteDateText;
	TextView userProfilePoliticsText;
	TextView userProfileSportText;
	TextView userProfileFavoriteFoodText;
	TextView userProfileZodiacSignText;
	TextView userProfileMaleChildsText;
	TextView userProfileFemaleChildsText;
	TextView userProfileLiveWithText;
	TextView userProfileSalaryText;
	TextView userProfileHomeTypeText;
	TextView userProfileHasBeachHouseText;
	TextView userProfileHasJacuzziText;
	TextView userProfileHasCarText;
	TextView userProfileHasPetText;
	TextView userProfileHasPoolText;
	TextView userProfileHasMotorcycleText;
	TextView userProfileHasBikeText;
	TextView userProfileDefectText;
	TextView userProfileHatesText;
	TextView userProfileDrinksText;
	TextView userProfileSmokesText;
	TextView userProfileFavoriteDrugText;
	TextView userProfileSexPosesText;
	TextView userProfileSexToysText;
	TextView userProfileGuiltyPleasureText;
	TextView userProfileSexualFantasyText;
	TextView userProfileFetishesText;
	
	private static SharedPreferences mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		int userId = intent.getIntExtra("id", 0);
		
		currentUser[0] = userId;
        userInfo = new UserInfo(this);
        userInfo.execute(currentUser);
		
	}


	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	private void profileLoad(String result) {
		
		setContentView(R.layout.activity_user_profile);
		
		userProfileOfferBtn = (Button) findViewById(R.id.userProfileOfferBtn);
		userProfileSendMessageBtn = (Button) findViewById(R.id.userProfileSendMessageBtn);
		userProfileUnblockInfoBtn = (Button) findViewById(R.id.userProfileUnblockBtn);
		userProfileUserImage = (ImageView) findViewById(R.id.userProfileUserImage);
		userProfileNameText = (TextView) findViewById(R.id.userProfileNameText);
		userProfileAgeText = (TextView) findViewById(R.id.userProfileAgeText);
		userProfileHeightText = (TextView) findViewById(R.id.userProfileHeightText);
		userProfileWeightText = (TextView) findViewById(R.id.userProfileWeightText);
		userProfileCountryText = (TextView) findViewById(R.id.userProfileCountryText);
		userProfileAboutMeText = (TextView) findViewById(R.id.userProfileAboutMeText);
		userProfileCrazyText = (TextView) findViewById(R.id.userProfileCrazyText);
		userProfileReligionText = (TextView) findViewById(R.id.userProfileReligionText);
		userProfileLanguagesText = (TextView) findViewById(R.id.userProfileLanguageText);
		userProfileOccupationText = (TextView) findViewById(R.id.userProfileOccupationText);
		userProfileFavoriteAlbumText = (TextView) findViewById(R.id.userProfileFavoriteAlbumText);
		userProfileFavoriteBookText = (TextView) findViewById(R.id.userProfileFavoriteBookText);
		userProfileFavoriteArtistText = (TextView) findViewById(R.id.userProfileFavoriteArtistText);
		userProfileFavoriteMovieText = (TextView) findViewById(R.id.userProfileFavoriteMovieText);
		userProfileAJokeText = (TextView) findViewById(R.id.userProfileAJokeText);
		userProfileADrinkText = (TextView) findViewById(R.id.userProfileADrinkText);
		userProfileAQuoteText = (TextView) findViewById(R.id.userProfileAQuoteText);
		userProfileFavoriteCountryText = (TextView) findViewById(R.id.userProfileFavoriteCountryText);
		userProfileAHobbyText = (TextView) findViewById(R.id.userProfileAHobbyText);
		userProfileAFavoriteDateText = (TextView) findViewById(R.id.userProfileFavoriteDateText);
		userProfilePoliticsText = (TextView) findViewById(R.id.userProfilePoliticsText);
		userProfileSportText = (TextView) findViewById(R.id.userProfileSportText);
		userProfileFavoriteFoodText = (TextView) findViewById(R.id.userProfileFavoriteFoodText);
		userProfileZodiacSignText = (TextView) findViewById(R.id.userProfileZodiacSignText);
		userProfileMaleChildsText = (TextView) findViewById(R.id.userProfileMaleChildsText);
		userProfileFemaleChildsText = (TextView) findViewById(R.id.userProfileFemaleChildsText);
		userProfileLiveWithText = (TextView) findViewById(R.id.userProfileLiveWithText);
		userProfileSalaryText = (TextView) findViewById(R.id.userProfileSalaryText);
		userProfileHomeTypeText = (TextView) findViewById(R.id.userProfileHomeTypeText);
		userProfileHasBeachHouseText = (TextView) findViewById(R.id.userProfileHasBeachHouseText);
		userProfileHasJacuzziText = (TextView) findViewById(R.id.userProfileHasJacuzziText);
		userProfileHasCarText = (TextView) findViewById(R.id.userProfileHasCarText);
		userProfileHasPetText = (TextView) findViewById(R.id.userProfileHasPetText);
		userProfileHasPoolText = (TextView) findViewById(R.id.userProfileHasPoolText);
		userProfileHasMotorcycleText = (TextView) findViewById(R.id.userProfileHasMotorcycleText);
		userProfileHasBikeText = (TextView) findViewById(R.id.userProfileHasBikeText);
		userProfileDefectText = (TextView) findViewById(R.id.userProfileDefectsText);
		userProfileHatesText = (TextView) findViewById(R.id.userProfileHatesText);
		userProfileDrinksText = (TextView) findViewById(R.id.userProfileDrinksText);
		userProfileSmokesText = (TextView) findViewById(R.id.userProfileSmokesText);
		userProfileFavoriteDrugText = (TextView) findViewById(R.id.userProfileFavoriteDrugText);
		userProfileSexPosesText = (TextView) findViewById(R.id.userProfileSexPosesText);
		userProfileSexToysText = (TextView) findViewById(R.id.userProfileSexToysText);
		userProfileGuiltyPleasureText = (TextView) findViewById(R.id.userProfileGuiltyPleasureText);
		userProfileSexualFantasyText = (TextView) findViewById(R.id.userProfileSexualFantasyText);
		userProfileFetishesText = (TextView) findViewById(R.id.userProfileFetishesText);
		
		flavorsRow = (TableRow) findViewById(R.id.flavorsRow);
		packagesRow = (TableRow) findViewById(R.id.packagesRow);
		bonusPackRow = (TableRow) findViewById(R.id.bonusPackRow);
		
		Intent intent = getIntent();
		
		try {
			final JSONObject resultObject = new JSONObject(result);
			resultImages = resultObject.getJSONArray("photos");
			
			/* Block buttons */
			
			if(resultObject.getBoolean("private_information_access")) {
				userProfileUnblockInfoBtn.setVisibility(View.GONE);
			} else {
				userProfileUnblockInfoBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						UnblockInfo unblockInfo = new UnblockInfo(UserProfile.this);
						try {
							unblockInfo.execute(resultObject.getInt("id"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			if(resultObject.getInt("contact") == 0) {
				userProfileSendMessageBtn.setEnabled(false);
				userProfileSendMessageBtn.setAlpha(40);
				
				userProfileOfferBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
						builder.setTitle("Atención");
						if(mSharedPreferences.getString("USER_SEX", "").equalsIgnoreCase("male")) {
							builder.setMessage(R.string.alert_credit_discount_offer);
							builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				    			@Override
				    			public void onClick(DialogInterface dialog, int id) {
				    				Toast.makeText(UserProfile.this, "Oferta cancelada", Toast.LENGTH_SHORT).show();
				    			}
				    		});
							builder.setPositiveButton(R.string.btn_continue_text, new DialogInterface.OnClickListener() {
				    			@Override
				    			public void onClick(DialogInterface dialog, int id) {
				    				SendOffer sendOffer = new SendOffer();
									try {
										sendOffer.execute(resultObject.getInt("id"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
				    			}
				    		});
							AlertDialog alert = builder.create();
							alert.show();
							
						} else {
							builder.setMessage(R.string.alert_credit_discount_buy);
							builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				    			@Override
				    			public void onClick(DialogInterface dialog, int id) {
				    				Toast.makeText(UserProfile.this, "Compra cancelada", Toast.LENGTH_SHORT).show();
				    			}
				    		});
							builder.setPositiveButton(R.string.btn_continue_text, new DialogInterface.OnClickListener() {
				    			@Override
				    			public void onClick(DialogInterface dialog, int id) {
				    				BuyProduct buyProduct = new BuyProduct();
									try {
										buyProduct.execute(resultObject.getInt("id"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
				    			}
				    		});
							AlertDialog alert = builder.create();
							alert.show();
							
						}
					}
				});
			} else {
				userProfileSendMessageBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(UserProfile.this, MessageDetail.class);
						try {
							intent.putExtra("id", resultObject.getInt("contact"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						startActivity(intent);
					}
				});
			}
			
			/* Personal info */
			
			if(!resultObject.getString("name").equals("") && !resultObject.isNull("name")) {
				userProfileNameText.setText(resultObject.getString("name"));
			} else {
				userProfileNameText.setText(intent.getStringExtra("username"));
			}
			if(!resultObject.getString("birthday").equals("") && !resultObject.isNull("birthday")) {
				userProfileAgeText.setText(Integer.toString(utilityBelt.calculateAge(resultObject.getString("birthday"))) + " años");
			} else {
				userProfileAgeText.setText("-");
			}
			if(!resultObject.getString("height").equals("") && !resultObject.isNull("height")) {
				userProfileHeightText.setText(resultObject.getString("height") + "mt");
			} else {
				userProfileHeightText.setText("-");
			}
			if(!resultObject.getString("weight").equals("") && !resultObject.isNull("weight")) {
				userProfileWeightText.setText(resultObject.getString("weight") + "Kg");
			} else {
				userProfileWeightText.setText("-");
			}
			if(!resultObject.getString("country").equals("") && !resultObject.isNull("country")) {
				userProfileCountryText.setText(resultObject.getString("country"));
			} else {
				userProfileCountryText.setText("-");
			}
			
			/* About me */
			
			if(!resultObject.getString("sex").equalsIgnoreCase("male")) {
				flavorsRow.setVisibility(View.GONE);
				packagesRow.setVisibility(View.GONE);
				bonusPackRow.setVisibility(View.GONE);
			} else {
				userProfileOfferBtn.setText(R.string.btn_buy);
				userProfileFlavorsText = (TextView) findViewById(R.id.userProfileFlavorsText);
				userProfilePackagesText = (TextView) findViewById(R.id.userProfilePackagesText);
				userProfileBonusPackText = (TextView) findViewById(R.id.userProfileBonusPackText);
				
				JSONArray elements;
				if(!resultObject.isNull("flavor_ids")) {
					elements = resultObject.getJSONArray("flavor_ids");
					for(int i = 0; i < elements.length(); i++) {
						userProfileFlavorsText.setText(userProfileFlavorsText.getText().toString() 
								+ flavorsList[elements.getInt(i)] + "  ");
					}
				}
				
				if(!resultObject.isNull("packaging_ids")) {
					elements = resultObject.getJSONArray("packaging_ids");
					for(int i = 0; i < elements.length(); i++) {
						userProfilePackagesText.setText(userProfilePackagesText.getText().toString() 
								+ packagesList[elements.getInt(i)] + "  ");
					}
				}
				
				if(!resultObject.isNull("bonus_pack_ids")) {
					elements = resultObject.getJSONArray("bonus_pack_ids");
					for(int i = 0; i < elements.length(); i++) {
						userProfileBonusPackText.setText(userProfileBonusPackText.getText().toString()
								+ bonusPackList[elements.getInt(i)] + "  ");
					}
				}
				
			}
			
			userProfileOfferBtn.setVisibility(View.VISIBLE);
			userProfileSendMessageBtn.setVisibility(View.VISIBLE);
			
			if(!resultObject.getString("short_description").equals("") && !resultObject.isNull("short_description")) {
				userProfileAboutMeText.setText(resultObject.getString("short_description") + "\n");
			}
			if(!resultObject.getString("crazy_experience").equals("") && !resultObject.isNull("crazy_experience")) {
				userProfileCrazyText.setText(userProfileCrazyText.getText().toString() + " " + resultObject.getString("crazy_experience") + "\n");
			} else {
				userProfileCrazyText.setText(userProfileCrazyText.getText().toString() + " -\n\n");
			}
			
			Spannable str = new SpannableString(userProfileCrazyText.getText().toString());
			str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			userProfileCrazyText.setText(str);
			
			/* Info */
			
			if(!resultObject.getString("religion").equals("") && !resultObject.isNull("religion")) {
				userProfileReligionText.setText(resultObject.getString("religion"));
			} else {
				userProfileReligionText.setText("-");
			}
			if(!resultObject.getString("languages").equals("") && !resultObject.isNull("languages")) {
				userProfileLanguagesText.setText(resultObject.getString("languages"));
			} else {
				userProfileLanguagesText.setText("-");
			}
			if(!resultObject.getString("occupation").equals("") && !resultObject.isNull("occupation")) {
				userProfileOccupationText.setText(resultObject.getString("occupation"));
			} else {
				userProfileOccupationText.setText("-");
			}
			if(!resultObject.getString("favorite_album").equals("") && !resultObject.isNull("favorite_album")) {
				userProfileFavoriteAlbumText.setText(resultObject.getString("favorite_album"));
			} else {
				userProfileFavoriteAlbumText.setText("-");
			}
			if(!resultObject.getString("favorite_book").equals("") && !resultObject.isNull("favorite_book")) {
				userProfileFavoriteBookText.setText(resultObject.getString("favorite_book"));
			} else {
				userProfileFavoriteBookText.setText("-");
			}
			if(!resultObject.getString("favorite_artist").equals("") && !resultObject.isNull("favorite_artist")) {
				userProfileFavoriteArtistText.setText(resultObject.getString("favorite_artist"));
			} else {
				userProfileFavoriteArtistText.setText("-");
			}
			if(!resultObject.getString("favorite_movie").equals("") && !resultObject.isNull("favorite_movie")) {
				userProfileFavoriteMovieText.setText(resultObject.getString("favorite_movie"));
			} else {
				userProfileFavoriteMovieText.setText("-");
			}
			if(!resultObject.getString("a_joke").equals("") && !resultObject.isNull("a_joke")) {
				userProfileAJokeText.setText(resultObject.getString("a_joke"));
			} else {
				userProfileAJokeText.setText("-");
			}
			if(!resultObject.getString("a_drink").equals("") && !resultObject.isNull("a_drink")) {
				userProfileADrinkText.setText(resultObject.getString("a_drink"));
			} else {
				userProfileADrinkText.setText("-");
			}
			if(!resultObject.getString("a_quote").equals("") && !resultObject.isNull("a_quote")) {
				userProfileAQuoteText.setText(resultObject.getString("a_quote"));
			} else {
				userProfileAQuoteText.setText("-");
			}
			if(!resultObject.getString("favorite_country").equals("") && !resultObject.isNull("favorite_country")) {
				userProfileFavoriteCountryText.setText(resultObject.getString("favorite_country"));
			} else {
				userProfileFavoriteCountryText.setText("-");
			}
			if(!resultObject.getString("a_hobby").equals("") && !resultObject.isNull("a_hobby")) {
				userProfileAHobbyText.setText(resultObject.getString("a_hobby"));
			} else {
				userProfileAHobbyText.setText("-");
			}
			if(!resultObject.getString("favorite_date").equals("") && !resultObject.isNull("favorite_date")) {
				userProfileAFavoriteDateText.setText(resultObject.getString("favorite_date"));
			} else {
				userProfileAFavoriteDateText.setText("-");
			}
			if(!resultObject.getString("political_belief").equals("") && !resultObject.isNull("political_belief")) {
				userProfilePoliticsText.setText(resultObject.getString("political_belief"));
			} else {
				userProfilePoliticsText.setText("-");
			}
			if(!resultObject.getString("sport").equals("") && !resultObject.isNull("sport")) {
				userProfileSportText.setText(resultObject.getString("sport"));
			} else {
				userProfileSportText.setText("-");
			}
			if(!resultObject.getString("favorite_food").equals("") && !resultObject.isNull("favorite_food")) {
				userProfileFavoriteFoodText.setText(resultObject.getString("favorite_food"));
			} else {
				userProfileFavoriteFoodText.setText("-");
			}
			if(!resultObject.getString("zodiac_sign").equals("") && !resultObject.isNull("zodiac_sign")) {
				userProfileZodiacSignText.setText(resultObject.getString("zodiac_sign"));
			} else {
				userProfileZodiacSignText.setText("-");
			}
			
			/* Accessories */
			
			if(!resultObject.getString("male_childs").equals("") && !resultObject.isNull("male_childs")) {
				userProfileMaleChildsText.setText(resultObject.getString("male_childs"));
			} else {
				userProfileMaleChildsText.setText("-");
			}
			if(!resultObject.getString("female_childs").equals("") && !resultObject.isNull("female_childs")) {
				userProfileFemaleChildsText.setText(resultObject.getString("female_childs"));
			} else {
				userProfileFemaleChildsText.setText("-");
			}
			if(!resultObject.getString("live_with").equals("") && !resultObject.isNull("live_with")) {
				userProfileLiveWithText.setText(resultObject.getString("live_with"));
			} else {
				userProfileLiveWithText.setText("-");
			}
			if(!resultObject.getString("salary").equals("") && !resultObject.isNull("salary")) {
				userProfileSalaryText.setText(resultObject.getString("salary"));
			} else {
				userProfileSalaryText.setText("-");
			}
			if(!resultObject.getString("home_type").equals("") && !resultObject.isNull("home_type")) {
				userProfileHomeTypeText.setText(resultObject.getString("home_type"));
			} else {
				userProfileHomeTypeText.setText("-");
			}
			if(!resultObject.getString("has_beach_house").equals("") && !resultObject.isNull("has_beach_house")) {
				userProfileHasBeachHouseText.setText(resultObject.getString("has_beach_house"));
			} else {
				userProfileHasBeachHouseText.setText("-");
			}
			if(!resultObject.getString("has_jacuzzi").equals("") && !resultObject.isNull("has_jacuzzi")) {
				userProfileHasJacuzziText.setText(resultObject.getString("has_jacuzzi"));
			} else {
				userProfileHasJacuzziText.setText("-");
			}
			if(!resultObject.getString("has_car").equals("") && !resultObject.isNull("has_car")) {
				userProfileHasCarText.setText(resultObject.getString("has_car"));
			} else {
				userProfileHasCarText.setText("-");
			}
			if(!resultObject.getString("has_pet").equals("") && !resultObject.isNull("has_pet")) {
				userProfileHasPetText.setText(resultObject.getString("has_pet"));
			} else {
				userProfileHasPetText.setText("-");
			}
			if(!resultObject.getString("has_pool").equals("") && !resultObject.isNull("has_pool")) {
				userProfileHasPoolText.setText(resultObject.getString("has_pool"));
			} else {
				userProfileHasPoolText.setText("-");
			}
			if(!resultObject.getString("has_motorcycle").equals("") && !resultObject.isNull("has_motorcycle")) {
				userProfileHasMotorcycleText.setText(resultObject.getString("has_motorcycle"));
			} else {
				userProfileHasMotorcycleText.setText("-");
			}
			if(!resultObject.getString("has_bike").equals("") && !resultObject.isNull("has_bike")) {
				userProfileHasBikeText.setText(resultObject.getString("has_bike"));
			} else {
				userProfileHasBikeText.setText("-");
			}
			
			/* Effects */
			
			if(!resultObject.getString("defect").equals("") && !resultObject.isNull("defect")) {
				userProfileDefectText.setText(resultObject.getString("defect"));
			} else {
				userProfileDefectText.setText("-");
			}
			if(!resultObject.getString("hates").equals("") && !resultObject.isNull("hates")) {
				userProfileHatesText.setText(resultObject.getString("hates"));
			} else {
				userProfileHatesText.setText("-");
			}
			if(!resultObject.getString("drinks").equals("") && !resultObject.isNull("drinks")) {
				userProfileDrinksText.setText(resultObject.getString("drinks"));
			} else {
				userProfileDrinksText.setText("-");
			}
			if(!resultObject.getString("smokes").equals("") && !resultObject.isNull("smokes")) {
				userProfileSmokesText.setText(resultObject.getString("smokes"));
			} else {
				userProfileSmokesText.setText("-");
			}
			if(!resultObject.getString("favourite_drug").equals("") && !resultObject.isNull("favourite_drug")) {
				userProfileFavoriteDrugText.setText(resultObject.getString("favourite_drug"));
			} else {
				userProfileFavoriteDrugText.setText("-");
			}
			if(!resultObject.getString("sex_poses").equals("") && !resultObject.isNull("sex_poses")) {
				userProfileSexPosesText.setText(resultObject.getString("sex_poses"));
			} else {
				userProfileSexPosesText.setText("-");
			}
			if(!resultObject.getString("sex_toys").equals("") && !resultObject.isNull("sex_toys")) {
				userProfileSexToysText.setText(resultObject.getString("sex_toys"));
			} else {
				userProfileSexToysText.setText("-");
			}
			if(!resultObject.getString("guilty_pleasure").equals("") && !resultObject.isNull("guilty_pleasure")) {
				userProfileGuiltyPleasureText.setText(resultObject.getString("guilty_pleasure"));
			} else {
				userProfileGuiltyPleasureText.setText("-");
			}
			if(!resultObject.getString("sexual_fantasy").equals("") && !resultObject.isNull("sexual_fantasy")) {
				userProfileSexualFantasyText.setText(resultObject.getString("sexual_fantasy"));
			} else {
				userProfileSexualFantasyText.setText("-");
			}
			if(!resultObject.getString("fetishes").equals("") && !resultObject.isNull("fetishes")) {
				userProfileFetishesText.setText(resultObject.getString("fetishes"));
			} else {
				userProfileFetishesText.setText("-");
			}
		} catch (JSONException e) {
			userProfileNameText.setText(intent.getStringExtra("username"));
			userProfileAgeText.setText("-");
			userProfileHeightText.setText("-");
			userProfileWeightText.setText("-");
			userProfileCountryText.setText("-");
			e.printStackTrace();
		}
		
		
		imageLoader = new ImageLoader(this);
		
		userProfileUserImage.getLayoutParams().height = 95;
		userProfileUserImage.getLayoutParams().width = 95;
		userProfileUserImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageLoader.DisplayImage(intent.getStringExtra("pic"), userProfileUserImage);
		
		mImageGrid = (TwoWayGridView) findViewById(R.id.userProfileImageGalleryGrid);
		imageAdapter = new PictureAdapter(this, 0, 0, 0, 0, 60, 60, resultImages);
		mImageGrid.setAdapter(imageAdapter);
		
		mImageGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(TwoWayAdapterView parent, View v, int position, long id) {
				View overlayView = v.findViewById(R.id.userProfileOverlayView);
				overlayView.setVisibility(View.VISIBLE);
				Intent intent = new Intent(UserProfile.this, UserImageGallery.class);
				intent.putExtra("from", "profile");
				intent.putExtra("images", resultImages.toString());
				intent.putExtra("position", position);
				startActivity(intent);	
			}
		});
		
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
	
	private class UserInfo extends AsyncTask<Integer, Integer, String> {
		
		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UserProfile activityRef;
		
		public UserInfo(UserProfile activityRef) {
			this.activityRef = activityRef;
		}
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
			
			dialog = ProgressDialog.show(UserProfile.this, "", "Cargando datos...", true);
			
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
		}
		
		@Override
		protected String doInBackground(Integer... params) {
		    
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://demosmartphone.supermanket.cl/apim/users/" + Integer.toString(currentUser[0]) + ".json?app_key="
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
				alert.showAlertDialog(UserProfile.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				activityRef.profileLoad(result);
				dialog.dismiss();

			}
			
		}
		
	}
	
	private class UnblockInfo extends AsyncTask<Integer, Void, String> {
		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UserProfile activityRef;
		
		public UnblockInfo(UserProfile activityRef) {
			this.activityRef = activityRef;
		}
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
			
			dialog = ProgressDialog.show(UserProfile.this, "", "Desbloqueando...", true);
			
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
		}
		
		@Override
		protected String doInBackground(Integer... params) {
		    
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://demosmartphone.supermanket.cl/apim/access/unlocked/" + Integer.toString(params[0]) + ".json?app_key="
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
				alert.showAlertDialog(UserProfile.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				try {
					JSONObject resultObject = new JSONObject(result);
					if(resultObject.has("errors")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
						builder.setTitle("Oh noes!");
						builder.setMessage(resultObject.getString("errors"));
						builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
			    			@Override
			    			public void onClick(DialogInterface dialog, int id) {
			    				Toast.makeText(UserProfile.this, "Desbloqueo cancelado", Toast.LENGTH_SHORT).show();
			    			}
			    		});
						builder.setPositiveButton(R.string.btn_buy_credits, new DialogInterface.OnClickListener() {
			    			@Override
			    			public void onClick(DialogInterface dialog, int id) {
			    				String url = "http://www.supermanket.com/credit_packs";
			    				Intent intent = new Intent(Intent.ACTION_VIEW);
			    				intent.setData(Uri.parse(url));
			    				startActivity(intent);
			    			}
			    		});
						AlertDialog alert = builder.create();
						dialog.dismiss();
						alert.show();
					} else  {
						activityRef.profileLoad(result);
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				

			}
			
		}
	}
	
	private class SendOffer extends AsyncTask<Integer, Void, String> {
		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
			
			dialog = ProgressDialog.show(UserProfile.this, "", "Enviando oferta...", true);
			
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
		}
		
		@Override
		protected String doInBackground(Integer... params) {
		    
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://demosmartphone.supermanket.cl/apim/offers/client/" + Integer.toString(params[0]) + ".json?app_key="
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
				alert.showAlertDialog(UserProfile.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
			} else {
				try {
					JSONObject resultObject = new JSONObject(result);
					if(resultObject.has("errors")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
						builder.setTitle("Oh noes!");
						builder.setMessage(resultObject.getString("errors"));
						builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
			    			@Override
			    			public void onClick(DialogInterface dialog, int id) {
			    				Toast.makeText(UserProfile.this, "Oferta cancelada", Toast.LENGTH_SHORT).show();
			    			}
			    		});
						builder.setPositiveButton(R.string.btn_buy_credits, new DialogInterface.OnClickListener() {
			    			@Override
			    			public void onClick(DialogInterface dialog, int id) {
			    				String url = "http://www.supermanket.com/credit_packs";
			    				Intent intent = new Intent(Intent.ACTION_VIEW);
			    				intent.setData(Uri.parse(url));
			    				startActivity(intent);
			    			}
			    		});
						AlertDialog alert = builder.create();
						alert.show();
					} else if(resultObject.has("notice")){
						alert.showAlertDialog(UserProfile.this, "Hey!", resultObject.getString("notice"), false);
					} else if(resultObject.has("error")) {
						JSONObject errorObject = resultObject.getJSONObject("error");
						JSONArray errorArray = errorObject.getJSONArray("client_id");
						String errorMsg = "";
						if(errorArray.length() > 1) {
							for(int i = 0; i < errorArray.length(); i++) {
								errorMsg = errorMsg + errorArray.getString(i) + "\n";
							}
						} else {
							errorMsg = errorArray.getString(0);
						}
						
						alert.showAlertDialog(UserProfile.this, "Oh noes!", errorMsg, false);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("Resultado", result);	
				dialog.dismiss();
			}
			
		}
	}
	
	private class BuyProduct extends AsyncTask<Integer, Void, String> {
		private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		
		@Override
		protected void onPreExecute() {
		
			super.onPreExecute();
			
			dialog = ProgressDialog.show(UserProfile.this, "", "Comprando producto...", true);
			
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
		}
		
		@Override
		protected String doInBackground(Integer... params) {
		    
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://demosmartphone.supermanket.cl/apim/orders/product/" + Integer.toString(params[0]) + ".json?app_key="
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
				alert.showAlertDialog(UserProfile.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
			} else {
				try {
					JSONObject resultObject = new JSONObject(result);
					if(resultObject.has("errors")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
						builder.setTitle("Oh noes!");
						builder.setMessage(resultObject.getString("errors"));
						builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
			    			@Override
			    			public void onClick(DialogInterface dialog, int id) {
			    				Toast.makeText(UserProfile.this, "Oferta cancelada", Toast.LENGTH_SHORT).show();
			    			}
			    		});
						builder.setPositiveButton(R.string.btn_buy_credits, new DialogInterface.OnClickListener() {
			    			@Override
			    			public void onClick(DialogInterface dialog, int id) {
			    				String url = "http://www.supermanket.com/credit_packs";
			    				Intent intent = new Intent(Intent.ACTION_VIEW);
			    				intent.setData(Uri.parse(url));
			    				startActivity(intent);
			    			}
			    		});
						AlertDialog alert = builder.create();
						alert.show();
					} else if(resultObject.has("notice")){
						alert.showAlertDialog(UserProfile.this, "Hey!", resultObject.getString("notice"), false);
					} else if(resultObject.has("error")) {
						JSONObject errorObject = resultObject.getJSONObject("error");
						JSONArray errorArray = errorObject.getJSONArray("client_id");
						String errorMsg = "";
						if(errorArray.length() > 1) {
							for(int i = 0; i < errorArray.length(); i++) {
								errorMsg = errorMsg + errorArray.getString(i) + "\n";
							}
						} else {
							errorMsg = errorArray.getString(0);
						}
						
						alert.showAlertDialog(UserProfile.this, "Oh noes!", errorMsg, false);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("Resultado", result);	
				dialog.dismiss();
			}
			
		}
	}

}
