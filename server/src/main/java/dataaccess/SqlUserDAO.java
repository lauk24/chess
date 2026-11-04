package dataaccess;
import model.UserData;
import java.sql.SQLException;

public class SqlUserDAO implements UserDAO {

    public SqlUserDAO() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {

    }
    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {

    }

}
