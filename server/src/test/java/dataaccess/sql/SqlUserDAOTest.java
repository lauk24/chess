package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.SqlUserDAO;
import model.UserData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SqlUserDAOTest {
    private SqlUserDAO access;
    private final UserData newUser = new UserData("newUser", "newPassword", "ne@gmail.com");

    @BeforeEach
    public void setup() throws DataAccessException {
        access = new SqlUserDAO();
        access.clear();
    }

    @Test
    public void clearSuccess() throws Exception {
        access.createUser(newUser);
        access.clear();
        assertNull(access.getUser("newUser"));
    }
}
