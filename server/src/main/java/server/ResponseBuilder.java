package server;

import org.json.JSONObject;

import java.io.PrintStream;

public class ResponseBuilder {

    /**
     * Request in JSON format
     */
    private JSONObject request;

    /**
     * The output stream
     */
    private PrintStream outStream;

    /**
     * The possible status code :
     * - OK             : When everything is ok
     * - DENIED         : When the command is not authorize for a user
     * - NOT_FOUND      : command not found
     * - TOKEN_ERRO     : A error in the connection token
     * - SERVER_ERROR   : A error on the server
     */
    public enum StatusCode {
        OK,
        DENIED,
        NOT_FOUND,
        TOKEN_ERROR,
        SERVER_ERROR
    }

    /**
     * The constructor of response builder
     * @param request : The request in JSON format
     */
    private ResponseBuilder(JSONObject request) {
        this.request = request;
    }

    /**
     * The constructor of response builder
     * @param request : The request in JSON format
     * @param outStream : The output stream
     */
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

    /**
     * Function which build data in JSON format
     * @param code : The status code
     * @param data : Data in JSON format
     * @return The data in json format
     */
    public JSONObject buildData(StatusCode code, JSONObject data) {
        JSONObject res = new JSONObject();
        // putting back the command
        res.put("command", request.get("command"));
        res.put("status", code.toString());
        // and the payload
        res.put("data", data);

        return res;
    }

    /**
     * Function which build message in JSON format
     * @param code : The status code
     * @param message : Message in JSON format
     * @return The message in json format
     */
    public JSONObject buildMessage(StatusCode code, String message) {
        // setting the payload
        JSONObject data = new JSONObject();
        data.put("message", message);

        return buildData(code, data);
    }

    /**
     * Show an error message
     * @param errorMessage : the content of error message
     */
    public void serverError(String errorMessage) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.SERVER_ERROR, errorMessage);
        outStream.println(response.toString());
    }

    /**
     * Show a denied message
     * @param errorMessage: the content of denied message
     */
    public void deniedError(String errorMessage) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.DENIED, errorMessage);
        outStream.println(response.toString());
    }

    /**
     * Show a "not found" message
     * @param errorMessage: the content of "not found" message
     */
    public void notFoundError(String errorMessage) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.NOT_FOUND, errorMessage);
        outStream.println(response.toString());
    }

    /**
     * Show a "token error" message
     * @param errorMessage: the content of "token error" message
     */
    public void tokenError(String errorMessage) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.TOKEN_ERROR, errorMessage);
        outStream.println(response.toString());
    }

    /**
     * Show a message and data when all with ok
     * @param data : The data in json format
     */
    public void okWithData(JSONObject data) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildData(StatusCode.OK, data);
        outStream.println(response.toString());
    }

    /**
     * Show a message when all with ok
     * @param message : the content of this message
     */
    public void ok(String message) {
        if (outStream == null) { throw new RuntimeException("output stream not set"); }

        JSONObject response = buildMessage(StatusCode.OK, message);
        outStream.println(response.toString());
    }
}
