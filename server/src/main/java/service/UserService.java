package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

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

    public AuthData login(LoginRequest request) throws BadRequestException, UnauthorizedException, DataAccessException {
        if (request == null || request.username() == null || request.password() == null){
            throw new BadRequestException("Error: bad request");
        }
        UserData user = userAccess.getUser(request.username());
        if (user == null) {
            throw new UnauthorizedException("Error: user does not exist");
        }
        if (!request.password().equals(user.password())) {
            throw new UnauthorizedException("Error: incorrect password");
        }
        return authAccess.createAuth(user.username());
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData auth = authAccess.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authAccess.deleteAuth(authToken);
    }
}
