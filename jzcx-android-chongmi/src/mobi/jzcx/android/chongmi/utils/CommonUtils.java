package mobi.jzcx.android.chongmi.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * [公共工具类，与Android API相关的辅助类]
 * 
 * 
 **/
public class CommonUtils {

	private static final String tag = CommonUtils.class.getSimpleName();

	/** 网络类型 **/
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	public static String getPath(Uri uri) {
		String[] proj = {MediaStore.Images.Media.DATA};
		ContentResolver cr = App.getInstance().getContentResolver();
		Cursor cursor = cr.query(uri, proj, null, null, null);
		cursor.moveToFirst();
		int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		return cursor.getString(actual_image_column_index);

	}

	public static boolean inRangeOfView(View view, MotionEvent ev) {
		int[] location = new int[2];
//		view.getLocationInWindow(location);
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
			return false;
		}
		return true;
	}

	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 根据key获取config.properties里面的值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getProperty(Context context, String key) {
		try {
			Properties props = new Properties();
			InputStream input = context.getAssets().open("config.properties");
			if (input != null) {
				props.load(input);
				return props.getProperty(key);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!TextUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 判断SDCard是否存在,并可写
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		String flag = Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(flag)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	public static String getAvatarSize(Context context) {
		return "?width=90&height=90";
	}

	public static String getImageSize(Context context) {
		return "?width=" + getScreenWidth(context) + "&height=" + getScreenWidth(context);
	}

	/**
	 * 获取屏幕显示信息对象
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm;
	}

	/**
	 * dp转pixel
	 */
	public static float dpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return dp * (metrics.densityDpi / 160f);
	}

	/**
	 * pixel转dp
	 */
	public static float pixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return px / (metrics.densityDpi / 160f);
	}

	/**
	 * 短信分享
	 * 
	 * @param mContext
	 * @param smstext
	 *            短信分享内容
	 * @return
	 */
	public static Boolean sendSms(Context mContext, String smstext) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
		mIntent.putExtra("sms_body", smstext);
		mContext.startActivity(mIntent);
		return null;
	}

	/**
	 * 邮件分享
	 * 
	 * @param mContext
	 * @param title
	 *            邮件的标题
	 * @param text
	 *            邮件的内容
	 * @return
	 */
	public static void sendMail(Context mContext, String title, String text) {
		// 调用系统发邮件
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// 设置文本格式
		emailIntent.setType("text/plain");
		// 设置对方邮件地址
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		// 设置标题内容
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		// 设置邮件文本内容
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		mContext.startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param activity
	 */
	public static void hideKeyboard(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null && imm.isActive()) {
				imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			}
		}
	}

	/**
	 * 显示软键盘
	 * 
	 * @param activity
	 */
	public static void showKeyboard(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (!imm.isActive()) {
				imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
			}
		}
	}

	/**
	 * 是否横屏
	 * 
	 * @param context
	 * @return true为横屏，false为竖屏
	 */
	public static boolean isLandscape(Context context) {
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是平板 这个方法是从 Google I/O App for Android 的源码里找来的，非常准确。
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static String getDeviceName() {
		return "myphone";
	}

	public static String getDeviceId(Context context) {
		String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return imei;
	}

	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}

	/**
	 * 得到周一
	 * 
	 * @return yyyy-MM-dd
	 */
	public static Date getMondayOfThisWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		return c.getTime();
	}

	/**
	 * 得到周日
	 * 
	 * @return yyyy-MM-dd
	 */
	public static Date getSundayOfThisWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 7);
		return c.getTime();
	}

	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static Date getDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getCreateDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		int totalHeight = 0;
		View view = null;
		int count = listAdapter.getCount();
		// if (count > 6) {
		// count = 6;
		// }
		for (int i = 0; i < count; i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0) {
				view.setLayoutParams(new AbsListView.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));
			}
			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		int totalHeight = 0;
		View view = null;
		int count = listAdapter.getCount();
		// if (count > 6) {
		// count = 6;
		// }
		for (int i = 0; i < count; i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0) {
				view.setLayoutParams(new AbsListView.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));
			}
			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 得到amr的时长
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static long getAmrDuration(File file) throws IOException {
		long duration = -1;
		int[] packedSize = {12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0};
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();// 文件的长度
			int pos = 6;// 设置初始位置
			int frameCount = 0;// 初始帧数
			int packedPos = -1;
			// ///////////////////////////////////////////////////
			byte[] datas = new byte[1];// 初始数据值
			while (pos <= length) {
				randomAccessFile.seek(pos);
				if (randomAccessFile.read(datas, 0, 1) != 1) {
					duration = length > 0 ? ((length - 6) / 650) : 0;
					break;
				}
				packedPos = (datas[0] >> 3) & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}
			// ///////////////////////////////////////////////////
			duration += frameCount * 20;// 帧数*20
		} finally {
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		}
		return duration;
	}

	public static boolean isTouchInItem(View view, int x, int y) {
		if (view == null) {
			return false;
		}

		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int viewx = location[0];
		int viewy = location[1];
		if (x < viewx || x > viewx + view.getWidth()) {
			return false;
		}

		if (y < viewy || y > viewy + view.getHeight()) {
			return false;
		}
		return true;
	}

	/**
	 * 获取现在时间
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 两个时间之间的天数
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		if (date != null) {
			long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
			return Math.abs(day);
		}
		return 0;
	}

	public static String getPetAge(Context context, String age) {
		long day = getDays(age, getStringDate());
		long year = day / 365;
		long month = (day - 365L * year) / 30;
		long nowday = day % 30 + 1;
		if (year != 0) {
			return year + context.getString(R.string.sui);
		}
		if (month != 0) {
			return month + context.getString(R.string.ge) + context.getString(R.string.yue);
		}
		if (year == 0 && month == 0) {
			return nowday + context.getString(R.string.tian);
		}
		return "";
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public static String getTimeSamp(Context context, String time) {
		Date date = getCreateDate(time);
		Date now = new Date();
		Calendar datecalender = Calendar.getInstance();
		datecalender.setTime(date);
		Calendar nowcalender = Calendar.getInstance();
		nowcalender.setTime(now);
		int dateyear = datecalender.get(Calendar.YEAR);
		int dateday = datecalender.get(Calendar.DAY_OF_YEAR);
		int datemonth = datecalender.get(Calendar.MONTH) + 1;
		int datehour = datecalender.get(Calendar.HOUR_OF_DAY);
		int dateminute = datecalender.get(Calendar.MINUTE);
		long samptime = nowcalender.getTimeInMillis() - datecalender.getTimeInMillis();
		int nowyear = nowcalender.get(Calendar.YEAR);
		int nowday = nowcalender.get(Calendar.DAY_OF_YEAR);
		int nowhour = nowcalender.get(Calendar.HOUR_OF_DAY);
		String pattern = "00";
		java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
		if (nowyear == dateyear) {
			if (nowday - dateday == 1) {
				return context.getString(R.string.yestearday) + " " + df.format(datehour) + ":" + df.format(dateminute);
			} else if (nowday == dateday) {
				if (samptime < 60 * 60 * 1000) {
					int minutesamp = (int) (samptime / 60 / 1000);
					if (minutesamp == 0) {
						minutesamp = 1;
					}
					return minutesamp + context.getString(R.string.minite);
				} else {
					int hoursamp = nowhour - datehour;
					if (hoursamp == 0) {
						hoursamp = 1;
					}
					return hoursamp + context.getString(R.string.hour);
				}
			} else {
				return df.format(datemonth) + "-" + df.format(datecalender.get(Calendar.DAY_OF_MONTH)) + " "
						+ df.format(datehour) + ":" + df.format(dateminute);
			}
		} else {
			return dateyear + "-" + df.format(datemonth) + "-" + df.format(datecalender.get(Calendar.DAY_OF_MONTH))
					+ " " + df.format(datehour) + ":" + df.format(dateminute);
		}
	}
}
