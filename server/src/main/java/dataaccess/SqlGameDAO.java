package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDAO extends SqlDAO implements GameDAO{
    public SqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException{
        var statement = "Truncate game";
        executeUpdate(statement);
    }
    public Collection<GameData> listGames() throws DataAccessException{
        var games = new ArrayList<GameData>();

    }
    public int createGame(String gameName) throws DataAccessException{
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String gameJson = new Gson().toJson(new ChessGame());
        return executeUpdate(statement, null, null, gameName, gameJson);
    }
    public GameData getGame(int gameID) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game where gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: unable to read games");
        }
        return null;
    }
    public void updateGame (GameData newGameData) throws DataAccessException{

    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var gameJson = rs.getString("game");
        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
