package com.supermanket.supermanket;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class Splash extends Activity {

private final int SPLASH_DISPLAY_LENGTH = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	}

	@Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSplashEnabled = sp.getBoolean("isSplashEnabled", true);
 
        if (isSplashEnabled) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                	Intent mainIntent = new Intent(Splash.this, Login.class);
                	Splash.this.startActivity(mainIntent);
                    Splash.this.finish(); 
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        else {
        	Intent mainIntent = new Intent(Splash.this, Login.class);
            Splash.this.startActivity(mainIntent);
            Splash.this.finish();
        }
    }

}
