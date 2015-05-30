package servlets;

import ourapp.TagExtractor;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
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
        return Arrays.asList("Genka", "Gennady Korotkevich", "Windows Genuine Advantage", "Kudah", "Kukarek", "Mamku tvoyu ebal");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        List<String> tags = null;
        try {
            tags = TagExtractor.findMatching(query);
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        //Collection<String> tags = getTags(query);
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
