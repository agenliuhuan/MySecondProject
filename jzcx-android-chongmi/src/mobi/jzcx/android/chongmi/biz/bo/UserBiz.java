package mobi.jzcx.android.chongmi.biz.bo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ChangePhoneObject;
import mobi.jzcx.android.chongmi.biz.vo.LoginObject;
import mobi.jzcx.android.chongmi.biz.vo.RegisteObject;
import mobi.jzcx.android.chongmi.biz.vo.ResetPsdObject;
import mobi.jzcx.android.chongmi.biz.vo.UserObject;
import mobi.jzcx.android.chongmi.client.ApiClient;
import mobi.jzcx.android.chongmi.client.UserClient;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.MD5;
import mobi.jzcx.android.chongmi.utils.PreferencesUtils;
import mobi.jzcx.android.core.mvc.BaseBiz;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.http.handler.AsyncHttpResponseHandler;
import mobi.jzcx.android.core.net.http.request.RequestParams;
import android.os.Message;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 用户相关请求
 */
public class UserBiz extends BaseBiz {

	public static void userLogin(final LoginObject loginObj) {
		String url = ApiClient.API_URL + UserClient.API_Login;
		RequestParams params = new RequestParams();
		params.put("phone", loginObj.getUsername());
		params.put("deviceName", loginObj.getDeviceName());
		params.put("password", MD5.md5(loginObj.getUserpsd()).toUpperCase());
		params.put("deviceId", loginObj.getDeviceId());
		params.put("deviceType", "2");
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_LOGIN);
				saveLogininfoToPreference(loginObj);
				JPushInterface.init(App.getInstance());
				JPushInterface.setAlias(App.getInstance(), CommonUtils.getDeviceId(App.getInstance()),
						new TagAliasCallback() {
							@Override
							public void gotResult(int resultCode, String arg1, Set<String> arg2) {
								if (resultCode == 0) {
									LogUtils.i("jpush success", arg1);
								}
							}
						});

			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_LOGIN;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	public static void useregisteLogin(final LoginObject loginObj) {
		String url = ApiClient.API_URL + UserClient.API_Login;
		RequestParams params = new RequestParams();
		params.put("phone", loginObj.getUsername());
		params.put("deviceName", loginObj.getDeviceName());
		params.put("password", MD5.md5(loginObj.getUserpsd()).toUpperCase());
		params.put("deviceId", loginObj.getDeviceId());
		params.put("deviceType", "2");
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_REGISTE_LOGIN);
				saveLogininfoToPreference(loginObj);
				JPushInterface.init(App.getInstance());
				JPushInterface.setAlias(App.getInstance(), CommonUtils.getDeviceId(App.getInstance()),
						new TagAliasCallback() {
							@Override
							public void gotResult(int resultCode, String arg1, Set<String> arg2) {
								if (resultCode == 0) {
									LogUtils.i("jpush success", arg1);
								}
							}
						});
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_REGISTE_LOGIN;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	protected static void saveLogininfoToPreference(LoginObject loginObj) {
		PreferencesUtils.setLoginPhone(loginObj.getUsername());
		// PreferencesUtils.setLoginPwd(loginObj.getUserpsd());
	}

	public static void getRegisterSms(String phone) {
		String url = ApiClient.API_URL + UserClient.API_Register_SendSMS;
		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("deviceId", CommonUtils.getDeviceId(App.getInstance()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_REGISTER_SMS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_REGISTER_SMS;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	public static void userRegister(RegisteObject registerObj) {
		String url = ApiClient.API_URL + UserClient.API_Register;
		RequestParams params = new RequestParams();
		params.put("phone", registerObj.getPhone());
		params.put("deviceId", CommonUtils.getDeviceId(App.getInstance()));
		params.put("password", MD5.md5(registerObj.getPassword()).toUpperCase());
		params.put("smsCode", registerObj.getCode());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_REGISTER);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_REGISTER;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	public static void fogotSendCode(String phone) {
		String url = ApiClient.API_URL + UserClient.API_Fogot_SendCode;
		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("deviceId", CommonUtils.getDeviceId(App.getInstance()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_FOGOTPSD_VCODE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_FOGOTPSD_VCODE;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	private static void fogotPsd(ResetPsdObject resetPsdObj) {
		String url = ApiClient.API_URL + UserClient.API_Fogot;
		RequestParams params = new RequestParams();
		params.put("phone", resetPsdObj.getPhone());
		params.put("password", MD5.md5(resetPsdObj.getNewPsd()).toUpperCase());
		params.put("smsCode", resetPsdObj.getVCode());
		params.put("deviceId", CommonUtils.getDeviceId(App.getInstance()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_FOGOTPSD);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_FOGOTPSD;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getDefaultHeaders(), params, responseHandler);
	}

	private static void getUserInfo(final String token) {
		String url = ApiClient.API_URL + UserClient.API_GENUSERINFO;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_GETUSERINFO);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_GETUSERINFO;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), null, responseHandler);
	}

	private static void modifyUserInfo(String token, UserObject user) {
		String url = ApiClient.API_URL + UserClient.API_Update_Info;
		RequestParams params = new RequestParams();
		String nickName = user.getNickName();
		if (!CommonTextUtils.isEmpty(nickName)) {
			params.put("nickName", nickName);
		}
		String gender = user.getGender();
		if (!CommonTextUtils.isEmpty(gender)) {
			params.put("gender", gender);
		}
		String birthday = user.getBirthday();
		if (!CommonTextUtils.isEmpty(birthday)) {
			params.put("birthday", birthday);
		}
		String userAvatar = user.getIcoUrl();
		if (!CommonTextUtils.isEmpty(userAvatar)) {
			try {
				File file = new File(userAvatar);
				params.put("profile_picture", file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_UPDATE_INFO);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_UPDATE_INFO;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	private static void resetPsd(String token, UserObject user) {
		String url = ApiClient.API_URL + UserClient.API_ResetPsd;
		RequestParams params = new RequestParams();
		params.put("oldPassword", MD5.md5(user.getOldPsd()).toUpperCase());
		params.put("newPassword", MD5.md5(user.getNewPsd()).toUpperCase());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_RESETPSD);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_RESETPSD;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	public static void logout(String token) {
		String url = ApiClient.API_URL + UserClient.API_Logout;
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_LOGOUT);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_LOGOUT;
				message.arg1 = 0;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), null, responseHandler);
	}

	private static void changePhone(String socialToken, ChangePhoneObject obj) {
		String url = ApiClient.API_URL + UserClient.API_ChangePhone;
		RequestParams params = new RequestParams();
		params.put("newPhone", obj.getPhone());
		params.put("deviceId", CommonUtils.getDeviceId(App.getInstance()));
		params.put("smsCode", obj.getCode());
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_CHANGEPHONE);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_CHANGEPHONE);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void changePhoneSms(String socialToken, String phone) {
		String url = ApiClient.API_URL + UserClient.API_ChangePhoneSms;
		RequestParams params = new RequestParams();
		params.put("newPhone", phone);
		params.put("deviceId", CommonUtils.getDeviceId(App.getInstance()));
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				ApiClient.sendSuccessMsg(response, YSMSG.RESP_CHANGEPHONESMS);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_CHANGEPHONESMS);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(socialToken), params, responseHandler);
	}

	private static void getCategories(String token, String id) {
		String url = ApiClient.API_URL + UserClient.GETPETCATEGORIES;
		RequestParams params = new RequestParams();
		params.put("parentId", id);
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(String response) {
				Message message = App.getInstance().obtainMessage();
				message.what = YSMSG.RESP_GETPETCATEGORIES;
				message.arg1 = 200;
				message.obj = response;
				CoreModel.getInstance().notifyOutboxHandlers(message);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				ApiClient.sendFiledMsg(YSMSG.RESP_GETPETCATEGORIES);
			}

			@Override
			public void onFinish() {

			}
		};
		ApiClient.post(url, ApiClient.getUserHeaders(token), params, responseHandler);
	}

	public static void handleMessage(Message msg) {
		switch (msg.what) {
			case YSMSG.REQ_LOGIN : {
				if (msg.obj != null) {
					LoginObject loginObj = (LoginObject) msg.obj;
					userLogin(loginObj);
				}
			}
				break;
			case YSMSG.REQ_REGISTE_LOGIN : {
				if (msg.obj != null) {
					LoginObject loginObj = (LoginObject) msg.obj;
					useregisteLogin(loginObj);
				}
			}
				break;

			case YSMSG.REQ_REGISTER : {
				if (msg.obj != null) {
					RegisteObject registerObj = (RegisteObject) msg.obj;
					userRegister(registerObj);
				}
			}
				break;
			case YSMSG.REQ_REGISTER_SMS : {
				if (msg.obj != null) {
					String phone = (String) msg.obj;
					getRegisterSms(phone);
				}
			}
				break;
			case YSMSG.REQ_FOGOTPSD_VCODE : {
				if (msg.obj != null) {
					String phone = (String) msg.obj;
					fogotSendCode(phone);
				}
			}
				break;
			case YSMSG.REQ_FOGOTPSD : {
				if (msg.obj != null) {
					ResetPsdObject rpObj = (ResetPsdObject) msg.obj;
					fogotPsd(rpObj);
				}
			}
				break;
			case YSMSG.REQ_GETUSERINFO : {
				String userToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(userToken)) {
					getUserInfo(userToken);
				}
			}
				break;
			case YSMSG.REQ_UPDATE_INFO : {
				String userToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(userToken)) {
					if (msg.obj != null) {
						UserObject user = (UserObject) msg.obj;
						modifyUserInfo(userToken, user);
					}
				}
			}
				break;
			case YSMSG.REQ_RESETPSD : {
				String userToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(userToken)) {
					if (msg.obj != null) {
						UserObject user = (UserObject) msg.obj;
						resetPsd(userToken, user);
					}
				}
			}
				break;
			case YSMSG.REQ_LOGOUT : {
				String userToken = PreferencesUtils.getUserToken();
				if (!CommonTextUtils.isEmpty(userToken)) {
					logout(userToken);
				}
			}
				break;
			case YSMSG.REQ_CHANGEPHONE : {
				if (msg.obj != null) {
					ChangePhoneObject obj = (ChangePhoneObject) msg.obj;
					String userToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(userToken) && obj != null) {
						changePhone(userToken, obj);
					}
				}
			}
				break;
			case YSMSG.REQ_CHANGEPHONESMS : {
				if (msg.obj != null) {
					String phone = String.valueOf(msg.obj);
					String userToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(userToken) && !CommonTextUtils.isEmpty(phone)) {
						changePhoneSms(userToken, phone);
					}
				}
			}
				break;
			case YSMSG.REQ_GETPETCATEGORIES : {
				if (msg.obj != null) {
					String parentId = String.valueOf(msg.obj);
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getCategories(socialToken, parentId);
					}
				} else {
					String socialToken = PreferencesUtils.getUserToken();
					if (!CommonTextUtils.isEmpty(socialToken)) {
						getCategories(socialToken, "0");
					}
				}

			}
				break;
			default :
				break;
		}
	}

}
