package Persistence;

import Domain.User;
import Persistence.DAO.AppointmentsDAO;
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

public class AppointmentsWrapper {

    public void add(Map<User,List<User>> appointments, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<AppointmentsDAO, String> appointmentsDAOManager = DaoManager.createDao(connectionSource,AppointmentsDAO.class);
            for(User user1 : appointments.keySet()){
                List<User> users= new LinkedList<>();
                for(User user2 : users){
                    AppointmentsDAO appointmentsDAO= new AppointmentsDAO(storeId,user1.getId(),user2.getId());
                    appointmentsDAOManager.create(appointmentsDAO);

                }
            }
            connectionSource.close();

        }catch(Exception e){

        }
    }

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }

    public boolean removeAppointment(int storeId, int ownerId, int managerId) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<AppointmentsDAO, String> appointmentsDAOManager = DaoManager.createDao(connectionSource, AppointmentsDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            map.put("managerId",ownerId);
            map.put("appointedId",managerId);
            List<AppointmentsDAO> appointmentsDAO= appointmentsDAOManager.queryForFieldValues(map);
            if(appointmentsDAO.size()==1){
                appointmentsDAOManager.executeRaw("DELETE FROM Appointments WHERE storeId="+storeId+"" +
                        " AND managerId="+ownerId+" AND appointedId="+managerId);
                connectionSource.close();
                return true;
            }else{
                connectionSource.close();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<User> get(int storeId, User manager) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<AppointmentsDAO, String> appointmentsDAOManager = DaoManager.createDao(connectionSource, AppointmentsDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            map.put("managerId",manager.getId());
            List<AppointmentsDAO> appointmentsDAOS= appointmentsDAOManager.queryForFieldValues(map);
            List<User> users= new LinkedList<>();
            UserWrapper userWrapper= new UserWrapper();
            for(AppointmentsDAO appointmentsDAO: appointmentsDAOS){
                users.add(userWrapper.get(appointmentsDAO.getAppointedId()));
            }
            connectionSource.close();
            return users;
        }catch (Exception e){
            return new LinkedList<>();
        }

    }

    public void add(User owner, User user, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<AppointmentsDAO, String> appointmentsDAOManager = DaoManager.createDao(connectionSource,AppointmentsDAO.class);
            AppointmentsDAO appointmentsDAO= new AppointmentsDAO(storeId, owner.getId(),user.getId());
            appointmentsDAOManager.create(appointmentsDAO);
            connectionSource.close();
        }catch(Exception e){

        }

    }

    public Map<User, List<User>> getAll(int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<AppointmentsDAO, String> AppointmentsDAOManager = DaoManager.createDao(connectionSource,AppointmentsDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<AppointmentsDAO> AppointmentsDAOs= AppointmentsDAOManager.queryForFieldValues(map);
            Map<User,List<User>> appointments= new HashMap<>();
            UserWrapper userWrapper= new UserWrapper();
            for(AppointmentsDAO appointmentsDAO : AppointmentsDAOs){
                User manager= userWrapper.get(appointmentsDAO.getManagerId());
                User appointed= userWrapper.get(appointmentsDAO.getAppointedId());
                if (!appointments.containsKey(manager)) {
                    appointments.put(manager, new LinkedList<>());
                }
                appointments.get(manager).add(appointed);
            }
            connectionSource.close();
            return appointments;
        }catch (Exception e){
            return new HashMap<>();
        }
    }
}
