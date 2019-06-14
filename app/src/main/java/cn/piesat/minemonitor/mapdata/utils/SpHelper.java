package cn.piesat.minemonitor.mapdata.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpHelper {

	static SharedPreferences sp;

	public static void init(Context context) {
		sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
	}

	public static String getStringValue(String key) {
		return sp.getString(key, null);
	}

	public static String getStringValue(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	public static void setStringValue(String key, String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void setLongValue(String key, long value) {
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static long getLongValue(String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}
	public static boolean getBooleanValue(String key) {
		return sp.getBoolean(key, true);
	}

	public static void setBooleanValue(String key, boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static int getIntValue(String key) {
		return sp.getInt(key, 0);
	}
	
	public static int getIntValue(String key,int defValue) {
		return sp.getInt(key, defValue);
	}

	public static void setIntValue(String key, int value) {
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static float getFloatValue(String key) {
		return sp.getFloat(key, 0.0f);
	}

	public static void setFloatValue(String key, float value) {
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	/**
	 * key-value,value为json串
	 * 
	 * **/
	public static void saveData(String key, String jsonString) {
		Editor editor = sp.edit();
		editor.putString(key, jsonString);
		editor.commit();
	}
	public static void savePush(String key, String jsonString) {
		Editor editor = sp.edit();
		editor.putString(key, jsonString);
		editor.commit();
	}
	public static int getPush(String key){
		return sp.getInt(key,0);
	}

}
