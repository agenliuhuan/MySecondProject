package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class AnswerQuestionObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4712080238290164864L;

	String questionId;
	String answerText;
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

}
