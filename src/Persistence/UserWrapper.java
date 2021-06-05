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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class UserWrapper {
    private static List<User> users = Collections.synchronizedList(new LinkedList<>());


    public boolean add(User user){
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<UserDAO, String> userManager = DaoManager.createDao(connectionSource, UserDAO.class);
            // create an instance of Account
            UserDAO account = new UserDAO(user.getId(),user.getUserName(),user.isRegistered(),user.getAge(),user.getLogged(),user.isSystemManager());
            // persist the account object to the database
            userManager.create(account);
            // close the connection source
            connectionSource.close();
            users.add(user);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void clean()
    {
          users = Collections.synchronizedList(new LinkedList<>());
    }

    public void setLogged(int logged,int userId)
    {
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<UserDAO, String> userManager = DaoManager.createDao(connectionSource, UserDAO.class);
            // create an instance of Account
            userManager.executeRaw("UPDATE Users\n" +
                    "SET logged= "+String.valueOf(logged)+
                    " WHERE Id = "+String.valueOf(userId)+";");
            connectionSource.close();
            User user = getUserById(userId);
            user.setUserLogged(logged);
        }
        catch (Exception e)
        {
        }
    }

    private User getUserById(int userId)
    {
        for(User user : users)
        {
            if(user.getId() == userId)
                return user;
        }
        return null;
    }



    public User get(int id)
    {
        try {
            if(getUserById(id)!=null)
            {
                return getUserById(id);
            }
            ReceiptWrapper receiptWrapper = new ReceiptWrapper();
            BagWrapper bagWrapper = new BagWrapper();
            UserMessagesWrapper userMessagesWrapper  = new UserMessagesWrapper();
            MemberStorePermissionsWrapper memberStorePermissionsWrapper = new MemberStorePermissionsWrapper();

            ConnectionSource connectionSource = connect();
            Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
            UserDAO userDAO = accountDao.queryForId(Integer.toString(id));
            User user = new User(userDAO.getUserName(),userDAO.getAge(),userDAO.getId(),userDAO.getRegistered());
            connectionSource.close();
            user.setLogged(userDAO.isLogged());
            user.setReceipts(receiptWrapper.getByUserId(user.getId()));
            user.setLoginMessages(userMessagesWrapper.getByUserId(user.getId()));
            user.setBags(bagWrapper.getAllUserBags(user.getId()));
            user.setMember(memberStorePermissionsWrapper.getMemberByUserId(id));


            return user;
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
            this.users.remove(getUserById(id));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }


    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }


    public void updateRegistered(String userName,int userId) {
        try {
            ConnectionSource connectionSource = connect();
            // instantiate the dao
            Dao<UserDAO, String> accountDao = DaoManager.createDao(connectionSource, UserDAO.class);
            accountDao.executeRaw("UPDATE Users SET userName='"+userName+"', logged=1, registered=1 WHERE id="+userId);
            // close the connection source
            connectionSource.close();
            getUserById(userId).setUserName(userName);

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

    }
}
