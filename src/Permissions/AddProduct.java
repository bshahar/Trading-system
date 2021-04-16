package Permissions;

import Domain.Member;
import Domain.Product;
import Domain.Store;

import java.util.List;

public class AddProduct {
    final private Member member;
    final private Store store;

    public AddProduct(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public boolean action(int productId,String name, List<Product.Category> categories, double price, String description, int quantity) {
        return store.addProductToStore(productId,name,categories,price,description,quantity);
    }
}
