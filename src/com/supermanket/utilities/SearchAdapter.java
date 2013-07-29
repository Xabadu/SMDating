package com.supermanket.utilities;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.supermanket.supermanket.R;
import com.supermanket.supermanket.Search;

public class SearchAdapter extends BaseExpandableListAdapter {
	
	private Activity context;
    private Map<String, List<String>> paramCollections;
    private List<String> params;
 
    public SearchAdapter(Activity context, List<String> params,
            Map<String, List<String>> paramCollections) {
        this.context = context;
        this.paramCollections = paramCollections;
        this.params = params;
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return paramCollections.get(params.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
 
        if (convertView == null) {
        	convertView = inflater.inflate(R.layout.search_child_item, null);
        }
 
        CheckBox item = (CheckBox) convertView.findViewById(R.id.searchCheckbox);
        item.setText(null);
                
        item.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Search.changeParam(childPosition, arg1);
			}
        	
        });
        
 
        item.setText(laptop);
        return convertView;
    }
 
    public int getChildrenCount(int groupPosition) {
        return paramCollections.get(params.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return params.get(groupPosition);
    }
 
    public int getGroupCount() {
        return params.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.search_group_item,
                    null);
        }
        
        TextView item = (TextView) convertView.findViewById(R.id.searchGroup);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
	
}
