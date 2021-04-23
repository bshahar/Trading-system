package Tests;

import Domain.Result;
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
    public void registerTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(1,API.register(userName,password).getdata());
    }
    //AT-4.2
    @Test
    public void loginBadPass(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(1,API.register(userName,"12").getdata());
        Result result= API.registeredLogin(userName,password);
        assertEquals(result.getdata(),-1);
    }

    //AT-4.3
    @Test
    public void failRegisterTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        Assertions.assertEquals(false,API.register(userName,password).isResult());
    }

    @Test
    public void loginTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        Result index = API.registeredLogin(userName,password);
        Assertions.assertEquals(index.getdata(),1);
        Assertions.assertTrue((Boolean)API.isLogged((int)index.getdata()).getdata());
    }

    //AT-10.1
    @Test
    public void logoutTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        API.registeredLogin(userName,password);
        Assertions.assertTrue((boolean)API.registeredLogout(1).getdata());
        Assertions.assertFalse((boolean)API.isLogged(1).getdata());
    }

    //AT-10.2
    @Test
    public void logoutTwiceTest(){
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        API.registeredLogin(userName,password);
        Assertions.assertTrue((boolean)API.registeredLogout(1).isResult());
        Assertions.assertFalse((boolean) API.registeredLogout(1).isResult());
    }



    @Test
    public void failLoginTest(){
        String userName="kandabior";
        String password= "or321654";
        Assertions.assertEquals(-1,API.registeredLogin(userName,password).getdata());
    }



    @Test
    //AT-1
    public void guestLoginTest(){
        Result index = API.guestLogin();
        assertEquals(index.getdata(), 1);
        Assertions.assertTrue((boolean) API.isLogged((int)index.getdata()).getdata());

    }
    @Test
    //AT-2
    public void guestLogoutTest(){
        int index = (int)API.guestLogin().getdata();
        Assertions.assertFalse((boolean) API.registeredLogout(index).isResult());
    }

    @Test
    public void scalabilityUserTest(){

        for (int i=1; i<1000; i++){
            if(i!=(int)API.guestLogin().getdata()){
                fail();
            }
        }
        Assertions.assertEquals(999,API.getNumOfUsers().getdata());
    }

    //AT-3.1
    @Test
    public void guestRegisterTest(){
        int guestId= (int)API.guestLogin().getdata();
        Assertions.assertEquals(1,API.guestRegister(guestId,"or","or321654").getdata());
        Assertions.assertTrue((boolean) API.isLogged(guestId).isResult());
    }
    //AT-3.2
    @Test
    public void guestRegisterTestFail(){
        int guestId= (int)API.guestLogin().getdata();
        String userName="kandabior";
        String password= "or321654";
        API.register(userName,password);
        Assertions.assertEquals(false,API.guestRegister(guestId,"kandabior","or321654").isResult());
    }

    @Test
    public void testme(){
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
