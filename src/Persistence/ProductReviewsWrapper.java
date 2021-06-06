package Persistence;

import Persistence.DAO.ProductReviewsDAO;
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


public class ProductReviewsWrapper {
    public void add(List<String> reviews,int productId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<ProductReviewsDAO, String> ProductReviewsDAOManager = DaoManager.createDao(connectionSource,ProductReviewsDAO.class);
            for(String review : reviews){
                ProductReviewsDAO productReviewsDAO= new ProductReviewsDAO(productId,review);
                ProductReviewsDAOManager.create(productReviewsDAO);
            }
            connectionSource.close();

        }catch(Exception e){

        }
    }

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }

}
