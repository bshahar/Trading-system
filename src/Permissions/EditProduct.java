package Permissions;

import Domain.Member;
import Domain.Product;
import Domain.Result;
import Domain.Store;

public class EditProduct {
    final private Member member;
    final private Store store;
    public EditProduct(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public Result action(Product product, int price, int amount) {
        if(price<=0 || amount <0){
            return new Result(false,"Price and Amount can't be negative");
        }
        product.setAmount(amount);
        product.setPrice(price);
        store.setProductAmount(product,amount);
        return new Result(true,"Product updated successfully ");
    }
}
