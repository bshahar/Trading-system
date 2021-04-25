package Domain.PurchaseFormat;

import Domain.Bag;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.PurchasePolicies.PurchaseCondition;
import Domain.PurchasePolicies.PurchasePolicy;
import Domain.User;

import java.util.Date;
import java.util.List;

public class ImmediatePurchase extends Purchase {
    private PurchaseCondition conditions;


    public boolean validatePurchase(User user, Date time, Bag bag) {
        return conditions.validateCondition(user, time, bag);
    }

    @Override
    public boolean validatePurchase(List<PurchasePolicy> policies) {
        return false;
    }
}