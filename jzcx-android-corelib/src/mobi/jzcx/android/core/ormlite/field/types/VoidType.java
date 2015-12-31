package mobi.jzcx.android.core.ormlite.field.types;

import java.lang.reflect.Field;

import mobi.jzcx.android.core.ormlite.field.FieldType;
import mobi.jzcx.android.core.ormlite.support.DatabaseResults;

/**
 * Marker class used to see if we have a customer persister defined.
 * 
 * @author graywatson
 */
public class VoidType extends BaseDataType {

	VoidType() {
		super(null, new Class<?>[] {});
	}

	@Override
	public Object parseDefaultString(FieldType fieldType, String defaultStr) {
		return null;
	}

	@Override
	public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) {
		return null;
	}

	@Override
	public boolean isValidForField(Field field) {
		return false;
	}
}
