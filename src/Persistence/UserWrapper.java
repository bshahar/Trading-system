package Persistence;

import Domain.User;
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
import java.util.List;
import java.util.Properties;

public class UserWrapper {


    public boolean add(User user){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
            // create an instance of Account
            UserDAO account = new UserDAO(user.getId(),user.getUserName(),user.isRegistered(),user.getAge(),user.getLogged(),user.isSystemManager());
            // persist the account object to the database
            accountDao.create(account);
            // close the connection source
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public User get(int id)
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
            UserDAO account2 = accountDao.queryForId(Integer.toString(id));
            //TODO FILL THE USER ASS
            // close the connection source
            connectionSource.close();
            return new User(account2.getUserName(),account2.getAge(),account2.getId(),account2.getRegistered());
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public List<User> get()
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
            List<UserDAO> users = accountDao.queryForAll();
            //TODO FILL THE USER ASS with for
            // close the connection source
            connectionSource.close();
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public int size()
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
            List<UserDAO> users = accountDao.queryForAll();
            //TODO FILL THE USER ASS with for
            // close the connection source
            connectionSource.close();
            return users.size();
        }
        catch (Exception e)
        {
            return -1;
        }
    }


    public User searchUserByName(String userName)
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
            List<UserDAO> users = accountDao.queryForEq("userName",userName);
            //TODO FILL THE USER ASS
            // close the connection source
            connectionSource.close();
            if(users.size()>0)
            {
              return new User(users.get(0).getUserName(),users.get(0).getAge(),users.get(0).getId(),users.get(0).getRegistered());
            }
            else
                return null;
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
            Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
            accountDao.deleteById(String.valueOf(id));
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