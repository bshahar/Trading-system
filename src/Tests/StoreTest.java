package Tests;

import Domain.*;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {


    //registered
    int registerId1;
    int registerId2;
    int registerId3;
    //guests
    int guestId1;
    int guestId2;
    //stores
    int storeId1;
    int storeId2;

    @BeforeEach
    public void setUp() {
//        User systemManager = new User("Elad",1,1);
//        tradingSystem= new TradingSystem(systemManager);
        API.initTradingSystem();
        String userName1="kandabior";
        String password1= "or321654";
        String userName2="elad";
        String password2= "elad321654";
        String userName3="erez";
        String password3= "erez321654";
        API.register(userName1,password1, 20);
        API.register(userName2,password2, 16);
        API.register(userName3,password3, 20);
        registerId1= (int)API.registeredLogin(userName1,password1).getData();
        registerId2=(int) API.registeredLogin(userName2,password2).getData();
        registerId3= (int)API.registeredLogin(userName3,password3).getData();
        storeId1=(int)API.openStore(registerId1,"kandabior store").getData();
        LinkedList <String> catList= new LinkedList<>();
        catList.add("FOOD");
        int productId=(int)API.addProduct(1, storeId1,"milk",catList ,10,"FOOD", 5 ).getData();


    }

    @Test
    //AT-5.1
    public void getStoreInformationSuccessTest() throws Exception{
        Assertions.assertEquals(storeId1, ((List<Store>)(API.getAllStoreInfo(registerId1).getData())).get(0).getStoreId());
    }

    @Test
    //AT-5.2
    public void getStoreInformationFailTest() throws Exception{
        API.registeredLogout(registerId1);
        assertFalse(API.getAllStoreInfo(registerId1).isResult());
    }

    @Test
    //AT-6
    public void getProductByNameSuccessTest() throws Exception {
        Filter filter = new Filter("NAME", "milk", 9, 15, -1, "", -1);
        //assume the first product gets id of 1
        Assertions.assertEquals(1,((Map<Integer,Integer>)(API.searchProduct(filter, registerId1).getData())).get(storeId1));
    }
    //AT-6
    @Test
    public void getProductByNameWrongPriceFailTest() throws Exception {
        Filter filter = new Filter("NAME", "milk", 1, 5, -1, "", -1);
        //assume the first product gets id of 1
        Assertions.assertEquals(0, ((Map<Integer,Integer>)(API.searchProduct(filter, registerId1).getData())).size());
    }
    //AT-6
    @Test
    public void getProductByNameWrongNameFailTest() throws Exception{
        Filter filter=new Filter("NAME","dani",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"",-1);
        Assertions.assertEquals(0,((Map<Integer,Integer>)(API.searchProduct(filter, registerId1).getData())).size());
    }
    //AT-6
    @Test
    public void getProductByCategorySuccessTest() throws Exception {
        Filter filter = new Filter("CATEGORY", "FOOD", 9, 15, -1, "", -1);
        //assume the first product gets id of 1
        int id = (int)((Map<Integer,Integer>)API.searchProduct(filter, registerId1).getData()).values().toArray()[0];
        assertEquals(1,id);
    }
    //AT-6
    @Test
    public void getProductByCategoryEmptyCategoryFailTest() throws Exception{
        Filter filter=new Filter("CATEGORY","DRINK",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"",-1);
        Assertions.assertFalse( API.searchProduct(filter, registerId1).isResult());
    }
    //AT-6
    @Test
    public void getProductByCategoryWrongCategoryFailTest() throws Exception{
        Filter filter=new Filter("NAME","milk",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"Drinks",-1);
        Assertions.assertFalse(API.searchProduct(filter, registerId1).isResult());
    }
    //AT-6
    @Test
    public void getProductByNameAndCategorySuccessTest() throws Exception{
        Filter filter=new Filter("NAME","milk",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"FOOD",-1);
        Assertions.assertEquals(1,((Map<Integer,Integer>)API.searchProduct(filter, registerId1).getData()).size());
    }

    @Test
    public void addToCartSuccessTest() throws Exception{
        Assertions.assertTrue(API.addProductToCart(registerId2,storeId1,1,3).isResult());
    }

    @Test
    //At-7
    public void addToCartGuestSuccessTest() throws Exception{
        guestId1=(int)API.guestLogin().getData();
        Assertions.assertTrue(API.addProductToCart(guestId1,storeId1,1,3).isResult());
    }

    @Test
    //At-7
    public void addToCartWrongProdFailTest() throws Exception{
        Assertions.assertFalse(API.addProductToCart(registerId2,storeId1,2,2).isResult());
    }

    @Test
    public void addToBagLoggedOutFailTest() throws Exception {
        API.registeredLogout(registerId2);
        Assertions.assertFalse(API.addProductToCart(registerId2,storeId1,1,1).isResult());
    }

    @Test
    public void addToCartRegisteredSuccessTest() throws Exception {
        API.addProductToCart(registerId2, storeId1, 1, 2);
        API.registeredLogout(registerId2);
        API.registeredLogin("elad", "elad321654");

        List<Bag> products = (List<Bag>) API.getCart(registerId2).getData();
        Assertions.assertTrue(products.get(0).getProductsAmounts().values().contains(2));
    }

    @Test
    public void addToCartGuestThenRegisteredSuccessTest() throws Exception {
        guestId1 = (int) API.guestLogin().getData();
        API.addProductToCart(guestId1, storeId1, 1, 2);
        API.guestRegister(guestId1, "dorin", "dorin321654");
        API.registeredLogout(guestId1);
        API.registeredLogin("dorin", "dorin321654");

        List<Bag> products = (List<Bag>) API.getCart(guestId1).getData();
        Assertions.assertTrue(products.get(0).getProductsAmounts().values().contains(2));
    }

    @Test
    //AT-11.1 success
    public void openStoreSuccessTest() throws Exception{
        Assertions.assertEquals(2,(int)API.openStore(registerId2,"elad store").getData());
    }

    @Test
    //AT-11.2 fail
    public void openStoreGuestFailTest() throws Exception{
        guestId1=(int)API.guestLogin().getData();
        Assertions.assertFalse(API.openStore(guestId1,"guest store").isResult());
    }

    @Test
    //AT-13 success
    public void addProductToStoreSuccessTest() throws Exception {
        List<String> categories = new LinkedList<>();
        categories.add("FOOD");
        assertTrue((int)(API.addProduct(registerId1,  storeId1, "water",categories,5,"drink", 5).getData())==2);

    }

    @Test
    //AT-13 fail
    public void addProductToStoreUserNotOwnerFailTest() throws Exception {
        List<String> categories = new LinkedList<>();
        categories.add("FOOD");
        assertFalse(API.addProduct(registerId2, storeId1, "water",categories,5,"drink", 5).isResult());
    }

    @Test
    //AT-13 success
    public void removeProductFromStoreSuccessTest(){
        API.removeProductFromStore(registerId1,storeId1,1);
        Assertions.assertEquals(0,((List<Product>)API.getAllStoreProducts(storeId1).getData()).size());
    }

    @Test
    //AT-13 alternate
    public void removeProductFromStoreProdDoesntExistFailTest() throws Exception{
        API.removeProductFromStore(registerId1,storeId1,2);
        Assertions.assertEquals(1,((List<Product>)API.getAllStoreProducts(storeId1).getData()).size());
    }

    @Test
    //AT-15.1
    public void addStoreOwnerSuccessTest() throws Exception{
        Assertions.assertTrue(API.addStoreOwner(registerId1,registerId2,storeId1).isResult());
    }

    @Test
    //AT-15.2
    public void addStoreOwnerGuestUserFailTest() throws Exception{
        guestId1=(int)API.guestLogin().getData();
        Assertions.assertFalse(API.addStoreOwner(registerId1,guestId1,storeId1).isResult());
    }

    @Test
    //AT-15
    public void addStoreOwnerRecursiveAppointmentFailTest() throws Exception{
        API.addStoreOwner(registerId1,registerId2,storeId1);
        Assertions.assertFalse(API.addStoreOwner(registerId2,registerId1,storeId1).isResult());
    }

    @Test
    //AT-15.2
    public void addStoreOwnerNotPermitFailTest() throws Exception{
        Assertions.assertFalse(API.addStoreOwner(registerId2,registerId3,storeId1).isResult());
    }

    @Test
    //AT-15.2
    public void addTwoStoreOwnersSuccessTest() throws Exception{
        API.addStoreOwner(registerId1,registerId2,storeId1);
        Assertions.assertTrue(API.addStoreOwner(registerId2,registerId3,storeId1).isResult());
    }

    @Test
    //AT-16.1
    public void addManagerTest() throws Exception{
        Assertions.assertTrue(API.addStoreManager(registerId1,registerId2,storeId1).isResult());
    }


    @Test
    //AT-16.2
    public void addStoreManagerNotPermitFailTest() {
        API.addStoreManager(registerId1,registerId2,storeId1);
        Assertions.assertFalse(API.addStoreManager(registerId2,registerId3,storeId1).isResult());
    }
    @Test
    //AT-16.2
    public void addStoreManagerGuestUserFailTest() throws Exception{
        guestId1=(int)API.guestLogin().getData();
        Assertions.assertFalse(API.addStoreManager(registerId1,guestId1,storeId1).isResult());
    }

    @Test
    //AT-18.1
    public void removeStoreManagerSuccessTest() throws Exception{
        API.addStoreManager(registerId1,registerId2,storeId1);
        Assertions.assertTrue(API.removeManager(registerId1,registerId2,storeId1).isResult());
    }

    @Test
    //AT-18.2
    public void removeStoreManagerNotTheAppointerFailTest() throws Exception{
        API.addStoreOwner(registerId1,registerId2,storeId1);
        API.addStoreManager(registerId2,registerId3,storeId1);
        Assertions.assertFalse(API.removeManager(registerId1,registerId3,storeId1).isResult());
    }

    @Test
    //AT-18.3
    public void removeStoreManagerWrongUserFailTest() throws Exception{
        Assertions.assertFalse(API.removeManager(registerId1,registerId3,storeId1).isResult());
    }


    @Test
    //AT-19.1
    public void getStoreWorkersInfoSuccessTest() throws Exception{
        List<User> workers = (List<User>)API.getStoreWorkers(registerId1,storeId1).getData();
        Assertions.assertEquals(workers.get(0).getId(), registerId1);
    }

    @Test
    //AT-19.2
    public void getStoreWorkersInfoNotPermitFailTest() throws Exception{
        assertFalse(API.getStoreWorkers(registerId2,storeId1).isResult());
    }

    @Test
    //AT-20.1
    public void getPurchaseHistorySuccessTest() throws Exception{
        API.addProductToCart(registerId2,storeId1,1,1);
        API.buyProduct(registerId2, storeId1, "Credit123");
        Assertions.assertEquals(storeId1,((List<Receipt>) API.getStorePurchaseHistory(registerId1,storeId1).getData()).get(0).getStoreId());
    }

    @Test
    //AT-20.2
    public void getPurchaseHistoryNotPermitFailTest() throws Exception{
        API.addProductToCart(registerId2,storeId1,1,1);

        API.buyProduct(registerId2, storeId1, "Credit123");
        assertFalse( API.getStorePurchaseHistory(registerId2,storeId1).isResult());
    }


    @Test
    //AT-20.3
    public void getPurchaseHistorySecondOwnerSuccessTest(){
        API.addStoreOwner(registerId1,registerId2,storeId1);
        API.addProductToCart(registerId2,storeId1,1,1);

        API.buyProduct(registerId2, storeId1, "Credit123");

        Assertions.assertEquals(storeId1,((List<Receipt>)API.getStorePurchaseHistory(registerId2,storeId1).getData()).get(0).getStoreId());
    }

    @Test
    //AT-8.1
    public void getCartInfoSuccessTest() throws  Exception{
        API.addProductToCart(registerId1, storeId1, 1, 1);
        Assertions.assertTrue( ((List<Bag>)API.getCart(registerId1).getData()).get(0).getProducts().get(0).getId() == 1);
    }

    @Test
    //AT-8.2
    public void getCartInfoLoggedOutFailTest() throws  Exception{
        API.addProductToCart(registerId1, storeId1, 1, 1);
        API.registeredLogout(registerId1);
        Assertions.assertFalse( API.getCart(registerId1).isResult());
    }

    @Test
    //AT-22.4
    public void registerTwoUsersSuccessSyncTest() {
        for (int i = 0; i < 100; i++) {
            setUp();
            registerTwoUsers();
            Assertions.assertEquals(4,(int)API.getNumOfUsers().getData());
        }
    }

    private void registerTwoUsers() {
        try {
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    API.register("same name", "1234", 20);
                }
            });
            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    API.register("same name", "1234", 20);
                }
            });

            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        } catch (Exception e) {
            fail();

        }
    }


    @Test
    //AT-22.3
    public void appointTwoManagersSuccessSyncTest() {
        for (int i = 0; i < 100; i++) {
            setUp();
            API.addStoreOwner(registerId1, registerId2, storeId1);
            appointTwoManagers();
            Assertions.assertEquals(3,((List<User>)API.getStoreWorkers(registerId1,storeId1).getData()).size());
        }

    }

    private void appointTwoManagers() {
        try {
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addStoreManager(registerId1, registerId3, storeId1);
                }
            });
            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addStoreManager(registerId2, registerId3, storeId1);
                }
            });

            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        } catch (Exception e) {
            fail();

        }
    }



    @Test
    //AT-22.5
    public void addNewStoresSuccessSyncTest() {
        for (int i = 0; i < 100; i++) {
            setUp();
            addNewStores();
            Assertions.assertEquals(101,(int)API.getNumOfStores().getData());
        }
    }

    private void addNewStores() {
        try {
            List<Thread> threads = new LinkedList<>();
            for (int i = 0; i < 100; i++) {
                threads.add(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        API.openStore(registerId1, "newStore");
                    }
                }));
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (Exception e) {
            fail();

        }
    }


    @Test
    //AT-22.6
    public void deleteSimultaneouslySuccessSyncTest(){
        for(int i=0; i<100; i++){
            setUp();
            deleteSimultaneously();
            Assertions.assertEquals(0,((List<Product>)API.getAllStoreProducts(storeId1).getData()).size());
        }
    }

    private void deleteSimultaneously() {
        try {
            API.addStoreOwner(registerId1, registerId2, storeId1);
            Thread thread1 = new Thread(() -> API.removeProductFromStore(registerId1, storeId1, 1));
            Thread thread2 = new Thread(() -> API.removeProductFromStore(registerId2, storeId1, 1));
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        } catch (Exception e) {
            fail();

        }
    }

}
