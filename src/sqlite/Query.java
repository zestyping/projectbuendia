package sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Omicron
 */
public abstract class Query implements Item {
	private final String sql;
    private final long startTime;
    private final boolean sensitive;
    

	protected Query(String sql, boolean sensitive) {
		this.sql = sql;
        this.sensitive = sensitive;
        this.startTime = System.currentTimeMillis();
	}

    public final String getString(){
        return this.sql;
    }
    public final boolean isSensitiveQuery(){
        return this.sensitive;
    }
    public final long getStartTime(){
        return this.startTime;
    }

	public abstract void execute(ResultSet result) throws SQLException;

	@Override
	public final boolean execute(SQLITEConnection connection) throws SQLException {
		long startTime = System.currentTimeMillis();
		ResultSet result = connection.executeQuery(sql);

		if ((System.currentTimeMillis() - startTime) >= 2000) {
			System.err.println("Query took: " + sql + ' ' + (System.currentTimeMillis() - startTime));
		}
		this.execute(result);
		return true;
	}
}
