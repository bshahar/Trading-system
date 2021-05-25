package Persistence;

import Domain.User;
import Persistence.DAO.StoreEmployeesDAO;
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

public class StoreOwnerWrapper {

    public void add(List<User> users, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreOwnerDAO, String> storeOwnerDAOManager = DaoManager.createDao(connectionSource,StoreOwnerDAO.class);
            for(User user : users){
                StoreOwnerDAO storeOwnerDAO= new StoreOwnerDAO(storeId,user.getId());
                storeOwnerDAOManager.create(storeOwnerDAO);
            }
            connectionSource.close();

        }catch(Exception e){

        }
    }

    public void add(User user, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreOwnerDAO, String> storeOwnerDAOManager = DaoManager.createDao(connectionSource,StoreOwnerDAO.class);
            StoreOwnerDAO storeOwnerDAO= new StoreOwnerDAO(storeId,user.getId());
            storeOwnerDAOManager.create(storeOwnerDAO);
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

    public List<User> getAll(int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreOwnerDAO, String> StoreOwnerDAO = DaoManager.createDao(connectionSource, StoreOwnerDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<StoreOwnerDAO> StoreOwnerDAOs= StoreOwnerDAO.queryForFieldValues(map);
            List<User> users= new LinkedList<>();
            UserWrapper userWrapper= new UserWrapper();
            for(StoreOwnerDAO storeOwnerDAO: StoreOwnerDAOs){
                users.add(userWrapper.get(storeOwnerDAO.getUserId()));
            }
            return users;
        }catch (Exception e){
            return new LinkedList<>();
        }    }

    public boolean contains(User user, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreOwnerDAO, String> StoreOwnerDAO = DaoManager.createDao(connectionSource, StoreOwnerDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<StoreOwnerDAO> StoreOwnerDAOs= StoreOwnerDAO.queryForFieldValues(map);
            for(StoreOwnerDAO storeOwnerDAO : StoreOwnerDAOs){
                if(storeOwnerDAO.getUserId()== user.getId()){
                    return true;
                }
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

    public boolean remove(User user,int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreOwnerDAO, String> StoreOwnerDAOManager = DaoManager.createDao(connectionSource, StoreOwnerDAO.class);
            int out=StoreOwnerDAOManager.delete(new StoreOwnerDAO(storeId,user.getId()));
            connectionSource.close();
            return out==1;
        }catch (Exception e){
            return false;
        }
    }
}
