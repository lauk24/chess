package dataaccess;
import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String token) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
}
