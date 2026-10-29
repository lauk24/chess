package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private AuthDAO authAccess;
    private GameDAO gameAccess;
    private GameService service;

    @BeforeEach
    void setup() {
        authAccess = new MemoryAuthDAO();
        gameAccess = new MemoryGameDAO();
        service = new GameService(authAccess, gameAccess);
    }

    @Test
    void createGameSuccess() throws Exception {
        AuthData auth = authAccess.createAuth("newUser");
        CreateGameRequest request = new CreateGameRequest(auth.authToken(), "newGame");
        int gameID = service.createGame(request);
        GameData game = gameAccess.getGame(gameID);

        assertNotNull(game);
        assertEquals(request.gameName(), game.gameName());
    }

    @Test
    void createGameBadToken() throws Exception {
        assertThrows(UnauthorizedException.class, () -> {
            service.createGame(new CreateGameRequest("badToken", "newGame"));
        });
    }

    @Test
    void createGameBadRequest() throws Exception {
        AuthData auth = authAccess.createAuth("newUser");
        assertThrows(BadRequestException.class, () -> {
            service.createGame(new CreateGameRequest(auth.authToken(), null));
        });
    }

    @Test
    void listGamesSuccess() throws Exception {
        String authToken = authAccess.createAuth("newUser").authToken();
        service.createGame(new CreateGameRequest(authToken, "game1"));
        service.createGame(new CreateGameRequest(authToken, "game2"));
        Collection<GameData> games = service.listGames(authToken);

        assertEquals(2, games.size());
    }

    @Test
    void listGamesBadToken() throws Exception {
        assertThrows(UnauthorizedException.class, () -> {
            service.listGames("badToken");
        });
    }

    @Test
    void joinGameSuccess() throws Exception {
        String authToken1 = authAccess.createAuth("newUser1").authToken();
        String authToken2 = authAccess.createAuth("newUser2").authToken();
        int gameID = service.createGame(new CreateGameRequest(authToken1, "game"));
        service.joinGame(new JoinGameRequest(authToken1, ChessGame.TeamColor.WHITE, gameID));
        service.joinGame(new JoinGameRequest(authToken2, ChessGame.TeamColor.BLACK, gameID));
        assertEquals("newUser1", gameAccess.getGame(gameID).whiteUsername());
        assertEquals("newUser2", gameAccess.getGame(gameID).blackUsername());
    }

    @Test
    void joinGameAlreadyTaken() throws Exception {
        String authToken1 = authAccess.createAuth("newUser1").authToken();
        String authToken2 = authAccess.createAuth("newUser2").authToken();
        int gameID = service.createGame(new CreateGameRequest(authToken1, "game"));
        service.joinGame(new JoinGameRequest(authToken1, ChessGame.TeamColor.WHITE, gameID));
        assertThrows(AlreadyTakenException.class, () -> {
            service.joinGame(new JoinGameRequest(authToken2, ChessGame.TeamColor.WHITE, gameID));
        });
    }

    @Test
    void joinGameBadToken() throws Exception {
        String authToken = authAccess.createAuth("newUser").authToken();
        int gameID = service.createGame(new CreateGameRequest(authToken, "game"));
        assertThrows(UnauthorizedException.class, () -> {
            service.joinGame(new JoinGameRequest("badToken", ChessGame.TeamColor.WHITE, gameID));
        });
    }

    @Test
    void joinGameBadRequest() throws Exception {
        String authToken = authAccess.createAuth("newUser").authToken();
        int gameID = service.createGame(new CreateGameRequest(authToken, "game"));
        assertAll(
                () -> assertThrows(BadRequestException.class, () -> {
                    service.joinGame(new JoinGameRequest(authToken, null, gameID));
                }),
                () -> assertThrows(BadRequestException.class, () -> {
                    service.joinGame(new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, 0));
                })
        );
    }
}
