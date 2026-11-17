package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() throws Exception {
        facade.clear();
    }

    @Test
    void registerSuccess() throws Exception {
        var authData = facade.register("newUser", "newPassword", "nu@gmail.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerDuplicate() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        assertThrows(ResponseException.class,
                () -> facade.register("newUser", "newPassword", "nu@gmail.com"));
    }

    @Test
    void loginSuccess() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        facade.logout();
        var authData = facade.login("newUser", "newPassword");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerBadPassword() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        facade.logout();
        assertThrows(ResponseException.class,
                () -> facade.login("newUser", "badPassword"));
    }

    @Test
    void logoutSuccess() throws Exception {
        var authData = facade.register("newUser", "newPassword", "nu@gmail.com");
        assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    void logoutError() throws Exception {
        var authData = facade.register("newUser", "newPassword", "nu@gmail.com");
        facade.logout();
        assertThrows(ResponseException.class,
                () -> facade.logout());
    }

    @Test
    void createGameSuccess() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        int gameID = facade.createGame("newGame");
        assertTrue(gameID > 0);
    }

    @Test
    void createGameNotAuth() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        facade.logout();
        assertThrows(ResponseException.class,
                () -> facade.createGame("badGame"));
    }

    @Test
    void listGamesSuccess() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        facade.createGame("newGame1");
        facade.createGame("newGame2");
        var games = facade.listGames();
        assertEquals(2, games.size());
    }

    @Test
    void listGamesNotAuth() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        facade.logout();
        assertThrows(ResponseException.class,
                () -> facade.listGames());
    }

    @Test
    void joinGameSuccess() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        int gameID = facade.createGame("newGame");
        facade.joinGame(gameID, ChessGame.TeamColor.WHITE);
        var games = facade.listGames();
        assertEquals("newUser", games.get(0).whiteUsername());
    }

    @Test
    void joinGameNotAuth() throws Exception {
        facade.register("newUser", "newPassword", "nu@gmail.com");
        int gameID = facade.createGame("newGame");
        facade.logout();
        assertThrows(ResponseException.class,
                () -> facade.joinGame(gameID, ChessGame.TeamColor.WHITE));
    }
}
