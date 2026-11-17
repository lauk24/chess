package client;

import com.google.gson.Gson;

import java.util.HashMap;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
    }

    private final Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code code() {
        return code;
    }

    public static ResponseException fromJson(String json, int httpStatusCode) {
        String message = "Something went wrong. Please try again.";
        try {
            var map = new Gson().fromJson(json, HashMap.class);
            var status = Code.valueOf(map.get("status").toString());
            message = map.get("message").toString();
            return new ResponseException(status, message);
        } catch (Exception ignored) { }
        return new ResponseException(fromHttpStatusCode(httpStatusCode), message);
    }

    public static Code fromHttpStatusCode(int httpStatusCode) {
        if (httpStatusCode/100 == 4) {
            return Code.ClientError;
        }
        return Code.ServerError;
    }
}
