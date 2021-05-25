package Domain.DiscountPolicies;

import Domain.Bag;
import Domain.Product;
import Domain.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
        for (Product p: bag.getProducts(user.getId())) {
            if(p.getId() == this.prodId)
                amount = bag.getProductsAmounts(user.getId()).get(p);
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
