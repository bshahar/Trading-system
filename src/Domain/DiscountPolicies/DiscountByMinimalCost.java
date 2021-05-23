package Domain.DiscountPolicies;

import Domain.*;
import Domain.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiscountByMinimalCost extends DiscountPolicy {
    private double minCost;

    public DiscountByMinimalCost(double minCost) {
        this.minCost = minCost;
    }

    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        double cost = 0;
        Map<Product,Integer> map= bag.getProductsAmounts(user.getId());
        for (Product p: bag.getProducts(user.getId())) {
            cost += p.getPrice() * map.get(p);
        }
        return cost >= this.minCost;
    }

    public void setMinimalCost(int newMinCost) { this.minCost = newMinCost; }


    @Override
    public String getPolicyName() {
        return "Minimal Cost";
    }

    @Override
    public List<String> getPolicyParams() {
        List<String> params = new LinkedList<>();
        params.add(String.valueOf(minCost));
        return params;
    }
}
