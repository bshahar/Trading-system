package Tests;

import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {


    @BeforeEach
    public void setUp() {
        API.initTradingSystem("Elad");
    }

    //AT-4.1
    @Test
    public void loginSuccessTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(1,API.register(userName,password));
    }
    //AT-4.2
    @Test
    public void loginBadPasswordFailTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(1,API.register(userName,"12"));
        int index = API.registeredLogin(userName,password);
        assertEquals(index,-1);
    }

    //AT-4.3
    @Test
    public void registerExistingUserFailTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        Assertions.assertEquals(-1,API.register(userName,password));
    }

    @Test
    public void registerThenLoginSuccessTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        int index = API.registeredLogin(userName,password);
        Assertions.assertEquals(index,1);
        Assertions.assertTrue(API.isLogged(index));
    }

    //AT-10.1
    @Test
    public void logoutSuccessTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        API.registeredLogin(userName,password);
        Assertions.assertTrue(API.registeredLogout(1));
        Assertions.assertFalse(API.isLogged(1));
    }

    //AT-10.2
    @Test
    public void logoutTwiceFailTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        API.registeredLogin(userName,password);
        Assertions.assertTrue(API.registeredLogout(1));
        Assertions.assertFalse(API.registeredLogout(1));
    }



    @Test
    public void isLoggedFailTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(-1,API.registeredLogin(userName,password));
    }



    @Test
    //AT-1
    public void guestLoginSuccessTest(){
        int index = API.guestLogin();
        assertEquals(index, 1);
        Assertions.assertTrue(API.isLogged(index));

    }
    @Test
    //AT-2
    public void guestLogoutSuccessTest(){
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
    public void guestRegisterSuccessTest(){
        int guestId= API.guestLogin();
        Assertions.assertEquals(1,API.guestRegister(guestId,"or","or321654"));
        Assertions.assertTrue(API.isLogged(guestId));
    }
    //AT-3.2
    @Test
    public void guestRegisterFailTest(){
        int guestId= API.guestLogin();
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        Assertions.assertEquals(-1,API.guestRegister(guestId,"kandabior","or321654"));
    }

    @Test
    public void TestMe(){
        System.out.println(getMaxTools(1,11));
    }

    public static int getMaxTools(int start,int end){
        if(start>end){
            return -1;
        }
        Map<Integer,Integer> map= new HashMap<>();
        for( int i=start; i<=end ;i++){
            String temp= String.valueOf(i);
            int sum=0;
            for(int j=0; j<temp.length(); j++){
                sum+= temp.charAt(j)- '0';
            }
            if(map.containsKey(sum)){
                map.put(sum,map.get(sum)+1);
            }else{
                map.put(sum,1);
            }
        }
        //get max
        int max=0;
        for (int key: map.keySet()){
            if(map.get(key)>max){
                max=map.get(key);
            }
        }
        return max;


    }






















}
