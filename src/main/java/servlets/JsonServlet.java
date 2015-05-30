package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Ignat Loskutov
 */
@WebServlet(
        name = "JsonServlet",
        urlPatterns = {"/query.json"}
)
public class JsonServlet extends HttpServlet {
    Collection<String> getTags(String pattern) {
        return Arrays.asList("Genka", "Gennady Korotkevich", "Windows Genuine Advantage", "mamka tvoya");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        StringBuilder json = new StringBuilder("[\n");
        Collection<String> tags = getTags(query);
        //Json
        try (OutputStream os = resp.getOutputStream()) {
            for (String s : tags) {
                json.append(String.format("\t{\"value\": \"%s\"},\n", s));
            }
            json.deleteCharAt(json.length() - 2);
            json.append("]");
            os.write(json.toString().getBytes());
        }
    }
}
