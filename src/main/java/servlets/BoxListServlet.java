package servlets;

import box.BoxAccount;
import box.BoxDirectory;
import box.BoxElement;
import box.BoxFile;
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
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Set;

/**
 * @author Daniyar Itegulov
 */
@WebServlet(
        name = "BoxListServlet",
        urlPatterns = {"/find"}
)
public class BoxListServlet extends HttpServlet {
    private static final String URL = "https://pp.vk.me/c624218/v624218371/4764/D9C41NS_3vQ.jpg";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tag = req.getParameter("tag");
        String entity = req.getParameter("entity");
        if (tag == null) {
            try (PrintWriter pw = resp.getWriter()) {
                pw.println("Tag must be specified");
            }
            return;
        } else if (entity == null) {
            try (PrintWriter pw = resp.getWriter()) {
                pw.println("Entity must be specified");
            }
            return;
        }
        BoxAccount boxAccount = new BoxAccount(entity);
        System.out.println("Let's find " + tag + " in " + boxAccount);
        Set<Long> set;
        try {
            set = TagExtractor.findFileIds(tag);
        } catch (SQLException  e) {
            e.printStackTrace();
            return;
        }
        try (OutputStream os = resp.getOutputStream()) {
            try (final JsonGenerator generator = Json.createGenerator(os)) {
                generator.writeStartArray();
                query(generator, boxAccount.getRoot(), set);
                generator.writeEnd();
            }
        }
    }

    private void query(JsonGenerator generator, BoxDirectory boxDirectory, Set<Long> set) {
        for (int i = 0; i < boxDirectory.getElementList().size(); i++) {
            BoxElement element = boxDirectory.getElementList().get(i);
            if (element instanceof BoxFile) {
                BoxFile file = (BoxFile) element;
                if (set.contains(file.getId())) {
                    generator.writeStartObject();
                    generator.write("name", file.getName());
                    generator.write("preview", URL);
                    generator.writeEnd();
                }
            } else {
                query(generator, (BoxDirectory) element, set);
            }
        }
    }
}
