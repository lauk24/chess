package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import service.*;

public class UserHandler {
    private final UserService service;
    private final Gson gson = new Gson();

    public UserHandler(UserService service){
        this.service = service;
    }

    public void register(Context ctx) throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
        AuthData auth = service.register(request);
        ctx.result(gson.toJson(auth));
    }

    public void login(Context ctx) throws BadRequestException, UnauthorizedException, DataAccessException {
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
        AuthData auth = service.login(request);
        ctx.result(gson.toJson(auth));
    }

    public void logout(Context ctx) throws UnauthorizedException, DataAccessException {
        service.logout(ctx.header("authorization"));
        ctx.result("{}");
    }
}
