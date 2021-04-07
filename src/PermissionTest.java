
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionTest {

    private static TradingSystem tradingSystem;
    private static User u1;
    private static User u2;


    @BeforeEach
    void setUp() {
        User systemManager = new User("Elad", 0, 1);
        tradingSystem = new TradingSystem(systemManager);
        String userName="elad";
        String password= "123";
        tradingSystem.register(userName,password);
        int id = tradingSystem.login(userName,password);
        String userName2="or";
       String password2= "123";
        tradingSystem.register(userName2,password2);
       tradingSystem.login(userName2,password2);
        tradingSystem.openStore(id,"Dorin's guys");
        u1 = tradingSystem.getUserById(1);
        u2 = tradingSystem.getUserById(2);
    }


    @Test
    public void systemMangerPermissions() {
        assertNotNull(tradingSystem.getAllPurchases(0));
    }
    @Test
    public void systemMangerPermissionsFail() {
        assertNull(tradingSystem.getAllPurchases(2));
    }
    @Test
    public void addProductPermission() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.AddProduct));
    }
    @Test
    public void addProductPermissionFail() {
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.AddProduct));
    }
    @Test
    public void removeProductPermission() {
       assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.RemoveProduct));
    }
    @Test
    public void removeProductPermissionFail() {
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.RemoveProduct));
    }
    @Test
    public void editProductPermission() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.EditProduct));
    }
    @Test
    public void editProductPermissionFail() {
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.EditProduct));
    }
    @Test
    public void DefinePurchasePolicy() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.DefinePurchasePolicy));
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.DefinePurchasePolicy));
    }
    @Test
    public void EditPurchasePolicy() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.EditPurchasePolicy));
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.EditPurchasePolicy));
    }
    @Test
    public void DefinePurchaseFormat() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.DefinePurchaseFormat));
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.DefinePurchaseFormat));
    }
    @Test
    public void EditPurchaseFormat() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.EditPurchaseFormat));
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.EditPurchaseFormat));
    }
    @Test
    public void DefineDiscountPolicy() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.DefineDiscountPolicy));
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.DefineDiscountPolicy));
    }
    @Test
    public void EditDiscountPolicy() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.EditDiscountPolicy));
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.EditDiscountPolicy));
    }
    @Test
    public void DefineDiscountFormat() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.DefineDiscountFormat));
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.DefineDiscountFormat));
    }
    @Test
    public void EditDiscountFormat() {
        assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u1, Permissions.Operations.EditDiscountFormat));
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2, Permissions.Operations.EditDiscountFormat));
    }

    @Test
    //AT-17.1
    public void changeManagerPermission() {
       tradingSystem.addStoreManager(u1.getId(),u2.getId(),1);
       List<Integer> permissions = new LinkedList<>();
       permissions.add(1);
       tradingSystem.addPermissions(u1.getId(),u2.getId(),1,permissions);
       assertTrue(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2,Permissions.Operations.AddProduct));
    }

    @Test
    //AT-17.2
    public void changeManagerPermissionFail() {
        tradingSystem.addStoreManager(u1.getId(),u2.getId(),1);
        List<Integer> permissions = new LinkedList<>();
        permissions.add(1);
        assertFalse(tradingSystem.addPermissions(u2.getId(),u1.getId(),1,permissions));
    }

    @Test
    //AT-17.3
    public void changeManagerPermissionFail2() {
        List<Integer> permissions = new LinkedList<>();
        permissions.add(1);
        tradingSystem.addPermissions(u1.getId(),u2.getId(),1,permissions);
        assertFalse(tradingSystem.getStoreById(1).getPermissions().validatePermission(u2,Permissions.Operations.AddProduct));
    }

    @Test
    //AT-21.1
    public void addPermissionToNonPermission() {
        tradingSystem.addStoreManager(u1.getId(),u2.getId(),1);
        List<Integer> permissions = new LinkedList<>();
        permissions.add(1);
        tradingSystem.addPermissions(u1.getId(),u2.getId(),1,permissions);
        LinkedList <Product.Category> catList= new LinkedList<>();
        catList.add(Product.Category.FOOD);
        assertNotEquals(-1, tradingSystem.addProductToStore(u2.getId(),1,"milk",catList ,10,"FOOD", 5 ));
    }

    @Test
    //AT-21.2
    public void removePermissionToHavePermission() {
        tradingSystem.addStoreManager(u1.getId(),u2.getId(),1);
        List<Integer> permissions = new LinkedList<>();
        permissions.add(1);
        tradingSystem.addPermissions(u1.getId(),u2.getId(),1,permissions);
        tradingSystem.removePermission(u1.getId(), u2.getId(),1, permissions);
        LinkedList <Product.Category> catList= new LinkedList<>();
        catList.add(Product.Category.FOOD);
        assertEquals(-1, tradingSystem.addProductToStore(u2.getId(),1,"milk",catList ,10,"FOOD", 5 ));
    }












}