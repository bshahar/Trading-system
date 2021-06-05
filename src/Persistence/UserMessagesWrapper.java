package Persistence;

import Persistence.DAO.UserMessagesDAO;
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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class UserMessagesWrapper {

    public boolean add(int userId, int messageId ,String message){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<UserMessagesDAO, String> UserMessagesManager = DaoManager.createDao(connectionSource, UserMessagesDAO.class);
            // create an instance of Account
            UserMessagesDAO userMessagesDAO = new UserMessagesDAO(messageId,message,userId);
            // persist the account object to the database
            UserMessagesManager.create(userMessagesDAO);
            // close the connection source
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Queue<String> getByUserId(int id)
    {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UserMessagesDAO, String> UserMessagesManager = DaoManager.createDao(connectionSource, UserMessagesDAO.class);
            List<UserMessagesDAO> UserMessagesDAOS = UserMessagesManager.queryForEq("userId",String.valueOf(id));
            Queue<String> messages = new ConcurrentLinkedDeque<>();
            for(UserMessagesDAO userMessagesDAO : UserMessagesDAOS)
            {
                messages.add(userMessagesDAO.getMessage());
            }
            connectionSource.close();
            return messages;
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
            Dao<UserMessagesDAO, String> UserMessagesManager = DaoManager.createDao(connectionSource, UserMessagesDAO.class);
            UserMessagesManager.deleteById(String.valueOf(id));
            // close the connection source
            connectionSource.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public void deleteAll(int id) {
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<UserMessagesDAO, String> UserMessagesManager = DaoManager.createDao(connectionSource, UserMessagesDAO.class);
            // create an instance of Account
            UserMessagesManager.executeRaw("DELETE FROM UserMessages WHERE userId = " +String.valueOf(id));
            connectionSource.close();
        }
        catch (Exception e)
        {

        }
    }


    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }


}
