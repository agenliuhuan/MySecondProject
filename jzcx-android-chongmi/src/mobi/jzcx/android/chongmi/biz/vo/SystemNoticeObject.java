package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;
import mobi.jzcx.android.core.ormlite.field.DatabaseField;

public class SystemNoticeObject extends BaseObject {

	/**
	 * {"NoticeMsgId":10,"NoticeMsgType":2,"Title":"123456","Context":"这是系统消息",
	 * "ExtrasData":{},"Member":null,"CreateTime":"2015-12-08 14:02:57"}
	 */
	private static final long serialVersionUID = -7916264467178126359L;

	String NoticeMsgId;

	int NoticeMsgType;

	String Title;

	String Context;

	String CreateTime;

	public String getNoticeMsgId() {
		return NoticeMsgId;
	}

	public void setNoticeMsgId(String noticeMsgId) {
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

}
