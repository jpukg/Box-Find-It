package idolondemand;

import idolondemand.entityextractor.EntitiesExtractor;
import idolondemand.entityextractor.EntityType;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Daniyar Itegulov
 */
public class EntitiesExtractorTest {

    private void testText(String text, String[] result, EntityType... types) {
        try {
            Set<String> res = EntitiesExtractor.fetchByText(text, types);
            System.out.println(res);
            for (String str : result) {
                assertTrue(res.contains(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private void testURL(String url, String[] result, EntityType... types) {
        try {
            Set<String> res = EntitiesExtractor.fetchByURL(url, types);
            System.out.println(res);
            for (String str : result) {
                assertTrue(res.contains(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private void testFile(String fileName, String[] result, EntityType... types) {
        try {
            Set<String> res = EntitiesExtractor.fetchByFile(new File(fileName), types);
            System.out.println(res);
            for (String str : result) {
                assertTrue(res.contains(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void test1_fetchText() throws Exception {
        testText("President Barack Obama paid tribute to anti-apartheid hero Nelson Mandela as he flew to South Africa" +
                        " on Friday but played down expectations of a meeting with the ailing black leader during an Africa" +
                        " tour promoting democracy and food security",
                new String[]{"Barack Obama", "Nelson Mandela"},
                EntityType.PEOPLE_ENG);
    }

    @Test
    public void test2_fetchURL() throws Exception {
        testURL("http://www.bbc.co.uk/news/business/companies/",
                new String[]{"BBC News", "Tata Steel Ltd", "Twitter"},
                EntityType.COMPAINES_ENG);
    }

    @Test
    public void test3_fetchFile() throws Exception {
        System.out.println(Arrays.toString(new File(".").list()));
        testFile("src/main/testRes/idolondemand/obama.txt",
                new String[]{"Barack Obama", "Nelson Mandela"},
                EntityType.PEOPLE_ENG);
    }



}