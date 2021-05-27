package Persistence;

import Domain.Receipt;
import Domain.ReceiptLine;
import Domain.User;
import Persistence.DAO.ReceiptDAO;
import Persistence.DAO.ReceiptLinesDAO;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ReceiptWrapper {


    public boolean add(Receipt receipt){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<ReceiptDAO, String> receiptManager = DaoManager.createDao(connectionSource, ReceiptDAO.class);
            // create an instance of Account
            ReceiptDAO receiptDAO = new ReceiptDAO(receipt.getId(),receipt.getStoreId(),receipt.getUserId(),receipt.getUserName(),receipt.getTotalCost(),receipt.getPaymentTransactionId(),receipt.getSupplementTransactionId());
            // persist the account object to the database
            receiptManager.create(receiptDAO);
            // close the connection source
            connectionSource.close();
            ReceiptLinesWrapper receiptLinesWrapper = new ReceiptLinesWrapper();
            for(ReceiptLine receiptLine : receipt.getLines()) {
                receiptLinesWrapper.add(receiptLine);
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Receipt get(int id)
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ReceiptDAO, String> receiptManager = DaoManager.createDao(connectionSource, ReceiptDAO.class);
            ReceiptDAO receiptDAO = receiptManager.queryForId(Integer.toString(id));
            ReceiptLinesWrapper receiptLinesWrapper = new ReceiptLinesWrapper();
            List<ReceiptLine> ReceiptLineList = receiptLinesWrapper.getByReceiptId(id);
            Receipt receipt = new Receipt(receiptDAO.getId(),receiptDAO.getStoreId(),receiptDAO.getUserId(),receiptDAO.getUserName(),receiptDAO.getPaymentTransactionId(),receiptDAO.getSupplementTransactionId());
            receipt.setTotalCost(receiptDAO.getTotalCost());
            receipt.setLines(ReceiptLineList);
            connectionSource.close();
            return receipt;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public List<Receipt> get()
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ReceiptDAO, String> receiptManager = DaoManager.createDao(connectionSource, ReceiptDAO.class);
            List<ReceiptDAO> receipts = receiptManager.queryForAll();
            List<Receipt> result = Collections.synchronizedList(new LinkedList<>());
            for(ReceiptDAO receiptDAO : receipts)
            {
                result.add(get(receiptDAO.getId()));
            }
            connectionSource.close();
            return result;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public List<Receipt> getByUserId(int userId)
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ReceiptDAO, String> receiptManager = DaoManager.createDao(connectionSource, ReceiptDAO.class);
            List<ReceiptDAO> receipts = receiptManager.queryForAll();
            List<Receipt> result = Collections.synchronizedList(new LinkedList<>());
            for(ReceiptDAO receiptDAO : receipts)
            {
                if(receiptDAO.getUserId() == userId)
                    result.add(get(receiptDAO.getId()));
            }
            connectionSource.close();
            return result;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public boolean delete(int id){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<ReceiptDAO, String> receiptManager = DaoManager.createDao(connectionSource, ReceiptDAO.class);
            receiptManager.deleteById(String.valueOf(id));
            // close the connection source
            connectionSource.close();
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
