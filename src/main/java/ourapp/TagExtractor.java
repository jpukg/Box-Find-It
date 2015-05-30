package ourapp;

import box.BoxAccount;
import box.BoxDirectory;
import box.BoxElement;
import box.BoxFile;
import idolondemand.entityextractor.EntitiesExtractor;
import idolondemand.entityextractor.EntityType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author Daniyar Itegulov
 */
public class TagExtractor {
    private static final String INSERT_SQL =  "insert into tags values(?,?,?)";

    private static Connection getConnection() throws URISyntaxException, SQLException, ClassNotFoundException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        int port = dbUri.getPort();

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }



    public static Set<String> extract(BoxAccount boxAccount) throws IOException,
            URISyntaxException, SQLException, ClassNotFoundException {
        return extract(boxAccount.getRoot(), boxAccount);
    }

    public static Set<String> extract(BoxDirectory boxDirectory, BoxAccount boxAccount) throws IOException,
            ClassNotFoundException, SQLException, URISyntaxException {
        Set<String> result = new HashSet<>();
        Connection connection = getConnection();
        for (BoxElement element : boxDirectory.getElementList()) {
            if (element instanceof BoxFile) {
                BoxFile file = (BoxFile) element;
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT loaded, tag FROM tags WHERE file_id = " + file.getId());
                if (!rs.next()) {
                    byte[] data = boxAccount.getFileContent(file.getId());
                    String string = new String(data);
                    Set<String> tags = EntitiesExtractor.fetchByText(string, EntityType.PEOPLE_ENG,
                            EntityType.COMPAINES_ENG, EntityType.COMPAINES_ENG);
                    result.addAll(tags);
                    String[] array = new String[tags.size()];
                    tags.toArray(array);
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
                    ps.setLong(1, file.getId());
                    ps.setDate(2, new Date(file.getModifiedAt().toDate().getTime()));
                    ps.setArray(3, connection.createArrayOf("text", array));
                    ps.executeUpdate();
                } else {
                    Date date = rs.getDate("loaded");
                    Array array = rs.getArray("tag");
                    String[] tags = (String[]) array.getArray();
                    Collections.addAll(result, tags);
                }
            } else {
                result.addAll(extract((BoxDirectory) element, boxAccount));
            }
        }
        connection.close();
        return result;
    }

    public static List<String> findMatching(String string) throws ClassNotFoundException, SQLException, URISyntaxException {
        Connection connection = getConnection();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT tag FROM (SELECT unnest(tag) tag FROM tags) x WHERE tag LIKE '%" + string + "%'");
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            //Array array = rs.getArray("tag");
            //String[] tags = (String[]) array.getArray();
            //Collections.addAll(list, tags);
            String tag = rs.getString("tag");
            list.add(tag);
        }
        connection.close();
        return list;
    }
}
