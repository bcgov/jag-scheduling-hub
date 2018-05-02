package hub;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Startup
@ContextName("cdi-context")
public class HubRouteBuilder extends RouteBuilder {

    @Inject
    PingBean pingBean;

    @Override
    public void configure() throws Exception {
        from("direct:ping").setBody(simple(pingBean.version()));
    }
}
