package Tests;

import Domain.Product;
import Domain.User;
import Persistence.DAO.ProductDAO;
import Persistence.DAO.UserDAO;
import Persistence.DataBaseHelper;
import Persistence.ProductCategoryWrapper;
import Persistence.UserWrapper;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import Service.PaymentAdapter;
import Service.SupplementAdapter;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

public class robust_stress {
    int registerId1;
    int registerId2;
    int registerId3;
    int storeId1;
    int productId1;
    int productId2;
    Map<String,String> payment1;
    Map<String,String> payment2;
    Map<String,String> payment3;
    Map<String,String> payment4;
    Map<String,String> payment5;

    Map<String,String> supplement1;
    Map<String,String> supplement2;
    Map<String,String> supplement3;
    Map<String,String> supplement4;
    Map<String,String> supplement5;
    UserWrapper users;
    Properties testProps;

    @BeforeEach
    public void setUp() throws Exception {
        DataBaseHelper.cleanAllTable("test");
        API.initTradingSystem("test");
        testProps = new Properties();
        try {
            API.initTradingSystem("test");
            InputStream input = getClass().getClassLoader().getResourceAsStream("testsSetUp.properties");
            if(input != null)
                testProps.load(input);
            else
                throw new FileNotFoundException("Property file was not found.");
        } catch (Exception e) {
        }
//        String userName1 = "erez";
//        String password1 = "erez1234";
//        String userName2 = "elad";
//        String password2 = "elad1234";
//        String userName3 = "or";
//        String password3 = "or1234";
        API.register(testProps.getProperty("user1name"), testProps.getProperty("user1password"), Integer.parseInt(testProps.getProperty("user1age")));
        API.register(testProps.getProperty("user2name"), testProps.getProperty("user2password"), Integer.parseInt(testProps.getProperty("user2age")));
        API.register(testProps.getProperty("user3name"), testProps.getProperty("user3password"), Integer.parseInt(testProps.getProperty("user3age")));

        registerId1 = (int) API.registeredLogin(testProps.getProperty("user1name"), testProps.getProperty("user1password")).getData();
        registerId2 = (int) API.registeredLogin(testProps.getProperty("user2name"), testProps.getProperty("user2password")).getData();
        registerId3 = (int) API.registeredLogin(testProps.getProperty("user3name"), testProps.getProperty("user3password")).getData();

        storeId1 = (int) API.openStore(registerId1, "Erez store").getData();
        LinkedList<String> catList = new LinkedList<>();
        catList.add("FOOD");
        productId1 = (int) API.addProduct(registerId1, storeId1, "milk", catList, 10, "FOOD", 1).getData();
        productId2 = (int) API.addProduct(registerId1, storeId1, "milk", catList, 10, "FOOD", 1).getData();
//
//        paymentMap.put("card_number", "123456789");
//        paymentMap.put("month", "1");
//        paymentMap.put("year", "2021");
//        paymentMap.put("holder", "or");
//        paymentMap.put("cvv", "123");
//        paymentMap.put("id","123456789");
//        supplementMap.put("name", "or");
//        supplementMap.put("address", "bash");
//        supplementMap.put("city","bash");
//        supplementMap.put("country","IL");
//        supplementMap.put("zip", "1234567");

        payment1 = new HashMap<>();
        payment1.put("card_number", "1231232123");
        payment1.put("month", "4");
        payment1.put("year", "2021");
        payment1.put("holder", testProps.getProperty("user2name"));
        payment1.put("cvv", "262");
        payment1.put("id", "123123123");

        payment2 = new HashMap<>();
        payment2.put("card_number", "2222333344445556");
        payment2.put("month", "4");
        payment2.put("year", "2021");
        payment2.put("holder", testProps.getProperty("user2name"));
        payment2.put("cvv", "262");
        payment2.put("id", "123456789");

        payment3 = new HashMap<>();
        payment3.put("card_number", "2222333344445557");
        payment3.put("month", "4");
        payment3.put("year", "2021");
        payment3.put("holder", testProps.getProperty("user2name"));
        payment3.put("cvv", "262");
        payment3.put("id", "123456780");

        payment4 = new HashMap<>();
        payment4.put("card_number", "2222333344445558");
        payment4.put("month", "4");
        payment4.put("year", "2021");
        payment4.put("holder", testProps.getProperty("user2name"));
        payment4.put("cvv", "262");
        payment4.put("id", "123456700");

        payment5 = new HashMap<>();
        payment5.put("card_number", "2222333344445559");
        payment5.put("month", "4");
        payment5.put("year", "2021");
        payment5.put("holder", testProps.getProperty("user2name"));
        payment5.put("cvv", "262");
        payment5.put("id", "123456000");





        supplement1 = new HashMap<>();
        supplement1.put("name", testProps.getProperty("user2name"));
        supplement1.put("address", "Hanesi");
        supplement1.put("city", "Hadera");
        supplement1.put("country", "Israel");
        supplement1.put("zip", "000000");

        supplement2 = new HashMap<>();
        supplement2.put("name", "Shahar");
        supplement2.put("address", "Hanesi");
        supplement2.put("city", "Hadera");
        supplement2.put("country", "Israel");
        supplement2.put("zip", "000000");

        supplement3 = new HashMap<>();
        supplement3.put("name", "Or");
        supplement3.put("address", "Hanesi");
        supplement3.put("city", "Hadera");
        supplement3.put("country", "Israel");
        supplement3.put("zip", "000000");

        supplement4 = new HashMap<>();
        supplement4.put("name", "Elad");
        supplement4.put("address", "Hanesi");
        supplement4.put("city", "Hadera");
        supplement4.put("country", "Israel");
        supplement4.put("zip", "000000");

        supplement5 = new HashMap<>();
        supplement5.put("name", "Dorin");
        supplement5.put("address", "Hanesi");
        supplement5.put("city", "Hadera");
        supplement5.put("country", "Israel");
        supplement5.put("zip", "000000");

        users = new UserWrapper();

    }


    @Test
    public void buyOverProduct(){
        API.addProductToCart(registerId2,storeId1, productId1, 2);
        boolean ans = API.buyProduct(registerId2,storeId1,payment1,supplement1).isResult();
        Assertions.assertEquals(false ,ans);
    }

    @Test
    public void queryToCloseDB() throws SQLException, IOException {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mysql://localhost:3306/mylocal", "root", "123456");
            connectionSource.close();
            // instantiate the dao
            Dao<UserDAO, String> userManager = DaoManager.createDao(connectionSource, UserDAO.class);
            // create an instance of Account
            UserDAO account = new UserDAO(5, "Erez", true, 20, true, false);
            // persist the account object to the database
            userManager.create(account);
            // close the connection source
            fail();
        }
        catch(Exception e){
        }
    }

    @Test
    public void buyingWhileClosingDB() {
        try{
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mysql://localhost:3306/orsonDB","root","orson");
            Thread thread1= new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        API.addProductToCart(registerId2, storeId1, productId1, 1);
                        Dao<ProductDAO, String> ProductDAOManager = DaoManager.createDao(connectionSource, ProductDAO.class);
                        ProductDAO productDAO = ProductDAOManager.queryForId(Integer.toString(productId1));
                        Product product = new Product(productDAO.getId(), productDAO.getName(), productDAO.getPrice(),
                                productDAO.getDescription(), productDAO.getStoreId(), productDAO.getRatesCount(), productDAO.getRate());
                        ProductCategoryWrapper productCategoryWrapper = new ProductCategoryWrapper();
                        product.setCategories(productCategoryWrapper.getCategories(product.getId()));
                    }
                    catch(Exception e){
                        fail();
                    }
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connectionSource.close();
                    } catch (IOException e) {
                        fail();
                    }
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void concurrentProductBuying(){
        try{
            Thread thread1= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addProductToCart(registerId2,storeId1, productId1, 1);
                    API.buyProduct(registerId2,storeId1,payment1,supplement1);
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addProductToCart(registerId3,storeId1, productId1, 1);
                    API.buyProduct(registerId3,storeId1,payment1,supplement1);
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void concurrentLogin(){
        API.register("try1", "123", 20);
        API.register("try2", "1234", 22);

        try{
            Thread thread1= new Thread(new Runnable() {
                @Override
                public void run() {
                    registerId1 = (int) API.registeredLogin("try1", "123").getData();
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    registerId2 = (int) API.registeredLogin("try2", "1234").getData();
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void concurrentQueringDB(){
        try{
            Thread thread1= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addProductToCart(registerId2,storeId1, productId1, 1);
                    API.buyProduct(registerId2,storeId1,payment1,supplement1).isResult();
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addProductToCart(registerId3,storeId1, productId2, 1);
                    API.buyProduct(registerId3,storeId1,payment1,supplement1).isResult();
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void paymentWithNoPaymentSystem(){

    }


    @Test
    public void concurrentPaymentSystem() throws InterruptedException {
        PaymentAdapter paySystem = new PaymentAdapter("https://cs-bgu-wsep.herokuapp.com/");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!paySystem.pay(payment1).isResult()){
                    fail();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!paySystem.pay(payment2).isResult()){
                    fail();
                }
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!paySystem.pay(payment3).isResult()){
                    fail();
                }
            }
        });
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!paySystem.pay(payment4).isResult()){
                    fail();
                }
            }
        });
        Thread thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!paySystem.pay(payment5).isResult()){
                    fail();
                }
            }
        });
        thread1.run();
        thread2.run();
        thread3.run();
        thread4.run();
        thread5.run();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
    }

    @Test
    public void scalabilyLogin() throws InterruptedException {
        for (int i=1; i<20; i++){
            Thread thread= new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!API.guestLogin().isResult()){
                        fail();
                    }
                }
            });
            thread.run();
            thread.join();
        }
    }


    @Test
    public void concurrentsupplymentSystem() throws InterruptedException {
        SupplementAdapter supSystem = new SupplementAdapter("https://cs-bgu-wsep.herokuapp.com/");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!supSystem.supply(supplement1).isResult()){
                    fail();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!supSystem.supply(supplement2).isResult()){
                    fail();
                }
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!supSystem.supply(supplement3).isResult()){
                    fail();
                }
            }
        });
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!supSystem.supply(supplement4).isResult()){
                    fail();
                }
            }
        });
        Thread thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!supSystem.supply(supplement5).isResult()){
                    fail();
                }
            }
        });
        thread1.run();
        thread2.run();
        thread3.run();
        thread4.run();
        thread5.run();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
    }


}
