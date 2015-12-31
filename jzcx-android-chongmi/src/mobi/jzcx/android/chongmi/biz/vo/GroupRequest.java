package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GroupRequest extends BaseObject {

	/**
	 * {"ActivityId":77,"ActivityName":"本来面目","RequestId":50,"MemberId":28,
	 * "IsJoin":false,"CreateTime":"2015-12-24 15:19:28","BeginTime":
	 * "2015-12-29 00:00:00"
	 * ,"EndTime":"2015-12-30 00:00:00","NoticeMsgId":12139,
	 * "NoticeMsgType":3,"Title"
	 * :"黑山老妖申请加入\"本来面目\"群组","Context":"12345678910","ExtrasData"
	 * :{"RequestId":50
	 * ,"MemberId":28,"ActivityId":77},"Member":{"MemberId":28,"NickName"
	 * :"黑山老妖","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/23/10/3cf155d4-626c-4995-8279-5b737cfa6e71.jpg"
	 * ,"Birthday":"1995-01-01","Gender":"1","LocateAddress":{"Lng":114.07548,
	 * "Lat" :22.5354},"LastLocateAddress":"广东省 深圳市","Pets":[{"PetId":39,
	 * "CategoryId"
	 * :12,"CategoryName":"德文卷毛猫","Gender":"0","Name":"250","Birthday":
	 * "2008-12-21","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/15/f93dcd0f-4bf6-41e3-83ba-82dd8c9d68ed.jpg"
	 * ,"Description":"","MemberId":28},{"PetId":38,"CategoryId":13,
	 * "CategoryName"
	 * :"东方猫","Gender":"0","Name":"黑底","Birthday":"2002-12-21","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/15/a6348af3-ae91-4e0b-800f-f4efc1df5af4.jpg"
	 * ,"Description":"","MemberId":28},{"PetId":30,"CategoryId":10,
	 * "CategoryName"
	 * :"波米拉猫","Gender":"0","Name":"喵喵","Birthday":"2000-12-21","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/11/1d379bd4-fe73-4d28-a4a6-8481bfc15596.jpg"
	 * ,"Description":"","MemberId":28},{"PetId":21,"CategoryId":49,
	 * "CategoryName"
	 * :"爱尔兰雪达犬","Gender":"0","Name":"大咪咪","Birthday":"2006-12-21","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/10/36d2f095-dad1-45bc-bfd6-36e976b8a9da.jpg"
	 * ,"Description":"恩恩怨怨","MemberId":28}],"FollowsCount":11,"FansCount":9,
	 * "IsFollowed":false},
	 */
	private static final long serialVersionUID = 6581019789735553486L;

	private String ActivityId;

	private String ActivityName;

	private String RequestId;

	private String MemberId;

	private boolean IsJoin;

	private String NoticeMsgId;

	private String NoticeMsgType;

	private String Title;

	private String Context;

	private String CreateTime;

	private String EndTime;

	private AccountDetailObject account;

	public boolean isIsJoin() {
		return IsJoin;
	}

	public void setIsJoin(boolean isJoin) {
		IsJoin = isJoin;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getActivityId() {
		return ActivityId;
	}

	public void setActivityId(String activityId) {
		ActivityId = activityId;
	}

	public String getActivityName() {
		return ActivityName;
	}

	public void setActivityName(String activityName) {
		ActivityName = activityName;
	}

	public String getRequestId() {
		return RequestId;
	}

	public void setRequestId(String requestId) {
		RequestId = requestId;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public String getNoticeMsgId() {
		return NoticeMsgId;
	}

	public void setNoticeMsgId(String noticeMsgId) {
		NoticeMsgId = noticeMsgId;
	}

	public String getNoticeMsgType() {
		return NoticeMsgType;
	}

	public void setNoticeMsgType(String noticeMsgType) {
		NoticeMsgType = noticeMsgType;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContext() {
		return Context;
	}

	public void setContext(String context) {
		Context = context;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public AccountDetailObject getAccount() {
		return account;
	}

	public void setAccount(AccountDetailObject account) {
		this.account = account;
	}

}
