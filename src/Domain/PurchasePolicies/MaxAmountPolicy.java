package Domain.PurchasePolicies;

import Domain.Bag;
import Domain.Product;
import Domain.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MaxAmountPolicy extends PurchasePolicy {
    private int prodId;
    private int maxAmount;


    public MaxAmountPolicy(int maxAmount, int prodId) {
        this.prodId = prodId;
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean validateCondition(User user, Date time, Bag bag) {
        int amount = 0;
        for (Product p: bag.getProducts(user.getId())) {
            if(p.getId() == this.prodId)
                amount = bag.getProductsAmounts(user.getId()).get(p);
        }
        return amount <= this.maxAmount;
    }

    public void setMaxAmount(int newMaxAmount) { this.maxAmount = newMaxAmount; }

    @Override
    public String getPolicyName() {
        return "Max Amount";
    }

    @Override
    public List<String> getPolicyParams() {
        List<String> params = new LinkedList<>();
        params.add(String.valueOf(maxAmount));
        params.add(String.valueOf(prodId));
        return params;
    }
}
