
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.nio.file.OpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {

    TradingSystem tradingSystem;
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
    public void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException {
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
        LinkedList <Product.Category> catList= new LinkedList<>();
        catList.add(Product.Category.FOOD);
        int productId=tradingSystem.addProductToStore(1, storeId1,"milk",catList ,10,"FOOD", 5 );


    }

    @Test
    //AT-5.1
    public void getInformationTest() throws Exception{
        assertEquals(storeId1, tradingSystem.getAllStoresInfo(registerId1).get(0).getStoreId());
    }

    @Test
    //AT-5.2
    public void getInformationFailTest() throws Exception{
        tradingSystem.logout(registerId1);
        assertNull(tradingSystem.getAllStoresInfo(registerId1));
    }

    @Test
    //AT-6
    public void getProductByNameTest() throws Exception{
        Filter filter=new Filter("NAME","milk",9,15,-1,"",-1);
        //assume the first product gets id of 1
        assertEquals(1,tradingSystem.getProducts(filter, registerId1).get(storeId1));
    }
    //AT-6
    @Test
    public void getProductByNameFailTest() throws Exception{
        Filter filter=new Filter("NAME","milk",1,5,-1,"",-1);
        //assume the first product gets id of 1
        assertEquals(0, tradingSystem.getProducts(filter, registerId1).size());
    }
    //AT-6
    @Test
    public void failGetProductTest() throws Exception{
        Filter filter=new Filter("NAME","dani",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"",-1);
        assertEquals(0,tradingSystem.getProducts(filter, registerId1).size());
    }
    //AT-6
    @Test
    public void getProductByCategoryTest() throws Exception{
        Filter filter=new Filter("CATEGORY","FOOD",9, 15,-1,"",-1);
        //assume the first product gets id of 1
        int id = (int)tradingSystem.getProducts(filter, registerId1).values().toArray()[0];
        assertEquals(1,id);
    }
    //AT-6
    @Test
    public void failGetProductByCategoryTest() throws Exception{
        Filter filter=new Filter("CATEGORY","DRINK",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"",-1);
        assertEquals(0,tradingSystem.getProducts(filter, registerId1).size());
    }
    //AT-6
    @Test
    public void failGetProductByNameAndCategoryTest() throws Exception{
        Filter filter=new Filter("NAME","milk",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"Drinks",-1);
        assertEquals(0,tradingSystem.getProducts(filter, registerId1).size());
    }
    //AT-6
    @Test
    public void GetProductByNameAndCategoryTest() throws Exception{
        Filter filter=new Filter("NAME","milk",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"FOOD",-1);
        assertEquals(1,tradingSystem.getProducts(filter, registerId1).size());
    }

    @Test
    public void addToCartTest() throws Exception{
        assertTrue(tradingSystem.addProductToBag(registerId2,storeId1,1,3));
    }

    @Test
    //At-7
    public void addToBagGuestTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertTrue(tradingSystem.addProductToBag(guestId1,storeId1,1,3));
    }

    @Test
    //At-7
    public void addToChartWrongProdTest() throws Exception{
        assertFalse(tradingSystem.addProductToBag(registerId2,storeId1,2,2));
    }

    @Test
    public void addToBagLogoutTest() throws Exception{
        tradingSystem.logout(registerId2);
        assertFalse(tradingSystem.addProductToBag(registerId2,storeId1,1,1));
    }

    @Test
    public void addToChartTest2() throws Exception{
        tradingSystem.addProductToBag(registerId2,storeId1,1,2);
        tradingSystem.logout(registerId2);
        tradingSystem.login("elad","elad321654");

        List<Bag> products=tradingSystem.getCart(registerId2);
        assertEquals(2, products.get(0).getProductIds().get(1));
    }

    @Test
    public void addToCartTest3() throws Exception{
        guestId1=tradingSystem.guestLogin();
        tradingSystem.addProductToBag(guestId1,storeId1,1,2);
        tradingSystem.guestRegister(guestId1,"dorin","dorin321654");
        tradingSystem.logout(guestId1);
        tradingSystem.login("dorin","dorin321654");

        List<Bag> products=tradingSystem.getCart(guestId1);
        assertEquals(2, products.get(0).getProductIds().get(1));
    }

    @Test
    //AT-11.1 success
    public void openStoreTest() throws Exception{
        assertEquals(2,tradingSystem.openStore(registerId2,"elad store"));
    }

    @Test
    //AT-11.2 fail
    public void guestOpenStoreFailTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertEquals(-1,tradingSystem.openStore(guestId1,"guest store"));
    }

    @Test
    //AT-13 success
    public void addProductTest() throws Exception{
        List<Product.Category> categories= new LinkedList<>();
        categories.add(Product.Category.FOOD);
        assertTrue(tradingSystem.addProductToStore(registerId1,  storeId1, "water",categories,5,"drink", 5)==2);

    }

    @Test
    //AT-13 fail
    public void addProductFailTest() throws Exception{
        List<Product.Category> categories= new LinkedList<>();
        categories.add(Product.Category.FOOD);
        assertFalse(tradingSystem.addProductToStore(registerId2, storeId1, "water",categories,5,"drink", 5)==2);
    }

    @Test
    //AT-13 success
    public void removeProductTest() throws Exception{
        tradingSystem.removeProductFromStore(registerId1,storeId1,1);
        assertEquals(0,tradingSystem.getProductsFromStore(storeId1).size());
    }

    @Test
    //AT-13 alternate
    public void removeProductFailTest() throws Exception{
        tradingSystem.removeProductFromStore(registerId1,storeId1,2);
        assertEquals(1,tradingSystem.getProductsFromStore(storeId1).size());
    }

    @Test
    //AT-15.1
    public void addOwnerTest() throws Exception{
        assertTrue(tradingSystem.addStoreOwner(registerId1,registerId2,storeId1));
    }

    @Test
    //AT-15.2
    public void addGuestOwnerFailTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertFalse(tradingSystem.addStoreOwner(registerId1,guestId1,storeId1));
    }

    @Test
    //AT-15
    public void addOwnerFailTest() throws Exception{
        tradingSystem.addStoreOwner(registerId1,registerId2,storeId1);
        assertFalse(tradingSystem.addStoreOwner(registerId2,registerId1,storeId1));
    }

    @Test
    //AT-15.2
    public void addOwnerFailTest2() throws Exception{
        assertFalse(tradingSystem.addStoreOwner(registerId2,registerId3,storeId1));
    }

    @Test
    //AT-15.2
    public void addOwnerTest2() throws Exception{
        tradingSystem.addStoreOwner(registerId1,registerId2,storeId1);
        assertTrue(tradingSystem.addStoreOwner(registerId2,registerId3,storeId1));
    }

    @Test
    //AT-16.1
    public void addManagerTest() throws Exception{
        assertTrue(tradingSystem.addStoreManager(registerId1,registerId2,storeId1));
    }


    @Test
    //AT-16.2
    public void addManagerFailTest() throws Exception{
        tradingSystem.addStoreManager(registerId1,registerId2,storeId1);
        assertFalse(tradingSystem.addStoreManager(registerId2,registerId3,storeId1));
    }
    @Test
    //AT-16.2
    public void addGuestManagerFailTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertFalse(tradingSystem.addStoreManager(registerId1,guestId1,storeId1));
    }

    @Test
    //AT-18.1
    public void removeManagerTest() throws Exception{
        tradingSystem.addStoreManager(registerId1,registerId2,storeId1);
        assertTrue(tradingSystem.removeManager(registerId1,registerId2,storeId1));
    }

    @Test
    //AT-18.2
    public void removeManagerFailTest() throws Exception{
        tradingSystem.addStoreOwner(registerId1,registerId2,storeId1);
        tradingSystem.addStoreManager(registerId2,registerId3,storeId1);
        assertFalse(tradingSystem.removeManager(registerId1,registerId3,storeId1));
    }

    @Test
    //AT-18.3
    public void removeManagerFailTest2() throws Exception{
        assertFalse(tradingSystem.removeManager(registerId1,registerId3,storeId1));
    }


    @Test
    //AT-19.1
    public void getStoreInfoTest() throws Exception{
        List<User> workers = tradingSystem.getWorkersInformation(registerId1,storeId1);
        assertEquals(workers.get(0).getId(), registerId1);
    }

    @Test
    //AT-19.2
    public void getStoreInfoFailTest() throws Exception{
        assertNull(tradingSystem.getWorkersInformation(registerId2,storeId1));
    }

    @Test
    //AT-20.1
    public void getPurchaseHistoryTest() throws Exception{
        tradingSystem.addProductToBag(registerId2,storeId1,1,1);
        Map<Integer, Integer> bag = new HashMap<>();
        bag.put(1, 3);
        tradingSystem.buyProducts(registerId2, storeId1, "Credit123");
        LinkedList <Product.Category> catList = new LinkedList<>();
        catList.add(Product.Category.FOOD);
        Product p = new Product(1,"milk",catList ,10,"FOOD");
        Map<Product, Integer> recLines = new HashMap<>();
        recLines.put(p, 3);
        Receipt receipt = new Receipt(storeId1,tradingSystem.getUserById(registerId2).getUserName(),recLines);
        assertEquals(receipt.getStoreId(), tradingSystem.getStorePurchaseHistory(registerId1,storeId1).get(0).getStoreId());
    }

    @Test
    //AT-20.2
    public void getPurchaseHistoryFailTest() throws Exception{
        tradingSystem.addProductToBag(registerId2,storeId1,1,1);
        Map<Integer, Integer> bag = new HashMap<>();
        bag.put(1, 3);
        tradingSystem.buyProducts(registerId2, storeId1, "Credit123");
        LinkedList <Product.Category> catList = new LinkedList<>();
        catList.add(Product.Category.FOOD);
        Product p = new Product(1,"milk",catList ,10,"FOOD");
        Map<Product, Integer> recLines = new HashMap<>();
        recLines.put(p, 3);
        Receipt receipt = new Receipt(storeId1,tradingSystem.getUserById(registerId2).getUserName(),recLines);
        assertNull( tradingSystem.getStorePurchaseHistory(registerId2,storeId1));
    }


    @Test
    //AT-20.3
    public void getPurchaseHistoryTest2(){
        tradingSystem.addStoreOwner(registerId1,registerId2,storeId1);
        tradingSystem.addProductToBag(registerId2,storeId1,1,1);
        Map<Integer, Integer> bag = new HashMap<>();
        bag.put(1, 3);
        tradingSystem.buyProducts(registerId2, storeId1, "Credit123");
        LinkedList <Product.Category> catList = new LinkedList<>();
        catList.add(Product.Category.FOOD);
        Product p = new Product(1,"milk",catList ,10,"FOOD");
        Map<Product, Integer> recLines = new HashMap<>();
        recLines.put(p, 3);
        Receipt receipt = new Receipt(storeId1,tradingSystem.getUserById(registerId2).getUserName(),recLines);
        assertEquals(receipt.getStoreId(), tradingSystem.getStorePurchaseHistory(registerId2,storeId1).get(0).getStoreId());
    }

    @Test
    //AT-8.1
    public void getCartInfo() throws  Exception{
        tradingSystem.addProductToBag(registerId1, storeId1, 1, 1);
        assertTrue( tradingSystem.getCart(registerId1).get(0).getProductIds().keySet().contains(1));
    }

    @Test
    //AT-8.2
    public void getCartInfoFail() throws  Exception{
        tradingSystem.addProductToBag(registerId1, storeId1, 1, 1);
        tradingSystem.logout(registerId1);
        assertEquals(0, tradingSystem.getCart(registerId1).size());
    }




































}
