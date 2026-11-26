package server.websocket;
import java.io.IOException;

public interface Connection {
    boolean isOpen();

    void send(String message) throws IOException;
}
