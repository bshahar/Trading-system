package Persistence;

import Domain.Product;
import Domain.Store;
import Domain.User;
import Persistence.DAO.ProductDAO;
import Persistence.DAO.StoreDAO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ProductWrapper {



    public boolean add(Product product){
        try {
            //Product
            ConnectionSource connectionSource = connect();
            Dao<ProductDAO, String> productDAOManager = DaoManager.createDao(connectionSource,ProductDAO.class);
            ProductDAO productDAO= new ProductDAO(product.getId(),product.getName(),product.getPrice(),product.getRate()
                    ,product.getRateCount(),product.getDescription(),product.getAmount(),product.getStoreId());
            productDAOManager.create(productDAO);
            connectionSource.close();

            //Categories

            //reviews


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
