package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDAO extends SqlDAO implements GameDAO {
    public SqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException{
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }
    public Collection<GameData> listGames() throws DataAccessException{
        var games = new ArrayList<GameData>();
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: unable to list games");
        }
        return games;
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
        var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
        executeUpdate(statement,
                newGameData.whiteUsername(),
                newGameData.blackUsername(),
                newGameData.gameName(),
                new Gson().toJson(newGameData.game()),
                newGameData.gameID()
        );
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
