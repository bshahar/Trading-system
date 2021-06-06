package Persistence;

import Domain.Product;
import Domain.PurchaseFormat.PurchaseOffer;
import Domain.Store;
import Persistence.DAO.PurchaseOffersDAO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class UserCounterOffersWrapper {
    private Map<Product, PurchaseOffer> value;
    private UserWrapper userWrapper;

    public UserCounterOffersWrapper() {
        this.value = new ConcurrentHashMap<>();
            this.userWrapper = new UserWrapper();
    }

    public void add(int storeId, Product prod, PurchaseOffer purchaseOffer) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UsersCounterOffersDAO, String> usersCounterOffersDao = DaoManager.createDao(connectionSource, UsersCounterOffersDAO.class);
            UsersCounterOffersDAO userCounterOffersDaoObj = new UsersCounterOffersDAO(storeId,purchaseOffer.getUser().getId(), prod.getId(),purchaseOffer.getId());
            usersCounterOffersDao.create(userCounterOffersDaoObj);

            connectionSource.close();
        } catch (Exception e) {
        }
        this.value.put(prod, purchaseOffer);
    }

    public void remove(int storeId, Product prod, PurchaseOffer po) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<UsersCounterOffersDAO, String> usersCounterOffersDao = DaoManager.createDao(connectionSource, UsersCounterOffersDAO.class);
            UsersCounterOffersDAO userCounterOffersDaoObj = new UsersCounterOffersDAO(storeId,po.getUser().getId(), prod.getId(),po.getId());
            usersCounterOffersDao.executeRaw("DELETE FROM UsersCounterOffers WHERE storeId = " + storeId + " AND userId = " + po.getUser().getId() + " AND productId = " + prod.getId() + " ;");
            Dao<PurchaseOffersDAO, String> purchaseOffersDAOS = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
            usersCounterOffersDao.executeRaw("DELETE FROM PurchaseOffers WHERE id = "+po.getId()  +" ;");
            connectionSource.close();
            this.value.remove(prod);

        } catch (Exception e) {

        }
    }

    public boolean contains(Product product) {
        return this.value.containsKey(product);
    }

    public PurchaseOffer get(Store store, int userId, Product product) {
        this.value = get(store, userId);
        for (Product p:value.keySet()) {
            if (p.getId() == product.getId())
                return this.value.get(p);
        }
       return null;
    }

    public Map<Product, PurchaseOffer> get(Store store, int userId) {
        if(this.value == null || this.value.isEmpty()){
            try {
                ConnectionSource connectionSource = connect();
                Dao<UsersCounterOffersDAO, String> userCounterOffersDAOManager =  DaoManager.createDao(connectionSource, UsersCounterOffersDAO.class);
                Map<String, Object> fields = new HashMap<>();
                fields.put("storeId", store.getStoreId());
                fields.put("userId", userId);
                List<UsersCounterOffersDAO> userCounterOffersDAOS = userCounterOffersDAOManager.queryForFieldValues(fields);

                for (UsersCounterOffersDAO userCounterOffers: userCounterOffersDAOS) {
                    Dao<PurchaseOffersDAO, String> accountDao = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
                    PurchaseOffersDAO pod = accountDao.queryForId(Integer.toString(userCounterOffers.getOfferId()));
                    PurchaseOffer pod2 = new PurchaseOffer(pod.getId(), pod.getPriceOfOffer(), pod.getQuantity(),userWrapper.get(pod.getUserId()));
                    this.value.put(store.getProductById(userCounterOffers.getProductId()),pod2 );
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
