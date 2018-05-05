package hub.camel;

import hub.SourceCode;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Startup
@ContextName("cdi-context")
public class PingRouteBuilder extends RouteBuilder {

    @Inject
    SourceCode sourceCode;

    @Override
    public void configure() {
        from("direct:ping").setBody(constant(sourceCode.version()));
    }
}
