package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class AddCommonObject extends BaseObject {
	private static final long serialVersionUID = 3423036886324858145L;

	String id;
	String text;
	boolean isVoice;
	String voicePath;
	String commentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isVoice() {
		return isVoice;
	}

	public void setVoice(boolean isVoice) {
		this.isVoice = isVoice;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

}
