package server;

import com.mongodb.*;
import config.Config;
import config.ServerProperties;
import io.Logging;
import mongodb.MongoConnectionProcessor;
import mongodb.MongoQuery;
import sqlite.ConnectionProcessor;
import sqlite.SQLITEConnection;
import sqlite.impl.OverrideQuery;
import web.MinimalServer;

import java.io.*;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * Created by wwadewitte on 10/1/14.
 */
public final class Server {

    private static ServerProperties systemProperties;
    public static ServerProperties getServerProperties() {
        return systemProperties;
    }
    private static Calendar calendar = new GregorianCalendar();
    private static ConnectionProcessor localDatabase;
    private static MongoConnectionProcessor mongoDatabase;

    public static Calendar getCalendar() {
        return calendar;
    }

    public static final void main(String[] args) throws InterruptedException {

        systemProperties = new ServerProperties(Config.CONFIGURATION_FILE);
        ServerProperties.read();
        try {
            System.setErr(new PrintStream(new Logging.ErrorFile(), true));
        }catch (Exception e) {
            System.out.println("Error was thrown with config.prop config. Switching to errors/");
            Logging.log("SEVERE", e);
            try {
                System.setErr(new PrintStream(new Logging.ErrorFile("errors/"), true));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                System.out.println("Closing error logging stream");
                // close the stream so it is no longer logged anywhere
                System.err.close();
            }
        }
        calendar.setTimeZone(TimeZone.getTimeZone(Config.SERVER_TIMEZONE));
        Logging.log("RESTART","--------------------------------------------------------------------");
        Logging.log("INFO", "The server was restarted");


        /*

        START SQLITE

         */
        localDatabase  = new ConnectionProcessor(new SQLITEConnection(Config.SQLITE_PATH));
        localDatabase.start();

        //Logging.writeSampleErrors(10);
        long start = System.currentTimeMillis();
        System.out.println("Putting thread to sleep until database is connected..");

        while(!localDatabase.isConnected()) {
            Thread.sleep(1);
        }

        long end = System.currentTimeMillis();

        Logging.log("INFO", Config.SQLITE_PATH + " (sqlite) took "+(end - start)+" ms to connect");

        localDatabase.executeQuery(new OverrideQuery("select 1 as `hah`") {

            @Override
            public void execute(ResultSet result) throws SQLException {
                while(result.next()) {
                    System.out.println(result.getInt("hah"));
                }
            }
        });




        /*

        START MONGODB

         */

        start = System.currentTimeMillis();
        System.out.println("Putting thread to sleep until database is connected..");
        try {
            mongoDatabase  = new MongoConnectionProcessor(new MongoClient(Config.MONGODB_HOST),Config.MONGODB_DB);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mongoDatabase.start();

        while(!mongoDatabase.isConnected()) {
            Thread.sleep(1);
        }

        end = System.currentTimeMillis();

        Logging.log("INFO", Config.MONGODB_DB + "@"+Config.MONGODB_HOST+" (mongo) took "+(end - start)+" ms to connect");
        BasicDBObject query = new BasicDBObject("i", 71);

        mongoDatabase.executeQuery(new MongoQuery("patients", query) {
            @Override
            public void execute(DBCursor result) throws MongoException {
                System.out.println("EXECUTIIIIN");
                try {
                    while(result.hasNext()) {
                        System.out.println(result.next());
                    }
                } finally {
                    result.close();
                }
            }
        });



        /*
        finally, we start the rest server
         */
        try {
            MinimalServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
