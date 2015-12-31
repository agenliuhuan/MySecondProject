package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;
import mobi.jzcx.android.core.ormlite.field.DatabaseField;

public class ImContactObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1486731003846792783L;

	@DatabaseField(id = true)
	private String contactId;

	@DatabaseField
	private String contactName;

	@DatabaseField
	private String contactAvatar;

	@DatabaseField
	private String meberId;

	@DatabaseField
	private boolean isGroup;

	public String getMeberId() {
		return meberId;
	}

	public void setMeberId(String meberId) {
		this.meberId = meberId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactAvatar() {
		return contactAvatar;
	}

	public void setContactAvatar(String contactAvatar) {
		this.contactAvatar = contactAvatar;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

}
