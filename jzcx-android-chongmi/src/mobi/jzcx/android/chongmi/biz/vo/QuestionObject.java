package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class QuestionObject extends BaseObject {

	/**
	 * {"Birthday":"","Gender":"0","IcoUrl":
	 * "http://assets.cm.mc2015.co//CM.App/2015/12/21/15/f73053df-1598-485a-94b6-de17c603b9dd.jpeg"
	 * ,"MemberId":31,"LastLoginTime":"2015-12-21 03:46:09","NickName":"看咯",
	 * "password"
	 * :"900d65f1a3402e7b06446f40743c83f9","Phone":"18911753716","RealName"
	 * :"","username"
	 * :"cce64e198fa048dfb174f4f996d064dc","Title":"你的干大肠的飞机坪阿达","QuestionId"
	 * :5,"OfficialAnswer":
	 * "请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度请百度"
	 * ,"Imgs":"","Context":"可以的打发打发发大发撒分对方水电费撒大多数DSD酸啊实打实啊","AnswerTime":
	 * "2015-12-21 06:02:50"
	 * ,"AnswerCount":null,"CreateTime":"2015-12-21 06:02:50"}
	 */
	private static final long serialVersionUID = 6174313120641520384L;
	private int QuestionId;
	private String Imgs;
	private String Phone;
	private String NickName;
	private String AnswerTime;
	private String RealName;
	private String password;
	private String Title;
	private String MemberId;
	private String username;
	private String Birthday;
	private String OfficialAnswer;
	private String Context;
	private String AnswerCount;
	private String CreateTime;
	private String LastLoginTime;
	private String Gender;
	private String IcoUrl;
	public int getQuestionId() {
		return QuestionId;
	}
	public void setQuestionId(int questionId) {
		QuestionId = questionId;
	}
	public String getImgs() {
		return Imgs;
	}
	public void setImgs(String imgs) {
		Imgs = imgs;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public String getAnswerTime() {
		return AnswerTime;
	}
	public void setAnswerTime(String answerTime) {
		AnswerTime = answerTime;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		MemberId = memberId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBirthday() {
		return Birthday;
	}
	public void setBirthday(String birthday) {
		Birthday = birthday;
	}
	public String getOfficialAnswer() {
		return OfficialAnswer;
	}
	public void setOfficialAnswer(String officialAnswer) {
		OfficialAnswer = officialAnswer;
	}
	public String getLastLoginTime() {
		return LastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		LastLoginTime = lastLoginTime;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public String getIcoUrl() {
		return IcoUrl;
	}
	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}
	public String getContext() {
		return Context;
	}
	public void setContext(String context) {
		Context = context;
	}
	public String getAnswerCount() {
		return AnswerCount;
	}
	public void setAnswerCount(String answerCount) {
		AnswerCount = answerCount;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

}
