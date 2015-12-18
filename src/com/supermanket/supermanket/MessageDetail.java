package com.supermanket.supermanket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.supermanket.supermanket.MessagesList.GetContacts;
import com.supermanket.utilities.AlertDialogs;
import com.supermanket.utilities.ConectivityTools;
import com.supermanket.utilities.DiscussArrayAdapter;
import com.supermanket.utilities.UtilityBelt;

public class MessageDetail extends Activity {

	private ArrayList<HashMap<String, String>> messages = new ArrayList<HashMap<String, String>>();
	private static ListView list;
	private static DiscussArrayAdapter adapter;
	private static int contactId;
	private static int currentPosition;
	private ImageButton sendMessageBtn;
	private ImageButton blockBtn;
	private ImageButton unblockBtn;
	public EditText messageDetailTextField;
	ConectivityTools ct;

	private static SharedPreferences mSharedPreferences;

	static final String SERVICE_BASE_URL = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		Intent intent = getIntent();
		contactId = intent.getIntExtra("id", 0);
		ct = new ConectivityTools(getApplicationContext());
		if (!ct.isConnectingToInternet()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MessageDetail.this);
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
        	GetMessages getMessages = new GetMessages(this);
    		getMessages.execute(contactId);
        }

	}

	public void showMessages(String data) {
		setContentView(R.layout.activity_message_detail);

		sendMessageBtn = (ImageButton) findViewById(R.id.messageDetailSendButton);
		messageDetailTextField = (EditText) findViewById(R.id.messageDetailTextField);
		blockBtn = (ImageButton) findViewById(R.id.messageDetailBlockBtn);
		unblockBtn = (ImageButton) findViewById(R.id.messageDetailUnBlockBtn);
		final Intent intent = getIntent();
		if(intent.getBooleanExtra("blocked", false)) {
			blockBtn.setVisibility(View.INVISIBLE);
			Log.d("Val", "true");
		} else {
			unblockBtn.setVisibility(View.INVISIBLE);
			Log.d("Val", "false");
		}

		JSONArray allMessages = null;
		try {
			allMessages = new JSONArray(data);
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
				if(singleMessage.getBoolean("read")) {
					msgInfo.put("read", "true");
				} else {
					msgInfo.put("read", "false");
				}

				messages.add(msgInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		list = (ListView) findViewById(R.id.messageDetailList);
		currentPosition = allMessages.length();
        adapter = new DiscussArrayAdapter(MessageDetail.this, messages);
        list.setAdapter(adapter);
        list.setSelection(currentPosition - 1);

        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				InputMethodManager imm = (InputMethodManager)getSystemService(
      			      Context.INPUT_METHOD_SERVICE);
      			imm.hideSoftInputFromWindow(messageDetailTextField.getWindowToken(), 0);
			}
        });



		blockBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MessageDetail.this);
				builder.setTitle(R.string.alert_attention_title);
				builder.setMessage(R.string.alert_block);
				builder.setPositiveButton(R.string.alert_block_yes, new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int id) {
	    				BlockContact block = new BlockContact(MessageDetail.this);
	    				block.execute(Integer.toString(intent.getIntExtra("id", 0)));
	    			}
	    		});
				builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		unblockBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MessageDetail.this);
				builder.setTitle(R.string.alert_attention_title);
				builder.setMessage(R.string.alert_unblock);
				builder.setPositiveButton(R.string.alert_unblock_yes, new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int id) {
	    				UnBlockContact unblock = new UnBlockContact(MessageDetail.this);
	    				unblock.execute(Integer.toString(intent.getIntExtra("id", 0)));
	    			}
	    		});
				builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		sendMessageBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		if(!messageDetailTextField.getText().toString().equalsIgnoreCase("")) {
        			if (!ct.isConnectingToInternet()) {
        				AlertDialog.Builder builder = new AlertDialog.Builder(MessageDetail.this);
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
        	        	SendMessage message = new SendMessage(MessageDetail.this);
            			message.execute(Integer.toString(intent.getIntExtra("id", 0)));
        	        }

        		}
        	}
        });

	}

	public static DiscussArrayAdapter getAdapter() {
		return adapter;
	}

	public static ListView getList() {
		return list;
	}

	public static int getContact() {
		return contactId;
	}

	public static int getCurrentPosition() {
		return currentPosition;
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
	                	Intent intent = new Intent(MessageDetail.this, Login.class);
	                	startActivity(intent);
	                	MessageDetail.this.finish();
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

    private class GetMessages extends AsyncTask<Integer, Void, String> {
    	private ProgressDialog dialog;
		private AlertDialogs alert = new AlertDialogs();
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private MessageDetail activityRef;

		public GetMessages(MessageDetail activityRef) {
			this.activityRef = activityRef;
		}

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
			HttpGet get = new HttpGet(SERVICE_BASE_URL + "contact/" + Integer.toString(params[0]) + "/messages.json?app_key="
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
    			alert.showAlertDialog(MessageDetail.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
    		} else {
    			Log.d("Result", result);
    			activityRef.showMessages(result);
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
		private MessageDetail activityRef;

		public SendMessage(MessageDetail activityRef) {
			this.activityRef = activityRef;
		}

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
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "contact/" + params[0] + "/messages.json?app_key="
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
    			alert.showAlertDialog(MessageDetail.this, "Oh noes!", "Ha ocurrido un error inesperado. Int�ntalo nuevamente", false);
				dialog.dismiss();
    		} else {
    			activityRef.showMessages(result);
    			dialog.dismiss();
    		}
    	}

    }

    public class BlockContact extends AsyncTask<String, Void, String> {

		MessageDetail activityRef;
		ProgressDialog dialog;
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		private ImageButton blockBtn;
		private ImageButton unblockBtn;

		public BlockContact(MessageDetail activityRef) {
			this.activityRef = activityRef;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(MessageDetail.this, "", "Bloqueando...", true);
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + "page" + api_secret);
			blockBtn = (ImageButton) findViewById(R.id.messageDetailBlockBtn);
			unblockBtn = (ImageButton) findViewById(R.id.messageDetailUnBlockBtn);
		}

		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVICE_BASE_URL + "contacts/" + params[0] + "/blocked.json?app_key="
					+ api_key + "&page=" + "&signature=" + signature);
            post.setHeader("content-type", "application/json");

            JSONObject blocked = new JSONObject();
            JSONObject bloqueado = new JSONObject();


            try {
				blocked.put("reason", "-");
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

            try {
            	bloqueado.put("blocked", blocked);
            } catch (JSONException e1) {
				e1.printStackTrace();
			}

            try {
				StringEntity entity = new StringEntity(bloqueado.toString());
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}


            try {
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
			Log.d("Resultado", result);
			try {
				JSONObject resultObj = new JSONObject(result);
				if(resultObj.getString("status").equalsIgnoreCase("ok")) {
					blockBtn.setVisibility(View.INVISIBLE);
					unblockBtn.setVisibility(View.VISIBLE);
					Toast.makeText(MessageDetail.this, "Contacto bloqueado", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(MessageDetail.this, "Error, int�ntalo nuevamente", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(MessageDetail.this, "Error, int�ntalo nuevamente", Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();

		}

	}

    public class UnBlockContact extends AsyncTask<String, Void, String> {

	MessageDetail activityRef;
	ProgressDialog dialog;
	private String api_key;
	private String api_secret;
	private String signature;
	private SharedPreferences mSharedPreferences;
	private UtilityBelt utilityBelt = new UtilityBelt();
	private ImageButton unblockBtn;
	private ImageButton blockBtn;

	public UnBlockContact(MessageDetail activityRef) {
		this.activityRef = activityRef;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(MessageDetail.this, "", "Desbloqueando", true);
		mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
		api_key = mSharedPreferences.getString("API_KEY", "");
		api_secret = mSharedPreferences.getString("API_SECRET", "");
		signature = utilityBelt.md5("app_key" + api_key + "page" + api_secret);
		unblockBtn = (ImageButton) findViewById(R.id.messageDetailUnBlockBtn);
		blockBtn = (ImageButton) findViewById(R.id.messageDetailBlockBtn);
	}

	@Override
	protected String doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(SERVICE_BASE_URL + "contacts/" + params[0] + "/unblocked.json?app_key="
				+ api_key + "&page=" + "&signature=" + signature);
        post.setHeader("content-type", "application/json");

        try {
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
		Log.d("Resultado", result);
		try {
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getString("status").equalsIgnoreCase("ok")) {
				unblockBtn.setVisibility(View.INVISIBLE);
				blockBtn.setVisibility(View.VISIBLE);
				Toast.makeText(MessageDetail.this, "Contacto desbloqueado", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(MessageDetail.this, "Error, int�ntalo nuevamente.", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(MessageDetail.this, "Error, int�ntalo nuevamente.", Toast.LENGTH_LONG).show();
		}
		dialog.dismiss();

	}

}

}
