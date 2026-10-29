package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import java.util.Collection;

public class GameService {
    private final AuthDAO authAccess;
    private final GameDAO gameAccess;

    public GameService(AuthDAO authAccess, GameDAO gameAccess) {
        this.authAccess = authAccess;
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

    public void joinGame(JoinGameRequest request) throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException {
        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (request.playerColor() == null) {
            throw new BadRequestException("Error: invalid player color");
        }


        GameData game = gameAccess.getGame(request.gameID());
        if (game == null) {
            throw new BadRequestException("Error: game does not exist");
        }
        GameData newGameData;
        if (request.playerColor() == ChessGame.TeamColor.BLACK) {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: black player already taken");
            }
            newGameData = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());

        } else {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: white player already taken");
            }
            newGameData = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
        }
        gameAccess.updateGame(newGameData);
    }
}
