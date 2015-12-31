package mobi.jzcx.android.core.ormlite.stmt;

import mobi.jzcx.android.core.ormlite.dao.Dao;

/**
 * Interface returned by the {@link DeleteBuilder#prepare()} which supports custom DELETE statements. This should be in
 * turn passed to the {@link Dao#delete(PreparedDelete)} method.
 * 
 * @param <T>
 *            The class that the code will be operating on.
 * @author graywatson
 */
public interface PreparedDelete<T> extends PreparedStmt<T> {
}
