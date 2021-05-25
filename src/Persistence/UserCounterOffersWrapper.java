package Persistence;

import Domain.Product;
import Domain.PurchaseFormat.PurchaseOffer;
import Persistence.DAO.UserApprovedOffersDAO;
import Persistence.DAO.UsersCounterOffersDAO;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class UserCounterOffersWrapper {
    private Map<Product, PurchaseOffer> value;

    public UserCounterOffersWrapper() {
        this.value = new ConcurrentHashMap<>();
    }

    public void add(int storeId, Product prod, PurchaseOffer purchaseOffer) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UsersCounterOffersDAO, String> usersCounterOffersDao = DaoManager.createDao(connectionSource, UsersCounterOffersDAO.class);
            UsersCounterOffersDAO userCounterOffersDaoObj = new UsersCounterOffersDAO(storeId,purchaseOffer.getUser().getId(), prod.getId(),purchaseOffer.getId());
            usersCounterOffersDao.create(userCounterOffersDaoObj);

            connectionSource.close();
        } catch (Exception e) {
            //TODO add rollback
        }
        this.value.put(prod, purchaseOffer);
    }

    public void remove(int storeId, Product prod, PurchaseOffer po) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UsersCounterOffersDAO, String> usersCounterOffersDao = DaoManager.createDao(connectionSource, UsersCounterOffersDAO.class);
            UsersCounterOffersDAO userCounterOffersDaoObj = new UsersCounterOffersDAO(storeId,po.getUser().getId(), prod.getId(),po.getId());
            usersCounterOffersDao.delete(userCounterOffersDaoObj);
            connectionSource.close();
            this.value.remove(prod);

        } catch (Exception e) {

        }
    }

    public boolean contains(Product product) {
        return this.value.containsKey(product);
    }

    public PurchaseOffer get(Product product) {
        return this.value.get(product);
    }

    public Map<Product, PurchaseOffer> get() {
        return this.value;
    }

    public ConnectionSource connect() throws IOException, SQLException {
        Properties appProps = new Properties();
        InputStream input = API.class.getClassLoader().getResourceAsStream("appConfig.properties");
        if (input != null)
            appProps.load(input);
        else
            throw new FileNotFoundException("Property file was not found.");

        boolean test = appProps.getProperty("forTests").equals("true");
        String url;
        String userName;
        String password;
        if (test) {
            url = appProps.getProperty("localDbURL");
            userName = appProps.getProperty("localDbUserName");
            password = appProps.getProperty("localDbPassword");
        } else {
            url = appProps.getProperty("dbURL");
            userName = appProps.getProperty("dbUsername");
            password = appProps.getProperty("dbPassword");
        }
        return new JdbcConnectionSource(url, userName, password);
    }
}
