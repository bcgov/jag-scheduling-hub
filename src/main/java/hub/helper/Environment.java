package hub.helper;

import hub.CsoSearch;

import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
public class Environment {

    private final static Logger LOGGER = Logger.getLogger(CsoSearch.class.getName());

    public String getValue(String key) {
        LOGGER.log(Level.INFO, "env."+key+"=" + System.getenv(key));
        LOGGER.log(Level.INFO, "properties."+key+"=" + System.getProperty(key));

        String value = System.getProperty(key);
        return value != null ? value : System.getenv(key);
    }
}
