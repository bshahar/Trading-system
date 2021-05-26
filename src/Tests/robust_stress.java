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
    Map<String,String> payment;
    Map<String,String> supplement;
    UserWrapper users;
    Properties testProps;

    @BeforeEach
    public void setUp() throws IOException {
        DataBaseHelper.cleanAllTable();
        API.initTradingSystem();
        testProps = new Properties();
        try {
            API.initTradingSystem();
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

        payment = new HashMap<>();
        payment.put("card_number", "2222333344445555");
        payment.put("month", "4");
        payment.put("year", "2021");
        payment.put("holder", testProps.getProperty("user2name"));
        payment.put("cvv", "262");
        payment.put("id", String.valueOf(registerId2));

        supplement = new HashMap<>();
        supplement.put("name", testProps.getProperty("user2name"));
        supplement.put("address", "Hanesi");
        supplement.put("city", "Hadera");
        supplement.put("country", "Israel");
        supplement.put("zip", "000000");

        users = new UserWrapper();

    }


    @Test
    public void buyOverProduct(){
        API.addProductToCart(registerId2,storeId1, productId1, 2);
        boolean ans = API.buyProduct(registerId2,storeId1,payment,supplement).isResult();
        Assertions.assertEquals(false ,ans);
    }

    @Test
    public void queryToCloseDB()  {
        try{
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mysql://localhost:3306/mylocal","root","123456");
            connectionSource.close();
            Dao<UserDAO, String> userManager = DaoManager.createDao(connectionSource, UserDAO.class);
            // create an instance of Account
            UserDAO account = new UserDAO(1,"orson",true,20,true,true);
            // persist the account object to the database
            userManager.create(account);

        }catch( Exception e){

        }

    }

    @Test
    public void buyingWhileClosingDB() {
        try{
            ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mysql://localhost:3306/mylocal","root","123456");
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
                    API.buyProduct(registerId2,storeId1,payment,supplement);
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addProductToCart(registerId3,storeId1, productId1, 1);
                    API.buyProduct(registerId3,storeId1,payment,supplement);
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
                    API.buyProduct(registerId2,storeId1,payment,supplement).isResult();
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addProductToCart(registerId3,storeId1, productId2, 1);
                    API.buyProduct(registerId3,storeId1,payment,supplement).isResult();
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
    public void concurrentPaymentSystem(){
        //when we are buying product we are checking also the payment system concurrently.
        try{
            Thread thread1= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addProductToCart(registerId2,storeId1, productId1, 1);
                    API.buyProduct(registerId2,storeId1,payment,supplement).isResult();
                }
            });
            Thread thread2= new Thread(new Runnable() {
                @Override
                public void run() {
                    API.addProductToCart(registerId3,storeId1, productId2, 1);
                    API.buyProduct(registerId3,storeId1,payment,supplement).isResult();
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
    public void supplyWithNoSupplymentSystem(){
        //empty the external system url for checking the robustness.
    }

    @Test
    public void concurrentsupplymentSystem(){

    }


}
