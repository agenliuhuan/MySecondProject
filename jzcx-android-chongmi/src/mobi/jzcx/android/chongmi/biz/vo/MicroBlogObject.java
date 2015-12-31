package mobi.jzcx.android.chongmi.biz.vo;

import java.util.ArrayList;

import mobi.jzcx.android.core.mvc.BaseObject;

public class MicroBlogObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6466537315251646442L;

	String Text;
	ArrayList<String> Photos;
	String Photo;
	String PetId;
	String MicroblogID;
	String CommentId;
	LngLatObject lnglat;

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	public ArrayList<String> getPhotos() {
		return Photos;
	}

	public void setPhotos(ArrayList<String> photos) {
		Photos = photos;
	}

	public String getPetId() {
		return PetId;
	}

	public void setPetId(String petId) {
		PetId = petId;
	}

	public LngLatObject getLnglat() {
		return lnglat;
	}

	public void setLnglat(LngLatObject lnglat) {
		this.lnglat = lnglat;
	}

	public String getMicroblogID() {
		return MicroblogID;
	}

	public void setMicroblogID(String microblogID) {
		MicroblogID = microblogID;
	}

	public String getCommentId() {
		return CommentId;
	}

	public void setCommentId(String commentId) {
		CommentId = commentId;
	}

}
