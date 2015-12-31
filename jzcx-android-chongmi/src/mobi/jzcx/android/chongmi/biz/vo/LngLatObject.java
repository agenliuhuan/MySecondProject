package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;

public class LngLatObject extends BaseObject {

	private static final long serialVersionUID = 3181680278788852033L;

	String Lng;
	String Lat;
	String adress;
	String city;
	String LastLocateAddress;

	public String getLng() {
		return Lng;
	}

	public void setLng(String lng) {
		Lng = lng;
	}

	public String getLat() {
		return Lat;
	}

	public void setLat(String lat) {
		Lat = lat;
	}

	public String getLastLocateAddress() {
		return LastLocateAddress;
	}

	public void setLastLocateAddress(String lastLocateAddress) {
		LastLocateAddress = lastLocateAddress;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
