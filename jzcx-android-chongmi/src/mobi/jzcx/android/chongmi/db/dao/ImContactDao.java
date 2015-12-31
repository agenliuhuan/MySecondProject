package mobi.jzcx.android.chongmi.db.dao;

import java.util.List;

import mobi.jzcx.android.chongmi.biz.vo.ImContactObject;

public class ImContactDao extends AbstractDao<ImContactObject> {
	public ImContactDao() {
		super();
	}

	public ImContactObject getImContactById(String contactid) {
		try {
			List<ImContactObject> contactList = findAll();
			for (ImContactObject contact : contactList) {
				if (contactid.equals(contact.getContactId())) {
					return contact;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
