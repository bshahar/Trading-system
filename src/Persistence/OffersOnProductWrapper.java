package Persistence;

import Domain.Policy;
import Domain.Product;
import Domain.PurchaseFormat.ImmediatePurchase;
import Domain.PurchaseFormat.PurchaseOffer;
import Domain.PurchasePolicies.AgeLimitPolicy;
import Domain.PurchasePolicies.MaxAmountPolicy;
import Domain.PurchasePolicies.MinAmountPolicy;
import Domain.Store;
import Domain.User;
import Persistence.DAO.*;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.CloseableWrappedIterable;
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
import java.util.concurrent.ConcurrentHashMap;

public class OffersOnProductWrapper {

    private Map<Product, LinkedList<PurchaseOffer>> value;

    public OffersOnProductWrapper() {
        this.value = new ConcurrentHashMap<>();
    }

    public void add(int storeId, Product prod, PurchaseOffer purchaseOffer) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<PurchaseOffersDAO, String> purchaseOfferDao = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
            PurchaseOffersDAO purchaseOfferDAOObj = new PurchaseOffersDAO(purchaseOffer.getId(), purchaseOffer.getPriceOfOffer(),purchaseOffer.getUser().getId(),purchaseOffer.getNumOfProd());
            purchaseOfferDao.create(purchaseOfferDAOObj);

            Dao<ProductOffersDAO, String> productOffersDAO = DaoManager.createDao(connectionSource, ProductOffersDAO.class);
            ProductOffersDAO productOfferDaoObj = new ProductOffersDAO(storeId, purchaseOffer.getId(), prod.getId());
            productOffersDAO.create(productOfferDaoObj);

            connectionSource.close();
        } catch (Exception e) {
            //TODO add rollback
        }
        if(this.value.containsKey(prod)) {
            LinkedList<PurchaseOffer> offers = this.value.get(prod);
            offers.add(purchaseOffer);
            this.value.put(prod, offers); //Overwrite old entry in map
        }
        else{
            LinkedList<PurchaseOffer> offers = new LinkedList<PurchaseOffer>();
            offers.add(purchaseOffer);
            this.value.put(prod, offers); //Overwrite old entry in map
        }
    }

    public void remove(Product prod, PurchaseOffer po) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<PurchaseOffersDAO, String> purchaseOffersDAO = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
            purchaseOffersDAO.deleteById(String.valueOf(po.getId()));
            connectionSource.close();
            LinkedList<PurchaseOffer> offers = this.value.get(prod);
            if(offers.size() == 1){
                this.value.remove(prod);
            }
            else{
                offers.remove(po);
                this.value.put(prod, offers); //Overwrite old entry in map
            }

        } catch (Exception e) {

        }
    }

    /*public boolean contains(Product product) {
        return this.value.containsKey(product);
    }*/

    public LinkedList<PurchaseOffer> get(Product product) {
        return this.value.get(product);
    }

    public Map<Product, LinkedList<PurchaseOffer>> get(Store store) {
       /* if(this.value == null || this.value.isEmpty()){
            try {
                ConnectionSource connectionSource = connect();
                Dao<ProductOffersDAO, String> productOffersDAO = DaoManager.createDao(connectionSource, ProductOffersDAO.class);
                CloseableWrappedIterable<ProductOffersDAO> itr = productOffersDAO.getWrappedIterable();
                while (itr.iterator().hasNext()){
                    ProductOffersDAO next = itr.iterator().next();
                    if(next.getStoreId() == store.getStoreId() ){
                        Dao<PurchaseOffersDAO, String> accountDao = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
                        PurchaseOffersDAO pod = accountDao.queryForId(Integer.toString(next.getOfferId()));
                        PurchaseOffer pod2 = new PurchaseOffer(pod.getId(), pod.getPriceOfOffer(), pod.getQuantity(),pod.getUserId());
                        this.value.put(store.getProductById(next.getProductId()),pod2);
                    }
                }

            }
            catch (Exception e){

            }
        }
        else*/
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
