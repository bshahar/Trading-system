package Tests;

import Domain.Product;
import Domain.Receipt;
import Domain.Result;
import Service.API;
import javafx.util.Pair;
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
        LinkedList<String> catList1 = new LinkedList<>();
        catList1.add("FOOD");
        LinkedList<String> catList2 = new LinkedList<>();
        catList2.add("DRINKS");
        productId1 = (int) API.addProduct(1, storeId1, "milk", catList1, 10, "FOOD", 100).getData();
        productId3 = (int) API.addProduct(1, storeId1, "bread", catList1, 10, "FOOD", 100).getData();
        productId2 = (int) API.addProduct(1, storeId1, "beer", catList2, 20, "ALCOHOL", 100).getData();
        begin = new Date();
        end = new GregorianCalendar(2022, Calendar.FEBRUARY, 11).getTime();
    }

    @Test
    public void addDiscountAndOpSuccessTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("5"); //min amount
        params1.add(String.valueOf(productId1));
        List<String> params2 = new LinkedList<>();
        params2.add("2"); //min amount
        params2.add(String.valueOf(productId3));
        policies.add(new Pair<> ("Minimal Amount", params1));
        policies.add(new Pair<> ("Minimal Amount", params2));
        //discount of 50% on beers if you buy at least 5 milk & 2 bread from now until february 2022
        Assertions.assertTrue(API.addDiscountOnProduct(storeId1, registerId1, productId2, "And", policies, begin, end, 50, "Sum").isResult());
    }

    @Test
    public void buyProductWithDiscountAndOpSuccessTest() {
        addDiscountAndOpSuccessTest();
        API.addProductToCart(registerId2, storeId1, productId1, 5);
        API.addProductToCart(registerId2, storeId1, productId3, 3);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 180;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void buyProductWithDiscountAndOpWrongAmountFailTest() {
        addDiscountAndOpSuccessTest();
        API.addProductToCart(registerId2, storeId1, productId1, 2);
        API.addProductToCart(registerId2, storeId1, productId3, 3);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 250;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void addDiscountOrOpSuccessTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("5"); //min amount
        params1.add(String.valueOf(productId1));
        List<String> params2 = new LinkedList<>();
        params2.add("2"); //min amount
        params2.add(String.valueOf(productId3));
        policies.add(new Pair<> ("Minimal Amount", params1));
        policies.add(new Pair<> ("Minimal Amount", params2));
        //discount of 50% on beers if you buy at least 5 milk & 2 bread from now until february 2022
        Assertions.assertTrue(API.addDiscountOnProduct(storeId1, registerId1, productId2, "Or", policies, begin, end, 50, "Sum").isResult());
    }

    @Test
    public void buyProductWithDiscountOrOpSuccessTest() {
        addDiscountOrOpSuccessTest();
        API.addProductToCart(registerId2, storeId1, productId1, 5);
        API.addProductToCart(registerId2, storeId1, productId3, 1);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 160;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void buyProductWithDiscountOrOpFailTest() {
        addDiscountAndOpSuccessTest();
        API.addProductToCart(registerId2, storeId1, productId1, 1);
        API.addProductToCart(registerId2, storeId1, productId3, 1);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 220;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void addDiscountNotPermittedFailTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("5"); //min amount
        params1.add(String.valueOf(productId1));;
        policies.add(new Pair<> ("Minimal Amount", params1));
        //discount of 50% on beers if you buy at least 5 milk & 2 bread from now until february 2022
        Assertions.assertFalse(API.addDiscountOnProduct(storeId1, registerId2, productId1, "Or", policies, begin, end, 50, "Sum").isResult());
    }

    @Test
    public void changeDiscountPolicySuccessTest() {
        addDiscountAndOpSuccessTest(); //define the discount policy
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("10"); //min amount
        params1.add(String.valueOf(productId1));
        policies.add(new Pair<> ("Minimal Amount", params1));
        //change discount policy
        API.editDiscountOnProduct(storeId1, registerId1, productId2, "", policies, begin, end, 20, "Sum");
        //buy products with new discount policy
        API.addProductToCart(registerId2, storeId1, productId1, 5);
        API.addProductToCart(registerId2, storeId1, productId3, 3);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 280;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void changeDiscountPolicyNotPermittedFailTest() {
        addDiscountAndOpSuccessTest(); //define the discount policy
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("10"); //min amount
        params1.add(String.valueOf(productId1));
        policies.add(new Pair<> ("Minimal Amount", params1));
        //try to change discount policy
        Assertions.assertFalse((API.editDiscountOnProduct(storeId1, registerId2, productId2, "", policies, begin, end, 20, "Sum")).isResult());
    }

    @Test
    public void addPurchasePolicySuccessTest() { //age limit on alcohol
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("18"); //min age
        params1.add(String.valueOf(productId2));
        policies.add(new Pair<> ("Minimal Amount", params1));
        //age limit on alcohol
        Assertions.assertTrue(API.addPurchasePolicyOnStore(storeId1, registerId1, "Age Limit", policies).isResult());
    }

    @Test
    public void addPurchaseNotPermittedFailTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("18"); //min age
        params1.add(String.valueOf(productId2));
        policies.add(new Pair<> ("Minimal Amount", params1));
        Assertions.assertFalse(API.addPurchasePolicyOnStore(storeId1, registerId2, "Age Limit", policies).isResult());
    }

    @Test
    public void purchaseContradictsPolicyFailTest() {
        addPurchasePolicySuccessTest();
        //user at age 16 tries to buy alcohol when policy is age >= 18
        API.addProductToCart(registerId2, storeId1, productId2, 5);
        Assertions.assertFalse(API.buyProduct(registerId2, storeId1, "Credit1").isResult());
    }

    //TODO test to implement:
    //check if sum/max work
    //add test of time limit
}
