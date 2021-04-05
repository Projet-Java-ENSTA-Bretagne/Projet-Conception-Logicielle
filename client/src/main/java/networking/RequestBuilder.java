package networking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * Class handling the construction of the commands to send to the server (JSON format).
 */
public class RequestBuilder {
    // Logging
    private static Logger log = LogManager.getLogger(RequestBuilder.class);

    /**
     * Builds a request (JSON format) without data.
     *
     * @param command The command to put inside the request (JSON object)
     * @return        The desired request (JSON format)
     */
    public static JSONObject buildWithoutData(String command) {
        JSONObject res = new JSONObject();

        // Setting the command
        res.put("command", command);

        return res;
    }

    /**
     * Builds a request (JSON format) with some given data.
     *
     * @param command The command (String) to put inside the request (JSON object)
     * @param data    The data (JSON format) to build the request with
     * @return        The desired request (JSON format)
     */
    public static JSONObject buildWithData(String command, JSONObject data) {
        JSONObject res = buildWithoutData(command);
        res.put("args", data);

        return res;
    }
}
