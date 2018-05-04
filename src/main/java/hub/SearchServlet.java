package hub;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SearchServlet", urlPatterns = {"/form7s"}, loadOnStartup = 1)
public class SearchServlet extends HttpServlet {

    @Inject
    @ContextName("cdi-context")
    private CamelContext context;

    private final static Logger LOGGER = Logger.getLogger(SearchServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ProducerTemplate producer = context.createProducerTemplate();
        String result = producer.requestBody("direct:search", req.getParameter("caseNumber"), String.class);
        LOGGER.log(Level.INFO, result);

        res.setHeader("content-type", "application/json");
        ServletOutputStream out = res.getOutputStream();
        out.print(result);

        if ("not-found".equalsIgnoreCase(result)) {
            res.setHeader("content-type", "text/plain");
            res.setStatus(404);
        }
    }

}

