package mobi.jzcx.android.chongmi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import mobi.jzcx.android.chongmi.App;

/**
 * 用户偏好设置工具
 */
public class PreferencesUtils {
	public static void setFieldStringValue(String prefName, String field, String value) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
		preferences.edit().putString(field, value).commit();
	}

	public static String getFieldStringValue(String prefName, String field) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return preferences.getString(field, "");
	}

	public static void setFieldIntValue(String prefName, String field, int value) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
		preferences.edit().putInt(field, value).commit();
	}

	public static int getFieldIntValue(String prefName, String field) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return preferences.getInt(field, -1);
	}

	public static void setFieldLongValue(String prefName, String field, long value) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
		preferences.edit().putLong(field, value).commit();
	}

	public static long getFieldLongValue(String prefName, String field) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return preferences.getLong(field, -1);
	}

	public static void setFieldBooleanValue(String prefName, String field, boolean value) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
		preferences.edit().putBoolean(field, value).commit();
	}

	public static boolean getFieldBooleanValue(String prefName, String field) {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return preferences.getBoolean(field, false);
	}

	/**
	 * 清楚所有信息
	 */
	public static void clear() {
		SharedPreferences preferences = App.getInstance().getSharedPreferences(FileUtils.APP, Context.MODE_PRIVATE);
		preferences.edit().clear().commit();
	}

	public static void setIsHaveComments(boolean haveMsg) {
		setFieldBooleanValue(FileUtils.APP, "haveComments", haveMsg);
	}

	public static boolean getIsHaveComments() {
		return getFieldBooleanValue(FileUtils.APP, "haveComments");
	}

	public static void setIsHaveSysMsgs(boolean haveMsg) {
		setFieldBooleanValue(FileUtils.APP, "haveSysMsg", haveMsg);
	}

	public static boolean getIsHaveSysMsgs() {
		return getFieldBooleanValue(FileUtils.APP, "haveSysMsg");
	}

	public static void setUserId(String token) {
		setFieldStringValue(FileUtils.APP, "UserId", token);
	}

	public static void setIsHaveRequestMsgs(boolean haveMsg) {
		setFieldBooleanValue(FileUtils.APP, "haveRequestMsg", haveMsg);
	}

	public static boolean getIsHaveRequestMsgs() {
		return getFieldBooleanValue(FileUtils.APP, "haveRequestMsg");
	}

	public static String getUserId() {
		String phone = "";
		try {
			phone = getFieldStringValue(FileUtils.APP, "UserId");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phone;
	}

	public static void setUserToken(String token) {
		setFieldStringValue(FileUtils.APP, "UserToken", token);
	}

	public static String getUserToken() {
		String phone = "";
		try {
			phone = getFieldStringValue(FileUtils.APP, "UserToken");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phone;
	}

	/**
	 * 设置登录号码
	 * 
	 * @param phone
	 */
	public static void setLoginPhone(String phone) {
		setFieldStringValue(FileUtils.APP, "LoginPhone", phone);
	}

	/**
	 * 读取登录号码
	 * 
	 * @return
	 */
	public static String getLoginPhone() {
		String phone = "";
		try {
			phone = getFieldStringValue(FileUtils.APP, "LoginPhone");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phone;
	}

	// 第三方登录
	public static void setThirdUid(String uid) {
		setFieldStringValue(FileUtils.APP, "ThirdUid", uid);
	}

	public static String getThirdUid() {
		return getFieldStringValue(FileUtils.APP, "ThirdUid");
	}

	public static void setThirdPlatform(String platform) {
		setFieldStringValue(FileUtils.APP, "ThirdPlatform", platform);
	}

	public static String getThirdPlatform() {
		return getFieldStringValue(FileUtils.APP, "ThirdPlatform");
	}

	public static void setThirdName(String name) {
		setFieldStringValue(FileUtils.APP, "ThirdName", name);
	}

	public static String getThirdName() {
		return getFieldStringValue(FileUtils.APP, "ThirdName");
	}

	public static void setThirdImage(String image) {
		setFieldStringValue(FileUtils.APP, "ThirdImage", image);
	}

	public static String getThirdImage() {
		return getFieldStringValue(FileUtils.APP, "ThirdImage");
	}

	public static void setThirdGender(int gender) {
		setFieldIntValue(FileUtils.APP, "ThirdGender", gender);
	}

	public static int getThirdGender() {
		int gender = getFieldIntValue(FileUtils.APP, "ThirdGender");
		if (gender == -1) {
			gender = 2;
		}
		return gender;
	}

	public static boolean isThirdLoginValid() {
		boolean valid = false;
		if (!TextUtils.isEmpty(getThirdUid())) {
			valid = true;
		}

		return valid;
	}

	public static void clearThirdInfo() {
		setThirdUid("");
		setThirdPlatform("");
		setThirdName("");
		setThirdImage("");
		setThirdGender(2);
	}
}