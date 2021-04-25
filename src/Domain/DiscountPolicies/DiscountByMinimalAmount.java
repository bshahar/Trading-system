package Domain.DiscountPolicies;

import Domain.Bag;
import Domain.Product;
import Domain.User;

import java.util.Date;

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
        for (Product p: bag.getProducts()) {
            if(p.getId() == this.prodId)
                amount = bag.getProductsAmounts().get(p);
        }
        return amount >= this.minAmount;
    }

}
