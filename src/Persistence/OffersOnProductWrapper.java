package Persistence;

import Domain.Product;
import Domain.PurchaseFormat.PurchaseOffer;
import Domain.Store;
import Persistence.DAO.*;
import Persistence.connection.JdbcConnectionSource;
import Service.API;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;

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
            Dao<LeftToApproveDAO, String> dao  = DaoManager.createDao(connectionSource, LeftToApproveDAO.class);

            ForeignCollection<LeftToApproveDAO> lta = dao.assignEmptyForeignCollection(tmp, "leftToApproveDAOS");
            LinkedList<Integer> users = purchaseOffer.getManager();
            for (Integer u: users) {
                lta.add(new LeftToApproveDAO(u));
                /*
                Dao<LeftToApproveDAO, String> leftToApproveDao = DaoManager.createDao(connectionSource, LeftToApproveDAO.class);
                LeftToApproveDAO leftToApproveDAOObj = new LeftToApproveDAO(purchaseOffer.getId(), u);
                leftToApproveDao.create(leftToApproveDAOObj);*/
            }


            Dao<PurchaseOffersDAO, String> purchaseOfferDao = DaoManager.createDao(connectionSource, PurchaseOffersDAO.class);
            PurchaseOffersDAO purchaseOfferDAOObj = new PurchaseOffersDAO(purchaseOffer.getId(), purchaseOffer.getPriceOfOffer(),purchaseOffer.getUser().getId(),purchaseOffer.getNumOfProd(), purchaseOffer.getCounterOffer(), lta);
            purchaseOfferDao.create(purchaseOfferDAOObj);

            Dao<ProductOffersDAO, String> productOffersDAO = DaoManager.createDao(connectionSource, ProductOffersDAO.class);
            ProductOffersDAO productOfferDaoObj = new ProductOffersDAO(storeId, purchaseOffer.getId(), prod.getId());
            productOffersDAO.create(productOfferDaoObj);

            connectionSource.close();
        } catch (Exception e) {
           System.out.println("hiii");
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
            LinkedList<PurchaseOffer> offers = this.value.get(prod);
            offers.remove(po);
            if (offers.isEmpty())
                this.value.remove(prod);
            else
                this.value.put(prod,offers);
            //this.value = get(store);

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
                    pod2.setCounterOfferDb(pod.getCounter());

                    Dao<LeftToApproveDAO, String> leftToApproveDAOManager =  DaoManager.createDao(connectionSource, LeftToApproveDAO.class);
                    Map<String, Object> fields2 = new HashMap<>();
                    fields.put("offerId", pod2.getId());
                    List<LeftToApproveDAO> leftToApproveDAOS = leftToApproveDAOManager.queryForFieldValues(fields2);

                    LinkedList<Integer> users = new LinkedList<>();
                    for (LeftToApproveDAO leftToApprove: leftToApproveDAOS) {
                        users.add(leftToApprove.getUserId());
                    }
                    pod2.addOwnersAndMangersLeft(users);

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

    public ConnectionSource connect() throws Exception{
        return DataBaseHelper.connect();
    }
}
