package Persistence;

import Domain.Store;
import Domain.User;
import Interface.TradingSystem;
import Persistence.DAO.StoreDAO;
import Persistence.DAO.StoreEmployeesDAO;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class StoreWrapper {
    private static List<Store> stores = Collections.synchronizedList(new LinkedList<>());

    public boolean add(Store store,User user){
        try {
            //Stores
            ConnectionSource connectionSource = connect();
            Dao<StoreDAO, String> storeDAOManager = DaoManager.createDao(connectionSource,StoreDAO.class);
            StoreDAO storeDAO= new StoreDAO(store.getStoreId(),store.getNotificationId(), store.getName(),store.getRate(),store.getRatesCount());
            storeDAOManager.create(storeDAO);
            connectionSource.close();

            //StoresEmployees
            StoreEmployeesWrapper storeEmployeesWrapper= new StoreEmployeesWrapper();
            storeEmployeesWrapper.add(user,store.getStoreId());
//
//            //StoreOwners
//            StoreOwnerWrapper storeOwnerWrapper= new StoreOwnerWrapper();
//            storeOwnerWrapper.add(store.getOwners(),store.getStoreId());
//
//            //StoreManagers
//            StoreManagerWrapper storeManagerWrapper= new StoreManagerWrapper();
//            storeManagerWrapper.add(store.getManagers(),store.getStoreId());
//
//            //StoreReceipt
//            StoreReceiptWrapper storeReceiptWrapper= new StoreReceiptWrapper();
//            storeReceiptWrapper.add(store.getReceipts(),store.getStoreId());
//
//            //Appointments
//            AppointmentsWrapper appointmentsWrapper= new AppointmentsWrapper();
//            appointmentsWrapper.add(store.getAppointments(),store.getStoreId());
//
//            //Inventory
//            InventoryWrapper inventoryWrapper= new InventoryWrapper();
//            inventoryWrapper.add(store.getInventory().getProductsAmounts(),store.getStoreId());
            stores.add(store);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Store getById(int storeId){
        try {
            if(getStoreById(storeId)!=null)
                return getStoreById(storeId);
            ConnectionSource connectionSource = connect();
            Dao<StoreDAO, String> StoreDAOManager = DaoManager.createDao(connectionSource,StoreDAO.class);
            StoreDAO storeDAO = StoreDAOManager.queryForId(Integer.toString(storeId));
            connectionSource.close();
            Store store= new Store(storeDAO.getStoreId(),storeDAO.getName(), TradingSystem.getPolicyCounter(),TradingSystem.getOfferCounter());
            store.setNotificationId(storeDAO.getNotificationId());
            store.setRate(storeDAO.getRate());
            store.setRateCount(storeDAO.getRatesCount());

//            //Employees
//            StoreEmployeesWrapper storeEmployeesWrapper= new StoreEmployeesWrapper();
//            List<User> employees= storeEmployeesWrapper.getUsersByStoreId(storeId);
//            store.setEmployees(employees);

            return store;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private Store getStoreById(int storeId)
    {
        for(Store store : stores)
        {
            if(store.getStoreId() == storeId)
                return store;
        }
        return null;
    }


    public List<Integer> getStoresByUserId(int userId){
        try {
            ConnectionSource connectionSource = connect();
            Dao<StoreEmployeesDAO, String> StoreEmployeesDAOManager = DaoManager.createDao(connectionSource,StoreEmployeesDAO.class);
            Map<String,Object> map=new HashMap<>();
            map.put("userId",userId);
            List<StoreEmployeesDAO> storeDAOs = StoreEmployeesDAOManager.queryForFieldValues(map);
            connectionSource.close();
            List<Integer> list= new LinkedList<>();
            for(StoreEmployeesDAO storeEmployeesDAO: storeDAOs){
                list.add(storeEmployeesDAO.getStoreId());
            }
            return list;

//            //Employees
//            StoreEmployeesWrapper storeEmployeesWrapper= new StoreEmployeesWrapper();
//            List<User> employees= storeEmployeesWrapper.getUsersByStoreId(storeId);
//            store.setEmployees(employees);

        }
        catch (Exception e)
        {
            return null;
        }
    }

//    private List<Integer> getStoresByUserIdCash(int userId)
//    {
//        List<Integer> list = Collections.synchronizedList(new LinkedList<>());
//
//        for(Store store : stores)
//        {
//            List<User> employees =  (List<User>)store.getEmployees().getData();
//            for(User user : employees)
//                if ()
//        }
//    }

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }

    public List<Store> getAllStores() {
        try {
            ConnectionSource connectionSource = connect();
            Dao<StoreDAO, String> StoreDAOManager = DaoManager.createDao(connectionSource,StoreDAO.class);
            List<StoreDAO> storeDAOs = StoreDAOManager.queryForAll();
            List<Store> stores =new LinkedList<>();
            for(StoreDAO storeDAO :storeDAOs){
                Store store= new Store(storeDAO.getStoreId(),storeDAO.getName(),TradingSystem.getPolicyCounter(),TradingSystem.getOfferCounter());
                store.setNotificationId(storeDAO.getNotificationId());
                store.setRate(storeDAO.getRate());
                store.setRateCount(storeDAO.getRatesCount());
                stores.add(store);
            }
            connectionSource.close();
            return stores;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void clean() {
        stores = Collections.synchronizedList(new LinkedList<>());
    }
}
