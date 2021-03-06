package com.supermanket.supermanket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.ConectivityTools;

public class Login extends Activity {

	// Progress dialog
    ProgressDialog pDialog;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    // Custom utilities
    private ConectivityTools ct;
    private AlertDialogs alert = new AlertDialogs();

    // UI Elements
    ImageButton btnLogin;
    ImageButton btnRegister;
    ImageButton btnFacebookLogin;
    ImageButton loginFormLoginBtn;
    Button loginFormCancelBtn;
    ImageButton registerFormRegisterBtn;
    ImageButton registerFormCancelBtn;
    Button registerFormChangeBirthdayBtn;
    EditText loginFormEmailField;
    EditText loginFormPasswordField;
    EditText registerFormUsernameField;
    EditText registerFormEmailField;
    EditText registerFormPasswordField;
    EditText registerFormConfirmPasswordField;
    ImageView loginLogoImage;
    RadioGroup registerFormGenreGroup;
    RelativeLayout loginLayout;

    // Facebook elements
    private static final List<String> READ_PERMISSIONS = Arrays.asList("email", "user_birthday", "user_hometown");

    // Other
    private int day;
    private int month;
    private int year;
    String respStr = null;
    final Calendar c = Calendar.getInstance();

    static final int DATE_DIALOG_ID = 999;

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";

    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;

    String SENDER_ID = "924830394291";

    static final String TAG = "GCMDemo";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;
    String regid;

    static final String SERVICE_BASE_URL = "";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ct = new ConectivityTools(getApplicationContext());



		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);

		regid = getRegistrationId(this);

		if(!isLoggedInAlready()) {
			loginScreen();
		} else {
			if(regid.length() == 0) {
				registerDevice(null);
			} else {
				Intent intent = new Intent(this, Dashboard.class);
				startActivity(intent);
			}
		}

	}

	private void registerDevice(ProgressDialog dialog) {
		context = getApplicationContext();
		regid = getRegistrationId(context);

		if(regid.length() == 0) {
			if (!ct.isConnectingToInternet()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
	        	registerBackground(dialog);
	        }

		} else {
			gcm = GoogleCloudMessaging.getInstance(this);
			Intent intent = new Intent(this, Dashboard.class);
			startActivity(intent);
			if(dialog.isShowing()) {
				dialog.dismiss();
			}
		}
		gcm = GoogleCloudMessaging.getInstance(this);
	}

	private String getRegistrationId(Context context) {
		String registrationId = mSharedPreferences.getString(PROPERTY_REG_ID, "");
		if(registrationId.length() == 0) {
			Log.v(TAG, "Registration not found");
			return "";
		}

		int registeredVersion = mSharedPreferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if(registeredVersion != currentVersion || isRegistrationExpired()) {
			Log.v(TAG, "Registration expired or App version changed");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch(NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private boolean isRegistrationExpired() {
		long expirationTime = mSharedPreferences.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
		return System.currentTimeMillis() > expirationTime;
	}

	private void registerBackground(final ProgressDialog dialog) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if(gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration id=" + regid;
					setRegistrationId(context, regid);
				} catch(IOException ex) {
					msg = "Error: " + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.v("ID", msg);
				Intent intent = new Intent(Login.this, Devices.class);
				startActivity(intent);
				if(dialog != null) {
					if(dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}
		}.execute();
	}

	private void setRegistrationId(Context context, String regId) {
		int appVersion = getAppVersion(context);
		Log.v(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		if(!mSharedPreferences.getString(PROPERTY_REG_ID, "").equalsIgnoreCase("")) {
			editor.putString("OLD_REG_ID", regId);
		}
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME_MS;

		Log.v(TAG, "Setting registration expiry time to " + new Timestamp(expirationTime));
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		editor.commit();
	}

	@Override
	protected void onResume() {
		synchronized (GcmBroadcastReceiver.CURRENTACTIVITYLOCK) {
            GcmBroadcastReceiver.currentActivity = this;
		}
		int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if(ConnectionResult.SUCCESS != code) {
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(code, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			if(errorDialog != null) {
				ErrorDialogFragment errorFragment =
                    new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getFragmentManager(),
                    "GCM Updates");
			}
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

	public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

	public void loginScreen() {

		setContentView(R.layout.activity_login);



		btnLogin = (ImageButton) findViewById(R.id.loginLoginBtn);
    	btnRegister = (ImageButton) findViewById(R.id.loginRegisterBtn);
    	btnFacebookLogin = (ImageButton) findViewById(R.id.loginFacebookBtn);
    	loginFormEmailField = (EditText) findViewById(R.id.loginEmailField);
    	loginFormPasswordField = (EditText) findViewById(R.id.loginPasswordField);
    	loginLogoImage = (ImageView) findViewById(R.id.loginLogo);
    	loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);

    	loginLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hideSoftKeyboard(Login.this);
			}
        });

    	DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		int density = displaymetrics.densityDpi;

		switch(density) {

			case DisplayMetrics.DENSITY_LOW:
				 loginLogoImage.setVisibility(View.INVISIBLE);
				 break;

			case DisplayMetrics.DENSITY_MEDIUM:
				 loginLogoImage.setVisibility(View.INVISIBLE);
				 break;
		}

    	try {
		    PackageInfo info = getPackageManager().getPackageInfo(
		            "com.supermanket.supermanket",
		            PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		    }
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

    	btnLogin.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			hideSoftKeyboard(Login.this);
				if(loginFormEmailField.getText().toString().equals("") || loginFormPasswordField.getText().toString().equals("")) {
					alert.showAlertDialog(Login.this, "Oh noes!", "Debes ingresar tus datos.", false);
				} else {
					if(isEmailValid(loginFormEmailField.getText().toString())) {
						if (!ct.isConnectingToInternet()) {
							AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
				        	new LoginSupermanket(Login.this).execute();
				        }

					} else {
						alert.showAlertDialog(Login.this, "Oh noes!", "Email no v�lido", false);
					}

				}
			}
	    });

    	btnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pDialog = ProgressDialog.show(Login.this, "", "Cargando...", true);
				createAccount();
			}
	    });

    	btnFacebookLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!ct.isConnectingToInternet()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
		        	pDialog = ProgressDialog.show(Login.this, "", "Cargando...", true);
					loginFacebook();
		        }

			}
	    });

	}

	public void createAccount() {

		pDialog.dismiss();

		setContentView(R.layout.register_form);

		registerFormRegisterBtn = (ImageButton) findViewById(R.id.registerFormRegisterBtn);
		registerFormCancelBtn = (ImageButton) findViewById(R.id.registerFormCancelBtn);
		registerFormChangeBirthdayBtn = (Button) findViewById(R.id.registerFormChangeBirthdayBtn);
		registerFormUsernameField = (EditText) findViewById(R.id.registerFormUsernameField);
		registerFormEmailField = (EditText) findViewById(R.id.registerFormEmailField);
		registerFormPasswordField = (EditText) findViewById(R.id.registerFormPasswordField);
		registerFormConfirmPasswordField = (EditText) findViewById(R.id.registerFormConfirmPasswordField);

		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);

		registerFormChangeBirthdayBtn.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		registerFormRegisterBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hideSoftKeyboard(Login.this);
				if(registerFormUsernameField.getText().toString().equals("")
						|| registerFormEmailField.getText().toString().equals("")
						|| registerFormPasswordField.getText().toString().equals("")
						|| registerFormConfirmPasswordField.getText().toString().equals("")
						|| registerFormChangeBirthdayBtn.getText().toString().equals("")) {
					alert.showAlertDialog(Login.this, "Oh noes!", "Debes completar todos los datos.", false);
				} else {
					if(!isEmailValid(registerFormEmailField.getText().toString())) {
						alert.showAlertDialog(Login.this, "Oh noes!", "Debes ingresar un correo v�lido.", false);
					} else {
						if(registerFormPasswordField.getText().toString().equals(registerFormConfirmPasswordField.getText().toString())) {
							if (!ct.isConnectingToInternet()) {
								AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
					        	new RegisterUser(Login.this).execute();
					        }

						} else {
							alert.showAlertDialog(Login.this, "Oh noes!", "Las contrase�as no coinciden.", false);
						}
					}
				}
			}
		});

		registerFormCancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loginScreen();
			}
		});

	}

	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	public void loginFacebook() {


		openActiveSession(Login.this, true, new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (state.isOpened()) {
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								Session sesion = Session.getActiveSession();
								Editor editor = mSharedPreferences.edit();
								editor.putString("FB_TOKEN", sesion.getAccessToken());
								editor.commit();
								if(pDialog != null && pDialog.isShowing()) {
									pDialog.dismiss();
								}
								if (!ct.isConnectingToInternet()) {
									AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
						        	LoginFb loginFb = new LoginFb(Login.this);
									loginFb.execute(sesion.getAccessToken());
						        }

							}
						}
					});
				}
				if(state.isClosed()) {
					Toast.makeText(Login.this, "Autentificaci�n cancelada", Toast.LENGTH_SHORT).show();
					loginScreen();
				}
			}
		}, READ_PERMISSIONS);

	}



	private boolean isLoggedInAlready() {
		return mSharedPreferences.getBoolean("LOGGED_IN", false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	private static Session openActiveSession(Activity activity, boolean allowLoginUI, StatusCallback callback, List<String> permissions) {
	    OpenRequest openRequest = new OpenRequest(activity).setPermissions(permissions).setCallback(callback);
	    Session session = new Builder(activity).build();
	    if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
	        Session.setActiveSession(session);
	        session.openForRead(openRequest);
	        return session;
	    }
	    return null;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener,
                         year-18, month,day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			registerFormChangeBirthdayBtn.setText(Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year));

		}
	};

	public boolean isEmailValid(String email) {
		Pattern pattern;
		Matcher matcher;
		String regExpn =
             "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                 +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

         CharSequence inputStr = email;

         pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
         matcher = pattern.matcher(inputStr);

         if(matcher.matches())
        	 return true;
         else
        	 return false;
	}



	private class LoginSupermanket extends AsyncTask<Void, Integer, String> {

		ProgressDialog dialog;
		EditText emailField;
		EditText passwordField;
		Login activityRef;

		public LoginSupermanket(Login activityRef) {
			this.activityRef = activityRef;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			emailField = (EditText) findViewById(R.id.loginEmailField);
			passwordField = (EditText) findViewById(R.id.loginPasswordField);
			dialog = ProgressDialog.show(Login.this, "", "Validando...", true, true);

		}

		@Override
		protected String doInBackground(Void... params) {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "session.json");
            post.setHeader("content-type", "application/json");

            JSONObject usuario = new JSONObject();
            JSONObject user = new JSONObject();

            try {
				user.put("username", emailField.getText().toString().trim());
				user.put("password", passwordField.getText().toString().trim());
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

            try {
            	usuario.put("user", user);
            } catch (JSONException e1) {
				e1.printStackTrace();
			}

            try {
				StringEntity entity = new StringEntity(usuario.toString());
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

			if(result == null) {
				alert.showAlertDialog(Login.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				try {
					JSONObject response = new JSONObject(result);
					JSONObject status = response.getJSONObject("user");
					mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
					Editor e = mSharedPreferences.edit();
		            e.putBoolean("LOGGED_IN", true);
		            e.putString("USER_ID", status.getString("id"));
		            e.putString("USER_SEX", status.getString("sex"));
		            e.putString("API_KEY", status.getString("api_key"));
		            e.putString("API_SECRET", status.getString("api_secret"));
		            e.commit();

		            activityRef.registerDevice(dialog);
		            //dialog.dismiss();

				} catch (JSONException e1) {
					JSONObject response;
					try {
						response = new JSONObject(result);
						String error = response.getString("errors");
						alert.showAlertDialog(Login.this, "Oh noes!", error, false);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					e1.printStackTrace();
					dialog.dismiss();
				}



			}

		}
	}

	private class LoginFb extends AsyncTask<String, Integer, String> {

		ProgressDialog dialog;
		Login activityRef;

		public LoginFb(Login activityRef) {
			this.activityRef = activityRef;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(Login.this, "", "Validando...", true, true);
		}

		@Override
		protected String doInBackground(String... params) {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "session/facebook.json");
            post.setHeader("content-type", "application/json");

            JSONObject usuario = new JSONObject();
            JSONObject user = new JSONObject();

            try {
				user.put("token", params[0]);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

            try {
            	usuario.put("user", user);
            } catch (JSONException e1) {
				e1.printStackTrace();
			}

            try {
				StringEntity entity = new StringEntity(usuario.toString());
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

			if(result == null) {
				alert.showAlertDialog(Login.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
			} else {
				try {
					JSONObject response = new JSONObject(result);
					JSONObject status = response.getJSONObject("user");
					mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
					Editor e = mSharedPreferences.edit();
					e.putBoolean("LOGGED_IN", true);
		            e.putString("USER_ID", status.getString("id"));
		            e.putString("USER_SEX", status.getString("sex"));
		            e.putString("API_KEY", status.getString("api_key"));
		            e.putString("API_SECRET", status.getString("api_secret"));
		            e.commit();

		            activityRef.registerDevice(dialog);

				} catch (JSONException e1) {
					JSONObject response;
					try {
						response = new JSONObject(result);
						String error = response.getString("errors");
						alert.showAlertDialog(Login.this, "Oh noes!", error, false);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					e1.printStackTrace();
					dialog.dismiss();
				}



			}

		}
	}

	private class RegisterUser extends AsyncTask<Void, Integer, String> {

		ProgressDialog dialog;
		int genre;
		int radioId;
		Button birthdayBtn;
		EditText emailField;
		EditText usernameField;
		EditText passwordField;
		RadioGroup genreGroup;
		String[] birthdayText;
		Login activityRef;

		public RegisterUser(Login activityRef) {
			this.activityRef = activityRef;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			usernameField = (EditText) findViewById(R.id.registerFormUsernameField);
			emailField = (EditText) findViewById(R.id.registerFormEmailField);
			passwordField = (EditText) findViewById(R.id.registerFormPasswordField);
			genreGroup = (RadioGroup) findViewById(R.id.registerFormGenreGroup);
			birthdayBtn = (Button) findViewById(R.id.registerFormChangeBirthdayBtn);
			birthdayText = birthdayBtn.getText().toString().split("/");

			radioId = genreGroup.getCheckedRadioButtonId();

			if(radioId == R.id.registerFormGenreFemale) {
				genre = 0;
			} else if(radioId == R.id.registerFormGenreMale) {
				genre = 1;
			}

			dialog = ProgressDialog.show(Login.this, "", "Creando cuenta...", true);

		}

		@Override
		protected String doInBackground(Void... params) {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "users.json");
            post.setHeader("content-type", "application/json");

            JSONObject usuario = new JSONObject();
            JSONObject user = new JSONObject();

            try {
				user.put("username", usernameField.getText().toString());
				user.put("email", emailField.getText().toString());
				user.put("password", passwordField.getText().toString());
				user.put("password_confirmation", passwordField.getText().toString());
				user.put("birthday(3i)", birthdayText[0]);
				user.put("birthday(2i)", birthdayText[1]);
				user.put("birthday(1i)", birthdayText[2]);
				user.put("sex", genre);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

            try {
				usuario.put("user", user);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

            try {
				StringEntity entity = new StringEntity(usuario.toString(), HTTP.UTF_8);
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

			if(result == null) {
				alert.showAlertDialog(Login.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
			} else {
				Log.d("Resultado", result);
				try {
					JSONObject response = new JSONObject(result);
					mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
					Editor e = mSharedPreferences.edit();
		            e.putString("USER_ID", response.getString("id"));
		            e.putString("USER_SEX", response.getString("sex"));
		            e.putString("API_KEY", response.getString("api_key"));
		            e.putString("API_SECRET", response.getString("api_secret"));
		            e.commit();

		            activityRef.registerDevice(dialog);

				} catch (JSONException e1) {
					JSONObject response;
					JSONObject errors;
					try {
						response = new JSONObject(result);
						Log.d("Resultado", result);
						errors = response.getJSONObject("errors");
						String error = "Existen los siguientes errores: \n\n";
						Iterator<?> keys = errors.keys();
						JSONArray errorLines;
						while(keys.hasNext()) {
							String key = (String) keys.next();
							error += key + ": ";
							errorLines = errors.getJSONArray(key);
							if(errorLines.length() > 1) {
								for(int i = 0; i < errorLines.length(); i++) {
									error += errorLines.getString(i) + "\n";
								}
							} else {
								error += errorLines.getString(0) + "\n";
							}
						}
						dialog.dismiss();
						alert.showAlertDialog(Login.this, "Oh noes!", error, false);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					e1.printStackTrace();
					dialog.dismiss();
				}



			}

		}

	}

}
