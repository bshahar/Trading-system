package Domain;

import Domain.PurchaseFormat.PurchaseOffer;
import javafx.util.Pair;

import java.util.*;


public class Bag {
    private Store store;
    Map<Product,Integer> productsAmounts;
    Map<Product,Double> productsApproved; // the product that approved and his price
    Map<Product, PurchaseOffer> counterOffers;

    public Bag(Store store)
    {
        this.store = store;
        this.productsAmounts = new HashMap<>();
        this.productsApproved = new HashMap<>();
        this.counterOffers = new HashMap<>();

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
            if(!this.productsApproved.containsKey(p))
                total += p.getPrice();
            else
                total += productsApproved.get(p);
        }
        return total;
    }

    public void approveCounterOffer(Product prod) {
        double priceOfOffer = this.counterOffers.get(prod).getPriceOfOffer();
        int amountOfProd = this.counterOffers.get(prod).getNumOfProd();
        this.productsAmounts.put(prod,amountOfProd);
        this.counterOffers.remove(prod);
        this.productsApproved.put(prod,priceOfOffer);

    }

    public void rejectCounterOffer(Product prod) {
        this.counterOffers.remove(prod);
    }

    public Map<Product, Double> getOfferPrices() {
        return this.productsApproved;
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
