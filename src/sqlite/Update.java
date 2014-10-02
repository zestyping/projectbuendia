package sqlite;

/**
 * @author Omicron
 */
public abstract class Update implements Item {
	private final String sql;
	private final boolean newStatement;

	protected Update(String sql, boolean newStatement) {
		this.sql = sql;
		this.newStatement = newStatement;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public final boolean execute(SQLITEConnection connection) {
		long startTime = System.currentTimeMillis();
		int result = connection.executeUpdate(sql, newStatement);
		if ((System.currentTimeMillis() - startTime) >= 5000) {
			System.err.println("Update took: " + (System.currentTimeMillis() - startTime));
		}
		if (result == -1) {
			return false;
		}
		if (result == -2) {
			return true;
		}
		return true;
	}
}
