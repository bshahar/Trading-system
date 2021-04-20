package Tests;

import Domain.Product;
import Domain.Receipt;
import Domain.User;
import Interface.TradingSystem;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {


    private int registerId1;
    private int registerId2;
    private int registerId3;
    private int storeId1;
    private int productId1;
    private int productId2;

    @BeforeEach
    public void setUp() {
//
//        User systemManager = new User("Elad",1,1);
//        tradingSystem= new TradingSystem(systemManager);
        API.initTradingSystem("Elad");
        String userName1="kandabior";
        String password1= "or321654";
        String userName2="elad";
        String password2= "elad321654";
        String userName3="erez";
        String password3= "erez321654";
        API.register(userName1,password1);
        API.register(userName2,password2);
        API.register(userName3,password3);
        registerId1= API.registeredLogin(userName1,password1);
        registerId2= API.registeredLogin(userName2,password2);
        registerId3= API.registeredLogin(userName3,password3);
        storeId1=API.openStore(registerId1,"kandabior store");
        LinkedList<Product.Category> catList= new LinkedList<>();
        catList.add(Product.Category.FOOD);
        productId1= API.addProduct(1, storeId1,"milk",catList ,10,"FOOD", 1 );


    }

    @Test
    //AT-9
    public void purchaseTest(){
        API.addProductToCart(registerId1,storeId1,productId1,1);
        Assertions.assertTrue(API.buyProduct(registerId1,storeId1,"123456789"));
    }


    @Test
    //AT-9
    public void purchaseTest2(){
        API.addProductToCart(registerId1,storeId1,productId1,1);
        API.addProductToCart(registerId1,storeId1,productId2,1);
        Assertions.assertTrue(API.buyProduct(registerId1,storeId1,"123456789"));
    }

    @Test
    //AT-9
    public void purchaseTest3(){
        API.addProductToCart(registerId1,storeId1,productId1,1);
        API.addProductToCart(registerId2,storeId1,productId1,1);
        Assertions.assertTrue(API.buyProduct(registerId1,storeId1,"123456789"));
        Assertions.assertFalse(API.buyProduct(registerId2, storeId1, "123456789"));
    }

    public void TestSync(){
        try{

            API.addProductToCart(registerId1,storeId1,productId1,1);
            API.addProductToCart(registerId2,storeId1,productId1,1);
            Thread thread1= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.buyProduct(registerId1,storeId1,"123456789");
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.buyProduct(registerId2,storeId1,"123456789");
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
    public void twoPurchaseSyncTest(){
        int orCount=0;
        int eladCount=0;
        for(int i=0; i<100; i++){
            setUp();
            TestSync();
            Assertions.assertEquals(1,API.getStorePurchaseHistory(registerId1,storeId1).size());
            String username=API.getStorePurchaseHistory(registerId1,storeId1).get(0).getUserName();
            if(username=="kandabior")
                orCount++;
            else
                eladCount++;
            assertTrue(((username=="kandabior")|| (username=="elad")));
            assertTrue(API.getStorePurchaseHistory(registerId1,storeId1).size()==1);
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
            Thread thread1= new Thread(() -> API.buyProduct(registerId2,storeId1,"123456789"));
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    if(API.removeProductFromStore(registerId1,storeId1,productId1)){
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
    public void getPersonalPurchaseHistoryTest(){
        API.addProductToCart(registerId1,storeId1,productId1,1);
        API.buyProduct(registerId1,storeId1,"123456789");
        List<Receipt> receiptList= API.getUserPurchaseHistory(registerId1);
        Assertions.assertTrue(receiptList.get(0).getUserId()==registerId1);
    }

    @Test
    //AT-12.2
    public void getPersonalPurchaseHistoryFailTest(){
        int guestId= API.guestLogin();
        API.addProductToCart(guestId,storeId1,productId1,1);
        API.buyProduct(guestId,storeId1,"123456789");
        Assertions.assertNull(API.getUserPurchaseHistory(guestId));

    }



}
