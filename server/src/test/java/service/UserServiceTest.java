package service;

import dataaccess.*;
import model.AuthData;
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
        AuthData auth = service.register(new RegisterRequest("newUser", "newPassword", "nu@mail.com"));

        assertEquals("newUser", auth.username());
        assertNotNull(auth.authToken());
        assertNotNull(userAccess.getUser("newUser"));
        assertNotNull(authAccess.getAuth(auth.authToken()));
    }
    @Test
    void registerAlreadyExists() throws Exception {
        service.register(new RegisterRequest("dupUser", "dupPassword", "du@mail.com"));

        assertThrows(AlreadyTakenException.class, () -> {
            service.register(new RegisterRequest("dupUser", "otherPassword", "other@mail.com"));
        });
    }

    @Test
    void registerBadRequest() throws Exception {
        assertAll(
                () -> assertThrows(BadRequestException.class, () -> {
                    service.register(null);
                }),
                () -> assertThrows(BadRequestException.class, () -> {
                    service.register(new RegisterRequest(null, "newPassword", "nu@mail.com"));
                }),
                () -> assertThrows(BadRequestException.class, () -> {
                    service.register(new RegisterRequest("newUser", null, "nu@mail.com"));
                }),
                () -> assertThrows(BadRequestException.class, () -> {
                    service.register(new RegisterRequest("newUser", "newPassword", null));
                })
        );
    }
}
