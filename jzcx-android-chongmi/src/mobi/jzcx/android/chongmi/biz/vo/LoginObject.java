package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

/**
 * 登录
 */
public class LoginObject extends BaseObject {

	private static final long serialVersionUID = -6843890748812857480L;

	private String username;
	private String userpsd;
	private String deviceName;
	private String deviceId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpsd() {
		return userpsd;
	}

	public void setUserpsd(String userpsd) {
		this.userpsd = userpsd;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
