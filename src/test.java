import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class test {
        public static void main(String args[]) {
            JSONObject obj = new JSONObject();
            obj.put("name", "sonoo");
            obj.put("age", new Integer(27));
            obj.put("salary", new Double(600000));
            String a = obj.toJSONString();
            Object obj2=JSONValue.parse(a);
            JSONObject jsonObject = (JSONObject) obj2;
            System.out.print(jsonObject);
        }
}
