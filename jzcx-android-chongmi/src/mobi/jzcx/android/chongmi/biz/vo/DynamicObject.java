package mobi.jzcx.android.chongmi.biz.vo;

import java.util.ArrayList;

import mobi.jzcx.android.core.mvc.BaseObject;

public class DynamicObject extends BaseObject {

	/**
	 * {"MicroblogId":7,"Member":{"MemberId":16,"NickName":"糊涂","ICOUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/9/16/d9359057-102d-44c7-bf3b-0fb3c2578b6f.jpg"
	 * ,"Gender":"1","LocateAddress":null,"LastLocateAddress":null},"Text":
	 * "hello","Photos":[
	 * "http://assets.cm.mc2015.co//CM.SNS/2015/12/7/9/b9b40da8-9021-494b-93f5-cbedaf3c7da0.jpg"
	 * ],"Pet":{"PetId":1,"AnimalSpecies":null,"CategoryId":66,"CategoryName":
	 * "波音达","Gender":"0","Name":"图体验","Birthday":"2012-08-06","ICOUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/9/17/bf481543-033a-433c-ba2d-6a2fc9697d42.jpg"
	 * ,"Description":"不靠谱了婆婆偷偷我的","MemberId":16},"LocateAddress":{"Lng":
	 * 22.568471,"Lat":114.109978},"LikeCount":0,"CommentCount":0,"LookCount":0,
	 * "Timestamp"
	 * :null,"CreateTime":"2015/12/7 17:18:55","IsLiked":false,"IsFollowed"
	 * :false ,"LikeMembers":null},
	 */
	private static final long serialVersionUID = 8156484737414883265L;

	String MicroblogId;
	AccountDetailObject accountObj;
	String Text;
	ArrayList<String> petPhotos;
	PetObject pet;
	String LikeCount;
	String CommentCount;
	String LookCount;
	String CreateTime;
	boolean IsLiked;
	boolean IsFollowed;
	ArrayList<AccountObject> lMembers;
	LngLatObject adressObject;
	String LastLocateAddress;

	public String getLastLocateAddress() {
		return LastLocateAddress;
	}
	public void setLastLocateAddress(String lastLocateAddress) {
		LastLocateAddress = lastLocateAddress;
	}
	public LngLatObject getAdressObject() {
		return adressObject;
	}
	public void setAdressObject(LngLatObject adressObject) {
		this.adressObject = adressObject;
	}
	public String getMicroblogId() {
		return MicroblogId;
	}
	public void setMicroblogId(String microblogId) {
		MicroblogId = microblogId;
	}

	public AccountDetailObject getAccountObj() {
		return accountObj;
	}
	public void setAccountObj(AccountDetailObject accountObj) {
		this.accountObj = accountObj;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public ArrayList<String> getPetPhotos() {
		return petPhotos;
	}
	public void setPetPhotos(ArrayList<String> petPhotos) {
		this.petPhotos = petPhotos;
	}
	public PetObject getPet() {
		return pet;
	}
	public void setPet(PetObject pet) {
		this.pet = pet;
	}
	public String getLikeCount() {
		return LikeCount;
	}
	public void setLikeCount(String likeCount) {
		LikeCount = likeCount;
	}
	public String getCommentCount() {
		return CommentCount;
	}
	public void setCommentCount(String commentCount) {
		CommentCount = commentCount;
	}
	public String getLookCount() {
		return LookCount;
	}
	public void setLookCount(String lookCount) {
		LookCount = lookCount;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public boolean isIsLiked() {
		return IsLiked;
	}
	public void setIsLiked(boolean isLiked) {
		IsLiked = isLiked;
	}
	public boolean isIsFollowed() {
		return IsFollowed;
	}
	public void setIsFollowed(boolean isFollowed) {
		IsFollowed = isFollowed;
	}
	public ArrayList<AccountObject> getlMembers() {
		return lMembers;
	}
	public void setlMembers(ArrayList<AccountObject> lMembers) {
		this.lMembers = lMembers;
	}

}
