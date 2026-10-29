package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();

        clearService = new ClearService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
        gameService = new GameService(authDAO, gameDAO);

        ClearHandler clearHandler = new ClearHandler(clearService);
        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);


        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", clearHandler)
                .post("/user", userHandler::register)
                .post("/session", userHandler::login)
                .delete("/session", userHandler::logout)
                .post("/game", gameHandler::createGame)
                .get("/game", gameHandler::listGames)
                .put("/game", gameHandler::joinGame)
                .exception(BadRequestException.class, this::badRequestHandler)
                .exception(AlreadyTakenException.class, this::alreadyTakenHandler)
                .exception(UnauthorizedException.class, this::unauthorizedHandler)
                .exception(Exception.class, this::exceptionHandler);
    }

    private void badRequestHandler(Exception ex, Context ctx) {
        sendError(ctx, 400, ex.getMessage());
    }

    private void alreadyTakenHandler(Exception ex, Context ctx) {
        sendError(ctx, 403, ex.getMessage());
    }

    private void unauthorizedHandler(Exception ex, Context ctx) {
        sendError(ctx, 401, ex.getMessage());
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
