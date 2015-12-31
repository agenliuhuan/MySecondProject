package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class ReportObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2813961518645012022L;
	String activityId;
	String microblogId;
	String commentMemberId;
	String Reason;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getMicroblogId() {
		return microblogId;
	}

	public void setMicroblogId(String microblogId) {
		this.microblogId = microblogId;
	}

	public String getCommentMemberId() {
		return commentMemberId;
	}

	public void setCommentMemberId(String commentMemberId) {
		this.commentMemberId = commentMemberId;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

}
