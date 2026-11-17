package client;

import org.junit.jupiter.api.*;
import server.Server;

import java.util.Collection;
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

}
