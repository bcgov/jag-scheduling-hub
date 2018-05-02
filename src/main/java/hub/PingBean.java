package hub;

import javax.inject.Named;

@Named
public class PingBean {

    public String version() {
        return System.getenv("OPENSHIFT_BUILD_COMMIT");
    }

}
