import java.util.*;


public class Bag {
    private Store store;
    List<Integer> productIds;


    public Bag(Store store)
    {
        this.store = store;
        this.productIds = new LinkedList<>();
    }

    public int getStoreId() {
        return this.store.getStoreId();
    }

    public void addProduct(int prodId) {
        this.productIds.add(prodId);
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void removeProduct(int prodId) {
        this.productIds.remove(prodId);
    }
}
