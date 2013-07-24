package com.supermanket.utilities;

import com.supermanket.supermanket.Account;
import com.supermanket.supermanket.R;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class LocationAdapter extends CursorAdapter 
							implements android.widget.AdapterView.OnItemClickListener {
	
	private AutoCompleteDbAdapter mDbHelper;
	private Context context;
	private TextView personalFormLocationId;
	
	public LocationAdapter(AutoCompleteDbAdapter mDbHelper, Context context) {
		super(context, null);
		this.mDbHelper = mDbHelper;
		this.context = context;
	}
	
	@Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider() != null) {
            return getFilterQueryProvider().runQuery(constraint);
        }

        Cursor cursor = mDbHelper.getLocations((constraint != null ? constraint.toString() : null));

        return cursor;
    }
	
	@Override
    public String convertToString(Cursor cursor) {
        final int columnIndex = cursor.getColumnIndexOrThrow("nombre");
        final String str = cursor.getString(columnIndex);
        return str;
    }
	
	@Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String text = convertToString(cursor);
        ((TextView) view).setText(text);
    }
	
	@Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view =
                inflater.inflate(android.R.layout.simple_dropdown_item_1line,
                        parent, false);

       return view;
    }
	
	@Override
    public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
        Cursor cursor = (Cursor) listView.getItemAtPosition(position);
        int idCiudad = cursor.getInt(cursor.getColumnIndexOrThrow("ciudad"));
        Account.setId(idCiudad);
    }

}
