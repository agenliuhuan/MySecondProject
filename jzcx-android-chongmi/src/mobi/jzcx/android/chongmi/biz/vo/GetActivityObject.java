package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GetActivityObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3318499563478738830L;
	LngLatObject lnglatObj;
	int pageIndex;

	public LngLatObject getLnglatObj() {
		return lnglatObj;
	}

	public void setLnglatObj(LngLatObject lnglatObj) {
		this.lnglatObj = lnglatObj;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

}
