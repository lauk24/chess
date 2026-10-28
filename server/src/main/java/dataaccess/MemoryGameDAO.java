package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MemoryGameDAO implements GameDAO {
    final private LinkedHashMap<Integer, GameData> games = new LinkedHashMap<>();
    int gameIndex = 0;

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public ArrayList<GameData> listGames(){
        return new ArrayList<>(games.values());
    }

    @Override
    public int createGame(String gameName) {
        int gameID = gameIndex++;
        games.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public void updateGame(GameData newGameData) {
        games.replace((newGameData.gameID()), newGameData);
    }
}
