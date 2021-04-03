import java.util.*;

public class Inventory {

    Map<Product , Integer> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public Inventory(Map<Product , Integer> products) {
        this.products = products;
    }

    public boolean addProduct(Product prod, int numOfProd) {
        synchronized (this) {
            if (this.products.containsKey(prod)) //TODO add to logger, print an error message?
                products.put(prod, products.get(prod) + numOfProd);
            this.products.put(prod, numOfProd);
        }
        return true;
    }

    public boolean validateProductId(int id){
        for (Map.Entry<Product, Integer> p : products.entrySet()) {
            if (p.getKey().getId() == id)
                return false;
        }
        return true;
    }

    public boolean CanBuyProduct(Product prod, int numOfProd) {
        if(this.products.containsKey(prod) && products.get(prod) >= numOfProd) //TODO add to logger, print an error message?
           return true;
        return false;
    }
    public boolean BuyProduct(Product prod, int numOfProd){
        synchronized (this) {
            if (this.products.containsKey(prod) && products.get(prod) >= numOfProd){
                products.put(prod, products.get(prod) - numOfProd);
                return true;
            }
            else
                return false;

        }
    }
    public String toString(){
        String output = "";
        for (Map.Entry<Product, Integer> p : products.entrySet()) {
            output = output + p.getKey().toString() + " Quantity - " + p.getValue();
        }
        return output;
    }

    public List<Integer> getProductsByName(String name) {
        List<Integer> output = new LinkedList<>();
        for (Map.Entry<Product, Integer> p : products.entrySet()) {
            if(p.getKey().getName().equals(name))
                output.add(p.getKey().getId());
        }
        return output;
    }

    public List<Integer> getProductsByCategory(String category) {
        /*
        Product.Category category1 = (Product.Category) Enum.valueOf(category);
        List<Integer> output = new LinkedList<>();
        for (Map.Entry<Product, Integer> p : products.entrySet()) {
            if(p.getKey().getCategories().contains())
                output.add(p.getKey().getId());
        }
        return output;*/
        return null;
    }
}
