package servlets;

import box.BoxAccount;
import box.BoxDirectory;
import box.BoxElement;
import box.BoxFile;
import ourapp.TagExtractor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        BoxAccount boxAccount = new BoxAccount(entity);
        Set<Long> set;
        try {
            set = TagExtractor.findFileIds(tag);
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{\"elements\":[");
        query(sb, boxAccount.getRoot(), set);
        sb.append("]}");
        try (PrintWriter pw = resp.getWriter()) {
            pw.println(sb.toString());
        }
    }

    private void query(StringBuilder sb, BoxDirectory boxDirectory, Set<Long> set) {
        for (int i = 0; i < boxDirectory.getElementList().size(); i++) {
            BoxElement element = boxDirectory.getElementList().get(i);
            if (element instanceof BoxFile) {
                BoxFile file = (BoxFile) element;
                if (set.contains(file.getId())) {
                    sb.append("{")
                            .append("\"name\":\"").append(file.getName()).append("\",")
                            .append("\"preview\":\"").append(URL).append("\"")
                            .append("},");
                }
            } else {
                query(sb, (BoxDirectory) element, set);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
    }
}
