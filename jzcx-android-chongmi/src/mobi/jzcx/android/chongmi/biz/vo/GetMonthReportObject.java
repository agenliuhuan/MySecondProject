package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GetMonthReportObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -386173904654368589L;

	String petid;

	int year;

	int month;

	public String getPetid() {
		return petid;
	}

	public void setPetid(String petid) {
		this.petid = petid;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

}
