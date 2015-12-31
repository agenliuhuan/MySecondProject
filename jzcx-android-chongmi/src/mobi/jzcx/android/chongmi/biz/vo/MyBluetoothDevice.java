package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class MyBluetoothDevice extends BaseObject {

	/**
	 * {"Mac":"e0c79d65c113","IsBind":false,"PetCollarId":0,"PetName":null}
	 */
	private static final long serialVersionUID = -4676356316289613858L;

	private String deviceName;

	private String PetCollarId;

	private String Mac;

	private boolean IsBind;

	private String PetName;

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

	public boolean isIsBind() {
		return IsBind;
	}

	public void setIsBind(boolean isBind) {
		IsBind = isBind;
	}

	public String getPetName() {
		return PetName;
	}

	public void setPetName(String petName) {
		PetName = petName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
