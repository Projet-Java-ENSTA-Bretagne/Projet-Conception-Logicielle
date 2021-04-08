
import networking.RequestBuilder;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class RequestBuilderTest {

    @Test
    public void WithDataTest() {
        JSONObject actual = RequestBuilder.buildWithoutData("test");

        assertTrue(actual.get("command") != null);
        assertEquals(actual.getString("command"), "test");
    }
    @Test
    public void WithoutDataTest() {
        JSONObject data = new JSONObject();
        data.put("data", "testdata");

        JSONObject actual = RequestBuilder.buildWithData("test", data);
        assertTrue(actual.get("command") != null);
        assertEquals(actual.getString("command"), "test");
        assertEquals(actual.getJSONObject("args"), data);

    }
}
