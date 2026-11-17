package client;

import com.google.gson.Gson;
import model.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private String authToken;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        var request = buildRequest("POST", "/user", new RegisterRequest(username, password, email));
        var response = sendRequest(request);
        var result = handleResponse(response, AuthData.class);
        authToken = result.authToken();
        return result;
    }

    public AuthData login(String username, String password) throws ResponseException {
        var request = buildRequest("POST", "/session", new LoginRequest(username, password));
        var response = sendRequest(request);
        var result = handleResponse(response, AuthData.class);
        authToken = result.authToken();
        return result;
    }

    public void logout() throws ResponseException {
        var request = buildRequest("DELETE", "/session", null) ;
        handleResponse(sendRequest(request), null);
        authToken = null;
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null);
        handleResponse(sendRequest(request), null);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, "Could not connect to server.");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body, status);
            }
            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "Something went wrong. Please try again.");
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful (int status) {
        return status /100 == 2;
    }
}
