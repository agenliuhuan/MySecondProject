package mobi.jzcx.android.core.ormlite.field.types;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;

import mobi.jzcx.android.core.ormlite.field.FieldType;
import mobi.jzcx.android.core.ormlite.field.SqlType;
import mobi.jzcx.android.core.ormlite.misc.SqlExceptionUtil;
import mobi.jzcx.android.core.ormlite.support.DatabaseResults;

/**
 * Type that persists a {@link java.util.Date} object.
 * 
 * <p>
 * NOTE: This is <i>not</i> the same as the {@link java.sql.Date} class that is handled by {@link SqlDateType}.
 * </p>
 * 
 * @author graywatson
 */
public class DateType extends BaseDateType {

	private static final DateType singleTon = new DateType();

	public static DateType getSingleton() {
		return singleTon;
	}

	private DateType() {
		super(SqlType.DATE, new Class<?>[] { java.util.Date.class });
	}

	protected DateType(SqlType sqlType, Class<?>[] classes) {
		super(sqlType, classes);
	}

	@Override
	public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
		DateStringFormatConfig dateFormatConfig = convertDateStringConfig(fieldType, getDefaultDateFormatConfig());
		try {
			return new Timestamp(parseDateString(dateFormatConfig, defaultStr).getTime());
		} catch (ParseException e) {
			throw SqlExceptionUtil.create("Problems parsing default date string '" + defaultStr + "' using '"
					+ dateFormatConfig + '\'', e);
		}
	}

	@Override
	public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
		return results.getTimestamp(columnPos);
	}

	@Override
	public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
		Timestamp value = (Timestamp) sqlArg;
		return new java.util.Date(value.getTime());
	}

	@Override
	public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
		java.util.Date date = (java.util.Date) javaObject;
		return new Timestamp(date.getTime());
	}

	@Override
	public boolean isArgumentHolderRequired() {
		return true;
	}

	/**
	 * Return the default date format configuration.
	 */
	protected DateStringFormatConfig getDefaultDateFormatConfig() {
		return defaultDateFormatConfig;
	}
}
