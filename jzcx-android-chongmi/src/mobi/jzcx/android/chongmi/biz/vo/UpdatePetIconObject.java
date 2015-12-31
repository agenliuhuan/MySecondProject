package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class UpdatePetIconObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1581392903123926843L;

	String petid;
	String imagepath;

	public String getPetid() {
		return petid;
	}

	public void setPetid(String petid) {
		this.petid = petid;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

}
