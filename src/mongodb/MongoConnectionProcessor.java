package mongodb;

import com.mongodb.*;
import mongodb.MongoItem;
import sqlite.Query;
import sqlite.Update;

import java.sql.SQLException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Omicron
 */
public final class MongoConnectionProcessor implements Runnable {

    private final MongoClient client;
    private DB database;
    private final Thread thread;
    private String dbName;
    private boolean running;
    private final Queue<MongoItem> items = new ConcurrentLinkedQueue<MongoItem>();
    private final Object lock = new Object();


    public MongoConnectionProcessor(MongoClient client, String db) {
        this.client = client;
        this.dbName = db;
        this.thread = new Thread(this);
    }


    public void start() {
        if (running) {
            throw new IllegalStateException("The processor is already running.");
        }
        thread.start();
    }

    public void stop() {
        if (!running) {
            throw new IllegalStateException("The processor is already stopped.");
        }
        running = false;
    }

    public boolean isConnected() {
        try {
            database.getCollectionNames();
        } catch (Exception e) {
            System.out.println("We're not connected to mongodb");
            return false;
        }
            return true;
    }

    public boolean safeConnect() {
        System.out.println("SAFECONNECt");
        this.database = client.getDB(dbName);
        for (int i=0; i < 100; i++) {
            database.getCollection("patients").insert(new BasicDBObject("i", i));
        }
        return isConnected();
    }

    @Override
    public void run() {

        running = true;
        MainLoop:
        while (running) {
            if (!isConnected()) {
                while (!safeConnect()) {

                }
            }

            while (!items.isEmpty()) {
                //synchronized (lock) {
                MongoItem item = items.peek();
                if (!item.canExecute()) {
                    items.remove();
                    continue;
                }
                try {
                    if (!item.execute(this.database)) {
                        continue MainLoop;
                    }
                } catch (MongoException e) {
                    e.printStackTrace();
                }
                items.remove();
                //}
            }
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        client.close();
    }

    public void waitForPendingPackets() {
        while (!items.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public boolean executeQuery(MongoQuery query) {

        if (!isConnected()) {
            synchronized (this) {
                this.notify();
            }
            return false;
        }
        boolean result = items.offer(query);
        synchronized (this) {
            this.notify();
        }
        return result;
    }

    public boolean forceQuery(MongoQuery query) {
        boolean result = items.offer(query);
        synchronized (this) {
            this.notify();
        }
        return result;
    }

    public boolean forceUpdate(MongoUpdate update) {

        boolean result = items.offer(update);
        if (!isConnected()) {
            return false;
        }
        return update.execute(database);
    }

    public boolean executeUpdate(MongoUpdate update) {

        boolean result = items.offer(update);
        synchronized (this) {
            this.notify();
        }
        return result;
    }

    public boolean blockingQuery(MongoQuery query) throws SQLException {

        synchronized (lock) {
            if (!isConnected()) {
                return false;
            }
            return query.execute(database);
        }
    }
}
