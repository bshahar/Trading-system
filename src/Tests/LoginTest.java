package Tests;

import Domain.Result;
import Persistence.DataBaseHelper;
import Persistence.UserWrapper;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {


    @BeforeEach
    public void setUp() {
        try {
            DataBaseHelper.cleanAllTable("test");
            API.initTradingSystem("test");
        } catch (Exception e) {
            System.out.println("ERROR!");
        }
    }

    //AT-4.1
    @Test
    public void loginSuccessTest(){

        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(1,API.register(userName,password, 20).getData());
    }
    //AT-4.2
    @Test
    public void loginBadPasswordFailTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(1,API.register(userName,"12", 20).getData());
        Result result= API.registeredLogin(userName,password);
        assertFalse(result.isResult());
    }

    //AT-4.3
    @Test
    public void registerExistingUserFailTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password, 20);
        Assertions.assertEquals(false,API.register(userName,password, 20).isResult());
    }

    @Test
    public void registerThenLoginSuccessTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password, 20);
        Result index = API.registeredLogin(userName,password);
        Assertions.assertEquals(index.getData(),1);
        Assertions.assertTrue((Boolean)API.isLogged((int)index.getData()).getData());
    }

    //AT-10.1
    @Test
    public void logoutSuccessTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password, 20);
        API.registeredLogin(userName,password);
        Assertions.assertTrue((boolean)API.registeredLogout(1).getData());
        Assertions.assertFalse((boolean)API.isLogged(1).getData());
    }

    //AT-10.2
    @Test
    public void logoutTwiceFailTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password, 20);
        API.registeredLogin(userName,password);
        Assertions.assertTrue((boolean)API.registeredLogout(1).isResult());
        Assertions.assertFalse((boolean) API.registeredLogout(1).isResult());
    }



    @Test
    public void isLoggedFailTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertFalse(API.registeredLogin(userName,password).isResult());
    }



    @Test
    //AT-1
    public void guestLoginSuccessTest(){
        Result index = API.guestLogin();
        assertEquals(index.getData(), 1);
        Assertions.assertTrue((boolean) API.isLogged((int)index.getData()).getData());

    }
    @Test
    //AT-2
    public void guestLogoutFailTest(){
        int index = (int)API.guestLogin().getData();
        Assertions.assertFalse((boolean) API.registeredLogout(index).isResult());
    }

    @Test
    public void scalabilityUserTest(){

        for (int i=1; i<60; i++){
            if(i!=(int)API.guestLogin().getData()){
                fail();
            }
        }
        Assertions.assertEquals(59,API.getNumOfUsers().getData());
    }

    //AT-3.1
    @Test
    public void guestRegisterSuccessTest(){
        int guestId= (int)API.guestLogin().getData();
        Assertions.assertEquals(1,API.guestRegister(guestId,"or","or321654").getData());
        Assertions.assertTrue((boolean) API.isLogged(guestId).isResult());
    }
    //AT-3.2
    @Test
    public void guestRegisterFailTest(){
        int guestId= (int)API.guestLogin().getData();
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password, 20);
        Assertions.assertEquals(false,API.guestRegister(guestId,"kandabior","or321654").isResult());
    }

    @Test
    public void connectionTest() throws IOException {



    }

}
