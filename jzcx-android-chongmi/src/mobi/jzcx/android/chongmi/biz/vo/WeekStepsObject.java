package mobi.jzcx.android.chongmi.biz.vo;

import java.util.ArrayList;

import mobi.jzcx.android.core.mvc.BaseObject;

public class WeekStepsObject extends BaseObject {

	/**
	 * {"Date":"2015-12-17 00:00:00","List":[{"DateDay" :
	 * "2015-12-17 00:00:00","Hours":12,"PetId":1,"Step":70, "CreateTime"
	 * :"2015-12-19 16:40:12","UpdateTime":"2015-12-19 16:40:12" }
	 * ,{"DateDay":"2015-12-17 00:00:00","Hours":18,"PetId":
	 * 1,"Step":86,"CreateTime":"2015-12-19 16:40:12",
	 * "UpdateTime":"2015-12-19 16:40:12"}
	 */
	private static final long serialVersionUID = -3540589369616835498L;

	String date;
	ArrayList<DayStepObject> dayStepList;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ArrayList<DayStepObject> getDayStepList() {
		return dayStepList;
	}
	public void setDayStepList(ArrayList<DayStepObject> dayStepList) {
		this.dayStepList = dayStepList;
	}

}
