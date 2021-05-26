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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ProductWrapper {

    private static List<Product> products = Collections.synchronizedList(new LinkedList<>());


    public boolean add(Product product){
        try {
            //Product
            ConnectionSource connectionSource = connect();
            Dao<ProductDAO, String> productDAOManager = DaoManager.createDao(connectionSource,ProductDAO.class);
            ProductDAO productDAO= new ProductDAO(product.getId(),product.getName(),product.getPrice(),product.getRate()
                    ,product.getRateCount(),product.getDescription(),product.getAmount(),product.getStoreId());
            productDAOManager.create(productDAO);
            connectionSource.close();

            //Reviews
            ProductReviewsWrapper productReviewsWrapper= new ProductReviewsWrapper();
            productReviewsWrapper.add(product.getReviews(),product.getId());

            //Categories
            ProductCategoryWrapper productCategoryWrapper= new ProductCategoryWrapper();
            productCategoryWrapper.add(product.getCategories(),product.getId());
            products.add(product);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Product getById(int productId) {
        try {
            if(getProductById(productId)!=null)
                return getProductById(productId);
            ConnectionSource connectionSource = connect();
            Dao<ProductDAO, String> ProductDAOManager = DaoManager.createDao(connectionSource, ProductDAO.class);
            ProductDAO productDAO = ProductDAOManager.queryForId(Integer.toString(productId));
            Product product = new Product(productDAO.getId(), productDAO.getName(), productDAO.getPrice(),
                    productDAO.getDescription(), productDAO.getStoreId(), productDAO.getRatesCount(), productDAO.getRate());
            connectionSource.close();
            ProductCategoryWrapper productCategoryWrapper= new ProductCategoryWrapper();
            product.setCategories(productCategoryWrapper.getCategories(product.getId()));
            return product;
        } catch (Exception e) {
            return null;
        }
    }

    public Product getProductById(int productId)
    {
        for(Product product:products)
        {
            if(product.getId() == productId)
                return product;
        }
        return null;
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


    public void clean()
    {
        products = Collections.synchronizedList(new LinkedList<>());
    }

}
