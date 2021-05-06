package Permissions;

import Domain.Member;
import Domain.Result;
import Domain.Store;

public class ViewPurchasePolicies {
    final private Member member;
    final private Store store;

    public ViewPurchasePolicies(Member member, Store store) {
        this.store = store;
        this.member = member;
    }

    public Result action(int prodId, String category) {
        if(prodId != -1)
            return this.store.viewPurchasePoliciesOnProduct(prodId);
        else if(!category.equals(""))
            return this.store.viewPurchasePoliciesOnCategory(category);
        else
            return this.store.viewPurchasePoliciesOnStore();
    }
}
