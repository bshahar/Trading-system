package Domain;

import Persistence.InventoryWrapper;

import java.util.*;

public class Inventory {

    private InventoryWrapper products;

    public Inventory() {
        this.products = new InventoryWrapper();
    }

    public boolean addProduct(Product prod, int quantity,int storeId) {
        if(quantity <= 0) return false;
        synchronized (this) {
            this.products.add(prod, quantity,storeId);
        }
        return true;
    }

    public boolean validateProductId(int id,int storeId){
        for (Product p: products.getAllProducts(storeId)) {
            if(p.getId() == id)
                return false;
        }
        return true;
    }

    public boolean prodExists(int id, int storeId){
        boolean found = false;
        for (Product p: products.getAllProducts(storeId)) {
            if (p.getId() == id) {
                found = true;
                break;
            }
        }
        return found;
    }

    public boolean canBuyProduct(Product prod, int numOfProd,int storeId) {
        Map<Integer,Integer> prodAmount= products.getAllProductsAmount(storeId);
        return numOfProd > 0 && prodAmount.containsKey(prod.getId()) && prodAmount.get(prod.getId()) >= numOfProd;
    }
    public boolean removeProductAmount(Product prod, int numOfProd,int storeId){
        Map<Integer,Integer> prodAmount= products.getAllProductsAmount(storeId);
        if (prodAmount.containsKey(prod.getId()) && prodAmount.get(prod.getId()) >= numOfProd){
            products.updateAmount(prod.getId(), prodAmount.get(prod.getId()) - numOfProd,storeId);
            return true;
        }
        else
            return false;
    }

    public String toString(int storeId){
        StringBuilder output = new StringBuilder();
        for (Map.Entry<Integer, Integer> p : products.getAllProductsAmount(storeId).entrySet()) {
            output.append(p.getKey().toString()).append(" Quantity - ").append(p.getValue());
        }
        return output.toString();
    }

    public List<Integer> getProductsByName(Filter filter,double storeRank,int storeId) {
        String name=filter.param;
        List<Integer> output = new LinkedList<>();
        for (Product p : products.getAllProducts(storeId)) {
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


    public List<Integer> getProductsByCategory(Filter filter, double storeRate,int storeId) {
        List<Integer> output = new LinkedList<>();
        for (Product p : products.getAllProducts(storeId)) {
            if(p.containsCategory(filter.param) && checkFilter(p,filter,storeRate))
                output.add(p.getId());
        }
        return output;
    }

    public List<Integer> getProductsByKeyWords(Filter filter, double storeRate,int storeId) {
        List<Integer> output = new LinkedList<>();
        for (Product p : products.getAllProducts(storeId)) {
            if(p.containsKeyWords(filter.param) && checkFilter(p,filter,storeRate) )
                output.add(p.getId());
        }
        return output;
    }

    public List<Integer> getProductsByPriceRange(String[] prices,int storeId) {
        List<Integer> output = new LinkedList<>();
        for (Product p : products.getAllProducts(storeId)) {
            if(p.inPriceRange(prices))
                output.add(p.getId());
        }
        return output;
    }

    public Product getProductById( int prodId,int storeId){
        for(Product p : this.products.getAllProducts(storeId)){
            if(p.getId() == prodId)
                return p;
        }
        return null;
    }

    public Product getProductByName(String prodName,int storeId){
        for(Product p : this.products.getAllProducts(storeId)){
            if(p.getName().equals(prodName))
                return p;
        }
        return null;
    }

    public Result removeProduct(int productId,int storeId) {
        Product p = getProductById(productId,storeId);
        int amount= products.getAmount(storeId,productId);
        if (p != null){
            products.remove(p,storeId,amount);
            return new Result(true,true);
        }
        return new Result(false, "product not exist");
    }


    public List<Product> getProducts(int storeId) {
        return  products.getAllProducts(storeId);
    }

    public void addProductAmount(Product product, Integer amount,int storeId) {
        Map<Integer,Integer> productsamount=products.getAllProductsAmount(storeId);
        if( productsamount.containsKey(product.getId())){
            this.products.updateAmount(product.getId(),  productsamount.get(product.getId())+amount,storeId);
        }
    }

    public Map<Integer,Integer> getProductsAmounts(int storeId) {
        return products.getAllProductsAmount(storeId);
    }

    public void setProductAmount(Product product, int amount,int storeId) {
        products.updateAmount(product.getId(),amount,storeId);

    }


}
