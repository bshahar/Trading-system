package Persistence;

import Domain.Product;
import Domain.User;
import Persistence.DAO.InventoryDAO;
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

public class InventoryWrapper {

    public void add(Map<Product,Integer> inventory, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<InventoryDAO, String> inventoryDAOManager = DaoManager.createDao(connectionSource,InventoryDAO.class);
            for(Product product: inventory.keySet()){
                InventoryDAO inventoryDAO= new InventoryDAO(storeId, product.getId(),inventory.get(product));
                inventoryDAOManager.create(inventoryDAO);
            }
            connectionSource.close();

        }catch(Exception e){

        }
    }
    public void add(Product product,int amount, int storeId) {
        try{
            ProductWrapper productWrapper =new ProductWrapper();
            productWrapper.add(product);

            ConnectionSource connectionSource = connect();
            Dao<InventoryDAO, String> inventoryDAOManager = DaoManager.createDao(connectionSource,InventoryDAO.class);
            InventoryDAO inventoryDAO= new InventoryDAO(storeId, product.getId(),amount);
            inventoryDAOManager.create(inventoryDAO);
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

    public List<Product> getAllProducts(int storeId) {
        try{
            ProductWrapper productWrapper= new ProductWrapper();
            ConnectionSource connectionSource = connect();
            Dao<InventoryDAO, String> inventoryDAOManager = DaoManager.createDao(connectionSource,InventoryDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<InventoryDAO> inventoryDAOS = inventoryDAOManager.queryForFieldValues(map);
            connectionSource.close();
            List<Product> out= new LinkedList<>();
            for(InventoryDAO inventoryDAO : inventoryDAOS){
                out.add(productWrapper.getById(inventoryDAO.getProductId()));
            }
            return out;
        }catch(Exception e){
            return null;
        }
    }

    public Map<Integer, Integer> getAllProductsAmount(int storeId) {
        try{
            ProductWrapper productWrapper= new ProductWrapper();
            ConnectionSource connectionSource = connect();
            Dao<InventoryDAO, String> inventoryDAOManager = DaoManager.createDao(connectionSource,InventoryDAO.class);
            Map<String,Object> map= new HashMap<>();
            map.put("storeId",storeId);
            List<InventoryDAO> inventoryDAOS = inventoryDAOManager.queryForFieldValues(map);
            Map<Integer,Integer> out= new HashMap<>();
            for(InventoryDAO inventoryDAO : inventoryDAOS){
                out.put(inventoryDAO.getProductId(),inventoryDAO.getAmount());
            }
            connectionSource.close();
            return out;
        }catch(Exception e){
            return null;
        }      }

    public void updateAmount(int prodId, int amount, int storeId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<InventoryDAO, String> inventoryDAOManager = DaoManager.createDao(connectionSource,InventoryDAO.class);
            inventoryDAOManager.executeRaw("UPDATE Inventory SET amount="+amount+" WHERE storeId="+storeId+" AND productId="+prodId);
            connectionSource.close();

        }catch(Exception e){

        }

    }

    public void remove(Product p, int storeId,int amount) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<InventoryDAO, String> inventoryDAOManager = DaoManager.createDao(connectionSource,InventoryDAO.class);
            InventoryDAO inventoryDAO= new InventoryDAO(storeId, p.getId(),amount);
            inventoryDAOManager.executeRaw("DELETE FROM Inventory WHERE storeId="+storeId+" AND productId="+p.getId());
            connectionSource.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public int getAmount(int storeId, int productId) {
        try{
            ConnectionSource connectionSource = connect();
            Dao<InventoryDAO, String> inventoryDAOManager = DaoManager.createDao(connectionSource,InventoryDAO.class);
            Map<String,Object> map=new HashMap<>();
            map.put("storeId",storeId);
            map.put("productId",productId);
            List<InventoryDAO> inventoryDAO= inventoryDAOManager.queryForFieldValues(map);
            connectionSource.close();
            return inventoryDAO.get(0).getAmount();

        }catch(Exception e){
            return -1;
        }    }
}
