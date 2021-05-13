package Domain.PurchaseFormat;

import Domain.Bag;
import Domain.DiscountPolicies.DiscountCondition;
import Domain.Product;
import Domain.PurchasePolicies.PurchaseCondition;
import Domain.PurchasePolicies.PurchasePolicy;
import Domain.User;

import java.util.Date;
import java.util.List;

public class PurchaseOffer extends Purchase {
    private int id;
    private PurchaseCondition conditions;
    private double priceOfOffer;
    private boolean isApproved;

    public PurchaseOffer(int id, PurchaseCondition conditions, double priceOfOffer){
        this.id = id;
        this.conditions = conditions;
        this.priceOfOffer = priceOfOffer;
        this.isApproved = false;
    }

    public boolean validatePurchase( User user, Date time, Bag bag) {
        return conditions.validateCondition( user, time, bag);
    }

    @Override
    public boolean validatePurchase(List<PurchasePolicy> policies) {
        return false;
    }

    public PurchaseCondition getConditions() { return this.conditions; }
}