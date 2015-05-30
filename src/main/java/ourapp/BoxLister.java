package ourapp;

import box.BoxAccount;
import box.BoxDirectory;
import box.BoxElement;
import box.BoxFile;

/**
 * @author Daniyar Itegulov
 */
public class BoxLister {
    private static final String URL = "https://pp.vk.me/c624218/v624218371/4764/D9C41NS_3vQ.jpg";

    public static void list(StringBuilder sb, BoxDirectory boxDirectory) {
        sb.append("{");
        sb.append("\"name\":\"").append(boxDirectory.getName()).append("\",");
        sb.append("\"preview\":\"").append(URL).append("\",");
        sb.append("\"subfiles\":[");
        for (int i = 0; i < boxDirectory.getElementList().size(); i++) {
            BoxElement element = boxDirectory.getElementList().get(i);
            if (element instanceof BoxFile) {
                BoxFile file = (BoxFile) element;
                sb.append("{")
                        .append("\"name\":\"").append(file.getName()).append("\",")
                        .append("\"preview\":\"").append(URL).append("\"")
                        .append("}");
            } else {
                list(sb, (BoxDirectory) element);
            }
            if (i != boxDirectory.getElementList().size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        sb.append("}");
    }
}
