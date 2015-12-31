package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class CheckCommentObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7188648025745334481L;

	String microblogid;
	String commentid;
	public String getMicroblogid() {
		return microblogid;
	}
	public void setMicroblogid(String microblogid) {
		this.microblogid = microblogid;
	}
	public String getCommentid() {
		return commentid;
	}
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

}
