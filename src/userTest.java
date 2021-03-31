
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class userTest {

    TradingSystem tradingSystem;

    @BeforeEach
    public void setUp(){
        tradingSystem= new TradingSystem();
    }

    @Test
    public void registerTest() throws Exception{
        String userName="kandabior";
        String password= "or321654";
        assertTrue(tradingSystem.register(userName,password));
    }



}
