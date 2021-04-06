
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    TradingSystem tradingSystem;

    @BeforeEach
    public void setUp() {
        User systemManager = new User("Elad",1,1);
        tradingSystem= new TradingSystem(systemManager);
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
        int index = tradingSystem.login(userName,password);
        assertEquals(index,tradingSystem.login(userName,password));
        assertTrue(tradingSystem.isLogged(index));
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
        assertFalse(tradingSystem.isLogged(1));
    }

    @Test
    public void guestLoginTest() throws  Exception{
        int index = tradingSystem.guestLogin();
        System.out.println(index);
        assertEquals(index, 1);
        assertTrue(tradingSystem.isLogged(index));

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
        assertTrue(tradingSystem.isLogged(guestId));
    }



















}
