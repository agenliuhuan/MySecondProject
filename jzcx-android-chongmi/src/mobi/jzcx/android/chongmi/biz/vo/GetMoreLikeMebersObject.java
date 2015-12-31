package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class GetMoreLikeMebersObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3875295903768236471L;

	String id;
	int index;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
