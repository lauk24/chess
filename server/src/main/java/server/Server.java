package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", Server::clear);
        javalin.post("/user", Server::register);
    }

    private static void clear(Context ctx){

    }

    private static void register(Context ctx) {
        var result = Map.of("username", "", "authtoken", "");
        ctx.result(new Gson().toJson(result));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
