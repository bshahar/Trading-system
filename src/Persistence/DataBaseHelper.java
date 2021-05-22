package Persistence;

import Domain.User;
import Persistence.DAO.BagProductAmountDAO;
import Persistence.DAO.MemberStorePermissionsDAO;
import Persistence.DAO.ReceiptDAO;
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
import java.util.Properties;

public class DataBaseHelper {

    public static void cleanAllTable() {
        try {

            ConnectionSource connectionSource = connect();

            Dao<UserDAO, String> userManager = DaoManager.createDao(connectionSource, UserDAO.class);
            userManager.executeRaw("DELETE FROM Users");

            Dao<BagProductAmountDAO, String> bagManger = DaoManager.createDao(connectionSource, BagProductAmountDAO.class);
            bagManger.executeRaw("DELETE FROM BagProductAmount");

            Dao<MemberStorePermissionsDAO, String> MemberStorePermissionsManager = DaoManager.createDao(connectionSource, MemberStorePermissionsDAO.class);
            MemberStorePermissionsManager.executeRaw("DELETE FROM MemberStorePermissions");

            Dao<ReceiptDAO, String> receiptManager = DaoManager.createDao(connectionSource, ReceiptDAO.class);
            receiptManager.executeRaw("DELETE FROM Receipts");

            connectionSource.close();
        } catch (Exception e)
        {

        }
    }

    public static ConnectionSource connect() throws IOException, SQLException {
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
