package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GetFansObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6116243402004317390L;

	String meberId;
	String pageindex;

	public String getMeberId() {
		return meberId;
	}

	public void setMeberId(String meberId) {
		this.meberId = meberId;
	}

	public String getPageindex() {
		return pageindex;
	}

	public void setPageindex(String pageindex) {
		this.pageindex = pageindex;
	}

}
