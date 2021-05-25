package Domain.DiscountPolicies;

import Domain.Bag;
import Domain.Product;
import Domain.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiscountByMinimalAmount extends DiscountPolicy {
    private int prodId;
    private int minAmount;


    public DiscountByMinimalAmount(int minAmount, int prodId) {
        this.prodId = prodId;
        this.minAmount = minAmount;
    }

    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        int amount = 0;
        Map<Product, Integer> amounts = bag.getProductsAmounts(user.getId());
        for (Product prod : amounts.keySet()) {
            if (prod.getId() == this.prodId)
                amount = amounts.get(prod);
        }
        return amount >= this.minAmount;
    }

    public void setMinimalAmount(int newMinAmount) { this.minAmount = newMinAmount; }

    @Override
    public String getPolicyName() {
        return "Minimal Amount";
    }

    @Override
    public List<String> getPolicyParams() {
        List<String> params = new LinkedList<>();
        params.add(String.valueOf(minAmount));
        params.add(String.valueOf(prodId));
        return params;
    }

}
