package mobi.jzcx.android.chongmi.biz.vo;

import mobi.jzcx.android.core.mvc.BaseObject;
import mobi.jzcx.android.core.ormlite.field.DatabaseField;

public class PetStyleObject extends BaseObject {

	/**
	 * "List":[{"CategoryId":1,"Name":"狗","IcoUrl":
	 * "http://assets.cm.mc2015.co/common/dog.png"
	 * ,"Order":0,"Spell":"G","ParentId"
	 * :0,"DBNullParameterList":null},{"CateporyId"
	 * :2,"Name":"猫","IcoUrl":"http://assets.cm.mc2015.co/common/cat.png"
	 * ,"Order":0,"Spell":"M","ParentId":0,"DBNullParameterList":null}
	 */
	private static final long serialVersionUID = -7946820152176460986L;

	private String CategoryId;

	private String Name;

	private String IcoUrl;

	private String Order;

	private String Spell;

	private String ParentId;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}

	public String getIcoUrl() {
		return IcoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		IcoUrl = icoUrl;
	}

	public String getParentId() {
		return ParentId;
	}

	public void setParentId(String parentId) {
		ParentId = parentId;
	}

	public String getOrder() {
		return Order;
	}

	public void setOrder(String order) {
		Order = order;
	}

	public String getSpell() {
		return Spell;
	}

	public void setSpell(String spell) {
		Spell = spell;
	}

}
