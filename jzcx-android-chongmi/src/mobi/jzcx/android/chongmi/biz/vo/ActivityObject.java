package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class ActivityObject extends BaseObject {

	/**
	 * "Activity":{"ActivityId":1,"MemberId":17,"IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/9/18/25577c49-8d91-4841-9da6-b063b6ac0631.jpeg"
	 * ,"Title":"咯哦","Intro":"弄回来看看具体考虑突突突","Address":"特色粉面饭","LocateAddress":{
	 * "Lng":114.109978,"Lat":22.568471},"ImGroupId":"137833184690700828",
	 * "MaxUserCount"
	 * :1,"MemberCount":8,"IsOwner":false,"BeginTime":"0001-01-01 12:00:00"
	 * ,"CreateTime":"0001-01-01 12:00:00","IsJoin":false
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1650251138255100201L;
	private String ActivityId;
	private String MemberId;
	private int MemberCount;
	private String Title;
	private String IcoUrl;
	private String Address;
	private int MaxUserCount;
	private String Intro;
	private String ImGroupId;

	private boolean IsJoin;
	private boolean IsOwner;
	private String CreateTime;
	private String BeginTime;
	private String EndTime;
	private LngLatObject lnglatObj;

	public boolean isIsOwner() {
		return IsOwner;
	}

	public void setIsOwner(boolean isOwner) {
		IsOwner = isOwner;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public int getMemberCount() {
		return MemberCount;
	}

	public void setMemberCount(int memberCount) {
		MemberCount = memberCount;
	}

	public String getIcoUrl() {
		return IcoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}

	public int getMaxUserCount() {
		return MaxUserCount;
	}

	public void setMaxUserCount(int maxUserCount) {
		MaxUserCount = maxUserCount;
	}

	public String getImGroupId() {
		return ImGroupId;
	}

	public void setImGroupId(String imGroupId) {
		ImGroupId = imGroupId;
	}

	public LngLatObject getLnglatObj() {
		return lnglatObj;
	}

	public void setLnglatObj(LngLatObject lnglatObj) {
		this.lnglatObj = lnglatObj;
	}

	public String getActivityId() {
		return ActivityId;
	}

	public void setActivityId(String activityId) {
		ActivityId = activityId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getIntro() {
		return Intro;
	}

	public void setIntro(String intro) {
		Intro = intro;
	}

	public boolean isIsJoin() {
		return IsJoin;
	}

	public void setIsJoin(boolean isJoin) {
		IsJoin = isJoin;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getBeginTime() {
		return BeginTime;
	}

	public void setBeginTime(String beginTime) {
		BeginTime = beginTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

}
