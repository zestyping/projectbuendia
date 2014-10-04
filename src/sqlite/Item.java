package sqlite;

import java.sql.SQLException;

/**
 * @author Pim de Witte
 */
public interface Item {
	boolean canExecute();
	boolean execute(SQLITEConnection connection) throws SQLException;
}
