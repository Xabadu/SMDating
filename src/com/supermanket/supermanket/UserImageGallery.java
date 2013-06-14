package com.supermanket.supermanket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.supermanket.utilities.GalleryAdapter;
import com.supermanket.utilities.UtilityBelt;

public class UserImageGallery extends Activity {

	private JSONObject resultObject;
	private JSONArray userImages;
	private String referral;
	private int position = 0;
	private LinearLayout layout;
	private Button userGalleryAddBtn;
	private Button userGalleryMainBtn;
	private Button userGalleryDeleteBtn;
	private static final int PICK_IMAGE = 1;
	private static final int PICK_CAMERA_IMAGE = 2;
	private Bitmap bitmap;
	Uri imageUri;
	MediaPlayer mp = new MediaPlayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_user_image_gallery);
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.userGalleryViewPager);
	    referral = intent.getStringExtra("from");
	    if(referral.equalsIgnoreCase("account")) {
	    	try {
	    		resultObject = new JSONObject(intent.getStringExtra("images"));
				userImages = resultObject.getJSONArray("photos");
				for(int i = 0; i < userImages.length(); i++) {
					JSONObject imageInfo = userImages.getJSONObject(i);
					if(imageInfo.getBoolean("avatar?")) {
						position = i;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } else if(referral.equalsIgnoreCase("profile")) {
	    	try {
				userImages = new JSONArray(intent.getStringExtra("images"));
				position = intent.getIntExtra("position", 0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	layout = (LinearLayout) findViewById(R.id.userGalleryActions);
	    	layout.setVisibility(View.GONE);
	    } 
	    position = intent.getIntExtra("position", 0);
	    GalleryAdapter adapter = new GalleryAdapter(this, userImages);
	    viewPager.setAdapter(adapter);
	    viewPager.setCurrentItem(position);
	    
	    userGalleryAddBtn = (Button) findViewById(R.id.userGalleryAddBtn);
	    userGalleryMainBtn = (Button) findViewById(R.id.userGalleryMainBtn);
	    userGalleryDeleteBtn = (Button) findViewById(R.id.userGalleryDeleteBtn);
	    
	    final CharSequence[] items = {"Cámara", "Galería"};
	    
	    userGalleryAddBtn.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		AlertDialog.Builder builder = new AlertDialog.Builder(UserImageGallery.this);
	    		builder.setTitle("Agregar desde");
	    		builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int id) {
	    				Toast.makeText(UserImageGallery.this, "Carga cancelada", Toast.LENGTH_SHORT).show();
	    			}
	    		});
	    		builder.setItems(items, new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int item) {
	    				if(item == 0) {
	    					String fileName = "pic.jpg";
	    					ContentValues values = new ContentValues();
	    					values.put(MediaStore.Images.Media.TITLE, fileName);
	    					values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture from camera");
	    					imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	    					Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	    					cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	    					startActivityForResult(cameraIntent, PICK_CAMERA_IMAGE);
	    				} else if(item == 1) {
	    					try {
	    						Intent galleryIntent = new Intent();
	    						galleryIntent.setType("image/*");
	    						galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
	    						startActivityForResult(Intent.createChooser(galleryIntent, "Selecciona una imagen"), PICK_IMAGE);
	    					} catch(Exception e) {
	    						Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
	    						Log.e(e.getClass().getName(), e.getMessage(), e);
	    					}
	    				}
	    			}
	    		});
	    		AlertDialog alert = builder.create();
	    		alert.show();
	    		
	    	}
	    });
	    
	    userGalleryMainBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
	    });
	    
	    userGalleryDeleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
	    });
	    
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Uri selectedImageUri = null;
		String filePath = null;
		
		switch(requestCode) {
			case PICK_IMAGE:
				if(resultCode == RESULT_OK) {
					selectedImageUri = data.getData();
				}
				break;
			case PICK_CAMERA_IMAGE:
				if(resultCode == RESULT_OK) {
					selectedImageUri = imageUri;
				} else if(resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "Captura cancelada", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Error de captura", Toast.LENGTH_SHORT).show();
				}
				break;
		}
		
		if(selectedImageUri != null) {
			try {
				
				String fileManagerString = selectedImageUri.getPath();
				String selectedImagePath = getPath(selectedImageUri);
				
				if(selectedImagePath != null) {
					filePath = selectedImagePath;
				} else if(fileManagerString != null) {
					filePath = fileManagerString;
				} else {
					Toast.makeText(this, "Ruta desconocida", Toast.LENGTH_SHORT).show();
				}
				
				if(filePath != null) {
					decodeFile(filePath);
				} else {
					bitmap = null;
				}
				
			} catch(Exception e) {
				Toast.makeText(getApplicationContext(), "Error interno",  Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
				Log.d("Error interno", e.toString());
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		String[] projection  = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if(cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(columnIndex);
		}
		return null;
	}
	
	public void decodeFile(String filePath) {
		
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);
		
		final int REQUIRED_SIZE = 1024;
		
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		
		while(true) {
			if(width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		if(bitmap != null) {
			ImageUpload imageUpload = new ImageUpload();
			imageUpload.execute();
		} else {
			if(filePath.contains("http://") || filePath.contains("https://")) {
				Toast.makeText(this, "Solo puedes subir imágenes almacenadas en tu teléfono", Toast.LENGTH_LONG).show();
			}
		}
		
		
	}
	
	class ImageUpload extends AsyncTask<Void, Void, String> {
		
		private ProgressDialog dialog;
		InputStream is;
		BitmapFactory.Options bfo;
		ByteArrayOutputStream bao;
		private String api_key;
		private String api_secret;
		private String signature;
		private SharedPreferences mSharedPreferences;
		private UtilityBelt utilityBelt = new UtilityBelt();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = ProgressDialog.show(UserImageGallery.this, "", "Subiendo imagen", true);
			mSharedPreferences = getApplicationContext().getSharedPreferences("SupermanketPreferences", 0);
			api_key = mSharedPreferences.getString("API_KEY", "");
			api_secret = mSharedPreferences.getString("API_SECRET", "");
			signature = utilityBelt.md5("app_key" + api_key + api_secret);
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			
			HttpClient client = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost post = new HttpPost("http://demosmartphone.supermanket.cl/apim/photos.json?app_key="
									+ api_key + "&signature=" + signature);
            //post.setHeader("content-type", "application/json");
            
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 100, bos);
			byte[] data = bos.toByteArray();
			entity.addPart("photo[image]", new ByteArrayBody(data, "image.jpg"));
			post.setEntity(entity);
			Log.d("entity", data.toString());

            try {
            	HttpResponse resp = client.execute(post, localContext);
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
			dialog.dismiss();
			
			Log.d("Resultado", result);
			
		}
		
	}

}
