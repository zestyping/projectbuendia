package mongodb;

import com.mongodb.*;
import sqlite.Item;
import sqlite.SQLITEConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * @author Pim de Witte
 */
public abstract class MongoQuery implements MongoItem {
    private BasicDBObject obj;
    private String collection;

    protected MongoQuery(String collection, BasicDBObject obj) {
        this.obj = obj;
        this.collection = collection;
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    public abstract void execute(DBCursor result) throws MongoException;

    @Override
    public final boolean execute(DB db) {
        DBCursor cursor = db.getCollection(collection).find(obj);
        this.execute(cursor);
        return true;
    }
}
