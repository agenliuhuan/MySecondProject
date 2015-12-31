package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

/**
 * 忘记密码
 */
public class ResetPsdObject extends BaseObject {

	private static final long serialVersionUID = 5256303948867768142L;

	private String phone;
	private String VCode;
	private String NewPsd;
	private int smsid;

	public String getNewPsd() {
		return NewPsd;
	}

	public void setNewPsd(String newPsd) {
		NewPsd = newPsd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVCode() {
		return VCode;
	}

	public void setVCode(String vCode) {
		VCode = vCode;
	}

	public int getSmsid() {
		return smsid;
	}

	public void setSmsid(int smsid) {
		this.smsid = smsid;
	}

}
