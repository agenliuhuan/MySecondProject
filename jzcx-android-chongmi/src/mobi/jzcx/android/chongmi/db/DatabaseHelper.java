package mobi.jzcx.android.chongmi.db;

import mobi.jzcx.android.chongmi.biz.vo.ImContactObject;
import mobi.jzcx.android.chongmi.biz.vo.SystemNoticeObject;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import mobi.jzcx.android.core.ormlite.support.ConnectionSource;
import mobi.jzcx.android.core.ormlite.table.TableUtils;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "chinahome";
	private static final String DATABASE_SUFFIX = ".db";
	private static final int DATABASE_VERSION = 1; // 数据库版本号

	public DatabaseHelper(final Context context, String userId) {
		super(context, DATABASE_NAME + userId + DATABASE_SUFFIX, null, DATABASE_VERSION);
	}

	// the DAO object we use to access the SimpleData table
	// private Dao<UserObject, Integer> userObjectDao = null;

	@Override
	public void onCreate(final SQLiteDatabase db, final ConnectionSource connectionSource) {
		try {
			LogUtils.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, ImContactObject.class);
		} catch (final SQLException e) {
			LogUtils.e(DatabaseHelper.class.getName(), "Can't create database" + e);
			throw new RuntimeException(e);
		} catch (final Exception e) {
			LogUtils.e(DatabaseHelper.class.getName(), "Can't create database" + e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource, final int oldVersion,
			final int newVersion) {
		try {
			LogUtils.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, ImContactObject.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (final SQLException e) {
			LogUtils.e(DatabaseHelper.class.getName(), "Can't drop databases" + e);
			throw new RuntimeException(e);
		} catch (final Exception e) {
			LogUtils.e(DatabaseHelper.class.getName(), "Can't create database" + e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
	}

}
