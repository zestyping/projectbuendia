package sqlite;

import java.sql.SQLException;

/**
 * @author Omicron
 */
public interface Item {
	boolean canExecute();
	boolean execute(SQLITEConnection connection) throws SQLException;
}
