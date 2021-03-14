package networking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class RequestBuilder {

    // Logging
    private static Logger log = LogManager.getLogger(RequestBuilder.class);

    public static JSONObject buildWithoutData(String command) {
        JSONObject res = new JSONObject();

        // Setting the commannd
        res.put("command", command);

        return res;
    }

    public static JSONObject buildWithData(String command, JSONObject data) {
        JSONObject res = buildWithoutData(command);
        res.put("data", data);

        return res;
    }
}
