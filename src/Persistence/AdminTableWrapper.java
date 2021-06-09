package Persistence;

import Domain.Result;
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
import org.json.JSONObject;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public Result get(String date)
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<AdminTableDAO, String> AdminManager = DaoManager.createDao(connectionSource, AdminTableDAO.class);
            AdminTableDAO adminTableDAO = AdminManager.queryForId(date);
            connectionSource.close();
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("GuestsCounter",adminTableDAO.getGuestsCounter());
            jsonObject.put("NormalUsersCounter",adminTableDAO.getNormalUsers());
            jsonObject.put("ManagersCounter",adminTableDAO.getOwners());
            jsonObject.put("OwnersCounter",adminTableDAO.getManagers());

            return new Result(true,jsonObject);
        }
        catch (Exception e)
        {
            return new Result(false,"cant get stats for the given date");
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
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("DD/MM/YYYY");
            String now = formatter.format(date);
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

    public void addToStats() {
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<AdminTableDAO, String> adminManager = DaoManager.createDao(connectionSource, AdminTableDAO.class);
            // create an instance of Account

            AdminTableDAO adminTable = new AdminTableDAO("08/06/2021",1,2,3,4);
            // persist the account object to the database
            adminManager.create(adminTable);

            AdminTableDAO adminTable2 = new AdminTableDAO("07/06/2021",4,5,6,7);
            // persist the account object to the database
            adminManager.create(adminTable2);
            // close the connection source
            connectionSource.close();
        }
        catch (Exception e)
        {
            return ;
        }

    }
}
