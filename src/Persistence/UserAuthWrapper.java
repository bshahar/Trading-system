package Persistence;

import Persistence.DAO.StoreDAO;
import Persistence.DAO.UserAuthDAO;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class UserAuthWrapper {

    public boolean add(String userName, String password){
        try {
            //Stores
            ConnectionSource connectionSource = connect();
            Dao<UserAuthDAO, String> UserAuthDAOManager = DaoManager.createDao(connectionSource,UserAuthDAO.class);
            UserAuthDAO userAuthDAO= new UserAuthDAO(userName,password);
            UserAuthDAOManager.create(userAuthDAO);
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

    public boolean containsKey(String userName) {
        try {
            //Stores
            ConnectionSource connectionSource = connect();
            Dao<UserAuthDAO, String> UserAuthDAOManager = DaoManager.createDao(connectionSource,UserAuthDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("userName",userName);
            List<UserAuthDAO> userAuthDAO= UserAuthDAOManager.queryForFieldValues(map);
            connectionSource.close();
            return userAuthDAO.size()==1;
        }
        catch (Exception e)
        {
            return true;
        }
    }

    public String get(String userName) {
        try {
            //Stores
            ConnectionSource connectionSource = connect();
            Dao<UserAuthDAO, String> UserAuthDAOManager = DaoManager.createDao(connectionSource,UserAuthDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("userName",userName);
            List<UserAuthDAO> userAuthDAO= UserAuthDAOManager.queryForFieldValues(map);
            connectionSource.close();
            return userAuthDAO.get(0).getPassword();
        }
        catch (Exception e)
        {
            return "";
        }
    }
}
