package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public record SessionConnection(Session session) implements Connection {
    @Override
    public boolean isOpen() {
        return session.isOpen();
    }
    @Override
    public void send(String message) throws IOException {
        synchronized (session) {
            session.getRemote().sendString(message);
        }
    }
}
