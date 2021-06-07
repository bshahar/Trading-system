package Persistence;

import Persistence.DAO.*;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import Service.KingLogger;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseHelper {

    private static String test;

    public static void setTest(String test) {
        DataBaseHelper.test = test;
    }

    public static void cleanAllTable(String isTest) {
        try {
            //test = isTest;
            ConnectionSource connectionSource = connect();

            Dao<UserDAO, String> userManager = DaoManager.createDao(connectionSource, UserDAO.class);
            userManager.executeRaw("DELETE FROM Users");

            UserWrapper userWrapper = new UserWrapper();
            userWrapper.clean();

            Dao<BagProductAmountDAO, String> bagManger = DaoManager.createDao(connectionSource, BagProductAmountDAO.class);
            bagManger.executeRaw("DELETE FROM BagProductAmount");

            Dao<MemberStorePermissionsDAO, String> MemberStorePermissionsManager = DaoManager.createDao(connectionSource, MemberStorePermissionsDAO.class);
            MemberStorePermissionsManager.executeRaw("DELETE FROM MemberStorePermissions");

            Dao<ReceiptDAO, String> receiptManager = DaoManager.createDao(connectionSource, ReceiptDAO.class);
            receiptManager.executeRaw("DELETE FROM Receipts");
            Dao<ReceiptLinesDAO, String> ReceiptLinesDAOManager = DaoManager.createDao(connectionSource,ReceiptLinesDAO.class);
            ReceiptLinesDAOManager.executeRaw("DELETE FROM ReceiptLines");

            Dao<StoreDAO, String> StoreManager = DaoManager.createDao(connectionSource,StoreDAO.class);
            StoreManager.executeRaw("DELETE FROM Stores");

            StoreWrapper storeWrapper = new StoreWrapper();
            storeWrapper.clean();

            Dao<StoreReceiptDAO, String> StoreReceiptDAOManager = DaoManager.createDao(connectionSource,StoreReceiptDAO.class);
            StoreReceiptDAOManager.executeRaw("DELETE FROM StoreReceipts");


            Dao<StoreEmployeesDAO, String> StoreEmployeesDAOManager = DaoManager.createDao(connectionSource,StoreEmployeesDAO.class);
            StoreEmployeesDAOManager.executeRaw("DELETE FROM StoreEmployees");

            Dao<StoreOwnerDAO, String> StoreOwnerDAOManager = DaoManager.createDao(connectionSource,StoreOwnerDAO.class);
            StoreOwnerDAOManager.executeRaw("DELETE FROM StoreOwners");

            Dao<StoreManagerDAO, String> StoreManagerDAOManager = DaoManager.createDao(connectionSource,StoreManagerDAO.class);
            StoreManagerDAOManager.executeRaw("DELETE FROM StoreManagers");

            Dao<InventoryDAO, String> InventoryDAOManager = DaoManager.createDao(connectionSource,InventoryDAO.class);
            InventoryDAOManager.executeRaw("DELETE FROM Inventory");

            Dao<AppointmentsDAO, String>AppointmentsDAOManager = DaoManager.createDao(connectionSource,AppointmentsDAO.class);
            AppointmentsDAOManager.executeRaw("DELETE FROM Appointments");

            Dao<ProductDAO, String>ProductDAOManager = DaoManager.createDao(connectionSource,ProductDAO.class);
            ProductDAOManager.executeRaw("DELETE FROM Products");

            ProductWrapper productWrapper = new ProductWrapper();
            productWrapper.clean();

            Dao<ProductReviewsDAO, String>ProductReviewsDAOManager = DaoManager.createDao(connectionSource,ProductReviewsDAO.class);
            ProductReviewsDAOManager.executeRaw("DELETE FROM ProductReviews");
            Dao<ProductCategoriesDAO, String>ProductCategoriesDAOManager = DaoManager.createDao(connectionSource,ProductCategoriesDAO.class);
            ProductCategoriesDAOManager.executeRaw("DELETE FROM ProductCategories");

            Dao<DiscountDAO, String> DiscountDAOManager = DaoManager.createDao(connectionSource, DiscountDAO.class);
            DiscountDAOManager.executeRaw("DELETE FROM Discounts");

            Dao<DiscountConditionDAO, String> DiscountConditionDAOManager = DaoManager.createDao(connectionSource, DiscountConditionDAO.class);
            DiscountConditionDAOManager.executeRaw("DELETE FROM DiscountCondition");

            Dao<PurchaseConditionDAO, String> PurchaseConditionDAOManager = DaoManager.createDao(connectionSource, PurchaseConditionDAO.class);
            PurchaseConditionDAOManager.executeRaw("DELETE FROM PurchaseCondition");

            Dao<ImmediatePurchasesDAO, String> ImmediatePurchasesDAOManager = DaoManager.createDao(connectionSource, ImmediatePurchasesDAO.class);
            ImmediatePurchasesDAOManager.executeRaw("DELETE FROM ImmediatePurchases");

            Dao<UsersCounterOffersDAO, String> usersCounterOffersDAOManager = DaoManager.createDao(connectionSource, UsersCounterOffersDAO.class);
            usersCounterOffersDAOManager.executeRaw("DELETE FROM UsersCounterOffers");

            Dao<UserApprovedOffersDAO, String> usersapproveOffersDAOManager = DaoManager.createDao(connectionSource, UserApprovedOffersDAO.class);
            usersapproveOffersDAOManager.executeRaw("DELETE FROM UserApprovedOffers");

            Dao<AdminTableDAO, String> AdminTableManager = DaoManager.createDao(connectionSource, AdminTableDAO.class);
            AdminTableManager.executeRaw("DELETE FROM AdminTable");

            Dao<UserAuthDAO, String>UserAuthDAOManager = DaoManager.createDao(connectionSource,UserAuthDAO.class);
            UserAuthDAOManager.executeRaw("DELETE FROM UserNamePasswords");
            Dao<CounterDAO, String>CounterDAOManager = DaoManager.createDao(connectionSource,CounterDAO.class);
            CounterDAOManager.executeRaw("DELETE FROM Counters");

            CounterDAO storeDAO= new CounterDAO(1,0,0,0,0,0,0,0,0,0,0);
            CounterDAOManager.create(storeDAO);



            connectionSource.close();
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static ConnectionSource connect() throws Exception{
        JSONObject jsonObject;
        boolean isTest = test.equals("test");
        JSONParser jsonParser = new JSONParser();
        FileReader reader;
        try {
            if (isTest) {
                reader = new FileReader("resources\\appConfigTest.json");
            } else {
                reader = new FileReader("resources\\appConfig.json");
            }
        }
        catch(Exception e){
            KingLogger.logError("INIT_TRADING_SYSTEM: json file was not found.");
            throw new FileNotFoundException("json file was not found.");
        }

        jsonObject = (JSONObject) jsonParser.parse(reader);

        if(jsonObject == null) {
            KingLogger.logError("INIT_TRADING_SYSTEM: json file was not found.");
            throw new FileNotFoundException("json file was not found.");
        }

        String url = (String) jsonObject.get("dbURL");
        String userName = (String) jsonObject.get("dbUserName");
        String password = (String) jsonObject.get("dbPassword");

        if(url == null){
            KingLogger.logError("DATABASE_HELPER: url attribute was not found.");
            throw new FileNotFoundException("url attribute was not found");
        }
        if(userName == null){
            KingLogger.logError("DATABASE_HELPER: userName attribute was not found.");
            throw new FileNotFoundException("userName attribute was not found");
        }
        if(password == null){
            KingLogger.logError("DATABASE_HELPER: password attribute was not found.");
            throw new FileNotFoundException("password attribute was not found");
        }
        return new JdbcConnectionSource(url,userName,password);

//        Properties appProps = new Properties();
//        InputStream input = API.class.getClassLoader().getResourceAsStream("appConfig.json");
//        if(input != null)
//            appProps.load(input);
//        else
//            throw new FileNotFoundException("Property file was not found.");
//
//        boolean test = appProps.getProperty("forTests").equals("true");
//        String url;
//        String userName;
//        String password;
//        if(test)
//        {
//            url = appProps.getProperty("localDbURL");
//            userName = appProps.getProperty("localDbUserName");
//            password = appProps.getProperty("localDbPassword");
//        }
//        else{
//            url = appProps.getProperty("dbURL");
//            userName = appProps.getProperty("dbUsername");
//            password = appProps.getProperty("dbPassword");
//        }
//        return new JdbcConnectionSource(url,userName,password);
    }
}
