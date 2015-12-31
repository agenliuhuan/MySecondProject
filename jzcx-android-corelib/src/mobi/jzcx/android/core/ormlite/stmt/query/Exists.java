package mobi.jzcx.android.core.ormlite.stmt.query;

import java.sql.SQLException;
import java.util.List;

import mobi.jzcx.android.core.ormlite.db.DatabaseType;
import mobi.jzcx.android.core.ormlite.stmt.ArgumentHolder;
import mobi.jzcx.android.core.ormlite.stmt.QueryBuilder.InternalQueryBuilderWrapper;
import mobi.jzcx.android.core.ormlite.stmt.Where;

/**
 * Internal class handling the SQL 'EXISTS' query part. Used by {@link Where#exists}.
 * 
 * @author graywatson
 */
public class Exists implements Clause {

	private final InternalQueryBuilderWrapper subQueryBuilder;

	public Exists(InternalQueryBuilderWrapper subQueryBuilder) {
		this.subQueryBuilder = subQueryBuilder;
	}

	public void appendSql(DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> argList)
			throws SQLException {
		sb.append("EXISTS (");
		subQueryBuilder.appendStatementString(sb, argList);
		sb.append(") ");
	}
}
