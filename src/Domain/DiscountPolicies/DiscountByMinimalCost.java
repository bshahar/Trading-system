package Domain.DiscountPolicies;

import Domain.Bag;

import java.util.Date;

public class DiscountByMinimalCost extends DiscountPolicy {
    private double minCost;

    public DiscountByMinimalCost(double minCost) {
        this.minCost = minCost;
    }

    @Override
    public boolean validateCondition(int userId, Date time, Bag bag) {
        return false;
        //todo
    }
}
