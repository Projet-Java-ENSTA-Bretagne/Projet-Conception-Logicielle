package server;

import org.json.JSONObject;

import java.io.PrintStream;

public class ResponseBuilder {
    public enum StatusCode {
        OK,
        DENIED,
        NOT_FOUND,
        SERVER_ERROR
    }

    public static JSONObject buildMessage(JSONObject request, StatusCode code, String message) {
        JSONObject res = new JSONObject();
        // putting back the command
        res.put("command", request.get("command"));
        res.put("status", code.toString());

        // setting the payload
        JSONObject data = new JSONObject();
        data.put("message", message);
        res.put("data", data);

        return res;
    }

    public static void sendServerError(PrintStream outStream, JSONObject request, String errorMessage) {
        JSONObject response = buildMessage(request, StatusCode.SERVER_ERROR, errorMessage);
        outStream.println(response.toString());
    }

    public static void sendDeniedError(PrintStream outStream, JSONObject request, String errorMessage) {
        JSONObject response = buildMessage(request, StatusCode.DENIED, errorMessage);
        outStream.println(response.toString());
    }
}
