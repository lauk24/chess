package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDataAccess {
    final private HashMap<Integer, AuthData> auth = new HashMap<>();

    @Override
    public void clear() {
        auth.clear();
    }
}
