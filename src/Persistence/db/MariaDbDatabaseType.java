package Persistence.db;

import com.j256.ormlite.field.FieldType;

/**
 * MariaDB database type information used to create the tables, etc.. It is an extension of MySQL.
 * 
 * @author kratorius
 */
public class MariaDbDatabaseType extends MysqlDatabaseType {

	private final static String DATABASE_URL_PORTION = "mariadb";
	private final static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
	private final static String DATABASE_NAME = "MariaDB";

	@Override
	public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
		return DATABASE_URL_PORTION.equals(dbTypePart);
	}

	@Override
	protected String[] getDriverClassNames() {
		return new String[] { DRIVER_CLASS_NAME };
	}

	@Override
	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	@Override
	protected void appendByteArrayType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		super.appendByteArrayType(sb, fieldType, fieldWidth);
	}

	@Override
	protected void appendLongStringType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		super.appendLongStringType(sb, fieldType, fieldWidth);
	}
}
