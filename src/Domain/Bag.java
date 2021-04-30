package Domain;

import java.util.*;


public class Bag {
    private Store store;
    Map<Product,Integer> productsAmounts;

    public Bag(Store store)
    {
        this.store = store;
        this.productsAmounts = new HashMap<>();
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
}
