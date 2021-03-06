package Persistence;

import Domain.Receipt;
import Persistence.DAO.StoreReceiptDAO;
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

public class StoreReceiptWrapper {

    public void add(List<Receipt> receipts, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreReceiptDAO, String> StoreReceiptDAOManager = DaoManager.createDao(connectionSource,StoreReceiptDAO.class);
            for(Receipt receipt : receipts){
                StoreReceiptDAO storeReceiptDAO= new StoreReceiptDAO(storeId,receipt.getId());
                StoreReceiptDAOManager.create(storeReceiptDAO);
            }
            connectionSource.close();

        }catch(Exception e){

        }
    }

    public void add(Receipt receipt, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreReceiptDAO, String> StoreReceiptDAOManager = DaoManager.createDao(connectionSource,StoreReceiptDAO.class);
            StoreReceiptDAO storeReceiptDAO= new StoreReceiptDAO(storeId,receipt.getId());
            StoreReceiptDAOManager.create(storeReceiptDAO);
            connectionSource.close();

        }catch(Exception e){

        }
    }

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }

    public boolean remove(Receipt receipt, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreReceiptDAO, String> StoreReceiptDAOManager = DaoManager.createDao(connectionSource, StoreReceiptDAO.class);
            int out=StoreReceiptDAOManager.delete(new StoreReceiptDAO(storeId,receipt.getId()));
            connectionSource.close();
            return out==1;
        }catch (Exception e){
            return false;
        }
    }

    public List<Receipt> getAll(int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreReceiptDAO, String> StoreReceiptDAOManager = DaoManager.createDao(connectionSource,StoreReceiptDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<StoreReceiptDAO> StoreReceiptDAOs= StoreReceiptDAOManager.queryForFieldValues(map);
            List<Receipt> receipts= new LinkedList<>();
            ReceiptWrapper receiptWrapper= new ReceiptWrapper();
            for(StoreReceiptDAO storeReceiptDAO: StoreReceiptDAOs){
                receipts.add(receiptWrapper.get(storeReceiptDAO.getReceiptId()));
            }
            connectionSource.close();
            return receipts;
        }catch (Exception e){
            return new LinkedList<>();
        }

    }
}
