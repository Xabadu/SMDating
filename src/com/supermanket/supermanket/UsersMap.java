package com.supermanket.supermanket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.ConectivityTools;
import com.supermanket.utilities.PopupAdapter;
import com.supermanket.utilities.UtilityBelt;

public class UsersMap extends FragmentActivity implements OnInfoWindowClickListener,
										GooglePlayServicesClient.ConnectionCallbacks,
										GooglePlayServicesClient.OnConnectionFailedListener {


	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private GoogleMap map;
	private Marker customMarker;
	private LatLng CURRENT_POSITION;
	private UiSettings mapSettings;
	private AlertDialogs aDialog = new AlertDialogs();
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private UserData userData;
	SaveLocation saveLocation;
	GetNearUsers getNearUsers;
	ImageLoader imageLoader = ImageLoader.getInstance();
	JSONArray otherUsersArray = null;
	ConectivityTools ct;

	ImageButton mapDistance100Btn;
	ImageButton mapDistance1000Btn;
	ImageButton mapDistance3000Btn;

	private static SharedPreferences mSharedPreferences;

	static final String SERVICE_BASE_URL = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		ct = new ConectivityTools(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
		if (!ct.isConnectingToInternet()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(UsersMap.this);
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
        	mLocationClient = new LocationClient(this, this, this);
        }

	}

	public void loadMap(String result) {
		setContentView(R.layout.activity_users_map);
		mCurrentLocation = mLocationClient.getLastLocation();
		if(mCurrentLocation != null) {
			Double[] coordenadas = new Double[3];
			coordenadas[0] = mCurrentLocation.getLatitude();
			coordenadas[1] = mCurrentLocation.getLongitude();
			coordenadas[2] = 9.0;
			saveLocation = new SaveLocation();
			saveLocation.execute(coordenadas);
			CURRENT_POSITION = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
			if(map == null) {
				map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.usersMapMap)).getMap();
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				mapSettings = map.getUiSettings();
				mapSettings.setCompassEnabled(false);
				mapSettings.setZoomControlsEnabled(false);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_POSITION, 16));
				map.addMarker(new MarkerOptions().position(CURRENT_POSITION).title("T�"));

				getNearUsers = new GetNearUsers();
				getNearUsers.execute(coordenadas);
			}

			mapDistance100Btn = (ImageButton) findViewById(R.id.mapDistance100Btn);
			mapDistance1000Btn = (ImageButton) findViewById(R.id.mapDistance1000Btn);
			mapDistance3000Btn = (ImageButton) findViewById(R.id.mapDistance3000btn);

			mapDistance100Btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Double[] coordenadas = new Double[3];
					coordenadas[0] = mCurrentLocation.getLatitude();
					coordenadas[1] = mCurrentLocation.getLongitude();
					coordenadas[2] = 0.1;
					map.clear();
					map.addMarker(new MarkerOptions().position(CURRENT_POSITION));
					getNearUsers = new GetNearUsers();
					getNearUsers.execute(coordenadas);
				}
			});

			mapDistance1000Btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Double[] coordenadas = new Double[3];
					coordenadas[0] = mCurrentLocation.getLatitude();
					coordenadas[1] = mCurrentLocation.getLongitude();
					coordenadas[2] = 1.0;
					map.clear();
					map.addMarker(new MarkerOptions().position(CURRENT_POSITION));
					getNearUsers = new GetNearUsers();
					getNearUsers.execute(coordenadas);
				}
			});

			mapDistance3000Btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Double[] coordenadas = new Double[3];
					coordenadas[0] = mCurrentLocation.getLatitude();
					coordenadas[1] = mCurrentLocation.getLongitude();
					coordenadas[2] = 3.0;
					map.clear();
					map.addMarker(new MarkerOptions().position(CURRENT_POSITION));
					getNearUsers = new GetNearUsers();
					getNearUsers.execute(coordenadas);
				}
			});


		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(UsersMap.this);
			builder.setTitle(R.string.alert_attention_title);
			builder.setMessage("Error detectando ubicaci�n. Verifica que tu dispositivo est� bien configurado.");
			builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
    			@Override
    			public void onClick(DialogInterface dialog, int id) {
    				UsersMap.this.finish();
    	        	startActivity(getIntent());
    			}
    		});
			builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			Toast.makeText(this, "Error detectando ubicaci�n. Verifica que tu dispositivo est� bien configurado."
					, Toast.LENGTH_LONG).show();
		}

	}

	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((UsersMap) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	@Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }



    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
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
	public void onInfoWindowClick(Marker marker) {
		if(marker.getTitle().equalsIgnoreCase("T�")) {
			Toast.makeText(this, "Aqu� est�s ahora :)", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Desconectado. Por favor reconectar.", Toast.LENGTH_SHORT).show();
		UsersMap.this.finish();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		if (connectionResult.hasResolution()) {
		    try {
		        connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
		    } catch (IntentSender.SendIntentException e) {
		        e.printStackTrace();
		    }
		} else {
		    aDialog.showAlertDialog(this, "Oh noes!", "Error, try again", false);
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		if(map == null) {
			mCurrentLocation = mLocationClient.getLastLocation();
			if (!ct.isConnectingToInternet()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(UsersMap.this);
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
            	AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.alert_attention_title);
				builder.setMessage(R.string.alert_logout);
				builder.setPositiveButton(R.string.btn_logout, new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int id) {
	    				Editor e = mSharedPreferences.edit();
	                	e.remove("LOGGED_IN");
	                    e.commit();
	                	Intent intent = new Intent(UsersMap.this, Login.class);
	                	startActivity(intent);
	                	UsersMap.this.finish();
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

	private class UserData extends AsyncTask<Integer, Integer, String> {

		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UsersMap activityRef;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private ProgressDialog dialog;

		public UserData(UsersMap activityRef) {
			this.activityRef = activityRef;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			dialog = ProgressDialog.show(UsersMap.this, "", "Cargando...", true);

			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);

		}

		@Override
		protected String doInBackground(Integer... params) {

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(SERVICE_BASE_URL + "profile.json?app_key="
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
				alert.showAlertDialog(UsersMap.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				activityRef.loadMap(result);
				dialog.dismiss();
			}

		}

	}

	private class SaveLocation extends AsyncTask<Double, Integer, String> {

		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			dialog = ProgressDialog.show(UsersMap.this, "", "Enviando ubicaci�n...", true);

			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);

		}

		@Override
		protected String doInBackground(Double... params) {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "point.json?app_key="
									+ api_key + "&signature=" + signature);
            post.setHeader("content-type", "application/json");

            try {
            	JSONObject point = new JSONObject();
            	JSONObject ubicacion = new JSONObject();
            	try {
					point.put("latitude", params[0]);
					point.put("longitude", params[1]);
					ubicacion.put("point", point);
					StringEntity entity = null;
					try {
						entity = new StringEntity(ubicacion.toString());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					post.setEntity(entity);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

			if(result == null) {
				alert.showAlertDialog(UsersMap.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado 2do", result);
				dialog.dismiss();
			}



		}

	}

	private class GetNearUsers extends AsyncTask<Double, Integer, String> implements OnInfoWindowClickListener{

		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private ProgressDialog dialog;
		private int radius;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(UsersMap.this, "", "Buscando usuarios cercanos...", true);
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
		}

		@Override
		protected String doInBackground(Double... params) {
			signature = utilityBelt.md5("app_key" + api_key + "page1" + api_secret);
			radius = params[2].intValue();
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(SERVICE_BASE_URL + "near_people/"
									+ Double.toString(params[0]) + "/" + Double.toString(params[1]) +
									"/" + Integer.toString(radius) + ".json?app_key=" + api_key + "&page=1&signature=" + signature);
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
				alert.showAlertDialog(UsersMap.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
			} else {
				try {
					JSONObject otherUsersObj = new JSONObject(result);
					otherUsersArray = otherUsersObj.getJSONArray("users");
					for(int i = 0; i < otherUsersArray.length(); i++) {
						JSONObject singleUserData = otherUsersArray.getJSONObject(i);
						final LatLng NEW_POSITION = new LatLng(singleUserData.getDouble("latitude"), singleUserData.getDouble("longitude"));
						map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.usersMapMap)).getMap();
						final View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_view, null);
						ImageView userImage = (ImageView) marker.findViewById(R.id.mapMarkerUserImage);
					    DisplayImageOptions options = new DisplayImageOptions.Builder().build();
					    final String username = singleUserData.getString("username");
					    String avatar = singleUserData.getString("avatar_medium");

					    imageLoader.displayImage(avatar,
					    			userImage, options, new ImageLoadingListener() {
					        @Override
					        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					        	customMarker = map.addMarker(new MarkerOptions()
								.position(NEW_POSITION)
								.title(username)
								.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(UsersMap.this, marker))));
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

						map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
					    map.setOnInfoWindowClickListener(this);
					    dialog.dismiss();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dialog.dismiss();
				}
			}
		}

		@Override
		public void onInfoWindowClick(Marker marker) {
			String user = marker.getTitle();
			if(!user.equalsIgnoreCase("T�")) {
				try {
					for(int i = 0; i < otherUsersArray.length(); i++) {
						JSONObject single = otherUsersArray.getJSONObject(i);
						if(user.equalsIgnoreCase(single.getString("username"))) {
							Intent intent = new Intent(UsersMap.this, UserProfile.class);
							intent.putExtra("id", single.getInt("id"));
							intent.putExtra("pic", single.getString("avatar_medium"));
							intent.putExtra("username", single.getString("username"));
							startActivity(intent);
						}
					}
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}


}
