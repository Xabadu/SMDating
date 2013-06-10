package com.supermanket.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import com.supermanket.supermanket.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
	
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
 
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.message_list_row, null);
 
        TextView title = (TextView)vi.findViewById(R.id.messageListNameText); // title
        TextView artist = (TextView)vi.findViewById(R.id.messageListLastMessageText); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.messageListDateTimeText); // duration
        ImageView thumb_image = (ImageView)vi.findViewById(R.id.messageListUserImage); // thumb image
 
        HashMap<String, String> message = new HashMap<String, String>();
        message = data.get(position);
 
        // Setting all values in listview
        title.setText(message.get("name"));
        artist.setText(message.get("message"));
        duration.setText("11/05/2013 14:30");
        //imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
	

}
