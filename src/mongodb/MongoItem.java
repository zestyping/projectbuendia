package mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import sqlite.SQLITEConnection;

import java.sql.SQLException;

/**
 * @author Pim de Witte
 */
public interface MongoItem {
	boolean canExecute();
	boolean execute(DB db) throws MongoException;
}
