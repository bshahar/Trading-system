package Tests;
import Service.API;
import Service.KingLogger;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigurationFileTest {

    org.json.simple.JSONObject jsonObject;
    JSONParser jsonParser;

    @BeforeAll
    public void setUp(){
        jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader("resources\\UnitConfigurationTest.json");
            jsonObject = (org.json.simple.JSONObject) jsonParser.parse(reader);
        }
        catch(Exception e){
        }
    }

    @Test
    public void checkRegister(){
        JSONArray registerArray = (JSONArray)jsonObject.get("register");
        String userName = (String) (( org.json.simple.JSONObject )registerArray.get(0)).get("username");
        String password = (String) (( org.json.simple.JSONObject )registerArray.get(0)).get("password");
        int age = Math.toIntExact((long)((org.json.simple.JSONObject)registerArray.get(0)).get("age"));
        Assertions.assertEquals("kandabior", userName);
        Assertions.assertEquals("or321654", password);
        Assertions.assertEquals(20, age);
    }

    @Test
    public void checkRegisterNumberOfUsers(){
        JSONArray registerArray = (JSONArray)jsonObject.get("register");
        int length = registerArray.size();
        Assertions.assertEquals(3, length);
    }

    @Test
    public void checkLogin(){
        JSONArray login = (JSONArray)jsonObject.get("login");
        String userName = (String) (( org.json.simple.JSONObject )login.get(1)).get("username");
        String password = (String) (( org.json.simple.JSONObject )login.get(1)).get("password");
        Assertions.assertEquals("elad", userName);
        Assertions.assertEquals("elad321654", password);
    }

    @Test
    public void checkLoginOutOfBounds(){
        try {
            JSONArray login = (JSONArray) jsonObject.get("login");
            String userName = (String) ((org.json.simple.JSONObject) login.get(3)).get("username");
            String password = (String) ((org.json.simple.JSONObject) login.get(3)).get("password");
            fail();
        }
        catch (Exception e){

        }
    }

    @Test
    public void checkOpenStore(){
        JSONArray openStore = (JSONArray)jsonObject.get("openStore");
        String userName = (String) (( org.json.simple.JSONObject )openStore.get(0)).get("userOwnerName");
        String storeName = (String) (( org.json.simple.JSONObject )openStore.get(0)).get("storeName");
        Assertions.assertEquals("elad", userName);
        Assertions.assertEquals("storeNameTest1", storeName);
    }

    @Test
    public void checkRemoveProdect(){
        JSONArray removeProduct = (JSONArray)jsonObject.get("removeProduct");
        String userName = (String) (( org.json.simple.JSONObject )removeProduct.get(0)).get("managerUserName");
        String storeName = (String) (( org.json.simple.JSONObject )removeProduct.get(0)).get("storeName");
        String productName = (String) (( org.json.simple.JSONObject )removeProduct.get(0)).get("prodName");
        Assertions.assertEquals("elad", userName);
        Assertions.assertEquals("storeNameTest1", storeName);
        Assertions.assertEquals("Corn", productName);
    }

    @Test
    public void checkAddStoreManager(){
        JSONArray addStoreManager = (JSONArray)jsonObject.get("addStoreManager");
        String appointer = (String) (( org.json.simple.JSONObject )addStoreManager.get(0)).get("appointerUserName");
        String appointee = (String) (( org.json.simple.JSONObject )addStoreManager.get(0)).get("appointeeUserName");
        String storeName = (String) (( org.json.simple.JSONObject )addStoreManager.get(0)).get("storeName");
        Assertions.assertEquals("elad", appointer);
        Assertions.assertEquals("kandabior", appointee);
        Assertions.assertEquals("storeNameTest1", storeName);
    }

    @Test
    public void checkAddProductNotFound(){
        JSONArray addProduct = (JSONArray) jsonObject.get("addProduct");
        Assertions.assertNull(addProduct);
    }

    @Test
    public void checkLogoutNotFound(){
        JSONArray logout = (JSONArray) jsonObject.get("logout");
        Assertions.assertNull(logout);
    }
}
