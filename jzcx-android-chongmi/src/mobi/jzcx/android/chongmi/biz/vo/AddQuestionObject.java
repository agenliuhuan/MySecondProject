package mobi.jzcx.android.chongmi.biz.vo;

import java.util.ArrayList;

import mobi.jzcx.android.core.mvc.BaseObject;

public class AddQuestionObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3053798696884893189L;

	String title;
	String context;
	ArrayList<String> photos;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public ArrayList<String> getPhotos() {
		return photos;
	}
	public void setPhotos(ArrayList<String> photos) {
		this.photos = photos;
	}

}
