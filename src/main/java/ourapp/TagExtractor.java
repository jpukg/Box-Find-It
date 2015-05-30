package ourapp;

import box.BoxAccount;
import box.BoxDirectory;
import box.BoxElement;
import box.BoxFile;
import idolondemand.entityextractor.EntitiesExtractor;
import idolondemand.entityextractor.EntityType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniyar Itegulov
 */
public class TagExtractor {
    public static Set<String> extract(BoxAccount boxAccount) throws IOException {
        return extract(boxAccount.getRoot(), boxAccount);
    }

    public static Set<String> extract(BoxDirectory boxDirectory, BoxAccount boxAccount) throws IOException {
        Set<String> result = new HashSet<>();
        for (BoxElement element : boxDirectory.getElementList()) {
            if (element instanceof BoxFile) {
                BoxFile file = (BoxFile) element;
                byte[] data = boxAccount.getFileContent(file.getId());
                String string = new String(data, StandardCharsets.UTF_8);
                Set<String> tags = EntitiesExtractor.fetchByText(string, EntityType.PEOPLE_ENG,
                        EntityType.COMPAINES_ENG, EntityType.COMPAINES_ENG);
                result.addAll(tags);
            } else {
                result.addAll(extract((BoxDirectory) element, boxAccount));
            }
        }
        return result;
    }
}
