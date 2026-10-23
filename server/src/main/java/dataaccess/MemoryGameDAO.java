package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, AuthData> games = new HashMap<>();

    @Override
    public void clear() {
        games.clear();
    }
}
