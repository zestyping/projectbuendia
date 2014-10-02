package server;

import config.Config;
import config.ServerProperties;
import io.FileChecks;
import io.Logging;
import sqlite.ConnectionProcessor;
import sqlite.SQLITEConnection;
import sqlite.impl.OverrideQuery;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

        //System.out.println("Getting roots");

    }

}
