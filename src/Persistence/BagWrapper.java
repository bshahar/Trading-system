package Persistence;

import Domain.*;
import Persistence.DAO.BagProductAmountDAO;
import Persistence.DAO.MemberStorePermissionsDAO;
import Persistence.DAO.ReceiptDAO;
import Persistence.DAO.UserDAO;
import Persistence.connection.JdbcConnectionSource;
import Persistence.spring.StoreWrapper;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BagWrapper {


    public boolean add(Bag bag, int userId){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<BagProductAmountDAO, String> BagManager = DaoManager.createDao(connectionSource, BagProductAmountDAO.class);
            // create an instance of Account
            Map<Product,Integer> productsAmounts = bag.getProductsAmounts();
            for ( Map.Entry<Product,Integer> entry : productsAmounts.entrySet())
            {
                BagProductAmountDAO bagDAO = new BagProductAmountDAO(userId,bag.getStoreId(),entry.getKey().getId(),entry.getValue());
                // persist the account object to the database
                BagManager.create(bagDAO);
            }
            // close the connection source
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public List<Bag> getAllUserBags(int userId)
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<BagProductAmountDAO, String> BagManager = DaoManager.createDao(connectionSource, BagProductAmountDAO.class);
            List<BagProductAmountDAO> bagsDAO = BagManager.queryForAll();
            List<BagProductAmountDAO> userId_bag = Collections.synchronizedList(new LinkedList<>());
            StoreWrapper storeWrapper = new StoreWrapper();
            ProductWrapper productWrapper = new ProductWrapper();
            List<Bag> result = Collections.synchronizedList(new LinkedList<>());
            //get all the bags of the user
            for (BagProductAmountDAO bagDAO : bagsDAO)
                if (bagDAO.getUserId() == userId)
                    userId_bag.add(bagDAO);
            //get all the bags divided by store id
            Map<Integer, List<BagProductAmountDAO>> storeId_Bag = new ConcurrentHashMap<>();
            for (BagProductAmountDAO bagDAO : bagsDAO) {
                if (storeId_Bag.containsKey(bagDAO.getStoreId())) {
                    storeId_Bag.get(bagDAO.getStoreId()).add(bagDAO);
                } else {
                    List<BagProductAmountDAO> newList = new LinkedList<>();
                    newList.add(bagDAO);
                    storeId_Bag.put(bagDAO.getStoreId(), newList);
                }
            }

            for (Map.Entry<Integer, List<BagProductAmountDAO>> entry : storeId_Bag.entrySet())
            {
                Bag bag = new Bag(storeWrapper.getById(entry.getKey()));
                Map<Product, Integer> productsAmount = new HashMap<>();
                for (BagProductAmountDAO bagOfUserLine : entry.getValue()) {
                    productsAmount.put(productWrapper.getById(bagOfUserLine.getProductId()),bagOfUserLine.getAmount());
                }
                bag.setProducts(productsAmount);
                result.add(bag);
            }
            connectionSource.close();
            return result;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public boolean deleteByUserId(int userId){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<BagProductAmountDAO, String> BagManager = DaoManager.createDao(connectionSource, BagProductAmountDAO.class);
            BagManager.executeRaw("DELETE FROM BagProductAmount WHERE user userId = "+String.valueOf(userId));
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean deleteByStoreId(int storeId){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<BagProductAmountDAO, String> BagManager = DaoManager.createDao(connectionSource, BagProductAmountDAO.class);
            BagManager.executeRaw("DELETE FROM BagProductAmount WHERE storeId = "+String.valueOf(storeId));
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void deleteBag(Bag b,int userId) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<BagProductAmountDAO, String> BagManager = DaoManager.createDao(connectionSource, BagProductAmountDAO.class);
            // instantiate the dao
            for (Map.Entry<Product, Integer> entry : b.getProductsAmounts().entrySet()) {
                BagManager.executeRaw("DELETE FROM BagProductAmount WHERE user userId = " + String.valueOf(userId)+" AND storeId= "+String.valueOf(b.getStoreId())+
                        "AND productId= "+entry.getKey().getId());
                connectionSource.close();
            }
        }
        catch(Exception e) {
        }
    }

    public ConnectionSource connect() throws IOException, SQLException {
        Properties appProps = new Properties();
        InputStream input = API.class.getClassLoader().getResourceAsStream("appConfig.properties");
        if(input != null)
            appProps.load(input);
        else
            throw new FileNotFoundException("Property file was not found.");

        boolean test = appProps.getProperty("forTests").equals("true");
        String url;
        String userName;
        String password;
        if(test)
        {
            url = appProps.getProperty("localDbURL");
            userName = appProps.getProperty("localDbUserName");
            password = appProps.getProperty("localDbPassword");
        }
        else{
            url = appProps.getProperty("dbURL");
            userName = appProps.getProperty("dbUsername");
            password = appProps.getProperty("dbPassword");
        }
        return new JdbcConnectionSource(url,userName,password);

    }


}
