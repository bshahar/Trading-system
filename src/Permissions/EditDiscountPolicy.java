package Permissions;

import Domain.Member;
import Domain.Product;
import Domain.Result;
import Domain.Store;

public class EditDiscountPolicy {
    final private Member member;
    final private Store store;

    public EditDiscountPolicy(Member member, Store store) {
        this.store = store;
        this.member = member;
    }

    public Result action(int prodId, String category) {
        if(prodId != -1)
            this.store.removeDiscountOnProduct(prodId);
        else if(!category.equals(""))
            this.store.removeDiscountOnCategory(category);
        else
            this.store.removeDiscountOnStore();
        return new Result(true, "Discount policy was removed successfully.");
    }
}
