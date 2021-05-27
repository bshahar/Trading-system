package Persistence;

import Domain.User;
import Persistence.DAO.CounterDAO;
import Persistence.DAO.MemberStorePermissionsDAO;
import Persistence.DAO.UserDAO;

import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class CounterWrapper {

    public int incAndGet(String counterName){
        try {
            int currentCounter = get(counterName);

            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<CounterDAO, String> CounterManager = DaoManager.createDao(connectionSource, CounterDAO.class);
            // create an instance of Account
            CounterManager.executeRaw("UPDATE Counters\n" +
                    "SET "+counterName+"= "+String.valueOf(currentCounter+1)+";");
            connectionSource.close();
            return currentCounter+1;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return -1;
        }
    }

    private int get(String counterName) {
        try {

            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<CounterDAO, String> CounterManager = DaoManager.createDao(connectionSource, CounterDAO.class);
            CounterDAO counterDao = CounterManager.queryForId("1");
            connectionSource.close();
            switch (counterName)
            {
                case "storeCounter":
                    return counterDao.getStoreCounter();
                case "userCounter":
                    return counterDao.getUserCounter();
                case "productCounter":
                    return counterDao.getProductCounter();
                case "receiptCounter":
                    return counterDao.getReceiptCounter();
                case "observableCounter":
                    return counterDao.getObservableCounter();
                case "conditionCounter":
                    return counterDao.getConditionCounter();
                case "offerCounter":
                    return counterDao.getOfferCounter();
                case "policyCounter":
                    return counterDao.getPolicyCounter();
                case "notificationCounter":
                    return counterDao.getNotificationCounter();
                case "messageCounter":
                    return counterDao.getMessageCounter();
            }

            return 0;
        }catch (Exception e)
        {
         return  -1;
        }
    }


    public CounterDAO getAll() {
        try {

            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<CounterDAO, String> CounterManager = DaoManager.createDao(connectionSource, CounterDAO.class);
            CounterDAO counterDao = CounterManager.queryForId("1");
            connectionSource.close();
            return counterDao;
        }catch (Exception e)
        {
            return  null;
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
