package Domain;

import java.util.*;

public class Inventory {

    private Map<Product , Integer> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public boolean addProduct(Product prod, int numOfProd) {
        if(numOfProd <= 0) return false;
        synchronized (this) {
            this.products.put(prod, numOfProd);
        }
        return true;
    }

    public boolean validateProductId(int id){
        for (Product p: products.keySet()) {
            if(p.getId() == id)
                return false;
        }
        return true;
    }

    public boolean prodExists(int id){
        boolean found = false;
        for (Product p: products.keySet()) {
            if(p.getId() == id)
                found = true;
        }
        return found;
    }

    public boolean canBuyProduct(Product prod, int numOfProd) {
        if(numOfProd >0 && this.products.containsKey(prod) && products.get(prod) >= numOfProd)
           return true;
        return false;
    }
    public boolean removeProductAmount(Product prod, int numOfProd){
        if (this.products.containsKey(prod) && products.get(prod) >= numOfProd){
            products.put(prod, products.get(prod) - numOfProd);
            return true;
        }
        else
            return false;


    }
    public String toString(){
        String output = "";
        for (Map.Entry<Product, Integer> p : products.entrySet()) {
            output = output + p.getKey().toString() + " Quantity - " + p.getValue();
        }
        return output;
    }

    public List<Integer> getProductsByName(Filter filter,double storeRank) {
        String name=filter.param;
        List<Integer> output = new LinkedList<>();
        for (Product p : products.keySet()) {
            String prodName=p.getName();
            if(prodName.equals(name) && checkFilter(p,filter,storeRank))
                output.add(p.getId());
        }
        return output;
    }

    private boolean checkFilter(Product product, Filter filter,double storeRank) {
        if (filter.minPrice > product.getPrice()) {
            return false;
        }
        if (filter.maxPrice < product.getPrice()) {
            return false;
        }
        if (filter.prodRank > product.getRate()) {
            return false;
        }
        if (!filter.category.equals("")) {
            if (!product.containsCategory(filter.category)) {

                return false;
            }
        }
        if (filter.storeRank > storeRank) {
            return false;
        }
        return true;
    }


    public List<Integer> getProductsByCategory(Filter filter, double storeRate) {
        List<Integer> output = new LinkedList<>();
        for (Product p : products.keySet()) {
            if(p.containsCategory(filter.param) && checkFilter(p,filter,storeRate))
                output.add(p.getId());
        }
        return output;
    }

    public List<Integer> getProductsByKeyWords(Filter filter, double storeRate) {
        List<Integer> output = new LinkedList<>();
        for (Product p : products.keySet()) {
            if(p.containsKeyWords(filter.param) && checkFilter(p,filter,storeRate) )
                output.add(p.getId());
        }
        return output;
    }

    public List<Integer> getProductsByPriceRange(String[] prices) {
        List<Integer> output = new LinkedList<>();
        for (Map.Entry<Product, Integer> p : products.entrySet()) {
            if(p.getKey().inPriceRange(prices))
                output.add(p.getKey().getId());
        }
        return output;
    }

    public Product getProductById( int prodId){
        for(Product p : this.products.keySet()){
            if(p.getId() == prodId)
                return p;
        }
        return null;
    }

    public Result removeProduct(int productId) {
        Product p = getProductById(productId);
        if (p != null){
            products.remove(p);
            return new Result(true,true);
        }
        return new Result(false, "product not exist");
    }

    public List<Integer> getProductsIds(){
        List <Integer> l = new ArrayList<>(this.products.values());
        return l;
    }

    public List<Product> getProducts() {
        return  (new LinkedList<>(products.keySet()));
    }

    public void addProductAmount(Product product, Integer amount) {
        if(this.products.containsKey(product)){
            this.products.put(product, this.products.get(product)+amount);
        }
    }

    public Map<Product,Integer> getProductsAmounts() {
        return products;
    }

    public void setProductAmount(Product product, int amount) {
        products.put(product,amount);

    }


}
