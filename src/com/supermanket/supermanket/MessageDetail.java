package com.supermanket.supermanket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.DiscussArrayAdapter;
import com.supermanket.utilities.UtilityBelt;

public class MessageDetail extends Activity {
	
	private ArrayList<HashMap<String, String>> messages = new ArrayList<HashMap<String, String>>();
	private ListView list;
	private DiscussArrayAdapter adapter;
	private Button sendMessageBtn;
	public EditText messageDetailTextField;
	
	private static SharedPreferences mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		showMessages();
	}
	
	public void showMessages() {
		setContentView(R.layout.activity_message_detail);

		sendMessageBtn = (Button) findViewById(R.id.messageDetailSendButton);
		messageDetailTextField = (EditText) findViewById(R.id.messageDetailTextField);
		final Intent intent = getIntent();
		GetMessages getMessages = new GetMessages();
		getMessages.execute(intent.getIntExtra("id", 0));
		
		sendMessageBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		if(!messageDetailTextField.getText().toString().equalsIgnoreCase("")) {
        			SendMessage message = new SendMessage();
        			message.execute(Integer.toString(intent.getIntExtra("id", 0)));
        		}
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
    
    private class GetMessages extends AsyncTask<Integer, Void, String> {
    	private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private JSONArray allMessages = null;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = ProgressDialog.show(MessageDetail.this, "", "Cargando mensajes...", true);
    		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
		}
		
		@Override
		protected String doInBackground(Integer... params) {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://demosmartphone.supermanket.cl/apim/contact/" + Integer.toString(params[0]) + "/messages.json?app_key="
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
    			alert.showAlertDialog(MessageDetail.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
    		} else {
    			Log.d("Result", result);
    			try {
					allMessages = new JSONArray(result);
					for(int i = 0; i < allMessages.length(); i++) {
						JSONObject singleMessage = allMessages.getJSONObject(i);
						HashMap<String, String> msgInfo = new HashMap<String, String>();
						msgInfo.put("message", singleMessage.getString("content"));
						if(singleMessage.getBoolean("mine")) {
							msgInfo.put("who", "self");
						} else {
							msgInfo.put("who", "other");
						}
						messages.add(msgInfo);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
    			list = (ListView) findViewById(R.id.messageDetailList);
   			 
    	        adapter = new DiscussArrayAdapter(MessageDetail.this, messages);
    	        list.setAdapter(adapter);
    	        list.setSelection(allMessages.length() - 1);
    			dialog.dismiss();
    		}
			
		}
		
    }
    
    private class SendMessage extends AsyncTask<String, Void, String> {
    	
    	private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private JSONObject message = new JSONObject();
		private JSONObject mensaje = new JSONObject();
		private JSONArray allMessages;
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		dialog = ProgressDialog.show(MessageDetail.this, "", "Enviando mensaje...", true);
    		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
			
			try {
				message.put("content", messageDetailTextField.getText().toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				mensaje.put("message", message);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
    	}
    	
    	@Override
    	protected String doInBackground(String... params) {
    		
    		HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://demosmartphone.supermanket.cl/apim/contact/" + params[0] + "/messages.json?app_key="
									+ api_key + "&signature=" + signature);
            post.setHeader("content-type", "application/json");
            
            try {
            	StringEntity entity = new StringEntity(mensaje.toString(), HTTP.UTF_8);
            	post.setEntity(entity);
            	HttpResponse resp = client.execute(post);
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
    			alert.showAlertDialog(MessageDetail.this, "Oh noes!", "Ha ocurrido un error inesperado. Inténtalo nuevamente", false);
				dialog.dismiss();
    		} else {
    			messageDetailTextField.setText("");
    			try {
					allMessages = new JSONArray(result);
					messages.clear();
					for(int i = 0; i < allMessages.length(); i++) {
						JSONObject singleMessage = allMessages.getJSONObject(i);
						HashMap<String, String> msgInfo = new HashMap<String, String>();
						msgInfo.put("message", singleMessage.getString("content"));
						if(singleMessage.getBoolean("mine")) {
							msgInfo.put("who", "self");
						} else {
							msgInfo.put("who", "other");
						}
						messages.add(msgInfo);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
    			list = (ListView) findViewById(R.id.messageDetailList);
    			adapter = new DiscussArrayAdapter(MessageDetail.this, messages);
    			list.setAdapter(adapter);
    	        
    	        Log.d("Total", Integer.toString(allMessages.length()));
    	        list.setSelection(allMessages.length() - 1);
    			dialog.dismiss();
    		}
    	}
    	
    }

}
