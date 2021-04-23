//package Tests;
//
//import Domain.*;
//import Interface.TradingSystem;
//import Service.API;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.crypto.NoSuchPaddingException;
//import java.nio.file.OpenOption;
//import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class StoreTest {
//
//
//    //registered
//    int registerId1;
//    int registerId2;
//    int registerId3;
//    //guests
//    int guestId1;
//    int guestId2;
//    //stores
//    int storeId1;
//    int storeId2;
//
//    @BeforeEach
//    public void setUp(){
////        User systemManager = new User("Elad",1,1);
////        tradingSystem= new TradingSystem(systemManager);
//        API.initTradingSystem("Elad");
//        String userName1="kandabior";
//        String password1= "or321654";
//        String userName2="elad";
//        String password2= "elad321654";
//        String userName3="erez";
//        String password3= "erez321654";
//        API.register(userName1,password1);
//        API.register(userName2,password2);
//        API.register(userName3,password3);
//        registerId1= API.registeredLogin(userName1,password1);
//        registerId2= API.registeredLogin(userName2,password2);
//        registerId3= API.registeredLogin(userName3,password3);
//        storeId1=API.openStore(registerId1,"kandabior store");
//        LinkedList <Product.Category> catList= new LinkedList<>();
//        catList.add(Product.Category.FOOD);
//        int productId=API.addProduct(1, storeId1,"milk",catList ,10,"FOOD", 5 );
//
//
//    }
//
//    @Test
//    //AT-5.1
//    public void getInformationTest() throws Exception{
//        Assertions.assertEquals(storeId1, API.getAllStoreInfo(registerId1).get(0).getStoreId());
//    }
//
//    @Test
//    //AT-5.2
//    public void getInformationFailTest() throws Exception{
//        API.registeredLogout(registerId1);
//        assertNull(API.getAllStoreInfo(registerId1));
//    }
//
//    @Test
//    //AT-6
//    public void getProductByNameTest() throws Exception{
//        Filter filter=new Filter("NAME","milk",9,15,-1,"",-1);
//        //assume the first product gets id of 1
//        Assertions.assertEquals(1,API.searchProduct(filter, registerId1).get(storeId1));
//    }
//    //AT-6
//    @Test
//    public void getProductByNameFailTest() throws Exception{
//        Filter filter=new Filter("NAME","milk",1,5,-1,"",-1);
//        //assume the first product gets id of 1
//        Assertions.assertEquals(0, API.searchProduct(filter, registerId1).size());
//    }
//    //AT-6
//    @Test
//    public void failGetProductTest() throws Exception{
//        Filter filter=new Filter("NAME","dani",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"",-1);
//        Assertions.assertEquals(0,API.searchProduct(filter, registerId1).size());
//    }
//    //AT-6
//    @Test
//    public void getProductByCategoryTest() throws Exception{
//        Filter filter=new Filter("CATEGORY","FOOD",9, 15,-1,"",-1);
//        //assume the first product gets id of 1
//        int id = (int)API.searchProduct(filter, registerId1).values().toArray()[0];
//        assertEquals(1,id);
//    }
//    //AT-6
//    @Test
//    public void failGetProductByCategoryTest() throws Exception{
//        Filter filter=new Filter("CATEGORY","DRINK",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"",-1);
//        Assertions.assertEquals(0,API.searchProduct(filter, registerId1).size());
//    }
//    //AT-6
//    @Test
//    public void failGetProductByNameAndCategoryTest() throws Exception{
//        Filter filter=new Filter("NAME","milk",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"Drinks",-1);
//        Assertions.assertEquals(0,API.searchProduct(filter, registerId1).size());
//    }
//    //AT-6
//    @Test
//    public void GetProductByNameAndCategoryTest() throws Exception{
//        Filter filter=new Filter("NAME","milk",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"FOOD",-1);
//        Assertions.assertEquals(1,API.searchProduct(filter, registerId1).size());
//    }
//
//    @Test
//    public void addToCartTest() throws Exception{
//        Assertions.assertTrue(API.addProductToCart(registerId2,storeId1,1,3));
//    }
//
//    @Test
//    //At-7
//    public void addToBagGuestTest() throws Exception{
//        guestId1=API.guestLogin();
//        Assertions.assertTrue(API.addProductToCart(guestId1,storeId1,1,3));
//    }
//
//    @Test
//    //At-7
//    public void addToChartWrongProdTest() throws Exception{
//        Assertions.assertFalse(API.addProductToCart(registerId2,storeId1,2,2));
//    }
//
//    @Test
//    public void addToBagLogoutTest() throws Exception{
//        API.registeredLogout(registerId2);
//        Assertions.assertFalse(API.addProductToCart(registerId2,storeId1,1,1));
//    }
//
//    @Test
//    public void addToChartTest2() throws Exception{
//        API.addProductToCart(registerId2,storeId1,1,2);
//        API.registeredLogout(registerId2);
//        API.registeredLogin("elad","elad321654");
//
//        List<Bag> products=API.getCart(registerId2);
//        Assertions.assertEquals(2, products.get(0).getProductIds().get(1));
//    }
//
//    @Test
//    public void addToCartTest3() throws Exception{
//        guestId1=API.guestLogin();
//        API.addProductToCart(guestId1,storeId1,1,2);
//        API.guestRegister(guestId1,"dorin","dorin321654");
//        API.registeredLogout(guestId1);
//        API.registeredLogin("dorin","dorin321654");
//
//        List<Bag> products=API.getCart(guestId1);
//        Assertions.assertEquals(2, products.get(0).getProductIds().get(1));
//    }
//
//    @Test
//    //AT-11.1 success
//    public void openStoreTest() throws Exception{
//        Assertions.assertEquals(2,API.openStore(registerId2,"elad store"));
//    }
//
//    @Test
//    //AT-11.2 fail
//    public void guestOpenStoreFailTest() throws Exception{
//        guestId1=API.guestLogin();
//        Assertions.assertEquals(-1,API.openStore(guestId1,"guest store"));
//    }
//
//    @Test
//    //AT-13 success
//    public void addProductTest() throws Exception{
//        List<Product.Category> categories= new LinkedList<>();
//        categories.add(Product.Category.FOOD);
//        assertTrue(API.addProduct(registerId1,  storeId1, "water",categories,5,"drink", 5)==2);
//
//    }
//
//    @Test
//    //AT-13 fail
//    public void addProductFailTest() throws Exception{
//        List<Product.Category> categories= new LinkedList<>();
//        categories.add(Product.Category.FOOD);
//        assertFalse(API.addProduct(registerId2, storeId1, "water",categories,5,"drink", 5)==2);
//    }
//
//    @Test
//    //AT-13 success
//    public void removeProductTest(){
//        API.removeProductFromStore(registerId1,storeId1,1);
//        Assertions.assertEquals(0,API.getAllStoreProducts(storeId1).size());
//    }
//
//    @Test
//    //AT-13 alternate
//    public void removeProductFailTest() throws Exception{
//        API.removeProductFromStore(registerId1,storeId1,2);
//        Assertions.assertEquals(1,API.getAllStoreProducts(storeId1).size());
//    }
//
//    @Test
//    //AT-15.1
//    public void addOwnerTest() throws Exception{
//        Assertions.assertTrue(API.addStoreOwner(registerId1,registerId2,storeId1));
//    }
//
//    @Test
//    //AT-15.2
//    public void addGuestOwnerFailTest() throws Exception{
//        guestId1=API.guestLogin();
//        Assertions.assertFalse(API.addStoreOwner(registerId1,guestId1,storeId1));
//    }
//
//    @Test
//    //AT-15
//    public void addOwnerFailTest() throws Exception{
//        API.addStoreOwner(registerId1,registerId2,storeId1);
//        Assertions.assertFalse(API.addStoreOwner(registerId2,registerId1,storeId1));
//    }
//
//    @Test
//    //AT-15.2
//    public void addOwnerFailTest2() throws Exception{
//        Assertions.assertFalse(API.addStoreOwner(registerId2,registerId3,storeId1));
//    }
//
//    @Test
//    //AT-15.2
//    public void addOwnerTest2() throws Exception{
//        API.addStoreOwner(registerId1,registerId2,storeId1);
//        Assertions.assertTrue(API.addStoreOwner(registerId2,registerId3,storeId1));
//    }
//
//    @Test
//    //AT-16.1
//    public void addManagerTest() throws Exception{
//        Assertions.assertTrue(API.addStoreManager(registerId1,registerId2,storeId1));
//    }
//
//
//    @Test
//    //AT-16.2
//    public void addManagerFailTest() {
//        API.addStoreManager(registerId1,registerId2,storeId1);
//        Assertions.assertFalse(API.addStoreManager(registerId2,registerId3,storeId1));
//    }
//    @Test
//    //AT-16.2
//    public void addGuestManagerFailTest() throws Exception{
//        guestId1=API.guestLogin();
//        Assertions.assertFalse(API.addStoreManager(registerId1,guestId1,storeId1));
//    }
//
//    @Test
//    //AT-18.1
//    public void removeManagerTest() throws Exception{
//        API.addStoreManager(registerId1,registerId2,storeId1);
//        Assertions.assertTrue(API.removeManager(registerId1,registerId2,storeId1));
//    }
//
//    @Test
//    //AT-18.2
//    public void removeManagerFailTest() throws Exception{
//        API.addStoreOwner(registerId1,registerId2,storeId1);
//        API.addStoreManager(registerId2,registerId3,storeId1);
//        Assertions.assertFalse(API.removeManager(registerId1,registerId3,storeId1));
//    }
//
//    @Test
//    //AT-18.3
//    public void removeManagerFailTest2() throws Exception{
//        Assertions.assertFalse(API.removeManager(registerId1,registerId3,storeId1));
//    }
//
//
//    @Test
//    //AT-19.1
//    public void getStoreInfoTest() throws Exception{
//        List<User> workers = API.getStoreWorkers(registerId1,storeId1);
//        Assertions.assertEquals(workers.get(0).getId(), registerId1);
//    }
//
//    @Test
//    //AT-19.2
//    public void getStoreInfoFailTest() throws Exception{
//        assertNull(API.getStoreWorkers(registerId2,storeId1));
//    }
//
//    @Test
//    //AT-20.1
//    public void getPurchaseHistoryTest() throws Exception{
//        API.addProductToCart(registerId2,storeId1,1,1);
//        Map<Integer, Integer> bag = new HashMap<>();
//        bag.put(1, 3);
//        API.buyProduct(registerId2, storeId1, "Credit123");
//        Assertions.assertEquals(storeId1, API.getStorePurchaseHistory(registerId1,storeId1).get(0).getStoreId());
//    }
//
//    @Test
//    //AT-20.2
//    public void getPurchaseHistoryFailTest() throws Exception{
//        API.addProductToCart(registerId2,storeId1,1,1);
//        Map<Integer, Integer> bag = new HashMap<>();
//        bag.put(1, 3);
//        API.buyProduct(registerId2, storeId1, "Credit123");
//        assertNull( API.getStorePurchaseHistory(registerId2,storeId1));
//    }
//
//
//    @Test
//    //AT-20.3
//    public void getPurchaseHistoryTest2(){
//        API.addStoreOwner(registerId1,registerId2,storeId1);
//        API.addProductToCart(registerId2,storeId1,1,1);
//        Map<Integer, Integer> bag = new HashMap<>();
//        bag.put(1, 3);
//        API.buyProduct(registerId2, storeId1, "Credit123");
//
//        Assertions.assertEquals(storeId1,API.getStorePurchaseHistory(registerId2,storeId1).get(0).getStoreId());
//    }
//
//    @Test
//    //AT-8.1
//    public void getCartInfo() throws  Exception{
//        API.addProductToCart(registerId1, storeId1, 1, 1);
//        Assertions.assertTrue( API.getCart(registerId1).get(0).getProductIds().keySet().contains(1));
//    }
//
//    @Test
//    //AT-8.2
//    public void getCartInfoFail() throws  Exception{
//        API.addProductToCart(registerId1, storeId1, 1, 1);
//        API.registeredLogout(registerId1);
//        Assertions.assertEquals(0, API.getCart(registerId1).size());
//    }
//
//    @Test
//    //AT-22.4
//    public void registerTwoUsersTest(){
//        for(int i=0; i<100; i++){
//            setUp();
//            registerTwoUsers();
//            Assertions.assertEquals(4,API.getNumOfUsers());
//        }
//
//    }
//
//    private void registerTwoUsers() {
//        try{
//            Thread thread1= new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    API.register("same name","1234");
//                }
//            });
//            Thread thread2= new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    API.register("same name","1234");
//                }
//            });
//
//            thread1.start();
//            thread2.start();
//            thread1.join();
//            thread2.join();
//        }catch (Exception e){
//            fail();
//
//        }
//
//
//    }
//
//
//    @Test
//    //AT-22.3
//    public void appointTwoManagersTest(){
//        for(int i=0; i<100; i++){
//            setUp();
//            API.addStoreOwner(registerId1,registerId2,storeId1);
//            appointTwoManagers();
//            Assertions.assertEquals(3,API.getStoreWorkers(registerId1,storeId1).size());
//        }
//
//    }
//
//    private void appointTwoManagers() {
//        try{
//            Thread thread1= new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    API.addStoreManager(registerId1,registerId3,storeId1);
//                }
//            });
//            Thread thread2= new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    API.addStoreManager(registerId2,registerId3,storeId1);
//                }
//            });
//
//            thread1.start();
//            thread2.start();
//            thread1.join();
//            thread2.join();
//        }catch (Exception e){
//            fail();
//
//        }
//
//
//    }
//
//
//
//    @Test
//    //AT-22.5
//    public void addNewStoresTest(){
//        for(int i=0; i<100; i++){
//            setUp();
//            addNewStores();
//            Assertions.assertEquals(101,API.getNumOfStores());
//        }
//    }
//
//    private void addNewStores() {
//        try{
//            List<Thread> threads=new LinkedList<>();
//            for(int i=0; i<100;i++){
//                threads.add(new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        API.openStore(registerId1,"newStore");
//                    }
//                }));
//            }
//            for(Thread thread : threads){
//                thread.start();
//            }
//            for(Thread thread : threads){
//                thread.join();
//            }
//        }catch (Exception e){
//            fail();
//
//        }
//
//
//    }
//
//
//
//    @Test
//    //AT-22.6
//    public void deleteSimultaneouslyTest(){
//        for(int i=0; i<100; i++){
//            setUp();
//            deleteSimultaneously();
//            Assertions.assertEquals(0,API.getAllStoreProducts(storeId1).size());
//        }
//    }
//
//    private void deleteSimultaneously() {
//        try{
//            API.addStoreOwner(registerId1, registerId2, storeId1);
//            Thread thread1=new Thread(()->API.removeProductFromStore(registerId1,storeId1,1));
//            Thread thread2=new Thread(()->API.removeProductFromStore(registerId2,storeId1,1));
//            thread1.start();
//            thread2.start();
//            thread1.join();
//            thread2.join();
//        }catch (Exception e){
//            fail();
//
//        }
//
//
//    }
//
//
//}
