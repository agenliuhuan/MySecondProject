package mobi.jzcx.android.chongmi.sdk.http;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import mobi.jzcx.android.chongmi.App;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.net.http.client.AsyncHttpClient;
import mobi.jzcx.android.core.net.http.handler.AsyncHttpResponseHandler;
import mobi.jzcx.android.core.net.http.request.RequestParams;

import org.apache.http.Header;

public class HttpManager {

	public static void get(String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		try {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.get(App.getInstance().getApplicationContext(), url, headers, params, responseHandler);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		try {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.put(App.getInstance().getApplicationContext(), url, params, responseHandler);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	public static void post(String url, Header[] headers, RequestParams params, String contentType, AsyncHttpResponseHandler responseHandler) {
		try {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.post(App.getInstance().getApplicationContext(), url, headers, params, contentType, responseHandler);
			if (params != null) {
				LogUtils.d("response", "params:\n" + params.toString());
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

}
