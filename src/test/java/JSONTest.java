import org.junit.Test;

public class JSONTest {



    @Test
    public void test(){
        String str ="jsonp1514({\"pageName\":\"mainsrp\"});";
        System.out.println(unwarpJSONP(str));
        String str2 ="jsonp1514();";
        System.out.println(unwarpJSONP(str2));
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
