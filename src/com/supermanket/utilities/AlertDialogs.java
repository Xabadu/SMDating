package com.supermanket.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogs {
	
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
            Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
 
        if(status != null)
        	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        		public void onClick(final DialogInterface dialog, final int which) {
        		}
        	});
 
        	alertDialog.show();
    	}
	
}