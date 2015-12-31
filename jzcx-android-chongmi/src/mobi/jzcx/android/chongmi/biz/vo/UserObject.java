package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

/**
 * 用户信息
 */
public class UserObject extends BaseObject {
	private static final long serialVersionUID = -1171216497280013178L;

	private String NickName;

	private String IcoUrl;

	private int msgNumber;

	private String Gender;

	private String Birthday;

	private String Phone;

	private String IMpassword;
	private String IMusername;

	private String newPsd;
	private String oldPsd;

	private String MemberId;

	private String thirdUid;

	private int FollowsCount;

	private int FansCount;

	public String getThirdUid() {
		return thirdUid;
	}

	public void setThirdUid(String thirdUid) {
		this.thirdUid = thirdUid;
	}

	public int getMsgNumber() {
		return msgNumber;
	}

	public void setMsgNumber(int msgNumber) {
		this.msgNumber = msgNumber;
	}

	public String getNewPsd() {
		return newPsd;
	}

	public void setNewPsd(String newPsd) {
		this.newPsd = newPsd;
	}

	public String getOldPsd() {
		return oldPsd;
	}

	public void setOldPsd(String oldPsd) {
		this.oldPsd = oldPsd;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getIcoUrl() {
		return IcoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public int getFollowsCount() {
		return FollowsCount;
	}

	public void setFollowsCount(int followsCount) {
		FollowsCount = followsCount;
	}

	public int getFansCount() {
		return FansCount;
	}

	public void setFansCount(int fansCount) {
		FansCount = fansCount;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getIMpassword() {
		return IMpassword;
	}

	public void setIMpassword(String iMpassword) {
		IMpassword = iMpassword;
	}

	public String getIMusername() {
		return IMusername;
	}

	public void setIMusername(String iMusername) {
		IMusername = iMusername;
	}

}
