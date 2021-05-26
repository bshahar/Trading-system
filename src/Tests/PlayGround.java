package Tests;

import Domain.User;
import Persistence.DAO.CounterDAO;
import Persistence.DAO.StoreDAO;
import Persistence.DAO.UserDAO;
import Persistence.UserWrapper;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.w3c.dom.css.Counter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class PlayGround {
    public static void main(String args[]) throws IOException, SQLException {
//       ConnectionSource connectionSource = connect();
//
//        Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
//        accountDao.executeRaw("DELETE FROM Users");
        //TableUtils.clearTable(connectionSource, UserDAO.class);

//
//        UserWrapper u = new UserWrapper();
//        u.add(new User("Elad",1,111,true));
//        u.get(111);
//        System.out.println("eeeefdfsfd");
//        u.get(111);

        ConnectionSource connectionSource = connect();
        Dao<CounterDAO, String> storeDAOManager = DaoManager.createDao(connectionSource,CounterDAO.class);
        CounterDAO storeDAO= new CounterDAO(1,0,0,0,0,0,0,0,0,0,0);
        storeDAOManager.create(storeDAO);
        connectionSource.close();


//        connectionSource.close();
       // API.initTradingSystem();
        //API.register("elad","sol",22);
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
