package service;

import dataaccess.*;
import model.*;
import java.util.ArrayList;
import java.util.Collection;

public class GameService {
    private final AuthDAO authAccess;
    private final UserDAO userAccess;
    private final GameDAO gameAccess;

    public GameService(AuthDAO authAccess, UserDAO userAccess, GameDAO gameAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    public int createGame(CreateGameRequest request) throws UnauthorizedException, BadRequestException, DataAccessException {
        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (request.gameName() == null) {
            throw new BadRequestException("Error: gameName is required");
        }

        return gameAccess.createGame(request.gameName());
    }

    public Collection<GameData> listGames(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData auth = authAccess.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameAccess.listGames();
    }
}
