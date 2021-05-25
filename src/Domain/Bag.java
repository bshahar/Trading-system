package Domain;

import Domain.PurchaseFormat.PurchaseOffer;
import Persistence.UserApprovedOffersWrapper;
import Persistence.UserCounterOffersWrapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Bag {
    private Store store;
    private int userId;
    Map<Product,Integer> productsAmounts;
    UserApprovedOffersWrapper productsApproved; // the product that approved and his price
    UserCounterOffersWrapper counterOffers;

    public Bag(Store store, int userId)
    {
        this.store = store;
        this.userId = userId;
        this.productsAmounts = new HashMap<>();
        this.productsApproved = new UserApprovedOffersWrapper();
        this.counterOffers = new UserCounterOffersWrapper();

    }

    public int getStoreId() {
        return this.store.getStoreId();
    }

    public void addProduct(Product product,int amount) {
        this.productsAmounts.put(product,amount);
    }

    public Map<Product,Integer> getProductsAmounts() {
        return productsAmounts;
    }

    public List<Product> getProducts() {
        List<Product> output = new LinkedList<>();
        for (Product prod: this.productsAmounts.keySet()) {
            output.add(prod);
        }
        return output;
    }

    public void setProducts(Map<Product,Integer> prods) {
        this.productsAmounts = prods;
    }

    public void removeProduct(Product prodId) {
        this.productsAmounts.remove(prodId);
    }

    public int getProdNum() {
        return productsAmounts.size();
    }

    public double getBagTotalCost () {
        double total = 0;
        for (Product p: this.productsAmounts.keySet()) {
            if(!this.productsApproved.contains(p))
                total += p.getPrice();
            else
                total += productsApproved.get(this.store, this.userId, p);
        }
        return total;
    }

    public void approveCounterOffer(Product prod) {
        double priceOfOffer = this.counterOffers.get(prod).getPriceOfOffer();
        int amountOfProd = this.counterOffers.get(prod).getNumOfProd();
        int userId = this.counterOffers.get(prod).getUser().getId();
        this.productsAmounts.put(prod,amountOfProd);
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
