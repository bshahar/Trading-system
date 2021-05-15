package Tests;

import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionTest {

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
    ;

    public enum Permission {
        DEF,
        AddProduct,
        AppointManager,
        AppointOwner,
        CloseStore,
        DefineDiscountFormat,
        DefineDiscountPolicy,
        DefinePurchaseFormat,
        DefinePurchasePolicy,
        EditDiscountFormat,
        EditDiscountPolicy,
        EditProduct,
        EditPurchaseFormat,
        EditPurchasePolicy,
        GetWorkersInfo,
        OpenStore,
        RemoveManagerAppointment,
        RemoveOwnerAppointment,
        None,
        RemoveProduct,
        ReopenStore,
        ReplayMessages,
        ViewMessages,
        ViewPurchaseHistory
    }

    @BeforeEach
    void setUp() {
        Properties testProps = new Properties();
        try {
            API.initTradingSystem(true);
            InputStream input = getClass().getClassLoader().getResourceAsStream("testsSetUp.properties");
            if(input != null)
                testProps.load(input);
            else
                throw new FileNotFoundException("Property file was not found.");
        } catch (Exception e) {
        }


        API.register(testProps.getProperty("user1name"), testProps.getProperty("user1password"), Integer.parseInt(testProps.getProperty("user1age")));
        API.register(testProps.getProperty("user2name"), testProps.getProperty("user2password"), Integer.parseInt(testProps.getProperty("user2age")));
        API.register(testProps.getProperty("user3name"), testProps.getProperty("user3password"), Integer.parseInt(testProps.getProperty("user3age")));

        registerId1 = (int) API.registeredLogin(testProps.getProperty("user1name"), testProps.getProperty("user1password")).getData();
        registerId2 = (int) API.registeredLogin(testProps.getProperty("user2name"), testProps.getProperty("user2password")).getData();
        registerId3 = (int) API.registeredLogin(testProps.getProperty("user3name"), testProps.getProperty("user3password")).getData();

        storeId1 = (int) API.openStore(registerId1, testProps.getProperty("storeNameTest")).getData();

        /*
        API.initTradingSystem("Elad");
        String userName1 = "kandabior";
        String password1 = "or321654";
        String userName2 = "elad";
        String password2 = "elad321654";
        String userName3 = "erez";
        String password3 = "erez321654";
        API.register(userName1, password1,20);
        API.register(userName2, password2,20);
        API.register(userName3, password3,20);
        registerId1 = (int) API.registeredLogin(userName1, password1).getData();
        registerId2 = (int) API.registeredLogin(userName2, password2).getData();
        registerId3 = (int) API.registeredLogin(userName3, password3).getData();
        storeId1 = (int) API.openStore(registerId1, "kandabior store").getData();
        //LinkedList<String> catList = new LinkedList<>();
        //catList.add("FOOD");
        //int productId = (int) API.addProduct(1, storeId1, "milk", catList, 10, "FOOD", 5).getData();

         */
    }


    @Test
    public void systemMangerPermissions() {
        assertTrue(API.getAllPurchases(123456).isResult());
    }

    @Test
    public void systemMangerPermissionsFail() {
        assertFalse(API.getAllPurchases(1).isResult());
    }

    @Test
    public void addProductPermission() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.AddProduct.ordinal()));
    }

    @Test
    public void addProductPermissionFail() {
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.AddProduct.ordinal()));
    }

    @Test
    public void removeProductPermission() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.RemoveProduct.ordinal()));
    }

    @Test
    public void removeProductPermissionFail() {
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.RemoveProduct.ordinal()));
    }

    @Test
    public void editProductPermission() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.EditProduct.ordinal()));
    }

    @Test
    public void editProductPermissionFail() {
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.EditProduct.ordinal()));
    }

    @Test
    public void DefinePurchasePolicy() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.DefinePurchasePolicy.ordinal()));
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.DefinePurchasePolicy.ordinal()));
    }

    @Test
    public void EditPurchasePolicy() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.EditPurchasePolicy.ordinal()));
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.EditPurchasePolicy.ordinal()));
    }

    @Test
    public void DefinePurchaseFormat() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.DefinePurchaseFormat.ordinal()));
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.DefinePurchaseFormat.ordinal()));
    }

    @Test
    public void EditPurchaseFormat() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.EditPurchaseFormat.ordinal()));
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.EditPurchaseFormat.ordinal()));
    }

    @Test
    public void DefineDiscountPolicy() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.DefineDiscountPolicy.ordinal()));
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.DefineDiscountPolicy.ordinal()));
    }

    @Test
    public void EditDiscountPolicy() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.EditDiscountPolicy.ordinal()));
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.EditDiscountPolicy.ordinal()));
    }

    @Test
    public void DefineDiscountFormat() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.DefineDiscountFormat.ordinal()));
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.DefineDiscountFormat.ordinal()));
    }

    @Test
    public void EditDiscountFormat() {
        Assertions.assertTrue(API.checkPermissions(registerId1, storeId1, Permission.EditDiscountFormat.ordinal()));
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.EditDiscountFormat.ordinal()));
    }

    @Test
    //AT-17.1
    public void changeManagerPermission() {
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.AddProduct.ordinal()));
        API.addStoreManager(registerId1, registerId2, storeId1);
        List<Integer> permissions = new LinkedList<>();
        permissions.add(Permission.AddProduct.ordinal());
        API.addPermissions(registerId1, registerId2, storeId1, permissions);
        Assertions.assertTrue(API.checkPermissions(registerId2, storeId1, Permission.AddProduct.ordinal()));
    }

    @Test
    //AT-17.2
    public void changeManagerPermissionFail() {
        List<Integer> permissions = new LinkedList<>();
        permissions.add(Permission.AddProduct.ordinal());
        Assertions.assertFalse(API.addPermissions(registerId2, registerId1, storeId1, permissions).isResult());
    }

    @Test
    //AT-17.3
    public void changeManagerPermissionFail2() {

        List<Integer> permissions = new LinkedList<>();
        permissions.add(Permission.AddProduct.ordinal());
        Assertions.assertFalse(API.addPermissions(registerId1, registerId2, storeId1, permissions).isResult());
    }


    @Test
    //AT-21.1
    public void addPermissionToNonPermission() {
        API.addStoreManager(registerId1, registerId2, storeId1);
        Assertions.assertTrue(API.checkPermissions(registerId2, storeId1, Permission.GetWorkersInfo.ordinal()));
        List<Integer> permissions = new LinkedList<>();
        permissions.add(Permission.GetWorkersInfo.ordinal());
        API.RemovePermissions(registerId1, registerId2, storeId1, permissions);
        Assertions.assertFalse(API.checkPermissions(registerId2, storeId1, Permission.GetWorkersInfo.ordinal()));

    }

    @Test
    //AT-21.2
    public void removePermissionToHavePermission() {
        List<Integer> permissions = new LinkedList<>();
        permissions.add(Permission.GetWorkersInfo.ordinal());
        Assertions.assertFalse(API.RemovePermissions(registerId2, registerId1, storeId1, permissions).isResult());
        Assertions.assertFalse(API.addPermissions(registerId2, registerId1, storeId1, permissions).isResult());
    }





}