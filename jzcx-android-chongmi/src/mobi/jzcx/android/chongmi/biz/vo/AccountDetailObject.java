package mobi.jzcx.android.chongmi.biz.vo;

import java.util.ArrayList;

import mobi.jzcx.android.core.mvc.BaseObject;

public class AccountDetailObject extends BaseObject {

	/**
	 * "Member":{"MemberId":16,"NickName":"糊涂","ICOUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/9/16/d9359057-102d-44c7-bf3b-0fb3c2578b6f.jpg"
	 * ,"Gender":"1","LocateAddress":null,"LastLocateAddress":null},"Pets":[{
	 * "PetId"
	 * :1,"AnimalSpecies":null,"CategoryId":66,"CategoryName":"波音达","Gender"
	 * :"0","Name":"图体验","Birthday":"2012-08-06","ICOUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/9/17/bf481543-033a-433c-ba2d-6a2fc9697d42.jpg"
	 * ,"Description":"不靠谱了婆婆偷偷我的","MemberId":16},{"PetId":2,"AnimalSpecies":
	 * null,"CategoryId":49,"CategoryName":"爱尔兰雪达犬","Gender":"1","Name":"毫不犹豫",
	 * "Birthday":"2015-12-09","ICOUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/10/10/3f711c4a-1015-4466-a42d-85380f0a4695.jpg"
	 * ,"Description":"沟通刘某图咯喔","MemberId":16}],"ImMember":{"uuid":
	 * "68b1fd4a-9e22-11e5-a01e-8196ffa03d39"
	 * ,"type":"user","created":1449630632724
	 * ,"modified":1449630632724,"username"
	 * :"13554996963","activated":true,"device_token"
	 * :"","nickname":"","password"
	 * :"D8578EDF8458CE06FBC5BB76A58C5CA4","MemberId"
	 * :16,"DBNullParameterList":null
	 */
	private static final long serialVersionUID = -3022578522913777552L;
	String MemberId;
	String NickName;
	String IcoUrl;
	String Gender;
	String Birthday;
	int FollowsCount;
	int FansCount;
	boolean IsFollowed;
	ArrayList<PetObject> pets;
	RongLianObject RLObj;
	ArrayList<String> photos;

	public ArrayList<String> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<String> photos) {
		this.photos = photos;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
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

	public boolean isIsFollowed() {
		return IsFollowed;
	}

	public void setIsFollowed(boolean isFollowed) {
		IsFollowed = isFollowed;
	}

	public ArrayList<PetObject> getPets() {
		return pets;
	}

	public void setPets(ArrayList<PetObject> pets) {
		this.pets = pets;
	}

	public RongLianObject getRLObj() {
		return RLObj;
	}

	public void setRLObj(RongLianObject rLObj) {
		RLObj = rLObj;
	}

}
