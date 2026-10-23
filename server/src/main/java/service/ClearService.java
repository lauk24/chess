package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.DataAccessException;

public class ClearService {
    private final AuthDataAccess authAccess;
    private final GameDataAccess gameAccess;
    private final UserDataAccess userAccess;

    public ClearService(AuthDataAccess authAccess, GameDataAccess gameAccess, UserDataAccess userAccess){
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
