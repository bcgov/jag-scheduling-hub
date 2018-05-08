package hub;

import hub.helper.Environment;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SourceCode {

    @Inject
    Environment environment;

    public String version() {
        return environment.getValue("OPENSHIFT_BUILD_COMMIT");
    }

}