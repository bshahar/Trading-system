package Persistence;

import Persistence.DAO.ProductCategoriesDAO;
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

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
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
