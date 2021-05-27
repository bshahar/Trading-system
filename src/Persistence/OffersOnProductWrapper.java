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
import org.omg.DynamicAny.DynAnyOperations;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OffersOnProductWrapper {

    private Map<Product, LinkedList<PurchaseOffer>> value;
    private UserWrapper userWrapper;

    public OffersOnProductWrapper() {
        this.value = new ConcurrentHashMap<>();
        this.userWrapper = new UserWrapper();
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

    public void remove(Store store, Product prod, PurchaseOffer po) {
        try {
            ConnectionSource connectionSource = connect();
            Dao<ProductOffersDAO, String> productOffersDAO = DaoManager.createDao(connectionSource, ProductOffersDAO.class);
            ProductOffersDAO productOffersDaoObj = new ProductOffersDAO(store.getStoreId(),po.getId(),prod.getId());
            productOffersDAO.executeRaw("DELETE FROM ProductOffers WHERE storeId = " + store.getStoreId() + " AND offerId = " + po.getId() + " AND productId = " + prod.getId() + " ;");
            connectionSource.close();
            this.value = get(store);

            /*ConnectionSource connectionSource = connect();
            Dao<PurchaseOffersDAO, String> purchaseOffersDAO = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
            purchaseOffersDAO.deleteById(String.valueOf(po.getId()));
            connectionSource.close();
            LinkedList<PurchaseOffer> offers = this.value.get(prod);
            if(offers != null && offers.size() == 1){
                this.value.remove(prod);
            }
            else{
                offers.remove(po);
                this.value.put(prod, offers); //Overwrite old entry in map
            }
*/
        } catch (Exception e) {
System.out.println(e);
        }
    }

    /*public boolean contains(Product product) {
        return this.value.containsKey(product);
    }*/

    public LinkedList<PurchaseOffer> get(Store store, Product product) {
        this.value = get(store);
        for (Product p: this.value.keySet()) {
            if(p.getId() == product.getId())
                return this.value.get(p);
        }
        return null;
    }

    public Map<Product, LinkedList<PurchaseOffer>> get(Store store) {
       if(this.value == null || this.value.isEmpty()){
            try {
                ConnectionSource connectionSource = connect();
                Dao<ProductOffersDAO, String> productOffersDAOManager =  DaoManager.createDao(connectionSource, ProductOffersDAO.class);
                Map<String, Object> fields = new HashMap<>();
                fields.put("storeId", store.getStoreId());
                List<ProductOffersDAO> productOffersDAOS = productOffersDAOManager.queryForFieldValues(fields);

                for (ProductOffersDAO prodOffer: productOffersDAOS) {
                    Dao<PurchaseOffersDAO, String> accountDao = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
                    PurchaseOffersDAO pod = accountDao.queryForId(Integer.toString(prodOffer.getOfferId()));
                    PurchaseOffer pod2 = new PurchaseOffer(pod.getId(), pod.getPriceOfOffer(), pod.getQuantity(),userWrapper.get(pod.getUserId()));
                    LinkedList<PurchaseOffer> offers = null;
                    if (this.value.containsKey(store.getProductById(prodOffer.getProductId()))) {
                        offers = this.value.get(store.getProductById(prodOffer.getProductId()));
                        offers.add(pod2);
                        this.value.put(store.getProductById(prodOffer.getProductId()), offers);
                    }
                    else{
                        offers = new LinkedList<PurchaseOffer>();
                        offers.add(pod2);
                        this.value.put(store.getProductById(prodOffer.getProductId()), offers);
                    }

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

    public void updateOfferPurchase(int purchaseOfferId, double newPrice){
        try {
            ConnectionSource connectionSource = connect();
            Dao<PurchaseOffersDAO, String> purchaseOffersDAOS = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
            purchaseOffersDAOS.executeRaw("UPDATE PurchaseOffers" + " SET priceOfOffer = " + newPrice + " WHERE id = " + purchaseOfferId+  ";");
            connectionSource.close();
        }
        catch(Exception e){
            System.out.println(e);
        }

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
