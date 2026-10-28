package service;

import dataaccess.*;
import model.*;

public class GameService {
    private final AuthDAO authAccess;
    private final UserDAO userAccess;
    private final GameDAO gameAccess;

    public GameService(AuthDAO authAccess, UserDAO userAccess, GameDAO gameAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }
}
