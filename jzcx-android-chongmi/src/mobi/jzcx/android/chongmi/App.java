package mobi.jzcx.android.chongmi;

import java.io.File;
import java.util.ArrayList;

import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.LngLatObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.log.CrashHandler;
import mobi.jzcx.android.core.mvc.BaseController;
import mobi.jzcx.android.core.mvc.utils.HandlerUtils.StaticHandler;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.utils.AndroidConfig;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;

public class App extends Application {
	private static final String TAG = App.class.getSimpleName();
	public static Context applicationContext;

	private static App mInstance;
	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;

	private ArrayList<Activity> mActivityList = new ArrayList<Activity>();
	private Activity mForegroundActivity;
	private StaticHandler mHandler = new StaticHandler();
	private LngLatObject lnglat = null;

	public static App getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		applicationContext = getApplicationContext();
		if (getCurProcessName(App.this).equals("mobi.jzcx.android.chongmi")) {
			super.onCreate();
			mInstance = this;
			init();
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		IMInitHelper.getInstance().init(applicationContext);

		CrashHandler.getInstance().init(this);

		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush

		AndroidConfig.init(applicationContext);
		Fresco.initialize(applicationContext);
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
		// initImageLoader();
		CoreModel.getInstance();

		if (LogUtils.ENABLE) {
			WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			Display display = windowManager.getDefaultDisplay();
			LogUtils.e(TAG, "display: " + display.getWidth() + "x" + display.getHeight());
		}

		// 创建App目录
		FileUtils.createDirectory(FileUtils.FAMILYSAFER);
		FileUtils.createDirectory(FileUtils.COVER);
		FileUtils.createDirectory(FileUtils.VOICE);
		FileUtils.createDirectory(FileUtils.PICTURE);

		// 百度地图初始化
		SDKInitializer.initialize(applicationContext);
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		initLocation();
		mLocationClient.start();

		// CCPAppManager.setContext(applicationContext);// IM初始化
		// ClientUser clientUser = new ClientUser("");
		// clientUser.setPassword("");
		// clientUser.setAppToken("");
		// clientUser.setLoginAuthType(ECInitParams.LoginAuthType.PASSWORD_AUTH);
		// CCPAppManager.setClientUser(clientUser);
		// SDKCoreHelper.init(App.getInstance().getApplicationContext(),
		// ECInitParams.LoginMode.FORCE_LOGIN);

	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000 * 60 * 5;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		mLocationClient.setLocOption(option);
	}

	public void exit() {
		MobclickAgent.onKillProcess(applicationContext);
		// SDKCoreHelper.logout();
		Fresco.shutDown();
		clearActivity();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private void initImageLoader() {
		// File cacheDir =
		// StorageUtils.getOwnCacheDirectory(getApplicationContext(),
		// FileUtils.COVER);
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(this).threadPoolSize(1)
		// // 线程池内加载的数量
		// .threadPriority(Thread.NORM_PRIORITY - 2).memoryCache(new
		// WeakMemoryCache())
		// // .denyCacheImageMultipleSizesInMemory()
		// .diskCacheFileNameGenerator(CCPAppManager.md5FileNameGenerator)
		// // 将保存的时候的URI名称用MD5 加密
		// .tasksProcessingOrder(QueueProcessingType.LIFO)
		// .diskCache(new UnlimitedDiscCache(cacheDir, null,
		// CCPAppManager.md5FileNameGenerator))// 自定义缓存路径
		// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		// // .writeDebugLogs() // Remove for release app
		// .build();// 开始构建
		// ImageLoader.getInstance().init(config);
	}

	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	public void addActivity(Activity activity) {
		if (activity != null && !mActivityList.contains(activity)) {
			mActivityList.add(activity);
		}
	}

	public void removeActivity(Activity activity) {
		if (activity != null) {
			mActivityList.remove(activity);
		}
	}

	public void clearActivity() {
		for (Activity activity : mActivityList) {
			activity.finish();
		}
		mActivityList.clear();
	}

	public void setForegroundActivity(Activity activity) {
		mForegroundActivity = activity;
	}

	public Activity getForegroundActivity() {
		return mForegroundActivity;
	}

	public Message obtainMessage() {
		return mHandler.obtainMessage();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			Log.i("BaiduLocationApiDem", sb.toString());
			lnglat = new LngLatObject();
			lnglat.setLat(String.valueOf(location.getLatitude()));
			lnglat.setLng(String.valueOf(location.getLongitude()));
			lnglat.setCity(location.getCity());
			if (CoreModel.getInstance().getMyself() != null) {
				BaseController.getInstance().sendMessage(YSMSG.REQ_UPDATE_LOCATION, 0, 0, lnglat);
			}
		}
	}

	public LngLatObject getLnglat() {
		return lnglat;
	}

}
