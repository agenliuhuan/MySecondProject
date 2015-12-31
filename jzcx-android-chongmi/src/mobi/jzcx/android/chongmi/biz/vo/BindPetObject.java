package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class BindPetObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6524289054677797964L;

	String petId;
	String petCollarId;
	public String getPetId() {
		return petId;
	}
	public void setPetId(String petId) {
		this.petId = petId;
	}
	public String getPetCollarId() {
		return petCollarId;
	}
	public void setPetCollarId(String petCollarId) {
		this.petCollarId = petCollarId;
	}

}
