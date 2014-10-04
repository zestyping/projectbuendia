package sqlite.impl;

import sqlite.Update;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: pimdewitte
 * Date: 7/27/14
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SQLiteUpdate extends Update {
    public SQLiteUpdate(String name) {
        super(name, false);
    }

    public abstract boolean execute(ResultSet result) throws SQLException;

    @Override
    public boolean canExecute() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
