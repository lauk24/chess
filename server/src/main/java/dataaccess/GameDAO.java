package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame (GameData newGameData) throws DataAccessException;
}
