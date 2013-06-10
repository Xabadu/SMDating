package com.supermanket.utilities;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UtilityBelt {
	
	@SuppressLint("SimpleDateFormat")
	public int calculateAge(String birthDate) {
		
		int yearOfBirth = Integer.parseInt(birthDate.substring(0, 4));
		int monthOfBirth = Integer.parseInt(birthDate.substring(5, 7));
		int dayOfBirth = Integer.parseInt(birthDate.substring(8, 10));
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		java.util.Date date = new java.util.Date();
		int currentYear = Integer.parseInt(dateFormat.format(date));
		
		dateFormat = new SimpleDateFormat("MM");
		
		date = new java.util.Date();
		int currentMonth = Integer.parseInt(dateFormat.format(date));

		dateFormat = new SimpleDateFormat("dd");
		date = new java.util.Date();
		int currentDay = Integer.parseInt(dateFormat.format(date));

		int age = currentYear - yearOfBirth;

		if(currentMonth < monthOfBirth) {
			age = age - 1;
		}

		if(currentMonth == monthOfBirth && currentDay < dayOfBirth) {
			age = age - 1;
		}
		
		return age;
	}
	
	public String md5(String original) {
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			byte[] hash = md.digest(original.getBytes("UTF-8")); 
			StringBuilder sb = new StringBuilder(2*hash.length); 
			for(byte b : hash) { 
				sb.append(String.format("%02x", b&0xff)); 
			} 
			original = sb.toString();
			return original;
		} catch (UnsupportedEncodingException ex) { 
			return "Noes";
		} catch (NoSuchAlgorithmException ex) { 
			return "Noes";
		}
	}

}
