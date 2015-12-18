package com.supermanket.supermanket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebView;

import com.supermanket.utilities.UtilityBelt;

public class Devices extends Activity {

	SharedPreferences mSharedPreferences;

	static final String SERVICE_BASE_URL = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devices);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		if(!mSharedPreferences.getString("registration_id", "").equals("")) {
			if(mSharedPreferences.getString("OLD_REG_ID", "").equalsIgnoreCase("")) {
				RegisterDevice registerDevice = new RegisterDevice();
				registerDevice.execute(mSharedPreferences.getString("registration_id", ""));
			} else {
				UpdateDevice updateDevice = new UpdateDevice();
				updateDevice.execute(mSharedPreferences.getString("OLD_REG_ID", ""), mSharedPreferences.getString("registration_id", ""));
			}
		} else {
			Intent intent = new Intent(this, Dashboard.class);
			startActivity(intent);
		}

	}

	private class RegisterDevice extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		int height = 0;
		int width = 0;
		String userAgent;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(Devices.this, "", "Configurando...", true);
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + "page" + api_secret);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			height = displaymetrics.heightPixels;
			width = displaymetrics.widthPixels;
			userAgent = new WebView(Devices.this).getSettings().getUserAgentString();
		}

		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "devices.json?app_key="
					+ api_key + "&page=" + "&signature=" + signature);
            post.setHeader("content-type", "application/json");

            JSONObject device = new JSONObject();
            JSONObject dispositivo = new JSONObject();

            String tablet = "false";
            if(width >= 640) {
            	tablet = "true";
            }

            try {
				device.put("active", "true");
				device.put("os", "1");
				device.put("os_version", android.os.Build.VERSION.RELEASE);
				device.put("resolution", Integer.toString(width) + "x" + Integer.toString(height));
				device.put("tablet", tablet);
				device.put("uid", params[0]);
				device.put("user_agent", userAgent);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

            try {
            	dispositivo.put("device", device);
            } catch (JSONException e1) {
				e1.printStackTrace();
			}

            try {
				StringEntity entity = new StringEntity(dispositivo.toString());
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}


            try {
            	HttpResponse resp = client.execute(post);
            	/*if(resp != null) {
            		return EntityUtils.toString(resp.getEntity());
            	} else {
            		return "Yeas";
            	}*/
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


			Intent intent = new Intent(Devices.this, Dashboard.class);
			startActivity(intent);
			dialog.dismiss();
			Devices.this.finish();

		}
	}

	private class UpdateDevice extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		int height = 0;
		int width = 0;
		String userAgent;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(Devices.this, "", "Configurando...", true);
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + "page" + api_secret);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			height = displaymetrics.heightPixels;
			width = displaymetrics.widthPixels;
			userAgent = new WebView(Devices.this).getSettings().getUserAgentString();
		}

		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "devices/" + params[0] + ".json?app_key="
					+ api_key + "&page=" + "&signature=" + signature);
            post.setHeader("content-type", "application/json");

            JSONObject device = new JSONObject();
            JSONObject dispositivo = new JSONObject();

            String tablet = "false";
            if(width >= 640) {
            	tablet = "true";
            }

            try {
				device.put("active", "true");
				device.put("os", "1");
				device.put("os_version", android.os.Build.VERSION.RELEASE);
				device.put("resolution", Integer.toString(width) + "x" + Integer.toString(height));
				device.put("tablet", tablet);
				device.put("uid", params[1]);
				device.put("user_agent", userAgent);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

            try {
            	dispositivo.put("device", device);
            } catch (JSONException e1) {
				e1.printStackTrace();
			}

            try {
				StringEntity entity = new StringEntity(dispositivo.toString());
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}


            try {
            	HttpResponse resp = client.execute(post);
				/*if(resp != null) {
					return EntityUtils.toString(resp.getEntity());
				} else {
					return "Yeas";
				}*/
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

			Intent intent = new Intent(Devices.this, Dashboard.class);
			startActivity(intent);
			dialog.dismiss();
			Devices.this.finish();

		}
	}

}
