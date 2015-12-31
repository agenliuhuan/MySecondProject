package mobi.jzcx.android.chongmi.client;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.YSMSG;
import mobi.jzcx.android.chongmi.biz.vo.ResponseObject;
import mobi.jzcx.android.chongmi.mode.CoreModel;
import mobi.jzcx.android.chongmi.sdk.http.HttpManager;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.core.net.http.handler.AsyncHttpResponseHandler;
import mobi.jzcx.android.core.net.http.request.RequestParams;
import mobi.jzcx.android.core.net.ojm.OJMFactory;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.json.JSONException;

import android.os.Message;

/**
 * 服务端请求基类
 */
public class ApiClient {
	/*
	 * 内网
	 */
	// public static final String API_URL = "http://app.cm.comi2015.com/";
	/*
	 * 外网
	 */
	public static final String API_URL = "http://app.chongmi.com/";
	// public static final String API_URL = "http://192.168.200.64:8090/";

	public static void post(String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		HttpManager.post(url, headers, params, null, responseHandler);
	}

	public static void get(String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		HttpManager.get(url, headers, params, responseHandler);
	}

	public static Header[] getUserHeaders(final String token) {
		Header tokenheader = new Header() {

			@Override
			public String getValue() {
				return token;
			}

			@Override
			public String getName() {
				return "token";
			}

			@Override
			public HeaderElement[] getElements() throws ParseException {
				return null;
			}
		};
		Header deviceidHeader = new Header() {

			@Override
			public String getValue() {
				return CommonUtils.getDeviceId(App.getInstance().getApplicationContext());
			}

			@Override
			public String getName() {
				return "deviceId";
			}

			@Override
			public HeaderElement[] getElements() throws ParseException {
				return null;
			}
		};
		Header defaultheader = new Header() {

			@Override
			public String getValue() {
				return "XMLHttpRequest";
			}

			@Override
			public String getName() {
				return "X-Requested-With";
			}

			@Override
			public HeaderElement[] getElements() throws ParseException {
				return null;
			}
		};
		Header[] headers = new Header[3];
		headers[0] = tokenheader;
		headers[1] = deviceidHeader;
		headers[2] = defaultheader;
		return headers;
	}

	public static Header[] getDefaultHeaders() {
		Header defaultheader = new Header() {

			@Override
			public String getValue() {
				return "XMLHttpRequest";
			}

			@Override
			public String getName() {
				return "X-Requested-With";
			}

			@Override
			public HeaderElement[] getElements() throws ParseException {
				return null;
			}
		};
		Header[] headers = new Header[1];
		headers[0] = defaultheader;
		return headers;
	}

	public static void sendSuccessMsg(String response, int msgId) {
		try {
			Message message = App.getInstance().obtainMessage();
			message.what = msgId;
			ResponseObject responseObj = OJMFactory.createOJM().fromJson(response, ResponseObject.class);
			if (responseObj.getErrorCode() == 0) {
				message.arg1 = 200;
				message.obj = responseObj.getResult();
			} else {
				message.arg1 = responseObj.getErrorCode();
				message.obj = responseObj.getErrorMsg();
				if (message.arg1 == 401) {
					message.what = YSMSG.RESP_OUTOFLINE;
				}
			}
			CoreModel.getInstance().notifyOutboxHandlers(message);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void sendFiledMsg(int msgId) {
		Message message = App.getInstance().obtainMessage();
		message.what = msgId;
		message.arg1 = -200;
		message.obj = App.getInstance().getString(R.string.network_error_text);
		CoreModel.getInstance().notifyOutboxHandlers(message);
	}

}
