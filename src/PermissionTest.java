
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//AT-19
public class PermissionTest {

    private static TradingSystem tradingSystem;
    private static User u1;
    private static User u2;


    @BeforeAll
    static void setUpBeforeAll() {
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



}