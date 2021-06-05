package Persistence;

import Domain.User;
import Persistence.DAO.AdminTableDAO;
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
import java.time.LocalDate;
import java.util.*;

public class AdminTableWrapper {

    public AdminTableDAO get(LocalDate date)
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<AdminTableDAO, String> AdminManager = DaoManager.createDao(connectionSource, AdminTableDAO.class);
            AdminTableDAO adminTableDAO = AdminManager.queryForId(date.toString());
            connectionSource.close();
            return adminTableDAO;
        }
        catch (Exception e)
        {
            return null;
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


    public void increaseCounter(String counterName) {
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            int currentCounter  = getCurrentCounter(counterName);
            currentCounter++;
            Dao<AdminTableDAO, String> AdminDao = DaoManager.createDao(connectionSource, AdminTableDAO.class);
            AdminDao.executeRaw("UPDATE AdminTable SET "+counterName+" = "+currentCounter+" WHERE Date = '"+java.time.LocalDate.now().toString()+"'");
            // close the connection source
            connectionSource.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

    }

    private int getCurrentCounter(String counterName) {
        AdminTableDAO adminTableDAO = get(java.time.LocalDate.now());
        switch (counterName)
        {
            case "GuestsCounter":
            {
                return adminTableDAO.getGuestsCounter();
            }
            case "NormalUsers":
            {
                return adminTableDAO.getNormalUsers();
            }
            case "Managers":
            {
                return adminTableDAO.getManagers();
            }
            case "Owners":
            {
                return adminTableDAO.getOwners();
            }
        }
        return -1;
    }

    public void add(LocalDate date) {
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<AdminTableDAO, String> adminManager = DaoManager.createDao(connectionSource, AdminTableDAO.class);
            // create an instance of Account
            String now = date.toString();
            AdminTableDAO adminTable = new AdminTableDAO(now,0,0,0,0);
            // persist the account object to the database
            adminManager.create(adminTable);
            // close the connection source
            connectionSource.close();
        }
        catch (Exception e)
        {
            return ;
        }
    }
}
