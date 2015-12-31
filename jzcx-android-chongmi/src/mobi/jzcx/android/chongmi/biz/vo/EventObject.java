package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class EventObject extends BaseObject {

	private static final long serialVersionUID = 5400696753919242246L;

	private String Title;
	private String IcoUrl;
	private String beginTime;
	private String Address;
	private String lnglat;
	private int MaxMemberCount;
	private String Intro;

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getLnglat() {
		return lnglat;
	}

	public void setLnglat(String lnglat) {
		this.lnglat = lnglat;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getIcoUrl() {
		return IcoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public int getMaxMemberCount() {
		return MaxMemberCount;
	}

	public void setMaxMemberCount(int maxMemberCount) {
		MaxMemberCount = maxMemberCount;
	}

	public String getIntro() {
		return Intro;
	}

	public void setIntro(String intro) {
		Intro = intro;
	}

}
