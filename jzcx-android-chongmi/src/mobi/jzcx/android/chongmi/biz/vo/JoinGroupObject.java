package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class JoinGroupObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5071958078159312018L;
	String actId;
	String reason;

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
