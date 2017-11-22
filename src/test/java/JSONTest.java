import org.junit.Test;

import java.util.Random;

public class JSONTest {


    @Test
    public void simpleTest() {
        for (int i = 0; i < 100; i++) {
            System.out.println(new Random().nextInt(4)+1);
        }

    }


    @Test
    public void test() {
        System.out.println();
        String fileSplit = System.getProperty("file.separator");
        System.out.println(System.getProperty("user.home") + fileSplit + "AppData" + fileSplit + "Local" + fileSplit + "Google" + fileSplit + "Chrome" + fileSplit + "User Data");
    }

    private String unwarpJSONP(String jsonp) {
        System.out.println("*************************" + jsonp);
        int length = jsonp.indexOf("(");
        String json = jsonp.substring(length + 1);
        int end = json.lastIndexOf(")");
        json = json.substring(0, end);
        return json;
    }
}
