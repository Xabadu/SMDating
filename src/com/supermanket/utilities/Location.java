package com.supermanket.utilities;

public class Location {
	
	private int locationId;
	private String locationName;
	
	public Location(int id, String name) {
		locationId = id;
		locationName = name;		
	}
	
	private int getId() {
		return locationId;
	}
	
	private String getName() {
		return locationName;
	}

}
