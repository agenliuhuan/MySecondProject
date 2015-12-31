package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class AnswerObject extends BaseObject {

	/**
	 * {"Birthday":"","Gender":"1","ICOUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/22/11/689661fc-ae82-4acb-8dfe-ea67e5874b5c.jpg"
	 * ,"MemberId":16,"LastLoginTime":"2015-12-18 05:42:45", "NickName"
	 * :"糊涂来图后悔了","password":"D8578EDF8458CE06FBC5BB76A58C5CA4"
	 * ,"Phone":"13554996963","RealName":"","username": "13554996963"
	 * ,"AnswerId":1,"QuestionId":3,"AnswerText"
	 * :"去拉了路了了涂了来咯了舞台上","CreateTime":"2015-12-22 03:50:52"}
	 */
	private static final long serialVersionUID = -988056215246335023L;

	private int AnswerId;
	private int QuestionId;
	private String NickName;
	private String MemberId;
	private String IcoUrl;
	private String Birthday;
	private String Gender;
	private String LastLoginTime;
	private String password;
	private String Phone;
	private String username;
	private String AnswerText;
	private String CreateTime;

	public int getQuestionId() {
		return QuestionId;
	}
	public void setQuestionId(int questionId) {
		QuestionId = questionId;
	}
	public String getBirthday() {
		return Birthday;
	}
	public void setBirthday(String birthday) {
		Birthday = birthday;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public String getLastLoginTime() {
		return LastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		LastLoginTime = lastLoginTime;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAnswerText() {
		return AnswerText;
	}
	public void setAnswerText(String answerText) {
		AnswerText = answerText;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public int getAnswerId() {
		return AnswerId;
	}
	public void setAnswerId(int answerId) {
		AnswerId = answerId;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public String getIcoUrl() {
		return IcoUrl;
	}
	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}

}
