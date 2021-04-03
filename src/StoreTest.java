
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

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
        tradingSystem= new TradingSystem();
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
        catList.add(Product.Category.Food);
        tradingSystem.addProductToStore(1,1, storeId1,"milk",catList ,10,"food", 5 );

    }

    @Test
    public void getInformationTest() throws Exception{
        //TODO make better test
        assertNotNull(tradingSystem.getAllStoresInfo());
    }

    @Test
    public void getProductByNameTest() throws Exception{
        String[]arr = new String[0];
        //assume the first product gets id of 1
        assertEquals(1,tradingSystem.getProducts("NAME","milk",arr).get(storeId1));
    }

    @Test
    public void failGetProductTest() throws Exception{
        String[]arr = new String[0];
        assertEquals(0,tradingSystem.getProducts("NAME","cheese",arr).size());
    }

    @Test
    public void getProductByCategoryTest() throws Exception{
        String[]arr = new String[0];
        //assume the first product gets id of 1
        assertEquals(1,tradingSystem.getProducts("CATEGORY","Food",arr).get(0));
    }

    @Test
    public void failGetProductByCategoryTest() throws Exception{
        String[]arr = new String[0];
        assertEquals(0,tradingSystem.getProducts("CATEGORY","Drinks",arr).size());
    }

    @Test
    public void addToChartTest() throws Exception{
        assertTrue(tradingSystem.addProductToChart(registerId2,storeId1,1));
    }

    @Test
    public void addToChartGuestTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        assertTrue(tradingSystem.addProductToChart(guestId1,storeId1,1));
    }

    @Test
    public void addToChartWrongProdTest() throws Exception{
        assertFalse(tradingSystem.addProductToChart(registerId2,storeId1,2));
    }

    @Test
    public void addToChartLogoutTest() throws Exception{
        tradingSystem.logout(registerId2);
        assertFalse(tradingSystem.addProductToChart(registerId2,storeId1,1));
    }

    @Test
    public void addToChartTest2() throws Exception{
        tradingSystem.addProductToChart(registerId2,storeId1,1);
        tradingSystem.logout(registerId2);
        tradingSystem.login("elad","elad321654");
        List<Integer> products=tradingSystem.getChart(registerId2);
        assertEquals(1, products.get(0));
    }

    @Test
    public void addToChartTest3() throws Exception{
        guestId1=tradingSystem.guestLogin();
        tradingSystem.addProductToChart(guestId1,storeId1,1);
        tradingSystem.guestRegister(guestId1,"dorin","dorin321654");
        tradingSystem.logout(guestId1);
        tradingSystem.login("dorin","dorin321654");
        assertEquals( 1,tradingSystem.getChart(guestId1).get(0));
    }

    @Test
    public void purchaseTest() throws Exception{
        tradingSystem.addProductToChart(registerId2,storeId1,1);
        assertTrue(tradingSystem.buyProducts(registerId2,1,"123456789"));
    }

    @Test
    public void failPurchaseTest() throws Exception{
        tradingSystem.addProductToChart(registerId2,storeId1,1);
        tradingSystem.logout(registerId2);
        assertFalse(tradingSystem.buyProducts(registerId2,1,"123456789"));
    }

    @Test
    public void guestPurchaseTest() throws Exception{
        guestId1=tradingSystem.guestLogin();
        tradingSystem.addProductToChart(guestId1,storeId1,1);
        assertTrue(tradingSystem.buyProducts(guestId1,1,"123456789"));
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
        categories.add(Product.Category.Food);
        assertTrue(tradingSystem.addProductToStore(registerId1, 2, storeId1, "water",categories,5,"drink", 5));

    }

    @Test
    public void addProductFailTest() throws Exception{
        List<Product.Category> categories= new LinkedList<>();
        categories.add(Product.Category.Food);
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
        assertEquals(0,tradingSystem.getProductsFromStore(storeId1).size());
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
        assertNotNull(tradingSystem.getWorkersInformation(registerId1,storeId1));
    }

    @Test
    public void getStoreInfoFailTest() throws Exception{
        assertNull(tradingSystem.getWorkersInformation(registerId2,storeId1));
    }

    @Test
    public void getPurchaseHistoryTest() throws Exception{
        assertNotNull(tradingSystem.getStorePurchaseHistory(registerId1,storeId1));
    }





































}
