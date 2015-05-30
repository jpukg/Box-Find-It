package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ignat Loskutov
 */
@WebServlet(
        name = "JsonServlet",
        urlPatterns = {"/query.json"}
)
public class JsonServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
            try (OutputStream os = resp.getOutputStream()) {
                os.write((String.format("[\n" +
                        "  {\n" +
                        "    \"value\": \"%s\",\n" +
                        "    \"tokens\": [\n" + // not sure if it’s used in any way
                        "      \"The\",\n" +
                        "      \"Broadway\",\n" +
                        "      \"Melody\"\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"value\": \"All the Kings Men\",\n" +
                        "    \"tokens\": [\n" +
                        "      \"All\",\n" +
                        "      \"the\",\n" +
                        "      \"Kings\",\n" +
                        "      \"Men\"\n" +
                        "    ]\n" +
                        "  }\n" +
                        "]", query).getBytes()));
            }
    }
}
