package mobi.jzcx.android.chongmi.db.dao;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import mobi.jzcx.android.chongmi.db.DatabaseHelper;
import mobi.jzcx.android.chongmi.db.DatabaseManager;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.ormlite.dao.Dao;
import mobi.jzcx.android.core.ormlite.stmt.DeleteBuilder;
import mobi.jzcx.android.core.ormlite.stmt.QueryBuilder;

public abstract class AbstractDao<T> {
	private static final String TAG = AbstractDao.class.getSimpleName();

	protected DatabaseHelper dbHelper;
	protected Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		dbHelper = DatabaseManager.getInstance().getHelper();
	}

	public T insert(T t) {
		T t2 = null;
		try {
			if (dbHelper != null) {
				t2 = dbHelper.getDao(entityClass).createIfNotExists(t);
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return t2;
	}

	public void insertAll(List<T> t) {
		try {
			if (dbHelper != null) {
				for (T t2 : t) {
					dbHelper.getDao(entityClass).createIfNotExists(t2);
				}
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void insertAllinBatchOperation(final List<T> t) throws Exception {
		try {
			if (dbHelper != null) {
				final Dao<T, ?> dao = dbHelper.getDao(entityClass);
				dao.callBatchTasks(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						for (T t2 : t) {
							dao.create(t2);
						}
						return true;
					}
				});
			}

		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void update(T t) {
		try {
			if (dbHelper != null) {
				dbHelper.getDao(entityClass).createOrUpdate(t);
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void refresh(T t) {
		try {
			if (dbHelper != null) {

				dbHelper.getDao(entityClass).update(t);
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void updateAllinBatchOperation(final List<T> t) throws Exception {
		try {
			if (dbHelper != null) {
				final Dao<T, ?> dao = dbHelper.getDao(entityClass);
				dao.callBatchTasks(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						for (T t2 : t) {
							dao.createOrUpdate(t2);
						}
						return true;
					}
				});
			}

		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public List<T> findAll() {
		List<T> list = null;
		try {
			if (dbHelper != null) {
				QueryBuilder query = dbHelper.getDao(entityClass).queryBuilder();
//				query.orderBy("insertTime", true);
				list = dbHelper.getDao(entityClass).query(query.prepare());
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return list;
	}

	public List<T> findPage(int pageNo, int pageSize) {
		List<T> list = null;
		try {
			if (dbHelper != null) {
				QueryBuilder query = dbHelper.getDao(entityClass).queryBuilder();
				query.offset((long) (pageNo - 1) * pageSize).limit((long) pageSize);
				list = dbHelper.getDao(entityClass).query(query.prepare());
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return list;
	}

	public boolean isEmpty() {
		boolean empty = true;
		List<T> list = null;
		try {
			if (dbHelper != null) {
				list = dbHelper.getDao(entityClass).queryForAll();
				empty = (list.size() > 0) ? false : true;
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return empty;
	}

	public int count() {
		int count = 0;
		List<T> list = null;
		try {
			if (dbHelper != null) {
				list = dbHelper.getDao(entityClass).queryForAll();
				count = list.size();
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return count;
	}

	public void delete(T t) {
		try {
			if (dbHelper != null) {
				dbHelper.getDao(entityClass).delete(t);
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void clear() {
		try {
			if (dbHelper != null) {
				DeleteBuilder db = dbHelper.getDao(entityClass).deleteBuilder();
				db.delete();
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public T findById(Integer id) {
		T t = null;
		try {
			if (dbHelper != null) {
				Dao<T, Integer> dao = dbHelper.getDao(entityClass);
				t = dao.queryForId(id);
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public boolean isExist(Integer id) {
		T t = null;
		boolean exist = false;
		try {
			if (dbHelper != null) {
				Dao<T, Integer> dao = dbHelper.getDao(entityClass);
				t = dao.queryForId(id);
				if (t != null) {
					exist = true;
				}
			}
		} catch (SQLException e) {
			LogUtils.e(TAG, "ERROR IN ABSTRACTDAO" + e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exist;
	}

}
