package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SqlUserDAOTest {
    private SqlUserDAO access;
    private final UserData newUser = new UserData("newUser", "newPassword", "nu@gmail.com");

    @BeforeEach
    public void setup() throws DataAccessException {
        access = new SqlUserDAO();
        access.clear();
    }

    @Test
    public void createUserSuccess() throws Exception {
        access.createUser(newUser);
        assertEquals(newUser, access.getUser("newUser"));
    }

    @Test
    public void createUserDup() throws Exception {
        access.createUser(newUser);
        UserData dupUser = new UserData("newUser", "dupPassword", "du@gmail.com");
        assertThrows(DataAccessException.class, () -> access.createUser(dupUser));
    }

    @Test
    public void createUserNull() throws Exception {
        UserData badUser = new UserData(null, "badPassword", "bu@gmail.com");
        assertThrows(DataAccessException.class, () -> access.createUser(badUser));
    }

    @Test
    public void getUserSuccess() throws Exception {
        access.createUser(newUser);
        UserData user = access.getUser("newUser");
        assertEquals("newUser", user.username());
        assertEquals("newPassword", user.password());
        assertEquals("nu@gmail.com", user.email());
    }

    @Test
    public void getUserNotFound() throws Exception {
        access.createUser(newUser);
        assertNull(access.getUser("badUser"));
    }

    @Test
    public void clearSuccess() throws Exception {
        access.createUser(newUser);
        access.clear();
        assertNull(access.getUser("newUser"));
    }
}
