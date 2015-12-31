package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class CommontObject extends BaseObject {

	/**
	 * "CommentId":51,"Text":"赶紧来","VoiceUrl":"","MemberID":16,"Member":{
	 * "MemberId":16,"NickName":"糊涂","ICOUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/9/16/d9359057-102d-44c7-bf3b-0fb3c2578b6f.jpg"
	 * ,"Birthday":"2010-01-09","Gender":"1","LocateAddress":null,
	 * "LastLocateAddress"
	 * :null},"TimeStamp":null,"ReplyMember":null,"CreateTime"
	 * :"2015-12-11 16:50:08"}
	 */
	private static final long serialVersionUID = -4918147282668832004L;

	String CommentId;
	String Text;
	String VoiceUrl;
	String MemberID;
	AccountObject account;
	String TimeStamp;
	String CreateTime;
	AccountObject replyAccount;

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	public String getVoiceUrl() {
		return VoiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		VoiceUrl = voiceUrl;
	}

	public String getMemberID() {
		return MemberID;
	}

	public void setMemberID(String memberID) {
		MemberID = memberID;
	}

	public AccountObject getAccount() {
		return account;
	}

	public void setAccount(AccountObject account) {
		this.account = account;
	}

	public String getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}

	public String getCommentId() {
		return CommentId;
	}

	public void setCommentId(String commentId) {
		CommentId = commentId;
	}

	public AccountObject getReplyAccount() {
		return replyAccount;
	}

	public void setReplyAccount(AccountObject replyAccount) {
		this.replyAccount = replyAccount;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

}
