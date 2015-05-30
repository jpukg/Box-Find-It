package servlets;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Ignat Loskutov
 */
@WebServlet(
        name = "JsonServlet",
        urlPatterns = {"/query.json"}
)
public class JsonServlet extends HttpServlet {
    Collection<String> getTags(String pattern) {
        return Arrays.asList("Genka", "Gennady Korotkevich", "Windows Genuine Advantage", "Kudah", "Kukarek", "Mamku tvoyu ebal");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        Collection<String> tags = getTags(query);
        try (OutputStream os = resp.getOutputStream()) {
            try (final JsonGenerator generator = Json.createGenerator(os)) {
                generator.writeStartArray();
                for (String s : tags) {
                    generator.writeStartObject();
                    generator.write("value", s);
                    generator.writeEnd();
                }
                generator.writeEnd();
            }
        }
    }
}
