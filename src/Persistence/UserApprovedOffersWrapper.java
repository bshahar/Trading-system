package Persistence;

import Domain.Product;
import Domain.Store;
import Persistence.DAO.UserApprovedOffersDAO;
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
import java.util.concurrent.ConcurrentHashMap;

public class UserApprovedOffersWrapper {
    private Map<Product, Double> value;

    public UserApprovedOffersWrapper() {
        this.value = new ConcurrentHashMap<>();
    }

    public void add(int storeId, int userId, Product prod, double purchaseOffer) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UserApprovedOffersDAO, String> userApprovedOffersDao = DaoManager.createDao(connectionSource, UserApprovedOffersDAO.class);
            UserApprovedOffersDAO userApprovedOffersDaoObj = new UserApprovedOffersDAO(userId,storeId, prod.getId(),purchaseOffer);
            userApprovedOffersDao.create(userApprovedOffersDaoObj);

            connectionSource.close();
        } catch (Exception e) {
            //TODO add rollback
        }
       this.value.put(prod, purchaseOffer);
    }

    public void remove(int storeId, Product prod, int userId, double priceOffer) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UserApprovedOffersDAO, String> userApprovedOffersDao = DaoManager.createDao(connectionSource, UserApprovedOffersDAO.class);
            UserApprovedOffersDAO userApprovedOffersDaoObj = new UserApprovedOffersDAO(userId,storeId, prod.getId(),priceOffer);
            userApprovedOffersDao.executeRaw("DELETE FROM UserApprovedOffers WHERE userId = " + userId + " AND storeId = " + storeId + " AND productId = " + prod.getId() + " ;");

            userApprovedOffersDao.delete(userApprovedOffersDaoObj);
            connectionSource.close();
            this.value.remove(prod);

        } catch (Exception e) {

        }
    }

    public boolean contains(Product product) {
        return this.value.containsKey(product);
    }

    public Double get(Store store, int userId, Product product) {
        this.value = get(store, userId);
        return this.value.get(product);
    }

    public Map<Product, Double> get(Store store, int userId) {
        if(this.value == null || this.value.isEmpty()){
            try {
                ConnectionSource connectionSource = connect();
                Dao<UserApprovedOffersDAO, String> userApprovedOffersDAOManager =  DaoManager.createDao(connectionSource, UserApprovedOffersDAO.class);
                Map<String, Object> fields = new HashMap<>();
                fields.put("storeId", store.getStoreId());
                fields.put("userId", userId);
                List<UserApprovedOffersDAO> userApprovedOffersDAOS = userApprovedOffersDAOManager.queryForFieldValues(fields);

                for (UserApprovedOffersDAO userApprovedOffers: userApprovedOffersDAOS) {
                    this.value.put(store.getProductById(userApprovedOffers.getProductId()),userApprovedOffers.getOfferPrice() );
                }
                connectionSource.close();
                return this.value;

            }
            catch (Exception e){
                return this.value;
            }
        }
        else
            return this.value;
    }

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }
}
