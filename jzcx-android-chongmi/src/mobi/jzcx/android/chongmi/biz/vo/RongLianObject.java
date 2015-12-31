package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class RongLianObject extends BaseObject {

	/**
	 * "uuid": "68b1fd4a-9e22-11e5-a01e-8196ffa03d39"
	 * ,"type":"user","created":1449630632724
	 * ,"modified":1449630632724,"username"
	 * :"13554996963","activated":true,"device_token"
	 * :"","nickname":"","password"
	 * :"D8578EDF8458CE06FBC5BB76A58C5CA4","MemberId"
	 * :16,"DBNullParameterList":null
	 */
	private static final long serialVersionUID = 8443054422811897493L;

	private String MemberId;
	private String uuid;
	private String username;
	private String password;
	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		MemberId = memberId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
