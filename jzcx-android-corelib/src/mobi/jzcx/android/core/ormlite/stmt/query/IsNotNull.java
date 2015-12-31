package mobi.jzcx.android.core.ormlite.stmt.query;

import java.sql.SQLException;
import java.util.List;

import mobi.jzcx.android.core.ormlite.db.DatabaseType;
import mobi.jzcx.android.core.ormlite.field.FieldType;
import mobi.jzcx.android.core.ormlite.stmt.ArgumentHolder;
import mobi.jzcx.android.core.ormlite.stmt.Where;

/**
 * Internal class handling the SQL 'IS NOT NULL' comparison query part. Used by {@link Where#isNull}.
 * 
 * @author graywatson
 */
public class IsNotNull extends BaseComparison {

	public IsNotNull(String columnName, FieldType fieldType) throws SQLException {
		super(columnName, fieldType, null, false);
	}

	@Override
	public void appendOperation(StringBuilder sb) {
		sb.append("IS NOT NULL ");
	}

	@Override
	public void appendValue(DatabaseType databaseType, StringBuilder sb, List<ArgumentHolder> argList) {
		// there is no value
	}
}
