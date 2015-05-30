package servlets;

import box.BoxAccount;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniyar Itegulov
 */
@WebServlet(
        name = "AuthCompleteServlet",
        urlPatterns = {"/complete"}
)
public class AuthCompleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter pw = resp.getWriter()) {
            if (req.getAttribute("error") != null) {
                pw.println("Something went wrong, sorry");
            } else {
                String code = req.getParameter("code");
                String state = req.getParameter("state");
                pw.println("Mda chet");
                if (!state.equals("kudah")) {
                    pw.println("Malformed request, sorry");
                } else {
                    HttpClient httpclient = HttpClients.createDefault();
                    HttpPost post = new HttpPost("https://app.box.com/api/oauth2/token");
                    List<NameValuePair> params = new ArrayList<>(2);
                    params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                    params.add(new BasicNameValuePair("code", code));
                    params.add(new BasicNameValuePair("client_id", BoxApiConstants.CLIENT_ID));
                    params.add(new BasicNameValuePair("client_secret", BoxApiConstants.CLIENT_SECRET));
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    HttpResponse response = httpclient.execute(post);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String entityString = EntityUtils.toString(entity);
                        pw.println(entityString);
                        BoxAccount boxAccount = new BoxAccount(entityString);
                        pw.println(boxAccount);
                    } else {
                        pw.println("Ti che vashe ti che");
                    }
                }
            }
        }
    }
}
