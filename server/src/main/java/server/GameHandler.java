package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import service.*;

import java.util.Map;

public class GameHandler {
    private final GameService service;
    private final Gson gson = new Gson();

    public GameHandler(GameService service){
        this.service = service;
    }

    public void createGame(Context ctx) throws BadRequestException, UnauthorizedException, DataAccessException {
        String authToken = ctx.header("authorization");
        CreateGameRequest body = gson.fromJson(ctx.body(), CreateGameRequest.class);
        String gameName = (body == null) ? null : body.gameName();
        int gameID = service.createGame(new CreateGameRequest(authToken, gameName));
        ctx.result(gson.toJson(Map.of("gameID", gameID)));
    }
}
