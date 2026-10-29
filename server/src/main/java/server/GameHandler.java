package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.GameData;
import service.*;

import java.util.Collection;
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

    public void listGames(Context ctx) throws UnauthorizedException, DataAccessException {
        Collection<GameData> games = service.listGames(ctx.header("authorization"));
        ctx.result(gson.toJson(Map.of("games", games)));
    }

    public void joinGame(Context ctx) throws UnauthorizedException, BadRequestException, DataAccessException, AlreadyTakenException {
        String authToken = ctx.header("authorization");
        JoinGameRequest body = gson.fromJson(ctx.body(), JoinGameRequest.class);
        if (body == null) {
            throw new BadRequestException("Error: bad request");
        }
        service.joinGame(new JoinGameRequest(authToken, body.playerColor(), body.gameID()));
        ctx.result("{}");
    }
}
