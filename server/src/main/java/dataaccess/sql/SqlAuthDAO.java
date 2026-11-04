package dataaccess.sql;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.*;
import java.util.UUID;

public class SqlAuthDAO extends SqlDAO implements AuthDAO {

    public SqlAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, token, username);
        return new AuthData(token, username);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT authToken, username FROM auth where authToken=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Error: unable to read auth %s, %s", statement, ex.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }
}
