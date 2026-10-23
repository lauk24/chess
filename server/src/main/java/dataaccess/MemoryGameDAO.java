package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDataAccess {
    final private HashMap<Integer, AuthData> games = new HashMap<>();

    @Override
    public void clear() {
        games.clear();
    }
}
