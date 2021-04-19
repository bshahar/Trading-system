package Tests;

import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {


    @BeforeEach
    public void setUp() {
        API.initTradingSystem("Elad");
    }

    //AT-4.1
    @Test
    public void registerTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(1,API.register(userName,password));
    }
    //AT-4.2
    @Test
    public void loginBadPass(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(1,API.register(userName,"12"));
        int index = API.registeredLogin(userName,password);
        assertEquals(index,-1);
    }

    //AT-4.3
    @Test
    public void failRegisterTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        Assertions.assertEquals(-1,API.register(userName,password));
    }

    @Test
    public void loginTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        int index = API.registeredLogin(userName,password);
        Assertions.assertEquals(index,1);
        Assertions.assertTrue(API.isLogged(index));
    }

    //AT-10.1
    @Test
    public void logoutTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        API.registeredLogin(userName,password);
        Assertions.assertTrue(API.registeredLogout(1));
        Assertions.assertFalse(API.isLogged(1));
    }

    //AT-10.2
    @Test
    public void logoutTwiceTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        API.registeredLogin(userName,password);
        Assertions.assertTrue(API.registeredLogout(1));
        Assertions.assertFalse(API.registeredLogout(1));
    }



    @Test
    public void failLoginTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(-1,API.registeredLogin(userName,password));
    }



    @Test
    //AT-1
    public void guestLoginTest(){
        int index = API.guestLogin();
        assertEquals(index, 1);
        Assertions.assertTrue(API.isLogged(index));

    }
    @Test
    //AT-2
    public void guestLogoutTest(){
        int index = API.guestLogin();
        Assertions.assertFalse(API.registeredLogout(index));
    }

    @Test
    public void scalabilityUserTest(){

        for (int i=1; i<1000; i++){
            if(i!=API.guestLogin()){
                fail();
            }
        }
        Assertions.assertEquals(999,API.getNumOfUsers());
    }

    //AT-3.1
    @Test
    public void guestRegisterTest(){
        int guestId= API.guestLogin();
        Assertions.assertEquals(1,API.guestRegister(guestId,"or","or321654"));
        Assertions.assertTrue(API.isLogged(guestId));
    }
    //AT-3.2
    @Test
    public void guestRegisterTestFail(){
        int guestId= API.guestLogin();
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        Assertions.assertEquals(-1,API.guestRegister(guestId,"kandabior","or321654"));
    }






















}
