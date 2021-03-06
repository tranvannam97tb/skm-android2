package jp.co.soliton.keymanager.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import jp.co.soliton.keymanager.StringList;

import java.io.File;

/**
 * Created by luongdolong on 11/1/2017.
 */
public class CommonUtils {

	public static void updateWidthToolbar(TextView tvLeft, TextView tvRight) {
		setWidthWrap(tvLeft);
		setWidthWrap(tvRight);
		tvLeft.measure(0, 0);
		tvRight.measure(0, 0);
		int maxWidth = tvLeft.getMeasuredWidth();
		int widthRight = tvRight.getMeasuredWidth();
		if (widthRight > maxWidth) {
			maxWidth = widthRight;
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) tvLeft.getLayoutParams();
			param.width = maxWidth;
			param.gravity = Gravity.LEFT;
			tvLeft.setLayoutParams(param);
		} else if (widthRight < maxWidth) {
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) tvRight.getLayoutParams();
			param.width = maxWidth;
			param.gravity = Gravity.RIGHT;
			tvRight.setLayoutParams(param);
		}
	}

	public static void updateHeader(Context context, TextView tvBack, TextView title) {
		title.measure(0, 0);
		tvBack.measure(0, 0);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		if (title.getMeasuredWidth() > width - (tvBack.getMeasuredWidth() * 2)) {
			tvBack.setText("");
			tvBack.measure(0, 0);
			int tmp = tvBack.getMeasuredWidth() * 2;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) title.getLayoutParams();
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			params.setMargins(tmp, params.topMargin, tmp, params.bottomMargin);
			title.setLayoutParams(params);
		}
	}

	private static void setWidthWrap(TextView txtView) {
		ViewGroup.LayoutParams params = txtView.getLayoutParams();
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		txtView.setLayoutParams(params);
	}

	public static String removeHttp(String host) {
		host = host.toLowerCase();
		String[] strDisallowed = new String[]{"http://", "https://"};
		for (int i = 0 ; i < strDisallowed.length; i++) {
			if (host.startsWith(strDisallowed[i])) {
				return host.replace(strDisallowed[i], "");
			}
		}
		return host;
	}

    // Sharedpref file name

    /**
     * put data in SharedPreferences
     *
     * @author luongdolong
     * @param context app context
     * @param key key get value
     * @param value value
     */
    public static void putPref(Context context, String key, Object value) {
        SharedPreferences pref = context.getSharedPreferences(StringList.m_str_store_preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean)value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer)value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float)value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long)value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.commit();
    }

    /**
     * get String from SharedPreferences
     *
     * @author luongdolong
     * @param context app context
     * @param key key
     * @return value
     */
    public static String getPrefString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(StringList.m_str_store_preference, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    /**
     * get Boolean from SharedPreferences
     *
     * @author luongdolong
     * @param context app context
     * @param key key
     * @return value
     */
    public static boolean getPrefBoolean(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(StringList.m_str_store_preference, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    /**
     * get Integer from SharedPreferences
     *
     * @author luongdolong
     * @param context app context
     * @param key key
     * @return value
     */
    public static int getPrefInteger(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(StringList.m_str_store_preference, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }
    /**
     * get Integer from SharedPreferences
     *
     * @author luongdolong
     * @param context app context
     * @param key key
     * @return value
     */
    public static int getPrefIntegerWithDefaultValue(Context context, String key, int defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(StringList.m_str_store_preference, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }

    /**
     * get Long from SharedPreferences
     *
     * @author luongdolong
     * @param context app context
     * @param key key
     * @return value
     */
    public static long getPrefLong(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(StringList.m_str_store_preference, Context.MODE_PRIVATE);
        return pref.getLong(key, 0);
    }

    /**
     * get Float from SharedPreferences
     *
     * @author luongdolong
     * @param context app context
     * @param key key
     * @return value
     */
    public static float getPrefFloat(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(StringList.m_str_store_preference, Context.MODE_PRIVATE);
        return pref.getFloat(key, 0);
    }

    /**
     * remove value from SharedPreferences with key
     *
     * @author luongdolong
     * @param context context
     * @param key key
     */
    public static void removePref(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(StringList.m_str_store_preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * check input is empty
     *
     * @author luongdolong
     * @param string input
     * @return true if empty, false if not
     */
    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        }
	    return string.isEmpty();
    }

    /**
     * trim the String
     *
     * @param string input string
     * @return trimmed string
     */
    public static String trim(String string) {
        if (isEmpty(string)) {
            return "";
        }
        return string.trim();
    }

    /**
     * convert String to int
     *
     * @author luongdolong
     * @param string input
     * @return int
     */
    public static int toInt(String string) {
        if (string == null) {
            return 0;
        } else {
            return Integer.parseInt(string);
        }
    }

    /**
     * convert String to double
     *
     * @author luongdolong
     * @param string input
     * @return double
     */
    public static double toDouble(String string) {
        if (string == null) {
            return 0;
        } else {
            return Double.parseDouble(string);
        }
    }

    /**
     * convert Object to String
     *
     * @author luongdolong
     * @param obj input
     * @return
     */
    public static String toStr(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * check String is number
     *
     * @author luongdolong
     * @param number input string
     * @return true if number, false if not
     */
    public static boolean isNumber(String number) {
        if (isEmpty(number)) {
            return true;
        }
        char[] c = number.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (!isDigit(c[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * check digit
     *
     * @author luongdolong
     * @param c char input
     * @return true if digit, false if not
     */
    public static boolean isDigit(char c) {
        int x = (int) c;
	    return (x >= 48 && x <= 57) || x == 45;
    }

    public static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
        } else {
            return "";
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return formatSize(totalBlocks * blockSize);
        } else {
            return "";
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
}
