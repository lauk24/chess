package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;

public class ClearService {
    private final AuthDAO authAccess;
    private final GameDAO gameAccess;
    private final UserDAO userAccess;

    public ClearService(AuthDAO authAccess, GameDAO gameAccess, UserDAO userAccess){
        this.authAccess = authAccess;
        this.gameAccess = gameAccess;
        this.userAccess = userAccess;
    }

    public void clear() throws DataAccessException{
        authAccess.clear();
        gameAccess.clear();
        userAccess.clear();
    }
}
