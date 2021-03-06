package Persistence.db;

import java.util.List;

import com.j256.ormlite.db.BaseDatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;

/**
 * HyberSQL database type information used to create the tables, etc..
 * 
 * @author graywatson
 */
public class HsqldbDatabaseType extends BaseDatabaseType {

	private final static String DATABASE_URL_PORTION = "hsqldb";
	private final static String DRIVER_CLASS_NAME = "org.hsqldb.jdbcDriver";
	private final static String DATABASE_NAME = "HSQLdb";

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
	protected void appendLongStringType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		sb.append("LONGVARCHAR");
	}

	@Override
	protected void appendBooleanType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		sb.append("BIT");
	}

	@Override
	protected void appendByteArrayType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		// was: "BINARY"
		if (fieldWidth == 0) {
			sb.append("VARBINARY(255)");
		} else {
			sb.append("VARBINARY(").append(fieldWidth).append(')');
		}
	}

	@Override
	protected void appendSerializableType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		appendByteArrayType(sb, fieldType, fieldWidth);
	}

	@Override
	protected void configureGeneratedIdSequence(StringBuilder sb, FieldType fieldType, List<String> statementsBefore,
			List<String> additionalArgs, List<String> queriesAfter) {
		// needs to match dropColumnArg()
		StringBuilder seqSb = new StringBuilder(128);
		seqSb.append("CREATE SEQUENCE ");
		appendEscapedEntityName(seqSb, fieldType.getGeneratedIdSequence());
		if (fieldType.getSqlType() == SqlType.LONG) {
			seqSb.append(" AS BIGINT");
		} else {
			// integer is the default
		}
		// with hsqldb (as opposed to all else) the sequences start at 0, grumble
		seqSb.append(" START WITH 1");
		statementsBefore.add(seqSb.toString());
		sb.append("GENERATED BY DEFAULT AS IDENTITY ");
		configureId(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
	}

	@Override
	public void appendEscapedEntityName(StringBuilder sb, String name) {
		sb.append('\"').append(name).append('\"');
	}

	@Override
	public void dropColumnArg(FieldType fieldType, List<String> statementsBefore, List<String> statementsAfter) {
		if (fieldType.isGeneratedIdSequence()) {
			StringBuilder sb = new StringBuilder(64);
			sb.append("DROP SEQUENCE ");
			appendEscapedEntityName(sb, fieldType.getGeneratedIdSequence());
			statementsAfter.add(sb.toString());
		}
	}

	@Override
	public boolean isIdSequenceNeeded() {
		return true;
	}

	@Override
	public boolean isSelectSequenceBeforeInsert() {
		return true;
	}

	@Override
	public boolean isVarcharFieldWidthSupported() {
		/**
		 * In version 2.X, VARCHAR(width) is required. In 1.8.X or before, it is not supported. Wonderful.
		 */
		if (driver != null && driver.getMajorVersion() >= 2) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isLimitAfterSelect() {
		return true;
	}

	@Override
	public void appendLimitValue(StringBuilder sb, long limit, Long offset) {
		// the 0 is the offset, could also use TOP X
		sb.append("LIMIT ");
		if (offset == null) {
			sb.append("0 ");
		} else {
			sb.append(offset).append(' ');
		}
		sb.append(limit).append(' ');
	}

	@Override
	public boolean isOffsetLimitArgument() {
		return true;
	}

	@Override
	public void appendOffsetValue(StringBuilder sb, long offset) {
		throw new IllegalStateException("Offset is part of the LIMIT in database type " + getClass());
	}

	@Override
	public void appendSelectNextValFromSequence(StringBuilder sb, String sequenceName) {
		sb.append("CALL NEXT VALUE FOR ");
		appendEscapedEntityName(sb, sequenceName);
	}

	@Override
	public boolean isEntityNamesMustBeUpCase() {
		return true;
	}

	@Override
	public String getPingStatement() {
		return "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SYSTEM_TABLES";
	}

	@Override
	public boolean isCreateIfNotExistsSupported() {
		// support for EXISTS subquery was added in 2.3.x, thanks to @lukewhitt
		return (driver != null && driver.getMajorVersion() >= 2 && driver.getMinorVersion() >= 3);
	}
}
