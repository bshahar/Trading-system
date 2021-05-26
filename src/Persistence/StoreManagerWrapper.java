package Persistence;

import Domain.User;
import Persistence.DAO.AppointmentsDAO;
import Persistence.DAO.StoreManagerDAO;
import Persistence.DAO.StoreOwnerDAO;
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

public class StoreManagerWrapper {

    public void add(List<User> users, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreManagerDAO, String> storeManagerDAOManager = DaoManager.createDao(connectionSource,StoreManagerDAO.class);
            for(User user : users){
                StoreManagerDAO storeManagerDAO= new StoreManagerDAO(storeId,user.getId());
                storeManagerDAOManager.create(storeManagerDAO);
            }
            connectionSource.close();

        }catch(Exception e){

        }
    }

    public void add(User user, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreManagerDAO, String> storeManagerDAOManager = DaoManager.createDao(connectionSource,StoreManagerDAO.class);
            StoreManagerDAO storeManagerDAO= new StoreManagerDAO(storeId,user.getId());
            storeManagerDAOManager.create(storeManagerDAO);
            connectionSource.close();

        }catch(Exception e){

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

    public boolean remove(int storeId,User user) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreManagerDAO, String> appointmentsDAOManager = DaoManager.createDao(connectionSource, StoreManagerDAO.class);
            appointmentsDAOManager.executeRaw("DELETE FROM StoreManagers WHERE storeId="+storeId+" AND userId="+user.getId());
            connectionSource.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<User> getAll(int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreManagerDAO, String> StoreManagerDAO = DaoManager.createDao(connectionSource, StoreManagerDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<StoreManagerDAO> StoreManagerDAOs= StoreManagerDAO.queryForFieldValues(map);
            List<User> users= new LinkedList<>();
            UserWrapper userWrapper= new UserWrapper();
            for(StoreManagerDAO storeManagerDAO:StoreManagerDAOs){
                users.add(userWrapper.get(storeManagerDAO.getUserId()));
            }
            connectionSource.close();
            return users;
        }catch (Exception e){
            return new LinkedList<>();
        }
    }

    public boolean contains(User user, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreManagerDAO, String> storeManagerDAOManager = DaoManager.createDao(connectionSource, StoreManagerDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<StoreManagerDAO> StoreManagerDAOs= storeManagerDAOManager.queryForFieldValues(map);
            for(StoreManagerDAO storeManagerDAO : StoreManagerDAOs){
                if(storeManagerDAO.getUserId()== user.getId()){
                    connectionSource.close();
                    return true;
                }
            }
            connectionSource.close();
            return false;
        }catch(Exception e){
            return false;
        }

    }
}
