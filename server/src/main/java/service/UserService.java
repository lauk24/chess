package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;

public class UserService {
    private final AuthDAO authAccess;
    private final UserDAO userAccess;

    public UserService(AuthDAO authAccess, UserDAO userAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
    }
}
