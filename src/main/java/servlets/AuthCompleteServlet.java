package servlets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
        if (req.getAttribute("error") != null) {
            resp.getOutputStream().write("Something went wrong, sorry".getBytes());
        } else {
            String code = req.getParameter("code");
            String state = req.getParameter("state");
            resp.getOutputStream().write((code + " ").getBytes());
            resp.getOutputStream().write((state + "\n").getBytes());
            if (!state.equals("kudah")) {
                resp.getOutputStream().write("Malformed request, sorry".getBytes());
            } else {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost post = new HttpPost("https://app.box.com/api/oauth2/token");
                List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                params.add(new BasicNameValuePair("code", code));
                params.add(new BasicNameValuePair("client_id", BoxApiConstants.CLIENT_ID));
                params.add(new BasicNameValuePair("client_secret", BoxApiConstants.CLIENT_SECRET));
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                HttpResponse response = httpclient.execute(post);
                HttpEntity entity = response.getEntity();
                resp.getOutputStream().write("MMMMMM!".getBytes());
                byte[] buf = new byte[2048];
                if (entity != null) {
                    try (InputStream is = entity.getContent()) {
                        int len = is.read(buf);
                        resp.getOutputStream().write(buf, 0, len);
                    }
                }
            }
        }
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }
}
