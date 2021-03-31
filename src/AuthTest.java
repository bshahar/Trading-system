
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTest {

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

    @Test
    public void failRegisterTest() throws Exception{
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        assertFalse(tradingSystem.register(userName,password));
    }

    @Test
    public void loginTest() throws  Exception{
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        assertEquals(1,tradingSystem.login(userName,password));
    }

    @Test
    public void failLoginTest() throws  Exception{
        String userName="kandabior";
        String password= "or321654";
        assertEquals(-1,tradingSystem.login(userName,password));
    }

    @Test
    public void logoutTest() throws  Exception{
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        tradingSystem.login(userName,password);
        assertTrue(tradingSystem.logout(1));
    }

    @Test
    public void guestLoginTest() throws  Exception{
        assertEquals(1, tradingSystem.guestLogin());
    }

    @Test
    public void guestLogoutTest() throws  Exception{
        int id= tradingSystem.guestLogin();
        assertEquals(true, tradingSystem.guestLogout(id));
    }

    @Test
    public void scalabilityUserTest() throws  Exception{

        for (int i=1; i<1000; i++){
            if(i!=tradingSystem.guestLogin()){
                fail();
            }
        }
        assertEquals(999,tradingSystem.getNumOfUsers());
    }

    @Test
    public void guestRegisterTest() throws  Exception{
        int guestId= tradingSystem.guestLogin();
        assertTrue(tradingSystem.guestRegister(guestId,"or","or321654"));
    }



















}
