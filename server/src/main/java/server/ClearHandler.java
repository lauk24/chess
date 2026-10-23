package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearService;
import org.jetbrains.annotations.NotNull;


public class ClearHandler implements Handler{
    private final ClearService clearService;

    public ClearHandler(ClearService clearService){
        this.clearService = clearService;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception{
        clearService.clear();
        ctx.result("{}");
    }
}
