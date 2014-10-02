package config;

import server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;



public final class ServerProperties {

    private static File configFile = null;

    public File getConfigFile() {
        return configFile;
    }
    public ServerProperties(String file) {
        configFile = new File(file);
    }

	public static final Properties properties = new Properties();

	public static void read() {
		if (configFile.exists()) {
			try {
				properties.load(new FileInputStream(configFile));
			} catch (FileNotFoundException e) {
				Server.log("STOP(CONFIG FILE WAS NOT FOUND!)",e);
			} catch (IOException e) {
				Server.log("STOP(CONFIG FILE THREW IO EXCEPTION)",e);
			}
			Config.SERVER_NAME =(properties.getProperty("SERVER_NAME"));
			Config.SERVER_VERSION = Integer.parseInt(properties.getProperty("SERVER_VERSION"));
            Config.SERVER_TIMEZONE = (properties.getProperty("SERVER_TIMEZONE"));
			Config.ENABLE_MONGOPUSH = Boolean.parseBoolean(properties.getProperty("ENABLE_MONGOPUSH"));
			Config.HTTP_PORT = Integer.parseInt(properties.getProperty("HTTP_PORT"));
			Config.DATABASE_HOST = (properties.getProperty("DATABASE_HOST"));
			Config.MONGODB_HOST = (properties.getProperty("MONGODB_HOST"));
			Config.MAX_LOGFILE_SIZE_MB = Integer.parseInt(properties.getProperty("MAX_LOGFILE_SIZE_MB"));
		} else {
            Server.log("WARNING","Configuration file was not found in project root. Now running from config.Config default properties");
        }
	}

}
