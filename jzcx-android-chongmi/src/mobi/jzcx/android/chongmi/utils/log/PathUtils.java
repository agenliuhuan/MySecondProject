package mobi.jzcx.android.chongmi.utils.log;

import java.io.File;

import android.os.Environment;

public class PathUtils {
	public final static String sdcardPath = Environment.getExternalStorageDirectory() + "";
	public final static String PATH = sdcardPath + "/exception/" + "mobi.jzcx.android.chongmi";
	public final static String BUGPATH = PATH + "/bug/";
	public final static String LOGPATH = PATH + "/log/";

	public PathUtils() {
		File PATH = new File(PathUtils.PATH);
		if (!PATH.exists()) {
			PATH.mkdirs();
		}
		File LOGPATH = new File(PathUtils.LOGPATH);
		if (!LOGPATH.exists()) {
			LOGPATH.mkdirs();
		}
		File BUGPATH = new File(PathUtils.BUGPATH);
		if (!BUGPATH.exists()) {
			BUGPATH.mkdirs();
		}
	}
}
