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
        RegisterRequest request = new RegisterRequest("newUser", "newPassword", "nu@mail.com");
        AuthData auth = service.register(request);

        assertEquals(request.username(), auth.username());
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

    @Test
    void loginSuccess() throws Exception {
        userAccess.createUser(new UserData("existingUser", "existingPassword", "ex@mail.com"));
        LoginRequest request = new LoginRequest("existingUser", "existingPassword");
        service.login(request);
        assertEquals(request.password(), userAccess.getUser(request.username()).password());
    }

    @Test
    void loginNotFound() throws Exception {
        LoginRequest request = new LoginRequest("existingUser", "existingPassword");
        assertThrows(UnauthorizedException.class, () -> {
            service.login(request);
        });
    }

    @Test
    void loginWrongPassword() throws Exception{
        userAccess.createUser(new UserData("existingUser", "existingPassword", "ex@mail.com"));
        LoginRequest request = new LoginRequest("existingUser", "wrongPassword");
        assertThrows(UnauthorizedException.class, () -> {
            service.login(request);
        });
    }

    @Test
    void loginBadRequest() throws Exception {
        assertAll(
                () -> assertThrows(BadRequestException.class, () -> {
                    service.login(null);
                }),
                () -> assertThrows(BadRequestException.class, () -> {
                    service.login(new LoginRequest(null, "newPassword"));
                }),
                () -> assertThrows(BadRequestException.class, () -> {
                    service.login(new LoginRequest("newUser", null));
                })
        );
    }

    @Test
    void logoutSuccess() throws Exception {
        AuthData auth = service.register(new RegisterRequest("newUser", "newPassword", "nu@mail.com"));
        service.logout(auth.authToken());
        assertNull(authAccess.getAuth(auth.authToken()));
    }

    @Test
    void logoutBadToken() throws Exception {
        AuthData auth = service.register(new RegisterRequest("newUser", "newPassword", "nu@mail.com"));
        assertThrows(UnauthorizedException.class, () -> {
            service.logout("badToken");
        });
    }
}
