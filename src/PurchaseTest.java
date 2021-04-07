import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {


    private TradingSystem tradingSystem;
    private int registerId1;
    private int registerId2;
    private int registerId3;
    private int storeId1;
    private int productId1;
    private int productId2;

    @BeforeEach
    public void setUp() {

        User systemManager = new User("Elad",1,1);
        tradingSystem= new TradingSystem(systemManager);
        String userName1="kandabior";
        String password1= "or321654";
        String userName2="elad";
        String password2= "elad321654";
        String userName3="erez";
        String password3= "erez321654";
        tradingSystem.register(userName1,password1);
        tradingSystem.register(userName2,password2);
        tradingSystem.register(userName3,password3);
        registerId1= tradingSystem.login(userName1,password1);
        registerId2= tradingSystem.login(userName2,password2);
        registerId3= tradingSystem.login(userName3,password3);
        storeId1=tradingSystem.openStore(registerId1,"kandabior store");
        LinkedList<Product.Category> catList= new LinkedList<>();
        catList.add(Product.Category.FOOD);
        productId1= tradingSystem.addProductToStore(1, storeId1,"milk",catList ,10,"FOOD", 1 );


    }

    @Test
    public void purchaseTest(){
        tradingSystem.addProductToBag(registerId1,storeId1,productId1,1);
        assertTrue(tradingSystem.buyProducts(registerId1,storeId1,"123456789"));
    }


    @Test
    public void purchaseTest2(){
        tradingSystem.addProductToBag(registerId1,storeId1,productId1,1);
        tradingSystem.addProductToBag(registerId1,storeId1,productId2,1);
        assertTrue(tradingSystem.buyProducts(registerId1,storeId1,"123456789"));
    }

    @Test
    public void purchaseTest3(){
        tradingSystem.addProductToBag(registerId1,storeId1,productId1,1);
        tradingSystem.addProductToBag(registerId2,storeId1,productId1,1);
        assertTrue(tradingSystem.buyProducts(registerId1,storeId1,"123456789"));
        assertFalse(tradingSystem.buyProducts(registerId2,storeId1,"123456789"));
    }

    public void TestSync(){
        try{

            tradingSystem.addProductToBag(registerId1,storeId1,productId1,1);
            tradingSystem.addProductToBag(registerId2,storeId1,productId1,1);
            Thread thread1= new Thread(new Runnable() {
                @Override
                public void run() {
                    tradingSystem.buyProducts(registerId1,storeId1,"123456789");
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    tradingSystem.buyProducts(registerId2,storeId1,"123456789");
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
    public void twoPurchaseSyncTest(){
        int orCount=0;
        int eladCount=0;
        for(int i=0; i<100; i++){
            setUp();
            TestSync();
            assertEquals(1,tradingSystem.getStorePurchaseHistory(registerId1,storeId1).size());
            String username=tradingSystem.getStorePurchaseHistory(registerId1,storeId1).get(0).getUserName();
            if(username=="kandabior")
                orCount++;
            else
                eladCount++;
            assertTrue((username=="kandabior")|| (username=="elad"));
        }
        System.out.println("or: "+ orCount);
        System.out.println("elad: "+ eladCount);
    }


    @Test
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
            tradingSystem.addProductToBag(registerId2,storeId1,productId1,1);
            Thread thread1= new Thread(() -> tradingSystem.buyProducts(registerId2,storeId1,"123456789"));
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    if(tradingSystem.removeProductFromStore(registerId1,storeId1,productId1)){
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

}
