package hub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PingServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType( "application/json" );
        String version = System.getenv("OPENSHIFT_BUILD_COMMIT");
        response.getWriter().print( "{\"version\":\"" + version + "\"}" );
    }
}
