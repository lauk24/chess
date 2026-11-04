package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import model.AuthData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SqlAuthDAOTest {
    private SqlAuthDAO access;

    @BeforeEach
    public void setup() throws DataAccessException {
        access = new SqlAuthDAO();
        access.clear();
    }

    @Test
    public void createAuthSuccess() throws Exception {
        AuthData auth = access.createAuth("newUser");
        assertNotNull(auth.authToken());
        assertEquals("newUser", auth.username());
    }

    @Test
    public void createAuthNull() {
        assertThrows(DataAccessException.class, () -> access.createAuth(null));
    }

    @Test
    public void getAuthSuccess() throws Exception {
        AuthData auth = access.createAuth("newUser");
        AuthData getAuth = access.getAuth(auth.authToken());
        assertEquals(auth.username(), getAuth.username());
        assertEquals(auth.authToken(), getAuth.authToken());
    }

    @Test
    public void getAuthBad() throws Exception {
        assertNull(access.getAuth("badToken"));
    }

    @Test
    public void getAuthNull() throws Exception {
        assertNull(access.getAuth(null));
    }

    @Test
    public void deleteAuthSuccess() throws Exception {
        AuthData auth1 = access.createAuth("newUser1");
        AuthData auth2 = access.createAuth("newUser2");
        access.deleteAuth(auth1.authToken());
        assertNull(access.getAuth(auth1.authToken()));
        assertNotNull(access.getAuth(auth2.authToken()));
    }

    @Test
    public void deleteAuthBad() throws Exception {
        AuthData auth = access.createAuth("newUser");
        assertDoesNotThrow(() -> access.deleteAuth("BadToken"));
        assertNotNull(access.getAuth(auth.authToken()));
    }

    @Test
    public void clearSuccess() throws Exception {
        AuthData auth = access.createAuth("newUser");
        access.clear();
        assertNull(access.getAuth(auth.authToken()));
    }
}
