package mobi.jzcx.android.chongmi.utils;

public class CommonTextUtils {
	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0 || str.toString().toLowerCase().equals("null")
				|| str.toString().trim().length() == 0)
			return true;
		else
			return false;
	}
}
