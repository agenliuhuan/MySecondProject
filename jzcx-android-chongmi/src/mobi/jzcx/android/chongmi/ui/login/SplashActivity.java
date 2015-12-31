package mobi.jzcx.android.chongmi.ui.login;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.db.DatabaseManager;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.im.IMInitHelper;
import mobi.jzcx.android.chongmi.ui.common.BaseExActivity;
import mobi.jzcx.android.chongmi.ui.main.MainActivity;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.core.mvc.BaseController;
import mobi.jzcx.android.core.mvc.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

public class SplashActivity extends BaseExActivity {
	private static final int SHOW_TIME_MIN = 2000;// 最小显示时间
	private long mStartTime;// 开始时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mStartTime = System.currentTimeMillis();// 记录开始时间，
		new Thread(new Runnable() {
			@Override
			public void run() {
				initView();

			}
		}).start();

		mSetStatusBar = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(mActivity);

	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(mActivity);
	}

	private void initView() {
		// boolean guide = PreferencesUtils.getFieldBooleanValue(FileUtils.APP,
		// "loginguide");
		// boolean thirdLoginValid = PreferencesUtils.isThirdLoginValid();
		// phone = PreferencesUtils.getLoginPhone();
		//
		// if (!guide) {
		// // start guideactivity
		// } else {
		//
		// }
		// if (thirdLoginValid) {
		// // login third
		// } else
		String userToken = PreferencesUtils.getUserToken();
		if (!TextUtils.isEmpty(userToken)) {
			sendEmptyMessage(YSMSG.REQ_GETUSERINFO);
			if (App.getInstance().getLnglat() != null) {
				sendMessage(YSMSG.REQ_UPDATE_LOCATION, 0, 0, App.getInstance().getLnglat());
			}
		} else {
			long loadingTime = System.currentTimeMillis() - mStartTime;// 计算一下总共花费的时间
			if (loadingTime < SHOW_TIME_MIN) {// 如果比最小显示时间还短，就延时进入MainActivity，否则直接进入
				mHandler.postDelayed(goToLoginActivity, SHOW_TIME_MIN - loadingTime);
			} else {
				mHandler.post(goToLoginActivity);
			}
		}

	}
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.RESP_GETUSERINFO :
				if (App.getInstance().getLnglat() != null) {
					BaseController.getInstance().sendMessage(YSMSG.REQ_UPDATE_LOCATION, 0, 0,
							App.getInstance().getLnglat());
				}
				// "MemberId":16,"Phone":"13554996963","RealName":"","Birthday":"0001-01-01T00:00:00","Gender":"",
				// "NickName":"13554996963","ICOUrl":"","LastLoginTime":"2015-12-09T11:39:26.33",
				// "Token":"52d74c7df027d6e1932aa65c3227d55a","password":"D8578EDF8458CE06FBC5BB76A58C5CA4","username":"13554996963"
				if (msg.arg1 == 200) {
					if (msg.obj != null) {
						try {
							String result = (String) msg.obj;
							JSONObject jsonobj = new JSONObject(result);
							UserObject user = new UserObject();
							user.setMemberId(jsonobj.getString("MemberId"));
							user.setPhone(jsonobj.getString("Phone"));
							user.setBirthday(jsonobj.getString("Birthday"));
							user.setGender(jsonobj.getString("Gender"));
							user.setNickName(jsonobj.getString("NickName"));
							user.setIcoUrl(jsonobj.getString("IcoUrl"));
							String ImPsd = jsonobj.getString("password");
							user.setIMpassword(ImPsd);
							final String ImUserName = jsonobj.getString("username");
							user.setIMusername(ImUserName);

							DatabaseManager.getInstance().initHelper(user.getMemberId());
							IMInitHelper.getInstance().init(App.getInstance());
							// 调用sdk登陆方法登陆聊天服务器
							EMChatManager.getInstance().login(ImUserName, ImPsd, new EMCallBack() {

								@Override
								public void onSuccess() {

									// 登陆成功，保存用户名
									IMInitHelper.getInstance().setCurrentUserName(ImUserName);
									// 注册群组和联系人监听
									IMInitHelper.getInstance().registerGroupAndContactListener();

									EMGroupManager.getInstance().loadAllGroups();
									EMChatManager.getInstance().loadAllConversations();
									// IMInitHelper.getInstance().getUserProfileManager().
									IMInitHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

									LogUtils.i("IMInitHelper", "login onSuccess");
								}

								@Override
								public void onProgress(int progress, String status) {
								}

								@Override
								public void onError(final int code, final String message) {
									LogUtils.i("IMInitHelper", "login onError");
								}
							});

							CoreModel.getInstance().setMyself(user);
							long loadingTime = System.currentTimeMillis() - mStartTime;// 计算一下总共花费的时间
							if (loadingTime < SHOW_TIME_MIN) {// 如果比最小显示时间还短，就延时进入MainActivity，否则直接进入
								mHandler.postDelayed(goToMainActivity, SHOW_TIME_MIN - loadingTime);
							} else {
								mHandler.post(goToMainActivity);
							}
							// insertMyselfTocontact();
							// sendEmptyMessage(YSMSG.REQ_GETRONGLIANMEBER);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {
					long loadingTime = System.currentTimeMillis() - mStartTime;// 计算一下总共花费的时间
					if (loadingTime < SHOW_TIME_MIN) {// 如果比最小显示时间还短，就延时进入MainActivity，否则直接进入
						mHandler.postDelayed(goToLoginActivity, SHOW_TIME_MIN - loadingTime);
					} else {
						mHandler.post(goToLoginActivity);
					}
				}
				break;
			case YSMSG.RESP_OUTOFLINE : {
				long loadingTime = System.currentTimeMillis() - mStartTime;// 计算一下总共花费的时间
				if (loadingTime < SHOW_TIME_MIN) {// 如果比最小显示时间还短，就延时进入MainActivity，否则直接进入
					mHandler.postDelayed(goToLoginActivity, SHOW_TIME_MIN - loadingTime);
				} else {
					mHandler.post(goToLoginActivity);
				}
			}
				break;
			default :
				break;
		}
	}

	// 进入下一个Activity
	Runnable goToMainActivity = new Runnable() {
		@Override
		public void run() {
			MainActivity.startActivity(mActivity);
			mActivity.finish();
		}
	};

	// 进入loginActivity
	Runnable goToLoginActivity = new Runnable() {
		@Override
		public void run() {
			LoginActivity.startActivity(App.getInstance().getForegroundActivity(), PreferencesUtils.getLoginPhone());
			mActivity.finish();
		}
	};

	private static void insertMyselfTocontact() {

	}
}
