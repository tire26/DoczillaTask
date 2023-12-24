package org.ru;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static Logger logger = Logger.getLogger(ConfigReader.class.getName());

    public static Properties getConfig() {
        Properties properties = new Properties();

        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return properties;
    }
}
