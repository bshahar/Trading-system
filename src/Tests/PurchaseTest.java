package Tests;

import Domain.Product;
import Domain.Receipt;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {


    private int registerId1;
    private int registerId2;
    private int registerId3;
    private int storeId1;
    private int storeId2;
    private int productId1;
    private int productId2;
    private Properties testProps;
    private Map<String, String> payment;
    private Map<String, String> supplement;

    @BeforeEach
    public void setUp() {
        testProps = new Properties();
        try {
            API.initTradingSystem();
            InputStream input = getClass().getClassLoader().getResourceAsStream("testsSetUp.properties");
            if(input != null)
                testProps.load(input);
            else
                throw new FileNotFoundException("Property file was not found.");
        } catch (Exception e) {
        }


        API.register(testProps.getProperty("user1name"), testProps.getProperty("user1password"), Integer.parseInt(testProps.getProperty("user1age")));
        API.register(testProps.getProperty("user2name"), testProps.getProperty("user2password"), Integer.parseInt(testProps.getProperty("user2age")));
        API.register(testProps.getProperty("user3name"), testProps.getProperty("user3password"), Integer.parseInt(testProps.getProperty("user3age")));

        registerId1 = (int) API.registeredLogin(testProps.getProperty("user1name"), testProps.getProperty("user1password")).getData();
        registerId2 = (int) API.registeredLogin(testProps.getProperty("user2name"), testProps.getProperty("user2password")).getData();
        registerId3 = (int) API.registeredLogin(testProps.getProperty("user3name"), testProps.getProperty("user3password")).getData();

        storeId1 = (int) API.openStore(registerId1, testProps.getProperty("storeNameTest")).getData();
        LinkedList<String> catList = new LinkedList<>();
        catList.add(testProps.getProperty("categoryFood"));
        productId1 = (int) API.addProduct(1, storeId1, testProps.getProperty("prodMilkName"), catList,
                Integer.parseInt(testProps.getProperty("milkPrice")),
                testProps.getProperty("descriptionFood"),
                Integer.parseInt(testProps.getProperty("prodQuantity1"))).getData();

        payment = new HashMap<>();
        payment.put("card_number", testProps.getProperty("creditCardNumber"));
        payment.put("month", testProps.getProperty("creditExpMonth"));
        payment.put("year", testProps.getProperty("creditExpYear"));
        payment.put("holder", testProps.getProperty("user1name"));
        payment.put("cvv", testProps.getProperty("creditCvv"));
        payment.put("id", String.valueOf(registerId1));

        supplement = new HashMap<>();
        supplement.put("name", testProps.getProperty("user1name"));
        supplement.put("address", testProps.getProperty("supplyAddress"));
        supplement.put("city", testProps.getProperty("supplyCity"));
        supplement.put("country", testProps.getProperty("supplyCountry"));
        supplement.put("zip", testProps.getProperty("supplyZipCode"));

        /*
        API.initTradingSystem("Elad");
        String userName1 = "kandabior";
        String password1 = "or321654";
        String userName2 = "elad";
        String password2 = "elad321654";
        String userName3 = "erez";
        String password3 = "erez321654";
        API.register(userName1, password1, 20);
        API.register(userName2, password2, 16);
        API.register(userName3, password3, 20);
        registerId1 = (int) API.registeredLogin(userName1, password1).getData();
        registerId2 = (int) API.registeredLogin(userName2, password2).getData();
        registerId3 = (int) API.registeredLogin(userName3, password3).getData();
        storeId1 = (int) API.openStore(registerId1, "kandabior store").getData();
        LinkedList<String> catList = new LinkedList<>();
        catList.add("FOOD");
        productId1 = (int) API.addProduct(1, storeId1, "milk", catList, 10, "FOOD", 1).getData();

         */
    }

    @Test
    //AT-9
    public void purchaseOneItemSuccessTest() {
        API.addProductToCart(registerId1, storeId1, productId1, 1);
        Assertions.assertTrue(API.buyProduct(registerId1, storeId1, payment, supplement).isResult());
    }


    @Test
    //AT-9
    public void purchaseTwoItemsSuccessTest() {
        API.addProductToCart(registerId1, storeId1, productId1, 1);
        API.addProductToCart(registerId1, storeId1, productId2, 1);
        Assertions.assertTrue(API.buyProduct(registerId1, storeId1, payment, supplement).isResult());
    }

    @Test
    //AT-9
    public void twoUsersPurchaseSameItemFailTest() {
        API.addProductToCart(registerId1, storeId1, productId1, 1);
        API.addProductToCart(registerId2, storeId1, productId1, 1);
        Assertions.assertTrue(API.buyProduct(registerId1, storeId1, payment, supplement).isResult());
        Assertions.assertFalse(API.buyProduct(registerId2, storeId1, payment, supplement).isResult());
    }

    public void TestSync(){
        try{

            API.addProductToCart(registerId1,storeId1,productId1,1);
            API.addProductToCart(registerId2,storeId1,productId1,1);
            Thread thread1= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.buyProduct(registerId1,storeId1, payment, supplement);
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.buyProduct(registerId2,storeId1, payment, supplement);
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        }catch(Exception e){
            fail();
        }
    }

    @Test
    //AT-22.1
    public void twoPurchasesSyncTest(){
        int orCount=0;
        int eladCount=0;
        for(int i=0; i<100; i++){
            setUp();
            TestSync();
            Assertions.assertEquals(1,((List<Object>)(API.getStorePurchaseHistory(registerId1,storeId1).getData())).size());
            String username=((List<Receipt>)(API.getStorePurchaseHistory(registerId1,storeId1).getData())).get(0).getUserName();
            if(username.equals("kandabior"))
                orCount++;
            else
                eladCount++;
            assertTrue(((username.equals("kandabior"))|| (username.equals("elad"))));
            assertEquals(1, ((List<Receipt>) (API.getStorePurchaseHistory(registerId1, storeId1).getData())).size());
        }
        System.out.println("or: "+ orCount);
        System.out.println("elad: "+ eladCount);
    }


    @Test
    //AT-22.2
    public void purchaseRemoveSyncTest(){
        try {
            for (int i = 0; i < 100; i++) {
                setUp();
                removePurchaseTest();

            }
        }catch(Exception e){
            fail();
        }
    }

    private boolean removePurchaseTest() {
        try{
            final boolean[] success = {false};
            API.addProductToCart(registerId2,storeId1,productId1,1);
            Thread thread1= new Thread(() -> API.buyProduct(registerId2,storeId1, payment, supplement));
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    if(API.removeProductFromStore(registerId1,storeId1,productId1).isResult()){
                        success[0] =true;
                    }
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            return success[0];
        }catch(Exception e){
            fail();
            return false;
        }
    }

    @Test
    //AT-12.1
    public void getPersonalPurchaseHistorySuccessTest(){
        API.addProductToCart(registerId1,storeId1,productId1,1);
        API.buyProduct(registerId1,storeId1, payment, supplement);
        List<Receipt> receiptList=(List<Receipt>) API.getUserPurchaseHistory(registerId1).getData();
        assertEquals(receiptList.get(0).getUserId(), registerId1);
    }

    @Test
    //AT-12.2
    public void getPersonalPurchaseHistoryFailTest(){
        int guestId= (int)API.guestLogin().getData();
        API.addProductToCart(guestId,storeId1,productId1,1);
        API.buyProduct(guestId,storeId1, payment, supplement);
        Assertions.assertFalse(API.getUserPurchaseHistory(guestId).isResult());
    }


}
