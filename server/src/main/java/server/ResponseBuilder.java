package server;

import org.json.JSONObject;

import java.io.PrintStream;

public class ResponseBuilder {

    private JSONObject request;
    private PrintStream outStream;

    public enum StatusCode {
        OK,
        DENIED,
        NOT_FOUND,
        TOKEN_ERROR,
        SERVER_ERROR
    }

    private ResponseBuilder(JSONObject request) {
        this.request = request;
    }

    private ResponseBuilder(JSONObject request, PrintStream outStream) {
        this.request = request;
        this.outStream = outStream;
    }

    public static ResponseBuilder forRequest(JSONObject request) {
        return new ResponseBuilder(request);
    }

    public static ResponseBuilder forRequest(JSONObject request, PrintStream outStream) {
        return new ResponseBuilder(request, outStream);
    }

    public JSONObject buildData(StatusCode code, JSONObject data) {
        JSONObject res = new JSONObject();
        // putting back the command
        res.put("command", request.get("command"));
        res.put("status", code.toString());
        // and the payload
        res.put("data", data);

        return res;
    }

    public JSONObject buildMessage(StatusCode code, String message) {
        // setting the payload
        JSONObject data = new JSONObject();
        data.put("message", message);

        return buildData(code, data);
    }

    public void serverError(String errorMessage) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.SERVER_ERROR, errorMessage);
        outStream.println(response.toString());
    }

    public void deniedError(String errorMessage) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.DENIED, errorMessage);
        outStream.println(response.toString());
    }

    public void notFoundError(String errorMessage) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.NOT_FOUND, errorMessage);
        outStream.println(response.toString());
    }

    public void tokenError(String errorMessage) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.TOKEN_ERROR, errorMessage);
        outStream.println(response.toString());
    }

    public void okWithData(JSONObject data) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildData(StatusCode.OK, data);
        outStream.println(response.toString());
    }

    public void ok(String message) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.OK, message);
        outStream.println(response.toString());
    }
}
