package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    @Test
    void clearSuccess() {
        ClearService service = new ClearService(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());
        assertDoesNotThrow(service::clear);
    }
}
