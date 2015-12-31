package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class PetObject extends BaseObject {

	/**
	 * "PetId":1,"AnimalSpecies":null,"CategoryId":66,"CategoryName":
	 * "波音达","Gender":"0","Name":"图体验","Birthday":"2012-08-06","ICOUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/9/17/bf481543-033a-433c-ba2d-6a2fc9697d42.jpg"
	 * ,"Description":"不靠谱了婆婆偷偷我的","MemberId":16
	 */
	private static final long serialVersionUID = -3380205937513098098L;

	String PetId;
	String CategoryId;
	String CategoryName;
	String Gender;
	String Name;
	String Birthday;
	String IcoUrl;
	String Description;
	String MemberId;

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public String getPetId() {
		return PetId;
	}

	public void setPetId(String petId) {
		PetId = petId;
	}

	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
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

	public String getIcoUrl() {
		return IcoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}
