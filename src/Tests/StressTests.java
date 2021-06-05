package Tests;

import Domain.Result;
import Persistence.DataBaseHelper;
import Service.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.fail;

public class StressTests {

    Map<String,String> paymentMap= new HashMap<>();
    Map<String,String> supplementMap= new HashMap<>();


    @BeforeEach
    public void setUp() {
        Properties testProps = new Properties();
        try {
            DataBaseHelper.cleanAllTable();
            API.initTradingSystem();
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
        } catch (Exception e) {
        }
    }


    @Test
    public void general_stress_100_different_requests_95_success_rate(){
        try {
            AtomicInteger totalRequests = new AtomicInteger();
            AtomicInteger successRequests = new AtomicInteger();
            for (int i = 0; i < 100; i++) {
                API.register("userNumber" + i, "password" + i, 20);
                API.registeredLogin("userNumber" + i, "password" + i);
            }
            List<String> list = new LinkedList<>();
            list.add("FOOD");
            for (int i = 1; i < 10; i++) {
                if(API.openStore(i, "store" + i).isResult()){
                    successRequests.incrementAndGet();
                }
                totalRequests.incrementAndGet();
                if(API.addProduct(i, i, "product" + i, list, 20, "tasty", 100).isResult()){
                    successRequests.incrementAndGet();
                }
                totalRequests.incrementAndGet();
            }
            //users 10-19 buy product
            Thread purchaseFromStore1Thread = new Thread(() -> {
                for (int i = 10; i < 20; i++) {
                    if (API.addProductToCart(i, 1, 1, 2).isResult()) {
                        successRequests.incrementAndGet();
                    }
                    totalRequests.incrementAndGet();
                    if (API.buyProduct(i, 1, paymentMap, supplementMap).isResult()) {
                        successRequests.incrementAndGet();
                    }
                    totalRequests.incrementAndGet();
                }
            });
            //users 20-29 buy product
            Thread purchaseFromStore2Thread = new Thread(() -> {
                for (int i = 20; i < 29; i++) {
                    if (API.addProductToCart(i, 2, 2, 2).isResult()) {
                        successRequests.incrementAndGet();
                    }
                    totalRequests.incrementAndGet();
                    if (API.buyProduct(i, 2, paymentMap, supplementMap).isResult()) {
                        successRequests.incrementAndGet();
                    }
                    totalRequests.incrementAndGet();

                }
            });
            //users 30-39 buy product
            Thread purchaseFromStore3Thread = new Thread(() -> {
                for (int i = 30; i < 39; i++) {
                    if (API.addProductToCart(i, 3, 3, 2).isResult()) {
                        successRequests.incrementAndGet();
                    }
                    totalRequests.incrementAndGet();
                    if (API.buyProduct(i, 3, paymentMap, supplementMap).isResult()) {
                        successRequests.incrementAndGet();
                    }
                    totalRequests.incrementAndGet();
                }
            });
            Thread getStoreInfo = new Thread(() -> {
                for (int i = 1; i < 10; i++) {
                    if (API.getStorePurchaseHistory(i, i).isResult()) {
                        successRequests.incrementAndGet();
                    }
                    totalRequests.incrementAndGet();
                }
            });
            Thread registerThread = new Thread(() -> {
                for (int i = 200; i < 220; i++) {
                    if (API.register("userNumber" + i, "password" + i, 20).isResult()) {
                        successRequests.incrementAndGet();
                    }
                    totalRequests.incrementAndGet();

                }
            });
            purchaseFromStore1Thread.start();
            purchaseFromStore2Thread.start();
            purchaseFromStore3Thread.start();
            getStoreInfo.start();
            registerThread.start();
            purchaseFromStore1Thread.join();
            purchaseFromStore2Thread.join();
            purchaseFromStore3Thread.join();
            getStoreInfo.join();
            registerThread.join();
            System.out.println("total requests of: "+totalRequests.get());
            System.out.println("success requests of: "+successRequests.get());
            System.out.println("success rate of: "+successRequests.get()/totalRequests.get());
            Assertions.assertTrue(successRequests.get()/totalRequests.get()>0.95);
        }
        catch(Exception e){
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void load_test_1000_stores_1000_products_10000_registered_1000000_purchases(){
        int success=0;
        int total=0;
        for(int i=1;i<1000010;i++){
            if(API.register("userName"+i,"password"+i,20).isResult()){
                success++;
            }
            total++;
        }
        List<String> catList= new LinkedList<>();
        catList.add("food");
        for(int i=1;i<1000;i++){
            if(API.openStore(i,"store"+i).isResult()){
                success++;
                for(int j=0;j<1000;j++){
                    if(API.addProduct(i,i,"product"+i,catList,30,"product",2000000).isResult()) {
                        success++;
                    }
                    total++;
                }
            }
            total++;
        }
        for(int i=1;i<1000000;i++){
            if(API.addProductToCart(i,1,1,1).isResult()){
                success++;
                if(API.buyProduct(i,1,paymentMap,supplementMap).isResult()){
                    success++;
                }
            }
            total+=2;
        }
        Assertions.assertTrue(success/total>=1);
    }

}
