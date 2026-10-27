package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class UserService {
    private final AuthDAO authAccess;
    private final UserDAO userAccess;

    public UserService(AuthDAO authAccess, UserDAO userAccess) {
        this.authAccess = authAccess;
        this.userAccess = userAccess;
    }

    public AuthData register(RegisterRequest request) throws BadRequestException, AlreadyTakenException, DataAccessException{
        if (request == null || request.username() == null || request.email() == null || request.password() == null){
            throw new BadRequestException("Error: bad request");
        }
        if (userAccess.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: user already exists");
        }
        userAccess.createUser(request.userData());
        return authAccess.createAuth(request.username());
    }
}
