package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Daniyar Itegulov
 */
@WebServlet(
        name = "BoxAuthServlet",
        urlPatterns = {"/auth"}
)
public class BoxAuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(String.format("https://app.box.com/api/oauth2/authorize?" +
                "response_type=code&redirect_uri=http://%s:%d/complete&client_id=%s&state=kudah",
                req.getServerName(),
                req.getServerPort(),
                BoxApiConstants.CLIENT_ID));
    }
}
