package Domain;

import Domain.PurchaseFormat.PurchaseOffer;
import Persistence.BagWrapper;
import Persistence.UserApprovedOffersWrapper;
import Persistence.UserCounterOffersWrapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Bag {
    private Store store;
    private int userId;
    BagWrapper productsAmounts;
    UserApprovedOffersWrapper productsApproved; // the product that approved and his price
    UserCounterOffersWrapper counterOffers;

    public Bag(Store store, int userId)
    {
        this.store = store;
        this.userId = userId;
        this.productsAmounts = new BagWrapper();
        this.productsApproved = new UserApprovedOffersWrapper();
        this.counterOffers = new UserCounterOffersWrapper();


    }

    public int getStoreId() {
        return this.store.getStoreId();
    }

    public void addProduct(Product product,int amount,int userId) {
        this.productsAmounts.add(product,amount,store.getStoreId(),userId);
    }

    public Map<Product,Integer> getProductsAmounts(int userId) {
        return productsAmounts.getProductsAmount(userId,store.getStoreId());
    }

    public List<Product> getProducts(int userId) {
        List<Product> output = new LinkedList<>();
        for (Product prod: this.productsAmounts.getProductsAmount(userId,store.getStoreId()).keySet()) {
            output.add(prod);
        }
        return output;
    }

    public Store getStore() {
        return store;
    }


    public void setProducts(Map<Product,Integer> prods,int userId,int storeId) {
        this.productsAmounts.addProducts(prods,userId,storeId);
    }

    public void removeProduct(Product prodId,int userId,int storeId) {
        this.productsAmounts.remove(prodId,userId,storeId);
    }

    public int getProdNum(int userId,int storeId) {
        return productsAmounts.size(userId,storeId);
    }

    public double getBagTotalCost (int userId,int storeId) {
        double total = 0;
        for (Product p: this.productsAmounts.getProductsAmount(userId,storeId).keySet()) {
            if(!this.productsApproved.contains(p))
                total += p.getPrice();
            else
                total += productsApproved.get(this.store, this.userId, p);
        }
        return total;
    }

    public void approveCounterOffer(Product prod,int storeId) {
        double priceOfOffer = this.counterOffers.get(prod).getPriceOfOffer();
        int amountOfProd = this.counterOffers.get(prod).getNumOfProd();
        int userId = this.counterOffers.get(prod).getUser().getId();
        this.productsAmounts.add(prod,amountOfProd,storeId,userId);
        this.counterOffers.remove(getStoreId(),prod,this.counterOffers.get(prod));
        this.productsApproved.add(getStoreId(),userId, prod,this.counterOffers.get(prod).getPriceOfOffer());

    }

    public void rejectCounterOffer(Product prod) {
        this.counterOffers.remove(getStoreId(),prod,this.counterOffers.get(prod));
    }

    public Map<Product, Double> getOfferPrices() {
        return this.productsApproved.get(this.store, this.userId);
    }

    public Map<Product, PurchaseOffer> getCounterOffers() {
        return this.counterOffers.get(this.store, this.userId);
    }

   /* public void offerApproved(Product prod, double offer){
        int quantity = productsMayApproved.get(prod);
        productsApproved.put(prod,offer); //save the price for this user
        productsAmounts.put(prod,quantity);
        productsMayApproved.remove(prod);
    }
    public void offerRejected(Product prod){
        productsMayApproved.remove(prod);
    }

    public void addNewOffer(Product prod, int numOfProd){
        this.productsMayApproved.put(prod,numOfProd);
    }*/


}
