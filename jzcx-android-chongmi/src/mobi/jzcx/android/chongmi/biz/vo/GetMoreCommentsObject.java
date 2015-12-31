package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GetMoreCommentsObject extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8801453682752502092L;
	String meberid;
	String index;

	public String getMeberid() {
		return meberid;
	}

	public void setMeberid(String meberid) {
		this.meberid = meberid;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
