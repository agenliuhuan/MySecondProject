package mobi.jzcx.android.chongmi.biz.vo;

import java.util.ArrayList;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GroupMeberObject extends BaseObject {

	/**
	 * "ImToken":"59f64724665d75bbe14bf7d56e9cc3fd","Phone":"18175721116",
	 * "ImMemberId":"bd6299fe560011e58b68ac853d9d52fd","ICOUrl":
	 * "http:\/\/assets.cm.mc2015.co\/\/CM.UserCenter\/2015\/9\/1\/20\/8fa1878d-3cae-4cf3-9011-9e78e2313317.jpeg","RealName":null,"NickName":"
	 * 神 猪 " } ]
	 */
	private static final long serialVersionUID = 5434258087037135495L;

	String Phone;
	String ImMemberId;
	String MemberId;
	String ImToken;
	String IcoUrl;
	String Gender;
	String NickName;
	String RealName;
	LngLatObject lnglat;
	ArrayList<PetObject> petList;

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public LngLatObject getLnglat() {
		return lnglat;
	}

	public void setLnglat(LngLatObject lnglat) {
		this.lnglat = lnglat;
	}

	public ArrayList<PetObject> getPetList() {
		return petList;
	}

	public void setPetList(ArrayList<PetObject> petList) {
		this.petList = petList;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getImMemberId() {
		return ImMemberId;
	}

	public void setImMemberId(String imMemberId) {
		ImMemberId = imMemberId;
	}

	public String getImToken() {
		return ImToken;
	}

	public void setImToken(String imToken) {
		ImToken = imToken;
	}

	public String getIcoUrl() {
		return IcoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

}
