package Permissions;

import Domain.DiscountFormat.Discount;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.Member;
import Domain.PurchasePolicies.PurchaseCondition;
import Domain.Result;
import Domain.Store;

import java.util.Date;

public class DefinePurchasePolicy {
    final private Member member;
    final private Store store;

    public DefinePurchasePolicy(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public Result action(String param, String category, int prodId, PurchaseCondition conditions) {
        switch (param) {
            case "PRODUCT":
                this.store.addPurchaseOnProduct(prodId, conditions);
                break;
            case "CATEGORY":
                this.store.addPurchaseOnCategory(category,conditions);
                break;
            case "STORE":
                this.store.addPurchaseOnStore(conditions);
                break;
        }
        return new Result(true, "Purchase policy added successfully.");
    }
}
