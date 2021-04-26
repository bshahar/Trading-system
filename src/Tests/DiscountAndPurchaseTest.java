package Tests;

import Domain.Product;
import Domain.Result;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
        registerId1 = (int) API.registeredLogin(userName1, password1).getdata();
        registerId2 = (int) API.registeredLogin(userName2, password2).getdata();
        storeId1 = (int) API.openStore(registerId1, "kandabior store").getdata();
        LinkedList<Product.Category> catList1 = new LinkedList<>();
        catList1.add(Product.Category.FOOD);
        LinkedList<Product.Category> catList2 = new LinkedList<>();
        catList2.add(Product.Category.DRINKS);
        productId1 = (int) API.addProduct(1, storeId1, "milk", catList1, 10, "FOOD", 5).getdata();
        productId3 = (int) API.addProduct(1, storeId2, "bread", catList1, 10, "FOOD", 100).getdata();
        productId2 = (int) API.addProduct(1, storeId1, "beer", catList2, 20, "ALCOHOL", 1).getdata();
        begin = new Date();
        end = new GregorianCalendar(2022, Calendar.FEBRUARY, 11).getTime();
    }

    @Test
    public void addDiscountAndOpSuccessTest() {
        Map<String, List<String>> policies = new HashMap<>();
        List<String> params1 = new LinkedList<>();
        params1.add("5"); //min amount
        params1.add(String.valueOf(productId1));
        List<String> params2 = new LinkedList<>();
        params2.add("2"); //min amount
        params2.add(String.valueOf(productId3));
        policies.put("Minimal Amount", params2);
        //discount of 50% on beers if you buy at least 5 milk & 2 bread from now until february 2022
        API.addDiscountOnProduct(storeId2, productId1, "And", policies, begin, end, 50);
    }

    @Test
    public void addDiscountOrOpSuccessTest() {
    }

    @Test
    public void addDiscountXorOpSuccessTest() {
    }

    @Test
    public void buyProductWithDiscountAndOpSuccessTest() {
    }

    @Test
    public void buyProductWithDiscountOrOpSuccessTest() {
    }

    @Test
    public void buyProductWithDiscountXorOpSuccessTest() {
    }

    @Test
    public void addDiscountNotPermittedFailTest() {
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
