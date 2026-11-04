package dataaccess.sql;
import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import model.GameData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SqlGameDAOTest {
    private SqlGameDAO access;

    @BeforeEach
    public void setup() throws DataAccessException {
        access = new SqlGameDAO();
        access.clear();
    }

    @Test
    public void createGameSuccess() throws Exception {
        int gameID1 = access.createGame("newGame1");
        int gameID2 = access.createGame("newGame2");
        assertNotEquals(gameID1, gameID2);
    }

    @Test
    public void createGameNull() throws Exception {
        assertThrows(DataAccessException.class, () -> access.createGame(null));
    }

    @Test
    public void getGameSuccess() throws Exception {
        int gameID = access.createGame("newGame");
        GameData game = access.getGame(gameID);
        assertEquals(gameID, game.gameID());
        assertEquals("newGame", game.gameName());
    }

    @Test
    public void getGameNotFound() throws Exception {
        access.createGame("newGame");
        assertNull(access.getGame(0));
    }

    @Test
    public void listGames() throws Exception {
        access.createGame("newGame1");
        access.createGame("newGame2");
        access.createGame("newGame3");
        var games = access.listGames();
        assertEquals(3, games.size());
    }

    @Test
    public void listGamesEmpty() throws Exception {
        var games = access.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    public void clearSuccess() throws Exception {
        int gameID = access.createGame("newGame");
        access.clear();
        assertNull(access.getGame(gameID));
    }
}
