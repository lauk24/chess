package server.websocket;
import com.google.gson.Gson;
import com.mysql.cj.Session;
import io.javalin.websocket.*;
import org.eclipse.jetty.server.Authentication;
import websocket.commands.UserGameCommand;


public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections;

    public WebSocketHandler(ConnectionManager connections)  {
        this.connections = connections;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket Connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        var connections = new SessionConnection(ctx.session);
        switch (command.getCommandType()) {
            case CONNECT -> ;
            case MAKE_MOVE -> ;
            case LEAVE -> ;
            case RESIGN -> ;
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void leave(Connection connection) {

    }
}
