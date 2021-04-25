package Domain.DiscountPolicies;

import Domain.*;

import java.util.Date;

public class DiscountByMinimalCost extends DiscountPolicy {
    private double minCost;

    public DiscountByMinimalCost(double minCost) {
        this.minCost = minCost;
    }

    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        double cost = 0;
        for (Product p: bag.getProducts()) {
            cost += p.getPrice() * bag.getProductsAmounts().get(p);
        }
        return cost >= this.minCost;
    }

}
