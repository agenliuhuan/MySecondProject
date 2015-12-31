package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class DayStepObject extends BaseObject {

	/**
	 * {"DateDay" : "2015-12-17 00:00:00","Hours":12,"PetId":1,"Step":70,
	 * "CreateTime" :"2015-12-19 16:40:12","UpdateTime":"2015-12-19 16:40:12" }
	 */
	private static final long serialVersionUID = -7684091884027763387L;

	String DateDay;
	int Hours;
	int PetId;
	int Step;
	String CreateTime;
	String UpdateTime;
	public String getDateDay() {
		return DateDay;
	}
	public void setDateDay(String dateDay) {
		DateDay = dateDay;
	}
	public int getHours() {
		return Hours;
	}
	public void setHours(int hours) {
		Hours = hours;
	}
	public int getPetId() {
		return PetId;
	}
	public void setPetId(int petId) {
		PetId = petId;
	}
	public int getStep() {
		return Step;
	}
	public void setStep(int step) {
		Step = step;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getUpdateTime() {
		return UpdateTime;
	}
	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}

}
