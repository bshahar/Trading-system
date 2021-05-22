package Persistence;

import Domain.Store;
import Domain.User;
import Persistence.DAO.StoreDAO;
import Persistence.DAO.UserDAO;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class StoreWrapper {



    public boolean add(Store store){
        try {
            //Stores
            ConnectionSource connectionSource = connect();
            Dao<StoreDAO, String> storeDAOManager = DaoManager.createDao(connectionSource,StoreDAO.class);
            StoreDAO storeDAO= new StoreDAO(store.getStoreId(),store.getNotificationId(), store.getName(),store.getRate(),store.getRatesCount());
            storeDAOManager.create(storeDAO);
            connectionSource.close();

            //StoresEmployees
            StoreEmployeesWrapper storeEmployeesWrapper= new StoreEmployeesWrapper();
            storeEmployeesWrapper.add((List<User>)store.getEmployees().getData(),store.getStoreId());

            //StoreOwners
            StoreOwnerWrapper storeOwnerWrapper= new StoreOwnerWrapper();
            storeOwnerWrapper.add(store.getOwners(),store.getStoreId());

            //StoreManagers
            StoreManagerWrapper storeManagerWrapper= new StoreManagerWrapper();
            storeManagerWrapper.add(store.getManagers(),store.getStoreId());

            //StoreReceipt
            StoreReceiptWrapper storeReceiptWrapper= new StoreReceiptWrapper();
            storeReceiptWrapper.add(store.getReceipts(),store.getStoreId());

            //Appointments
            AppointmentsWrapper appointmentsWrapper= new AppointmentsWrapper();
            appointmentsWrapper.add(store.getAppointments(),store.getStoreId());

            //Inventory
            InventoryWrapper inventoryWrapper= new InventoryWrapper();
            inventoryWrapper.add(store.getInventory().getProductsAmounts(),store.getStoreId());

            return true;
        }
        catch (Exception e)
        {
            return false;
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
