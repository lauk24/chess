package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ClearServiceTest {
    @Test
    void clearSuccess() {
        ClearService service = new ClearService(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());
        assertDoesNotThrow(service::clear);
    }
}
