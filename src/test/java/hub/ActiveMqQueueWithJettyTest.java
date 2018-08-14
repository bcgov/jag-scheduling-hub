package hub;

import hub.http.ActiveMqServlet;
import hub.support.HavingHubRunning;
import hub.support.ThirdParty;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

import static hub.support.Resource.bodyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActiveMqQueueWithJettyTest extends HavingHubRunning {

    private String url = "http://localhost:" + port + "/message/tuesday?type=queue";

    @Test
    public void allowsToPublishOneMessage() throws Exception {
        context.addServlet(new ServletHolder(new ActiveMqServlet()), "/message/*");
        server.start();
        ThirdParty.post(url, "body=coucou");

        assertThat(bodyOf(url), equalTo("coucou"));
    }
}
