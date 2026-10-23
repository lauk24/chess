package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.ClearService;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final ClearService clearService;

    public Server() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();

        clearService = new ClearService(authDAO, gameDAO, userDAO);
        ClearHandler clearHandler = new ClearHandler(clearService);


        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", clearHandler)
                .post("/user", Server::register)
                .exception(Exception.class, this::exceptionHandler);
    }

    private static void register(Context ctx) {
        var result = Map.of("username", "", "authtoken", "");
        ctx.result(new Gson().toJson(result));
    }

    private void exceptionHandler(Exception ex, Context ctx) {
        ctx.status(500);
        ctx.result(new Gson().toJson(Map.of("message", "Error: " + ex.getMessage())));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
