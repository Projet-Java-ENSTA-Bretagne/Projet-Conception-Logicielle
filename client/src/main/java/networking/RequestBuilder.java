package networking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class RequestBuilder {
    /**
     * Class handling the construction of the commands to send to the server (JSON format).
     */

    // Logging
    private static Logger log = LogManager.getLogger(RequestBuilder.class);

    public static JSONObject buildWithoutData(String command) {
        /**
         * Builds a request (JSON format) without data.
         * @param command The command to put inside the request (JSON object)
         * @return The desired request (JSON format)
         */

        JSONObject res = new JSONObject();

        // Setting the commannd
        res.put("command", command);

        return res;
    }

    public static JSONObject buildWithData(String command, JSONObject data) {
        /**
         * Builds a request (JSON format) with some given data.
         * @param command The command to put inside the request (JSON object)
         * @param data The data (JSON format) to build the request with
         * @return The desired request (JSON format)
         */

        JSONObject res = buildWithoutData(command);
        res.put("data", data);

        return res;
    }
}
