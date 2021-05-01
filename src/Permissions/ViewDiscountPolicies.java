package Permissions;

import Domain.Member;
import Domain.Result;
import Domain.Store;

public class ViewDiscountPolicies {
    final private Member member;
    final private Store store;

    public ViewDiscountPolicies(Member member, Store store) {
        this.store = store;
        this.member = member;
    }

    public Result action(int prodId, String category) {
        if(prodId != -1)
            return this.store.viewDiscountPoliciesOnProduct(prodId);
        else if(!category.equals(""))
            return this.store.viewDiscountPoliciesOnCategory(category);
        else
            return this.store.viewDiscountPoliciesOnStore();
    }
}
