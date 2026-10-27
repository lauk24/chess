package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.ClearService;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final ClearService clearService;
    private final UserService userService;

    public Server() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();

        clearService = new ClearService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);

        ClearHandler clearHandler = new ClearHandler(clearService);
        UserHandler userHandler = new UserHandler(userService);


        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", clearHandler)
                .post("/user", userHandler::register)
                .exception(BadRequestException.class, this::badRequestHandler)
                .exception(AlreadyTakenException.class, this::alreadyTakenHandler)
                .exception(Exception.class, this::exceptionHandler);
    }

    private void badRequestHandler(Exception ex, Context ctx) {
        sendError(ctx, 400, ex.getMessage());
    }

    private void alreadyTakenHandler(Exception ex, Context ctx) {
        sendError(ctx, 403, ex.getMessage());
    }

    private void exceptionHandler(Exception ex, Context ctx) {
        sendError(ctx, 500, ex.getMessage());
    }

    private void sendError(Context ctx, int errorCode, String message) {
        ctx.status(errorCode);
        ctx.result(new Gson().toJson(Map.of("message", message)));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
