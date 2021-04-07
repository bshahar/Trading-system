
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

    //AT-4.1
    @Test
    public void registerTest(){
        String userName="kandabior";
        String password= "or321654";
        assertTrue(tradingSystem.register(userName,password));
    }
    //AT-4.2
    @Test
    public void loginBadPass(){
        String userName="kandabior";
        String password= "or321654";
        assertTrue(tradingSystem.register(userName,"12"));
        int index = tradingSystem.login(userName,password);
        assertEquals(index,-1);
    }

    //AT-4.3
    @Test
    public void failRegisterTest(){
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        assertFalse(tradingSystem.register(userName,password));
    }

    @Test
    public void loginTest(){
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        int index = tradingSystem.login(userName,password);
        assertEquals(index,tradingSystem.login(userName,password));
        assertTrue(tradingSystem.isLogged(index));
    }

    //AT-10.1
    @Test
    public void logoutTest(){
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        tradingSystem.login(userName,password);
        assertTrue(tradingSystem.logout(1));
        assertFalse(tradingSystem.isLogged(1));
    }

    //AT-10.2
    @Test
    public void logoutTwiceTest(){
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        tradingSystem.login(userName,password);
        assertTrue(tradingSystem.logout(1));
        assertFalse(tradingSystem.logout(1));
    }



    @Test
    public void failLoginTest(){
        String userName="kandabior";
        String password= "or321654";
        assertEquals(-1,tradingSystem.login(userName,password));
    }



    @Test
    //AT-1
    public void guestLoginTest(){
        int index = tradingSystem.guestLogin();
        assertEquals(index, 1);
        assertTrue(tradingSystem.isLogged(index));

    }
    @Test
    //AT-2
    public void guestLogoutTest(){
        int index = tradingSystem.guestLogin();
        assertFalse(tradingSystem.logout(index));
    }

    @Test
    public void scalabilityUserTest(){

        for (int i=1; i<1000; i++){
            if(i!=tradingSystem.guestLogin()){
                fail();
            }
        }
        assertEquals(999,tradingSystem.getNumOfUsers());
    }

    //AT-3.1
    @Test
    public void guestRegisterTest(){
        int guestId= tradingSystem.guestLogin();
        assertTrue(tradingSystem.guestRegister(guestId,"or","or321654"));
        assertTrue(tradingSystem.isLogged(guestId));
    }
    //AT-3.2
    @Test
    public void guestRegisterTestFail(){
        int guestId= tradingSystem.guestLogin();
        String userName="kandabior";
        String password= "or321654";
        tradingSystem.register(userName,password);
        assertFalse(tradingSystem.guestRegister(guestId,"kandabior","or321654"));
    }






















}
