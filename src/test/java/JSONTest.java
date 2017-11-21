import org.junit.Test;

public class JSONTest {



    @Test
    public void test(){
        System.out.println();
        String fileSplit =System.getProperty("file.separator");
        System.out.println(System.getProperty("user.home")+fileSplit+"AppData"+fileSplit+"Local"+fileSplit+"Google"+fileSplit+"Chrome"+fileSplit+"User Data");
    }

    private String unwarpJSONP(String jsonp) {
        System.out.println("*************************"+jsonp);
        int length = jsonp.indexOf("(");
        String json = jsonp.substring(length + 1);
        int end =json.lastIndexOf(")");
        json = json.substring(0, end);
        return json;
    }
}
