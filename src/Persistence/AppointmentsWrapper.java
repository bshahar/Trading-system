package Persistence;

import Domain.User;
import Persistence.DAO.AppointmentsDAO;
import Persistence.DAO.StoreManagerDAO;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
