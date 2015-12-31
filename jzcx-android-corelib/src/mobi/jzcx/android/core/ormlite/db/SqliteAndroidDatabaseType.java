package mobi.jzcx.android.core.ormlite.db;

import java.sql.SQLException;

import mobi.jzcx.android.core.ormlite.android.DatabaseTableConfigUtil;
import mobi.jzcx.android.core.ormlite.field.DataPersister;
import mobi.jzcx.android.core.ormlite.field.FieldType;
import mobi.jzcx.android.core.ormlite.field.types.DateStringType;
import mobi.jzcx.android.core.ormlite.field.types.TimeStampStringType;
import mobi.jzcx.android.core.ormlite.field.types.TimeStampType;
import mobi.jzcx.android.core.ormlite.support.ConnectionSource;
import mobi.jzcx.android.core.ormlite.table.DatabaseTableConfig;

/**
 * Sqlite database type information for the Android OS that makes native calls to the Android OS database APIs.
 * 
 * @author graywatson
 */
public class SqliteAndroidDatabaseType extends BaseSqliteDatabaseType {

	@Override
	public void loadDriver() {
		// noop
	}

	public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
		// not used by the android code
		return true;
	}

	@Override
	protected String getDriverClassName() {
		// no driver to load in android-land
		return null;
	}

	public String getDatabaseName() {
		return "Android SQLite";
	}

	@Override
	protected void appendDateType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		// default is to store the date as a string
		appendStringType(sb, fieldType, fieldWidth);
	}

	@Override
	protected void appendBooleanType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		// we have to convert booleans to numbers
		appendShortType(sb, fieldType, fieldWidth);
	}

	@Override
	public DataPersister getDataPersister(DataPersister defaultPersister, FieldType fieldType) {
		if (defaultPersister == null) {
			return super.getDataPersister(defaultPersister, fieldType);
		}
		// we are only overriding certain types
		switch (defaultPersister.getSqlType()) {
			case DATE :
				if (defaultPersister instanceof TimeStampType) {
					return TimeStampStringType.getSingleton();
				} else {
					return DateStringType.getSingleton();
				}
			default :
				return super.getDataPersister(defaultPersister, fieldType);
		}
	}

	@Override
	public boolean isNestedSavePointsSupported() {
		return false;
	}

	@Override
	public boolean isBatchUseTransaction() {
		return true;
	}

	@Override
	public <T> DatabaseTableConfig<T> extractDatabaseTableConfig(ConnectionSource connectionSource, Class<T> clazz)
			throws SQLException {
		return DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
	}
}
