package mobi.jzcx.android.core.ormlite.stmt.query;

import java.sql.SQLException;
import java.util.List;

import mobi.jzcx.android.core.ormlite.db.DatabaseType;
import mobi.jzcx.android.core.ormlite.stmt.ArgumentHolder;

/**
 * Internal marker class for query clauses.
 * 
 * @author graywatson
 */
public interface Clause {

	/**
	 * Add to the string-builder the appropriate SQL for this clause.
	 * 
	 * @param tableName
	 *            Name of the table to prepend to any column names or null to be ignored.
	 */
	public void appendSql(DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> argList)
			throws SQLException;
}
