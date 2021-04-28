package Tests;

import Domain.Product;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DiscountAndPurchaseTest {

    private int registerId1;
    private int registerId2;
    private int registerId3;
    private int storeId1;
    private int storeId2;
    private int productId1;
    private int productId2;
    private int productId3;
    private Date begin;
    private Date end;

    @BeforeEach
    public void setUp() {
        API.initTradingSystem("Elad");
        String userName1 = "kandabior";
        String password1 = "or321654";
        String userName2 = "elad";
        String password2 = "elad321654";
        API.register(userName1, password1, 20);
        API.register(userName2, password2, 16);
        registerId1 = (int) API.registeredLogin(userName1, password1).getData();
        registerId2 = (int) API.registeredLogin(userName2, password2).getData();
        storeId1 = (int) API.openStore(registerId1, "kandabior store").getData();
        LinkedList<Product.Category> catList1 = new LinkedList<>();
        catList1.add(Product.Category.FOOD);
        LinkedList<Product.Category> catList2 = new LinkedList<>();
        catList2.add(Product.Category.DRINKS);
        productId1 = (int) API.addProduct(1, storeId1, "milk", catList1, 10, "FOOD", 5).getData();
        productId3 = (int) API.addProduct(1, storeId1, "bread", catList1, 10, "FOOD", 100).getData();
        productId2 = (int) API.addProduct(1, storeId1, "beer", catList2, 20, "ALCOHOL", 1).getData();
        begin = new Date();
        end = new GregorianCalendar(2022, Calendar.FEBRUARY, 11).getTime();
    }

    @Test
    public void addDiscountSuccessTest() {
        Map<String, List<String>> policies = new HashMap<>();
        List<String> params1 = new LinkedList<>();
        params1.add("5"); //min amount
        params1.add(String.valueOf(productId1));
        List<String> params2 = new LinkedList<>();
        params2.add("2"); //min amount
        params2.add(String.valueOf(productId3));
        policies.put("Minimal Amount", params2);
        //discount of 50% on beers if you buy at least 5 milk & 2 bread from now until february 2022
        Assertions.assertTrue(API.addDiscountOnProduct(storeId1, registerId1, productId1, "And", policies, begin, end, 50).isResult());
    }


    @Test
    public void buyProductWithDiscountAndOpSuccessTest() {
        addDiscountSuccessTest();

    }


    @Test
    public void addDiscountNotPermittedFailTest() {
        Map<String, List<String>> policies = new HashMap<>();
        List<String> params1 = new LinkedList<>();
        params1.add("5"); //min amount
        params1.add(String.valueOf(productId1));
        //discount of 50% on beers if you buy at least 5 milk & 2 bread from now until february 2022
        Assertions.assertFalse(API.addDiscountOnProduct(storeId1, registerId2, productId1, "Or", policies, begin, end, 50).isResult());
    }


    @Test
    public void buyProductWithDiscountWrongAmountFailTest() {
    }

    @Test
    public void addPurchasePolicySuccessTest() { //age limit on alcohol
    }

    @Test
    public void purchaseContradictsPolicyFailTest() { //age 16 buys alcohol
    }
}
