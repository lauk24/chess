package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserDAO userAccess;
    private AuthDAO authAccess;
    private UserService service;

    @BeforeEach
    void setup() {
        userAccess = new MemoryUserDAO();
        authAccess = new MemoryAuthDAO();
        service = new UserService(authAccess, userAccess);
    }



    @Test
    void registerSuccess() throws Exception {
        AuthData auth = service.register(new RegisterRequest("NewUser", "newUserPassword", "nu@mail.com"));

        assertEquals("NewUser", auth.username());
        assertNotNull(auth.authToken());
        assertNotNull(userAccess.getUser("NewUser"));
        assertNotNull(authAccess.getAuth(auth.authToken()));
    }
}
