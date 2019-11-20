package com.example.suixinplayer.uitli;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences
 */
public class SharedPreferencesMgr {

	@SuppressWarnings("unused")
	private static Context context;
	private static SharedPreferences sPrefs;

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings({ "static-access", "deprecation" })
	/**
	 * SharedPreferences文件的生成
	 *
	 * @param context
	 * @param fileName
	 */
	private SharedPreferencesMgr(Context context, String fileName) {
		this.context = context;
		sPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);//MODE_PRIVATE用这个7.0以上不会报错
	}

	public static void init(Context context, String fileName) {
		new SharedPreferencesMgr(context, fileName);
	}

	public static String fileName;

	public static int getInt(String key, int defaultValue) {
		return sPrefs.getInt(key, defaultValue);
	}

	public static void setInt(String key, int value) {
		sPrefs.edit().putInt(key, value).commit();
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return sPrefs.getBoolean(key, defaultValue);
	}

	public static void setBoolean(String key, boolean value) {
		if (sPrefs == null)
			return ;
		sPrefs.edit().putBoolean(key, value).commit();
	}

	public static String getString(String key, String defaultValue) {
		if (sPrefs == null)
			return defaultValue;
		return sPrefs.getString(key, defaultValue);
	}

	public static void setString(String key, String value) {
		if (sPrefs == null)
			return;
		sPrefs.edit().putString(key, value).commit();
	}

	public static void clearAll() {
		if (sPrefs == null)
			return;
		sPrefs.edit().clear().commit();
	}
}
