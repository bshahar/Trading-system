
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionTest {

    TradingSystem tradingSystem;
    User systemManager = new User("Elad", 1, 1);
    @BeforeEach
    public void setUp() {
        tradingSystem = new TradingSystem(systemManager);
    }

    @Test
    public void systemMangerPermissions() {
        assertNotNull(tradingSystem.getAllPurchases(this.systemManager.getId()));
    }
    @Test
    public void systemMangerPermissionsFail() {
        assertNull(tradingSystem.getAllPurchases(2));
    }

}