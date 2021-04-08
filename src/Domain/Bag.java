package Domain;

import java.util.*;


public class Bag {
    private Store store;
    Map<Integer,Integer>  productIdsAmount;


    public Bag(Store store)
    {
        this.store = store;
        this.productIdsAmount = new HashMap<>();
    }

    public int getStoreId() {
        return this.store.getStoreId();
    }

    public void addProduct(int prodId,int amount) {
        this.productIdsAmount.put(prodId,amount);
    }

    public Map<Integer,Integer> getProductIds() {
        return productIdsAmount;
    }

    public void removeProduct(int prodId) {
        this.productIdsAmount.remove(prodId);
    }
}
