package Tests;

import Domain.Receipt;
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
    private int productId4;
    private String begin;
    private String end;

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
        productId2 = (int) API.addProduct(1, storeId1, "beer", catList2, 20, "ALCOHOL", 100).getData();
        productId3 = (int) API.addProduct(1, storeId1, "bread", catList1, 10, "FOOD", 100).getData();
        productId4 = (int) API.addProduct(1, storeId1, "cheese", catList1, 15, "FOOD", 100).getData();
        begin = "01/06/2021";
        end = "01/06/2022";
    }

    @Test
    public void addDiscountOnProductAndOpSuccessTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("5"); //min amount
        params1.add(String.valueOf(productId1));
        List<String> params2 = new LinkedList<>();
        params2.add("2"); //min amount
        params2.add(String.valueOf(productId3));
        policies.add(new Pair<> ("Minimal Amount", params1));
        policies.add(new Pair<> ("Minimal Amount", params2));
        //discount of 20% on beers if you buy at least 5 milk AND 2 bread from now until february 2022
        Assertions.assertTrue(API.addDiscountOnProduct(storeId1, registerId1, productId2, "And", policies, begin, end, 20, "Sum").isResult());
    }

    @Test
    public void buyProductWithDiscountAndOpSuccessTest() {
        addDiscountOnProductAndOpSuccessTest();
        API.addProductToCart(registerId2, storeId1, productId1, 5);
        API.addProductToCart(registerId2, storeId1, productId3, 3);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 240;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void buyProductWithDiscountAndOpWrongAmountFailTest() {
        addDiscountOnProductAndOpSuccessTest();
        API.addProductToCart(registerId2, storeId1, productId1, 2);
        API.addProductToCart(registerId2, storeId1, productId3, 3);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 250;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void addDiscountOnProductOrOpSuccessTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("5"); //min amount
        params1.add(String.valueOf(productId1));
        List<String> params2 = new LinkedList<>();
        params2.add("2"); //min amount
        params2.add(String.valueOf(productId3));
        policies.add(new Pair<> ("Minimal Amount", params1));
        policies.add(new Pair<> ("Minimal Amount", params2));
        //discount of 50% on beers if you buy at least 5 milk OR 2 bread from now until february 2022
        Assertions.assertTrue(API.addDiscountOnProduct(storeId1, registerId1, productId2, "Or", policies, begin, end, 50, "Max").isResult());
    }

    @Test
    public void buyProductWithDiscountOrOpSuccessTest() {
        addDiscountOnProductOrOpSuccessTest();
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
        addDiscountOnProductAndOpSuccessTest();
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
        addDiscountOnProductAndOpSuccessTest(); //define the discount policy
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
        addDiscountOnProductAndOpSuccessTest(); //define the discount policy
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("10"); //min amount
        params1.add(String.valueOf(productId1));
        policies.add(new Pair<> ("Minimal Amount", params1));
        //try to change discount policy
        Assertions.assertFalse((API.editDiscountOnProduct(storeId1, registerId2, productId2, "", policies, begin, end, 20, "Sum")).isResult());
    }

    @Test
    public void addPurchasePolicyOnCategorySuccessTest() { //age limit on drinks category
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("18"); //min age
        policies.add(new Pair<> ("Age Limit", params1));
        //age limit on drinks
        Assertions.assertTrue(API.addPurchasePolicyOnCategory(storeId1, registerId1, "DRINKS","", policies).isResult());
    }

    @Test
    public void addPurchasePolicyOnStoreSuccessTest() { //age limit on drinks category
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("10"); //min age
        policies.add(new Pair<> ("Age Limit", params1));
        List<String> params2 = new LinkedList<>();
        params2.add("5"); //min amount
        params2.add(String.valueOf(productId1)); //prod id
        policies.add(new Pair<> ("Min Amount", params2));
        //age limit on drinks
        Assertions.assertTrue(API.addPurchasePolicyOnStore(storeId1, registerId1,"And", policies).isResult());
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
    public void purchaseContradictsAgePolicyFailTest() {
        addPurchasePolicyOnCategorySuccessTest();
        //user at age 16 tries to buy alcohol when policy is age >= 18
        API.addProductToCart(registerId2, storeId1, productId2, 5);
        Assertions.assertFalse(API.buyProduct(registerId2, storeId1, "Credit1").isResult());
    }

    @Test
    public void purchaseContradictsAgeAndAmountPolicyFailTest() {
        addPurchasePolicyOnStoreSuccessTest();
        API.addProductToCart(registerId2, storeId1, productId2, 5);
        Assertions.assertFalse(API.buyProduct(registerId2, storeId1, "Credit1").isResult());
    }

    @Test
    public void addDiscountOnCategoryNoneOpSuccessTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("7"); //min amount
        params1.add(String.valueOf(productId1));
        policies.add(new Pair<> ("Minimal Amount", params1));
        //discount of 10% on drinks if you buy at least 7 milk from now until february 2022
        Assertions.assertTrue(API.addDiscountPolicyOnCategory(storeId1, registerId1, "DRINKS", "", policies, begin, end, 10, "Sum").isResult());
    }

    @Test
    public void buyProductsWithSumOfTwoDiscountsSuccessTest() {
        addDiscountOnProductAndOpSuccessTest(); //20% on beers if bag contains at least 5 milk and 2 bread
        addDiscountOnCategoryNoneOpSuccessTest(); //10% on drinks if bag contains at least 7 milk

        API.addProductToCart(registerId2, storeId1, productId1, 10);
        API.addProductToCart(registerId2, storeId1, productId3, 2);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 260;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void addDiscountOnCategoryOrOpSuccessTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        List<String> params1 = new LinkedList<>();
        params1.add("7"); //min amount
        params1.add(String.valueOf(productId1));
        List<String> params2 = new LinkedList<>();
        params2.add("100"); //min cost
        policies.add(new Pair<> ("Minimal Amount", params1));
        policies.add(new Pair<> ("Minimal Cost", params2));
        //discount of 10% on drinks if you buy at least 7 milk OR in cost of at least 100 from now until february 2022
        Assertions.assertTrue(API.addDiscountPolicyOnCategory(storeId1, registerId1, "DRINKS", "", policies, begin, end, 10, "Max").isResult());
    }

    @Test
    public void buyProductsWithMaxBetweenTwoDiscountsSuccessTest() {
        addDiscountOnProductOrOpSuccessTest(); //50% on beers if bag contains at least 5 milk or 2 bread
        addDiscountOnCategoryOrOpSuccessTest(); //10% on drinks if bag is at cost of at least 100 or contains at least 7 milk

        API.addProductToCart(registerId2, storeId1, productId1, 6);
        //API.addProductToCart(registerId2, storeId1, productId3, 2);
        API.addProductToCart(registerId2, storeId1, productId2, 10);
        double expectedTotal = 160;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, "Credit1").getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

}
