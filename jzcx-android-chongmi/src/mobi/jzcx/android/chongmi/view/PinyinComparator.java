package mobi.jzcx.android.chongmi.view;

import java.util.Comparator;

import mobi.jzcx.android.chongmi.biz.vo.PetStyleObject;

/**
 * 
 * @author xiaanming
 * 
 */
public class PinyinComparator implements Comparator<PetStyleObject> {

	public int compare(PetStyleObject o1, PetStyleObject o2) {
		// 这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
		if (o2.getSpell().equals("#")) {
			return -1;
		} else if (o1.getSpell().equals("#")) {
			return 1;
		} else {
			return o1.getSpell().compareTo(o2.getSpell());
		}
	}
}
