package com.supermanket.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.supermanket.supermanket.R;

public class PopupAdapter implements InfoWindowAdapter{
	
	LayoutInflater inflater = null;
	
	public PopupAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}
	
	@Override
	public View getInfoContents(Marker marker) {
		
		View popup = inflater.inflate(R.layout.map_popup, null);
		TextView name = (TextView) popup.findViewById(R.id.mapPopupNameText);
		
		name.setText(marker.getTitle());
		
		return popup;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

}
