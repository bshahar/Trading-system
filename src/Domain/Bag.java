package Domain;

import java.util.*;


public class Bag {
    private Store store;
    Map<Product,Integer>  productIdsAmount;


    public Bag(Store store)
    {
        this.store = store;
        this.productIdsAmount = new HashMap<>();
    }

    public int getStoreId() {
        return this.store.getStoreId();
    }

    public void addProduct(Product product,int amount) {
        this.productIdsAmount.put(product,amount);
    }

    public Map<Product,Integer> getProductIds() {
        return productIdsAmount;
    }

    public void removeProduct(Product prod) {
        this.productIdsAmount.remove(prod);
    }

    public int getProdNum() {
        return productIdsAmount.size();
    }
}
