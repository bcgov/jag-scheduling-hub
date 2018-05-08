package hub.support;

import hub.helper.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class EnvironmentTest {

    private Environment environment;

    @Before
    public void environment() {
        environment = new Environment();
    }

    @Test
    public void isHelperToGetSystemProperties() {
        System.setProperty("Life", "is good");

        assertThat(environment.getValue("Life"), equalTo("is good"));
    }

    @Test
    public void isHelperToGetEnvironementVariable() {
        assertThat(environment.getValue("War"), equalTo(null));
    }
}