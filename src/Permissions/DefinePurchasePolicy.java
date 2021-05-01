package Permissions;

import Domain.Member;
import Domain.PurchasePolicies.PurchaseCondition;
import Domain.Result;
import Domain.Store;

public class DefinePurchasePolicy {
    final private Member member;
    final private Store store;

    public DefinePurchasePolicy(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public Result action(PurchaseCondition condition) {
        this.store.addPurchasePolicy(condition);
        return new Result(true, "Purchase policy added successfully.");
    }
}
