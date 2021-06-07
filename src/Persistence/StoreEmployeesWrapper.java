package Persistence;

import Domain.User;
import Persistence.DAO.StoreEmployeesDAO;
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


public class StoreEmployeesWrapper {

    public void add(User user,int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreEmployeesDAO, String> storeEmployeesDAOManager = DaoManager.createDao(connectionSource,StoreEmployeesDAO.class);
            StoreEmployeesDAO storeEmployeesDAO= new StoreEmployeesDAO(storeId,user.getId());
            storeEmployeesDAOManager.create(storeEmployeesDAO);

            connectionSource.close();

        }catch(Exception e){

        }
    }

    public void add(List<User> users,int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreEmployeesDAO, String> storeEmployeesDAOManager = DaoManager.createDao(connectionSource,StoreEmployeesDAO.class);
            for(User user : users){
                StoreEmployeesDAO storeEmployeesDAO= new StoreEmployeesDAO(storeId,user.getId());
                storeEmployeesDAOManager.create(storeEmployeesDAO);
            }
            connectionSource.close();

        }catch(Exception e){

        }
    }

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }

    public void remove(int storeId, User manager) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<StoreEmployeesDAO, String> StoreEmployeesDAO = DaoManager.createDao(connectionSource, StoreEmployeesDAO.class);
            StoreEmployeesDAO.executeRaw("DELETE FROM StoreEmployees WHERE storeId="+storeId+" AND userId="+manager.getId());
            connectionSource.close();
//            return out==1;
        } catch (Exception e) {
//            return false;
        }

    }

    public List<User> getAll(int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<StoreEmployeesDAO, String> StoreEmployeesDAO = DaoManager.createDao(connectionSource, StoreEmployeesDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<StoreEmployeesDAO> storeEmployeesDAOs= StoreEmployeesDAO.queryForFieldValues(map);
            List<User> users= new LinkedList<>();
            UserWrapper userWrapper= new UserWrapper();
            for(StoreEmployeesDAO storeEmployeesDAO: storeEmployeesDAOs){
                users.add(userWrapper.get(storeEmployeesDAO.getUserId()));
            }
            connectionSource.close();
            return users;
        }catch (Exception e){
            return new LinkedList<>();
        }
    }
}
