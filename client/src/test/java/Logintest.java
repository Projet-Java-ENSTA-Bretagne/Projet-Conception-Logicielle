import junit.framework.TestCase;
import org.json.JSONObject;

public class Logintest extends TestCase {
    public void testSuccessfulLogin() {
        JSONObject loginData = new JSONObject();
        loginData.put("username", "test");
        loginData.put("password", "test");
    }
    public void testLoginError() {

    }
}
