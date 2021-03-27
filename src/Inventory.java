import java.util.*;

public class Inventory {

    List<Product> products;

    public boolean addProduct(Product prod) {
        if(this.products.contains(prod)) //TODO add to logger, print an error message?
            return false;
        this.products.add(prod);
        return true;
    }
}
