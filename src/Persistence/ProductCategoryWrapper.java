package Persistence;

import Domain.User;
import Persistence.DAO.ProductCategoriesDAO;
import Persistence.DAO.ProductReviewsDAO;
import Persistence.DAO.StoreEmployeesDAO;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import sun.awt.image.ImageWatched;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;


public class ProductCategoryWrapper {
    public void add(List<String> reviews,int productId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<ProductCategoriesDAO, String> ProductCategoriesDAOManager = DaoManager.createDao(connectionSource,ProductCategoriesDAO.class);
            for(String review : reviews){
                ProductCategoriesDAO productCategoriesDAO= new ProductCategoriesDAO(productId,review);
                ProductCategoriesDAOManager.create(productCategoriesDAO);
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

    public List<String> getCategories(int id) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<ProductCategoriesDAO, String> ProductCategoriesDAOManager = DaoManager.createDao(connectionSource,ProductCategoriesDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("productId",id);
            List<ProductCategoriesDAO> categoriesDAOS= ProductCategoriesDAOManager.queryForFieldValues(map);
            connectionSource.close();
            List<String> out= new LinkedList<>();
            for(ProductCategoriesDAO productCategoriesDAO : categoriesDAOS){
                out.add(productCategoriesDAO.getCategory());
            }
            return out;

        }catch(Exception e){
            return new LinkedList<>();
        }
    }
}
