
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {

    TradingSystem tradingSystem;
    int id;
    @BeforeEach
    public void setUp(){
        tradingSystem= new TradingSystem();
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        id= tradingSystem.login(userName,password);

    }

    @Test
    public void getInformationTest() throws Exception{
        assertNotNull(tradingSystem.getAllStoresInfo());

    }





















}
