package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private UserDAO userAccess;
    private AuthDAO authAccess;
    private GameDAO gameAccess;
    private GameService service;

    @BeforeEach
    void setup() {
        userAccess = new MemoryUserDAO();
        authAccess = new MemoryAuthDAO();
        gameAccess = new MemoryGameDAO();
        service = new GameService(authAccess, userAccess, gameAccess);
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
}
