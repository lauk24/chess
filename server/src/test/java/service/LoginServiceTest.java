package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
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
}
