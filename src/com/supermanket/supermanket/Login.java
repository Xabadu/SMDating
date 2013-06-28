package com.supermanket.supermanket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
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
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
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
    Button btnLogin;
    Button btnRegister;
    Button btnFacebookLogin;
    Button loginFormLoginBtn;
    Button loginFormCancelBtn;
    Button registerFormRegisterBtn;
    Button registerFormCancelBtn;
    Button registerFormChangeBirthdayBtn;
    EditText loginFormEmailField;
    EditText loginFormPasswordField;
    EditText registerFormUsernameField;
    EditText registerFormEmailField;
    EditText registerFormPasswordField;
    EditText registerFormConfirmPasswordField;
    RadioGroup registerFormGenreGroup;
    
    // Facebook elements
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final List<String> READ_PERMISSIONS = Arrays.asList("email");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;
    
    // Other
    private int day;
    private int month;
    private int year;
    String respStr = null;
    final Calendar c = Calendar.getInstance();
    
    static final int DATE_DIALOG_ID = 999;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT > 9) {
        	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        	StrictMode.setThreadPolicy(policy);
        }
		
		ct = new ConectivityTools(getApplicationContext());
		 
        if (!ct.isConnectingToInternet()) {
            alert.showAlertDialog(Login.this, "Error de Conexión",
                    "Esta aplicación necesita una conexión a Internet para funcionar.", false);
            return;
        }
		 
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
	        
	    if(!isLoggedInAlready()){
	    	loginScreen();
	    } else {
	    	Intent intent = new Intent(this, Dashboard.class);
	        startActivity(intent);
	    }
		
	}
	
	public void loginScreen() {
		
		setContentView(R.layout.activity_login);
		
		btnLogin = (Button) findViewById(R.id.loginLoginBtn);
    	btnRegister = (Button) findViewById(R.id.loginRegisterBtn);
    	btnFacebookLogin = (Button) findViewById(R.id.loginFacebookBtn);
    	loginFormEmailField = (EditText) findViewById(R.id.loginEmailField);
    	loginFormPasswordField = (EditText) findViewById(R.id.loginPasswordField);
    	
    	btnLogin.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
				if(loginFormEmailField.getText().toString().equals("") || loginFormPasswordField.getText().toString().equals("")) {
					alert.showAlertDialog(Login.this, "Oh noes!", "Debes ingresar tus datos.", false);
				} else {
					if(isEmailValid(loginFormEmailField.getText().toString())) {
						new loginService().execute();
					} else {
						alert.showAlertDialog(Login.this, "Oh noes!", "Email no válido", false);
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
				pDialog = ProgressDialog.show(Login.this, "", "Cargando...", true);
				loginFacebook();
			}
	    });
		
	}
	
	public void createAccount() {
		
		pDialog.dismiss();
		
		setContentView(R.layout.register_form);
		
		registerFormRegisterBtn = (Button) findViewById(R.id.registerFormRegisterBtn);
		registerFormCancelBtn = (Button) findViewById(R.id.registerFormCancelBtn);
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
				if(registerFormUsernameField.getText().toString().equals("") 
						|| registerFormEmailField.getText().toString().equals("") 
						|| registerFormPasswordField.getText().toString().equals("")
						|| registerFormConfirmPasswordField.getText().toString().equals("")
						|| registerFormChangeBirthdayBtn.getText().toString().equals("")) {
					alert.showAlertDialog(Login.this, "Oh noes!", "Debes completar todos los datos.", false);
				} else {
					if(!isEmailValid(registerFormEmailField.getText().toString())) {
						alert.showAlertDialog(Login.this, "Oh noes!", "Debes ingresar un correo válido.", false);
					} else {
						if(registerFormPasswordField.getText().toString().equals(registerFormConfirmPasswordField.getText().toString())) {
							new registerService().execute();
						} else {
							alert.showAlertDialog(Login.this, "Oh noes!", "Las contraseñas no coinciden.", false);
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
	
	public void loginFacebook() {
		
		Session session = Session.getActiveSession();
    	
    	if(session != null) {
    		session.close();
    	}
		
		pDialog = ProgressDialog.show(Login.this, "", "Un momento...");
    	
		if(!isLoggedInAlready()) {
			
			openActiveSession(Login.this, true, new Session.StatusCallback() {

				@Override
				public void call(Session session, SessionState state, Exception exception) {
					if (state.isOpened()) {
						Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user, Response response) {
								if (user != null) {
														                									
									/* Preferencia Interna */
									
									Editor e = mSharedPreferences.edit();
					                e.putBoolean("LOGGED_IN", true);
					                e.putString("USER_ID", user.getId());
					                e.putString("USER_FIRSTNAME", user.getFirstName());
					                e.putString("USER_FULLNAME", user.getName());
					                e.putString("USER_EMAIL", user.asMap().get("email").toString());
					                e.commit();
					                
					                HttpClient client = new DefaultHttpClient();
					                
					                HttpPost post = new HttpPost("http://play.medialabs.net/usuario/update/");
					                post.setHeader("content-type", "application/json");
					                
					                String userLocation = "Parts Unknown";
					                
					                if(user.getLocation() != null) {
					                	userLocation = user.getLocation().getProperty("name").toString();
					                }
					                
					                JSONObject usuario = new JSONObject();
					                
					                try {
										usuario.put("fb_id", user.getId());
										usuario.put("first_name", user.getFirstName());
										usuario.put("middle_name", user.getMiddleName());
										usuario.put("last_name", user.getLastName());
										usuario.put("full_name", user.getName());
										usuario.put("birthday", user.getBirthday());
										usuario.put("username", user.getUsername());
										usuario.put("location", userLocation);
										usuario.put("fb_url", user.getLink());
										usuario.put("email", user.asMap().get("email").toString());
										
										try {
											StringEntity entity = new StringEntity(usuario.toString());
											post.setEntity(entity);
										} catch (UnsupportedEncodingException e1) {
											e1.printStackTrace();
										}
										
										try {
											HttpResponse resp = client.execute(post);
										} catch (ClientProtocolException e1) {
											e1.printStackTrace();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
										
									} catch (JSONException e1) {
										e1.printStackTrace();
									}
					                
								} 
							}
						});
					}
					if(state.isClosed()) {
						pDialog.dismiss();
						Toast.makeText(Login.this, "Autentificación cancelada", Toast.LENGTH_SHORT).show();
						loginScreen();
					}
				}
			}, READ_PERMISSIONS);

		} else {
			Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
		}
		
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

		// when dialog box is closed, below method will be called.
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
	
	
	
	private class loginService extends AsyncTask<Void, Integer, String> {
		
		ProgressDialog dialog;
		AlertDialogs alertDialog = new AlertDialogs();
		EditText emailField;
		EditText passwordField;
		
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
			HttpPost post = new HttpPost("http://demosmartphone.supermanket.cl/apim/session.json");
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
				alert.showAlertDialog(Login.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
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
		                
		            Intent intent = new Intent(Login.this, Dashboard.class);
		            startActivity(intent);
		                dialog.dismiss();
		                finish();
					
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
				}
				
				dialog.dismiss();

			}
			
		}
	}
	
	private class registerService extends AsyncTask<Void, Integer, String> {
		
		ProgressDialog dialog;
		AlertDialogs alertDialog = new AlertDialogs();
		int genre;
		int radioId;
		Button birthdayBtn;
		EditText emailField;
		EditText usernameField;
		EditText passwordField;
		RadioGroup genreGroup;
		String[] birthdayText;
		
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
			HttpPost post = new HttpPost("http://demosmartphone.supermanket.cl/apim/users.json");
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
				alert.showAlertDialog(Login.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
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
		                
		            Intent intent = new Intent(Login.this, Dashboard.class);
		            startActivity(intent);
		            dialog.dismiss();
		            Login.this.finish();
					
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
				}
				
				dialog.dismiss();

			}
			
		}
		
	}

}
