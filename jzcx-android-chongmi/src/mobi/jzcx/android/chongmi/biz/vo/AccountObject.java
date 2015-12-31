package mobi.jzcx.android.chongmi.biz.vo;

import java.util.ArrayList;

import mobi.jzcx.android.core.mvc.BaseObject;

public class AccountObject extends BaseObject {

	/**
	 * {"MemberId":28,"NickName":"黑山老妖","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/23/10/3cf155d4-626c-4995-8279-5b737cfa6e71.jpg"
	 * ,"Birthday":"1995-01-01","Gender":"1","LocateAddress":{"Lng":114.109978,
	 * "Lat":22.568471},"LastLocateAddress":"广东省 深圳市","Pets":[{"PetId":39,
	 * "CategoryId"
	 * :12,"CategoryName":"德文卷毛猫","Gender":"0","Name":"250","Birthday"
	 * :"2008-12-21","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/15/f93dcd0f-4bf6-41e3-83ba-82dd8c9d68ed.jpg"
	 * ,"Description":"","MemberId":28},{"PetId":38,"CategoryId":13,
	 * "CategoryName"
	 * :"东方猫","Gender":"0","Name":"黑底","Birthday":"2002-12-21","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/15/a6348af3-ae91-4e0b-800f-f4efc1df5af4.jpg"
	 * ,"Description":"","MemberId":28},{"PetId":30,"CategoryId":10,
	 * "CategoryName"
	 * :"波米拉猫","Gender":"0","Name":"喵喵","Birthday":"2000-12-21","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/11/1d379bd4-fe73-4d28-a4a6-8481bfc15596.jpg"
	 * ,"Description":"","MemberId":28},{"PetId":21,"CategoryId":49,
	 * "CategoryName"
	 * :"爱尔兰雪达犬","Gender":"0","Name":"大咪咪","Birthday":"2006-12-21","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/10/36d2f095-dad1-45bc-bfd6-36e976b8a9da.jpg"
	 * ,"Description":"恩恩怨怨","MemberId":28}],"FollowsCount":11,"FansCount":9,
	 * "IsFollowed":true}
	 */
	private static final long serialVersionUID = 5725664954206565536L;

	String MemberId;
	String NickName;
	String IcoUrl;
	String LastLocateAddress;
	String Gender;
	LngLatObject LocateAddress;
	ArrayList<PetObject> petList;

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public LngLatObject getLocateAddress() {
		return LocateAddress;
	}

	public void setLocateAddress(LngLatObject locateAddress) {
		LocateAddress = locateAddress;
	}

	public ArrayList<PetObject> getPetList() {
		return petList;
	}

	public void setPetList(ArrayList<PetObject> petList) {
		this.petList = petList;
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

	public String getLastLocateAddress() {
		return LastLocateAddress;
	}

	public void setLastLocateAddress(String lastLocateAddress) {
		LastLocateAddress = lastLocateAddress;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

}
