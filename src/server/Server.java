package server;

import config.Config;
import config.ServerProperties;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

    public static final void main(String[] args) {
        try {
            System.setErr(new PrintStream(new ErrorFile(), true));
        }catch (Exception e) {
            log("SEVERE",e);
        }
        systemProperties = new ServerProperties(Config.CONFIGURATION_FILE);
        ServerProperties.read();
        calendar.setTimeZone(TimeZone.getTimeZone(Config.SERVER_TIMEZONE));
        log("RESTART","--------------------------------------------------------------------");
        log("INFO", "The server was restarted");

        try {
            //throw new Exception();
        }catch (Exception e) {
            log("CRUCIAL",e);
        }

    }




    public static void log(String errorLevel,Exception e) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            System.err.print("["+errorLevel+" (version "+Config.SERVER_VERSION+") "+sdf.format(calendar.getTime())+"]");
            e.printStackTrace(System.err);
    }

    public static void log(String errorLevel,String s) {
        //synchronized(System.err) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.err.println("[" + errorLevel + " (version " + Config.SERVER_VERSION + ") " + sdf.format(calendar.getTime()) + "] " + s);
    }






    private static class ErrorFile extends FileOutputStream {
        private static String getErrorFile() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
            String name = ("errors/Error-v" +
                    Config.SERVER_VERSION + "-" +
                    sdf.format(calendar.getTime()) +
                    ".log");
            return name;
        }

        private final PrintStream errorStream = System.err;

        ErrorFile() throws FileNotFoundException {
            super(ErrorFile.getErrorFile(), true);
        }

        @Override
        public void write(int b) throws IOException {
            errorStream.write(b);
            super.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            errorStream.write(b);
            super.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            errorStream.write(b, off, len);
            super.write(b, off, len);
        }
    }

}
