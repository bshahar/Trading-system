
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
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
        tradingSystem.addProductToStore(1,1, storeId1,"milk",catList ,10,"FOOD", 5 );


    }

    @Test
    //AT-3
    public void getInformationTest() throws Exception{
        //TODO make better test
        assertNotNull(tradingSystem.getAllStoresInfo());
    }

    @Test
    public void getProductByNameTest() throws Exception{
        Filter filter=new Filter("NAME","milk",9,15,-1,"",-1);
        //assume the first product gets id of 1
        assertEquals(1,tradingSystem.getProducts(filter).get(storeId1));
    }

    @Test
    public void getProductByNameFailTest() throws Exception{
        Filter filter=new Filter("NAME","milk",1,5,-1,"",-1);
        //assume the first product gets id of 1
        assertEquals(0,tradingSystem.getProducts(filter).size());
    }

    @Test
    public void failGetProductTest() throws Exception{
        Filter filter=new Filter("NAME","dani",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"",-1);
        assertEquals(0,tradingSystem.getProducts(filter).size());
    }

    @Test
    public void getProductByCategoryTest() throws Exception{
        Filter filter=new Filter("CATEGORY","FOOD",9, 15,-1,"",-1);
        //assume the first product gets id of 1
        int id = (int)tradingSystem.getProducts(filter).values().toArray()[0];
        assertEquals(1,id);
    }

    @Test
    public void failGetProductByCategoryTest() throws Exception{
        Filter filter=new Filter("CATEGORY","DRINK",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"",-1);
        assertEquals(0,tradingSystem.getProducts(filter).size());
    }

    @Test
    public void failGetProductByNameAndCategoryTest() throws Exception{
        Filter filter=new Filter("NAME","milk",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"Drinks",-1);
        assertEquals(0,tradingSystem.getProducts(filter).size());
    }

    @Test
    public void GetProductByNameAndCategoryTest() throws Exception{
        Filter filter=new Filter("NAME","milk",Integer.MIN_VALUE,Integer.MAX_VALUE,-1,"FOOD",-1);
        assertEquals(1,tradingSystem.getProducts(filter).size());
    }

    @Test
    public void addToCartTest() throws Exception{
        assertTrue(tradingSystem.addProductToBag(registerId2,storeId1,1));
    }

    @Test
    public void addToBagGuestTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertTrue(tradingSystem.addProductToBag(guestId1,storeId1,1));
    }

    @Test
    public void addToBagWrongProdTest() throws Exception{
        assertFalse(tradingSystem.addProductToBag(registerId2,storeId1,2));
    }

    @Test
    public void addToBagLogoutTest() throws Exception{
        tradingSystem.logout(registerId2);
        assertFalse(tradingSystem.addProductToBag(registerId2,storeId1,1));
    }

    @Test
    public void addToCartTest2() throws Exception{
        tradingSystem.addProductToBag(registerId2,storeId1,1);
        tradingSystem.logout(registerId2);
        tradingSystem.login("elad","elad321654");

        Map<Integer, List<Integer>> products=tradingSystem.getCart(registerId2);
        assertEquals(1, products.get(storeId1).get(0));
    }

    @Test
    public void addToCartTest3() throws Exception{
        guestId1=tradingSystem.guestLogin();
        tradingSystem.addProductToBag(guestId1,storeId1,1);
        tradingSystem.guestRegister(guestId1,"dorin","dorin321654");
        tradingSystem.logout(guestId1);
        tradingSystem.login("dorin","dorin321654");
        Map<Integer, List<Integer>> products=tradingSystem.getCart(guestId1);
        assertEquals( 1,products.get(storeId1).get(0));
    }

    @Test
    public void purchaseTest() throws Exception{
        tradingSystem.addProductToBag(registerId2,storeId1,1);
        //assertTrue(tradingSystem.buyProducts(registerId2,1,"123456789"));
    }

    @Test
    public void failPurchaseTest() throws Exception{
        tradingSystem.addProductToBag(registerId2,storeId1,1);
        tradingSystem.logout(registerId2);
        //assertFalse(tradingSystem.buyProducts(registerId2,1,"123456789"));
    }

    @Test
    public void guestPurchaseTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        tradingSystem.addProductToBag(guestId1,storeId1,1);
        //assertTrue(tradingSystem.buyProducts(guestId1,1,"123456789"));
    }

    @Test
    public void openStoreTest() throws Exception{
        assertEquals(2,tradingSystem.openStore(registerId2,"elad store"));
    }

    @Test
    public void guestOpenStoreFailTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertEquals(-1,tradingSystem.openStore(guestId1,"guest store"));
    }

    @Test
    public void addProductTest() throws Exception{
        List<Product.Category> categories= new LinkedList<>();
        categories.add(Product.Category.FOOD);
        assertTrue(tradingSystem.addProductToStore(registerId1, 2, storeId1, "water",categories,5,"drink", 5));

    }

    @Test
    public void addProductFailTest() throws Exception{
        List<Product.Category> categories= new LinkedList<>();
        categories.add(Product.Category.FOOD);
        //TODO check if false or true
        assertFalse(tradingSystem.addProductToStore(registerId2,3, storeId1, "water",categories,5,"drink", 5));
    }

    @Test
    public void removeProductTest() throws Exception{
        tradingSystem.removeProductFromStore(registerId1,storeId1,1);
        assertEquals(0,tradingSystem.getProductsFromStore(storeId1).size());
    }

    @Test
    public void removeProductFailTest() throws Exception{
        tradingSystem.removeProductFromStore(registerId2,storeId1,1);
        assertEquals(1,tradingSystem.getProductsFromStore(storeId1).size());
    }

    @Test
    public void addOwnerTest() throws Exception{
        assertTrue(tradingSystem.addStoreOwner(registerId1,registerId2,storeId1));
    }

    @Test
    public void addGuestOwnerFailTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertFalse(tradingSystem.addStoreOwner(registerId1,guestId1,storeId1));
    }

    @Test
    public void addOwnerFailTest() throws Exception{
        tradingSystem.addStoreOwner(registerId1,registerId2,storeId1);
        assertFalse(tradingSystem.addStoreOwner(registerId2,registerId1,storeId1));
    }

    @Test
    public void addManagerTest() throws Exception{
        assertTrue(tradingSystem.addStoreManager(registerId1,registerId2,storeId1));
    }

    @Test
    public void addGuestManagerFailTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertFalse(tradingSystem.addStoreManager(registerId1,guestId1,storeId1));
    }

    @Test
    public void removeManagerTest() throws Exception{
        tradingSystem.addStoreManager(registerId1,registerId2,storeId1);
        assertTrue(tradingSystem.removeManager(registerId1,registerId2,storeId1));
    }

    @Test
    public void removeManagerFailTest() throws Exception{
        tradingSystem.addStoreOwner(registerId1,registerId2,storeId1);
        tradingSystem.addStoreManager(registerId2,registerId3,storeId1);
        assertFalse(tradingSystem.removeManager(registerId1,registerId3,storeId1));
    }


    @Test
    public void getStoreInfoTest() throws Exception{
        List<User> workers = tradingSystem.getWorkersInformation(registerId1,storeId1);
        assertEquals(workers.get(0).getId(), registerId1);
    }

    @Test
    public void getStoreInfoFailTest() throws Exception{
        assertNull(tradingSystem.getWorkersInformation(registerId2,storeId1));
    }

    @Test
    public void getPurchaseHistoryTest() throws Exception{
        tradingSystem.addProductToBag(registerId2,storeId1,1);
        Map<Integer, Integer> bag = new HashMap<>();
        bag.put(1, 3);
        tradingSystem.buyProducts(registerId2, storeId1, bag, "Credit123");
        LinkedList <Product.Category> catList = new LinkedList<>();
        catList.add(Product.Category.FOOD);
        Product p = new Product(1,"milk",catList ,10,"FOOD");
        Map<Product, Integer> recLines = new HashMap<>();
        recLines.put(p, 3);
        Receipt receipt = new Receipt(storeId1,tradingSystem.getUserById(registerId2).getUserName(),recLines);
        assertEquals(receipt.getStoreId(), tradingSystem.getStorePurchaseHistory(registerId1,storeId1).get(0).getStoreId());
    }





































}
