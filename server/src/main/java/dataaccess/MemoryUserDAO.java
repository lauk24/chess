package dataaccess;
import model.*;
import java.util.HashMap;

public class MemoryUserDAO implements UserDataAccess {
    final private HashMap<Integer, AuthData> users = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }
}
