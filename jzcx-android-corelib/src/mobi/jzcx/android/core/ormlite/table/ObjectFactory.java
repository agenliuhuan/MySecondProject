package mobi.jzcx.android.core.ormlite.table;

import java.lang.reflect.Constructor;
import java.sql.SQLException;

import mobi.jzcx.android.core.ormlite.dao.Dao;

/**
 * Interface that allows you to inject a factory class that creates objects of this class. You sert it on the Dao using:
 * {@link Dao#setObjectFactory(ObjectFactory)}.
 * 
 * @author graywatson
 */
public interface ObjectFactory<T> {

	/**
	 * Construct and return an object of a certain class.
	 * 
	 * @throws SQLException
	 *             if there was a problem creating the object.
	 */
	public T createObject(Constructor<T> construcor, Class<T> dataClass) throws SQLException;
}
