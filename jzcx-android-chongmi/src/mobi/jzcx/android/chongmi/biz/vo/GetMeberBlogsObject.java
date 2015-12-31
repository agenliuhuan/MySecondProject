package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GetMeberBlogsObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -295992286968668744L;
	String meberId;
	String index;

	public String getMeberId() {
		return meberId;
	}

	public void setMeberId(String meberId) {
		this.meberId = meberId;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
