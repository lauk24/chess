package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.util.IO;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final Map<Integer, Set<Connection>> games = new ConcurrentHashMap<>();

    public void add(int gameID, Connection connection) {
        Set<Connection> set = games.get(gameID);
        if (set == null) {
            set = ConcurrentHashMap.newKeySet();
            games.put(gameID, set);
        }
        set.add(connection);
    }

    public void remove(int gameID, Connection connection) {
        Set<Connection> set = games.get(gameID);
        if (set != null) {
            set.remove(connection);
        }
    }

    public void broadcast(int gameID, Connection excludeConnection, ServerMessage message) throws IOException {
        Set<Connection> set = games.get(gameID);
        if (set == null) {
            return;
        }
        for (Connection connection : set) {
            if (!connection.isOpen()) {
                continue;
            }
            if (connection.equals(excludeConnection)) {
                continue;
            }
            try {
                connection.send(new Gson().toJson(message));
            } catch (IOException ex) {
                set.remove(connection);
            }
        }
    }
}
