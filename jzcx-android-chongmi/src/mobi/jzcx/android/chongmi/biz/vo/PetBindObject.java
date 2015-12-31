package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class PetBindObject extends BaseObject {

	/**
	 * "PetId":2,"Gender":"1","Name":"毫不犹豫","Birthday":"2015-12-09 12:00:00",
	 * "Description":"沟通刘某图咯喔","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/10/10/3f711c4a-1015-4466-a42d-85380f0a4695.jpg"
	 * ,"CreateTime":"2015-12-10 10:45:47","CategoryId":49,"MemberId":16,
	 * "IsBind":false,"CategoryName":"爱尔兰雪达犬","PetCollarId":0
	 */
	private static final long serialVersionUID = -3251999032355513577L;

	private String PetId;
	private String Mac;
	private String Gender;
	private String Name;
	private String Birthday;
	private String Description;
	private String IcoUrl;
	private String CreateTime;
	private String CategoryId;
	private String MemberId;
	private boolean IsBind;
	private String CategoryName;

	private String PetCollarId;

	public String getPetId() {
		return PetId;
	}

	public void setPetId(String petId) {
		PetId = petId;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getIcoUrl() {
		return IcoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public boolean isIsBind() {
		return IsBind;
	}

	public void setIsBind(boolean isBind) {
		IsBind = isBind;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public String getPetCollarId() {
		return PetCollarId;
	}

	public void setPetCollarId(String petCollarId) {
		PetCollarId = petCollarId;
	}

	public String getMac() {
		return Mac;
	}

	public void setMac(String mac) {
		Mac = mac;
	}

}
