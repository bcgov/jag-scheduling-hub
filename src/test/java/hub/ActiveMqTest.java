package hub;

import hub.http.ActiveMqServlet;
import hub.support.AsyncHttpResponse;
import hub.support.HavingHubRunning;
import hub.support.ThirdParty;
import org.junit.Assert;
import org.junit.Test;

import static hub.support.AsyncGetRequest.asyncGet;
import static org.hamcrest.CoreMatchers.equalTo;

public class ActiveMqTest extends HavingHubRunning {

    @Test
    public void allowsToPublishOneMessage() throws Exception {
        context.setInitParameter("org.apache.activemq.brokerURL", "vm://localhost");
        context.addServlet(ActiveMqServlet.class, "/message/*");
        server.start();

        AsyncHttpResponse client = asyncGet("http://localhost:8888/message/this-queue?type=queue");
        ThirdParty.post("http://localhost:8888/message/this-queue?type=queue", "body=hello");

        Assert.assertThat(client.getBody(), equalTo("hello"));
    }
}