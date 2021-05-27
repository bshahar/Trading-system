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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ReceiptLinesWrapper {

    public boolean add(ReceiptLine receiptLine){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<ReceiptLinesDAO, String> receiptLineManager = DaoManager.createDao(connectionSource, ReceiptLinesDAO.class);
            // create an instance of Account
            ReceiptLinesDAO receiptLineDAO = new ReceiptLinesDAO(receiptLine.getReceiptId(),receiptLine.getProdId(),receiptLine.getProdName(),receiptLine.getPrice(),receiptLine.getAmount());
            // persist the account object to the database
            receiptLineManager.create(receiptLineDAO);
            // close the connection source
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public List<ReceiptLine> get()
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ReceiptLinesDAO, String> ReceiptLineDaoManager = DaoManager.createDao(connectionSource, ReceiptLinesDAO.class);
            List<ReceiptLinesDAO> receiptLineDaos = ReceiptLineDaoManager.queryForAll();
            List<ReceiptLine> result = new LinkedList<>();
            for(ReceiptLinesDAO rc : receiptLineDaos)
            {
                result.add(new ReceiptLine(rc.getProdName(),rc.getPrice(),rc.getAmount(),rc.getReceiptId(),rc.getProductId()));
            }
            connectionSource.close();
            return result;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public boolean delete(int id1,int id2){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<ReceiptLinesDAO, String> ReceiptLineDaoManager = DaoManager.createDao(connectionSource, ReceiptLinesDAO.class);
            List<String> ids = new LinkedList<>();
            ids.add(String.valueOf(id1));
            ids.add(String.valueOf(id2));
            ReceiptLineDaoManager.deleteIds(ids);
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

    public List<ReceiptLine> getByReceiptId(int id) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ReceiptLinesDAO, String> ReceiptLineDaoManager = DaoManager.createDao(connectionSource, ReceiptLinesDAO.class);
            List<ReceiptLinesDAO> receiptLineDaos = ReceiptLineDaoManager.queryForAll();
            List<ReceiptLine> result = new LinkedList<>();
            for(ReceiptLinesDAO rc : receiptLineDaos)
            {
                if(rc.getReceiptId()==id)
                    result.add(new ReceiptLine(rc.getProdName(),rc.getPrice(),rc.getAmount(),rc.getReceiptId(),rc.getProductId()));
            }
            connectionSource.close();
            return result;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
