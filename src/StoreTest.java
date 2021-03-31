
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
        tradingSystem.addProductToStore(1,"milk",catList ,10,"food" );

    }

    @Test
    public void getInformationTest() throws Exception{
        //TODO make better test
        assertNotNull(tradingSystem.getAllStoresInfo());
    }

    @Test
    public void getProductByNameTest() throws Exception{
        String[]arr = new String[0];
        //assume the first product get id of 1
        assertEquals(1,tradingSystem.getProducts("NAME","milk",arr).get(0));
    }

    @Test
    public void failGetProductTest() throws Exception{
        String[]arr = new String[0];
        assertEquals(0,tradingSystem.getProducts("NAME","cheese",arr).size());
    }

    @Test
    public void getProductByCategoryTest() throws Exception{
        String[]arr = new String[0];
        //assume the first product get id of 1
        assertEquals(1,tradingSystem.getProducts("CATEGORY","food",arr).get(0));
    }

    @Test
    public void failGetProductByCategoryTest() throws Exception{
        String[]arr = new String[0];
        assertEquals(0,tradingSystem.getProducts("CATEGORY","drinks",arr).size());
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































}
