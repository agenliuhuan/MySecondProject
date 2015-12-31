package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class NoticeObject extends BaseObject {

	/**
	 * NoticeMsgType：
	 * 
	 * 语音评论VoiceComment=0;
	 * 
	 * 文本评论TextComment = 1;
	 * 
	 * 系统System = 2;
	 * 
	 * 活动ActivityAdd =3;
	 * 
	 * 点赞Like=4.
	 * 
	 * 
	 * "NoticeMsgId":27,"NoticeMsgType":1,"Title":"这是什么？？","Context":"这是什么？？",
	 * "ExtrasData"
	 * :{"CommentId":95,"MemberId":11,"MicroblogId":90},"CreateTime":
	 * "2015/12/15 11:26:08"
	 */
	private static final long serialVersionUID = -2883300059633072100L;

	int NoticeMsgId;

	int NoticeMsgType;

	String Title;

	String Context;

	String CreateTime;

	String CommentId;

	String MemberId;

	String MicroblogId;

	AccountObject account;

	String detailImg;

	public int getNoticeMsgId() {
		return NoticeMsgId;
	}

	public void setNoticeMsgId(int noticeMsgId) {
		NoticeMsgId = noticeMsgId;
	}

	public int getNoticeMsgType() {
		return NoticeMsgType;
	}

	public void setNoticeMsgType(int noticeMsgType) {
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

	public String getCommentId() {
		return CommentId;
	}

	public void setCommentId(String commentId) {
		CommentId = commentId;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public String getMicroblogId() {
		return MicroblogId;
	}

	public void setMicroblogId(String microblogId) {
		MicroblogId = microblogId;
	}

	public AccountObject getAccount() {
		return account;
	}

	public void setAccount(AccountObject account) {
		this.account = account;
	}

	public String getDetailImg() {
		return detailImg;
	}

	public void setDetailImg(String detailImg) {
		this.detailImg = detailImg;
	}

}
