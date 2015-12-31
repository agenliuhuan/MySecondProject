package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GetWeekReportObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 601561572221717957L;

	String petid;

	String beginTime;

	String endTime;

	public String getPetid() {
		return petid;
	}

	public void setPetid(String petid) {
		this.petid = petid;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
