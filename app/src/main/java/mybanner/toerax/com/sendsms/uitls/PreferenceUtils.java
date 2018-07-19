package mybanner.toerax.com.sendsms.uitls;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Map;
import java.util.Set;

/**
 * 小数据保存
 * 
 * @ClassName: PreferenceUtils
 * @Description: TODO
 * @author wu cheng hong
 * @date 2015-7-2 下午1:41:13
 * 
 */
public class PreferenceUtils {

	public static String getPrefString(Context context, String key, final String defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	public static void setPrefString(Context context, final String key, final String value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	public static boolean getPrefBoolean(Context context, final String key, final boolean defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	public static boolean hasKey(Context context, final String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
	}

	public static void setPrefString(Context context, Map<String, String> map) {
		final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			edit.putString(key, String.valueOf(map.get(key)));
		}

		edit.commit();
	}

	public static void setPrefBoolean(Context context, final String key, final boolean value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, value).commit();
	}

	public static void setPrefInt(Context context, final String key, final int value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putInt(key, value).commit();
	}

	public static int getPrefInt(Context context, final String key, final int defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	public static void setPrefFloat(Context context, final String key, final float value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putFloat(key, value).commit();
	}

	public static float getPrefFloat(Context context, final String key, final float defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	public static void setSettingLong(Context context, final String key, final long value) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putLong(key, value).commit();
	}

	public static long getPrefLong(Context context, final String key, final long defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	public static void clearPreference(Context context) {
		final SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
		final Editor editor = p.edit();
		editor.clear();
		editor.commit();
	}

	public static void setPrefOtherString(Context context, final String key, final String value) {
		SharedPreferences settings = context.getSharedPreferences("launchCount", 0);
		settings.edit().putString(key, value).commit();
	}

	public static String getPrefOtherString(Context context, final String key, String defaultValue) {
		SharedPreferences sp = context.getSharedPreferences("launchCount", 0);
		return sp.getString(key, defaultValue);
	}

	/**
	 * 保存其它数据
	 * 
	 * @return
	 */
	public static void setOtherPreferences(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences("otherPreferences", 0);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	

	public static void clearOtherPreferences(Context context){
		SharedPreferences sp = context.getSharedPreferences("otherPreferences", 0);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
	
	public static String getOtherPreferences(Context context, String key, String defValue){
		SharedPreferences sp = context.getSharedPreferences("otherPreferences", 0);
		return sp.getString(key, defValue);
	}

	/**
	 * 启动次数
	 * 
	 * @return
	 */
	public static String getLaunchCount(Context context) {
		SharedPreferences sp = context.getSharedPreferences("launchCount", 0);
		String count = sp.getString("count", "0");
		if (count.equals("0")) {
			Editor editor = sp.edit();
			editor.putString("count", "1");
			editor.commit();
		}
		return count;
	}
	
	/**
	 * 新增page判断
	 * 
	 * @return
	 */
	public static String getFirstViewPage(Context context) {
		SharedPreferences sp = context.getSharedPreferences("firstViewPage", 0);
		String count = sp.getString("cNumber", "0");
		if (count.equals("0")) {
			Editor editor = sp.edit();
			editor.putString("cNumber", "1");
			editor.commit();
		}
		return count;
	}

}
