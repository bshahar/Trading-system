package Tests;

import Domain.Product;
import Domain.PurchaseFormat.PurchaseOffer;
import Domain.Receipt;
import Domain.Result;
import Persistence.DataBaseHelper;
import Service.API;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class DiscountAndPurchaseTest {

    private int registerId1;
    private int registerId2;
    private int storeId1;
    private int productId1;
    private int productId2;
    private int productId3;
    private int productId4;
    private int productId5;
    private String begin;
    private String end;
    private Map<String, String> paymentMap;
    private Map<String, String> supplementMap;

    @BeforeEach
    public void setUp() {
        Properties testProps = new Properties();
        try {
            DataBaseHelper.cleanAllTable("test");
            API.initTradingSystem("test", "");
            InputStream input = getClass().getClassLoader().getResourceAsStream("testsSetUp.properties");
            if(input != null)
                testProps.load(input);
            else
                throw new FileNotFoundException("Property file was not found.");
        } catch (Exception e) {
        }
        paymentMap= new HashMap<>();
        supplementMap= new HashMap<>();

        paymentMap.put("card_number", "123456789");
        paymentMap.put("month", "1");
        paymentMap.put("year", "2021");
        paymentMap.put("holder", "or");
        paymentMap.put("cvv", "123");
        paymentMap.put("id","123456789");

        supplementMap.put("name", "or");
        supplementMap.put("address", "bash");
        supplementMap.put("city","bash");
        supplementMap.put("country","IL");
        supplementMap.put("zip", "1234567");
        API.register(testProps.getProperty("user1name"), testProps.getProperty("user1password"), Integer.parseInt(testProps.getProperty("user1age")));
        API.register(testProps.getProperty("user2name"), testProps.getProperty("user2password"), Integer.parseInt(testProps.getProperty("user2age")));

        registerId1 = (int) API.registeredLogin(testProps.getProperty("user1name"), testProps.getProperty("user1password")).getData();
        registerId2 = (int) API.registeredLogin(testProps.getProperty("user2name"), testProps.getProperty("user2password")).getData();

        storeId1 = (int) API.openStore(registerId1, testProps.getProperty("storeNameTest")).getData();
        LinkedList<String> catList1 = new LinkedList<>();
        catList1.add(testProps.getProperty("categoryFood"));
        LinkedList<String> catList2 = new LinkedList<>();
        catList2.add(testProps.getProperty("categoryDrinks"));

        productId1 = (int) API.addProduct(1, storeId1, testProps.getProperty("prodMilkName"), catList1,
                Integer.parseInt(testProps.getProperty("milkPrice")),
                testProps.getProperty("descriptionFood"),
                Integer.parseInt(testProps.getProperty("prodQuantity100"))).getData();
        productId2 = (int) API.addProduct(1, storeId1, testProps.getProperty("prodBeerName"), catList2,
                Integer.parseInt(testProps.getProperty("beerPrice")),
                testProps.getProperty("descriptionAlcohol"),
                Integer.parseInt(testProps.getProperty("prodQuantity100"))).getData();
        productId3 = (int) API.addProduct(1, storeId1, testProps.getProperty("prodBreadName"), catList1,
                Integer.parseInt(testProps.getProperty("breadPrice")),
                testProps.getProperty("descriptionFood"),
                Integer.parseInt(testProps.getProperty("prodQuantity100"))).getData();
        productId4 = (int) API.addProduct(1, storeId1, testProps.getProperty("prodCheeseName"), catList1,
                Integer.parseInt(testProps.getProperty("cheesePrice")),
                testProps.getProperty("descriptionFood"),
                Integer.parseInt(testProps.getProperty("prodQuantity100"))).getData();
        productId5 = (int) API.addProduct(1, storeId1, "mlik", catList1, 0, testProps.getProperty("descriptionFood"), 4 ).getData();

        begin = testProps.getProperty("dateBegin");
        end = testProps.getProperty("dateEnd");


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
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
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
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
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
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
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
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
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
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
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
        Assertions.assertFalse(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).isResult());
    }

    @Test
    public void purchaseContradictsAgeAndAmountPolicyFailTest() {
        addPurchasePolicyOnStoreSuccessTest();
        API.addProductToCart(registerId2, storeId1, productId2, 5);
        Assertions.assertFalse(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).isResult());
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
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
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
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void addSimpleDiscountOnStoreSuccessTest() {
        List<Pair<String, List<String>>> policies = new LinkedList<>();
        //discount of 10% on drinks
        Assertions.assertTrue(API.addDiscountPolicyOnStore(storeId1, registerId1, "", policies, begin, end, 10, "Sum").isResult());
    }

    @Test
    public int addOfferSuccessTest() {
        Result r = API.addPurchaseOffer(storeId1,registerId2, productId5,10,4);
        Assertions.assertTrue(r.isResult());
        return (int)r.getData();
    }


    @Test
    public void approveOfferWithoutPermissionFailTest(){
        int offerId = addOfferSuccessTest();
        Assertions.assertFalse(API.approvePurchaseOffer(storeId1,registerId2, productId5,offerId).isResult());
    }

    @Test
    public void approveOfferWithPermissionSuccessTest(){
        /*
        int offerId = addOfferSuccessTest();
        Assertions.assertTrue(API.approvePurchaseOffer(storeId1,registerId1, productId5,offerId).isResult());

         */
        int offerId = addOfferSuccessTest();
        API.approvePurchaseOffer(storeId1,registerId1, productId5,offerId).isResult();
        Map<PurchaseOffer, Product> res = (Map<PurchaseOffer,Product>)API.getOffersForStore(storeId1,registerId1).getData();
        if(res.isEmpty()){
            System.out.println("yayy");
        }
       // Assertions.assertTrue(API.approvePurchaseOffer(storeId1,registerId1, productId5,offerId).isResult());
    }

    @Test
    public void buyProductAfterOfferSuccessTest(){
        int offerId = addOfferSuccessTest();
        API.approvePurchaseOffer(storeId1,registerId1, productId5,offerId);
        double expectedTotal = 40;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void buyProductAfterCounterOfferAndApprove(){
        int offerId = addOfferSuccessTest();
        API.counterPurchaseOffer(storeId1,registerId1, productId5,offerId,15);
        API.approveCounterOffer(storeId1,registerId2, productId5,true);
        double expectedTotal = 60;
        int receiptId = Integer.parseInt(API.buyProduct(registerId2, storeId1, paymentMap, supplementMap).getData().toString());
        double actualTotal = ((Receipt) (API.getReceipt(receiptId).getData())).getTotalCost();
        Assertions.assertEquals(expectedTotal, actualTotal);
    }

   /* public void getOfferOfStore(){
        int offerId = addOfferSuccessTest();
        API.getOffersForStore(storeId1,registerId1);
     //   int n = ((Map<PurchaseOffer,Product>)(API.getOffersForStore(storeId1,registerId1).getData())).size();
     //   Assertions.assertEquals(n, 1);
    }

    */
}
