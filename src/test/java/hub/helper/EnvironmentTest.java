package hub.helper;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EnvironmentTest {

    @Test
    public void isReadyForTdd() {
        assertThat(1+1, equalTo(2));
    }
}
