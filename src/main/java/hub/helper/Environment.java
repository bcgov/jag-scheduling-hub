package hub.helper;

import hub.CsoSearch;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Environment {

    private final static Logger LOGGER = Logger.getLogger(CsoSearch.class.getName());

    public static String getValue(String key) {
        LOGGER.log(Level.INFO, "env."+key+"=" + System.getenv(key));
        LOGGER.log(Level.INFO, "properties."+key+"=" + System.getProperty(key));

        return System.getenv(key) != null ? System.getenv(key) : System.getProperty(key);
    }
}
